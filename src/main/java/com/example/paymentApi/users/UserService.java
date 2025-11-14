package com.example.paymentApi.users;

import com.example.paymentApi.shared.ExceptionThrower;
import com.example.paymentApi.shared.HttpRequestUtil;
import com.example.paymentApi.shared.utility.PasswordHashUtil;
import com.example.paymentApi.shared.utility.OtpGenerator;
import com.example.paymentApi.shared.utility.TokenResponseUtil;
import com.example.paymentApi.shared.utility.Verifier;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {
    private final ModelMapper modelMapper;
    private final JwtService jwtService;
    private final ExceptionThrower exceptionThrower;
    private final UserRepository userRepository;
    private final Verifier verifier;
    private final PasswordHashUtil passwordHashUtil;

    public UserService(ModelMapper modelMapper, JwtService jwtService,
                       ExceptionThrower exceptionThrower, UserRepository userRepository,
                       Verifier verifier, PasswordHashUtil passwordHashUtil) {
        this.modelMapper = modelMapper;
        this.jwtService = jwtService;
        this.exceptionThrower = exceptionThrower;
        this.userRepository = userRepository;
        this.verifier = verifier;
        this.passwordHashUtil = passwordHashUtil;
    }


    public UserResponse signUp(UserRequest userRequest, String id) {
        userRepository.findById(id)
                .ifPresent(user ->
                        exceptionThrower.throwUserAlreadyExistException(HttpRequestUtil.getServletPath()));
        verifier.setResourceUrl(HttpRequestUtil.getServletPath()).verifyParams(
                userRequest.getFirstName(),
                userRequest.getLastName(),
                userRequest.getPassword(),
                userRequest.getPhoneNumber()
        );
        verifier.verifyEmail(userRequest.getEmailAddress());

        User user = new User();
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setEmailAddress(userRequest.getEmailAddress());
        user.setPassword(passwordHashUtil.hashPassword(userRequest.getPassword()));
        user.setAcceptedTerms(userRequest.getAcceptedTerms());

        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserResponse.class);
    }

    public UserResponse login(LoginRequest loginRequest, HttpServletResponse httpServletResponse) {

        User user = userRepository.findByEmailAddress(loginRequest.getEmailAddress()).orElseThrow(() ->
                exceptionThrower.throwUserNotFoundExistException(HttpRequestUtil.getServletPath()));

        boolean passwordMatches = passwordHashUtil.verifyPassword(loginRequest.getPassword(), user.getPassword());
        if (!passwordMatches) {
            exceptionThrower.throwInvalidLoginException(HttpRequestUtil.getServletPath());
        }
        if (!user.isVerified()) {
            exceptionThrower.throwUserNotVerifiedException(HttpRequestUtil.getServletPath());
        }

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        TokenResponseUtil.setAccessTokenHeader(httpServletResponse, accessToken);
        TokenResponseUtil.setRefreshTokenCookie(httpServletResponse, refreshToken);

        UserResponse userResponse = modelMapper.map(user, UserResponse.class);
        userResponse.setToken(accessToken);
        userResponse.setRefreshToken(refreshToken);
        return userResponse;

    }

    @Transactional
    public String sendOtp(String emailAddress) {
        User user = userRepository.findByEmailAddress(emailAddress).orElseThrow(() ->
                exceptionThrower.throwUserNotFoundExistException(HttpRequestUtil.getServletPath()));

        verifier.setResourceUrl(HttpRequestUtil.getServletPath())
                .verifyEmail(emailAddress);

        OtpGenerator.OtpData otp = OtpGenerator.generateOtp();

        user.setOtp(otp.getOtp());

        user.setOtpExpiryTime(otp.getExpiryTime());

        userRepository.save(user);

        //TODO: Set up emailService and send OTP

        return "Otp successfully sent";
    }

    public String verifyOtp(String otp, String emailAddress) {
        User user = userRepository.findByEmailAddress(emailAddress).orElseThrow(() ->
                exceptionThrower.throwUserNotFoundExistException(HttpRequestUtil.getServletPath()));

        if (user.getOtp() == null || user.getOtpExpiryTime() == null) {
            exceptionThrower.throwOtpNotFoundException(HttpRequestUtil.getServletPath());
        }

        if (LocalDateTime.now().isAfter(user.getOtpExpiryTime())) {
            exceptionThrower.throwOtpExpiredException(HttpRequestUtil.getServletPath());
        }

        if (!otp.equals(user.getOtp())) {
            exceptionThrower.throwInvalidOtpException(HttpRequestUtil.getServletPath());
        }
        verifier.setResourceUrl(HttpRequestUtil.getServletPath())
                .verifyParams(otp);
        verifier.verifyEmail(emailAddress);
        verifier.isValidString(otp);

        user.setVerified(true);
        user.setOtp(null);
        user.setOtpExpiryTime(null);

        userRepository.save(user);

        return "Email successfully verified.";

    }

    public UserResponse refreshToken(String id, String refreshToken, HttpServletResponse httpServletResponse) {
        User user = userRepository.findById(id).orElseThrow(() ->
                exceptionThrower.throwUserNotFoundExistException(HttpRequestUtil.getServletPath()));

        if (!jwtService.validateRefreshToken(refreshToken)) {
            exceptionThrower.throwInvalidRefreshTokenException(HttpRequestUtil.getServletPath());
        }
        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        TokenResponseUtil.setAccessTokenHeader(httpServletResponse, newAccessToken);
        TokenResponseUtil.setRefreshTokenCookie(httpServletResponse, newRefreshToken);

        UserResponse response = modelMapper.map(user, UserResponse.class);
        response.setToken(newAccessToken);
        response.setRefreshToken(newRefreshToken);
        return response;

    }

    public String resetPassword(String id, String newEmail) {
        User user = userRepository.findById(id).orElseThrow(() ->
                exceptionThrower.throwUserNotFoundExistException(HttpRequestUtil.getServletPath()));

        verifier.setResourceUrl(HttpRequestUtil.getServletPath()).verifyEmail(newEmail);
        user.setPassword(passwordHashUtil.hashPassword(newEmail));
        userRepository.save(user);
        return "Password Reset successfully";
    }
}

