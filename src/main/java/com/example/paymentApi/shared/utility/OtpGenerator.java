package com.example.paymentApi.shared.utility;

import java.security.SecureRandom;
import java.time.LocalDateTime;

public class OtpGenerator{

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final int OTP_LENGTH = 5;
    private static final int EXPIRY_MINUTES = 300000;

    private OtpGenerator() {
        // prevent instantiation
    }

    /**
     * Generates a new OTP and its expiry time.
     *
     * @return OtpData object containing the OTP and expiry time
     */
    public static OtpData generateOtp() {
        int otpValue = secureRandom.nextInt(OTP_LENGTH);// ensures a 5-digit OTP
        String otp = String.valueOf(otpValue);
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(EXPIRY_MINUTES);

        return new OtpData(otp, expiryTime);
    }

    /**
     * Simple holder for OTP and expiry time.
     */
    public static class OtpData {
        private final String otp;
        private final LocalDateTime expiryTime;

        public OtpData(String otp, LocalDateTime expiryTime) {
            this.otp = otp;
            this.expiryTime = expiryTime;
        }

        public String getOtp() {
            return otp;
        }

        public LocalDateTime getExpiryTime() {
            return expiryTime;
        }

        public boolean isExpired() {
            return LocalDateTime.now().isAfter(expiryTime);
        }
    }
}
