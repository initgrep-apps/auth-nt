package com.initgrep.cr.msauth.auth.constants;

public final class ValidationConstants {
    public static final String EMAIL_PATTERN = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    public static final String EMAIL_INVALID = "Email is not valid";
    public static final String PHONE_PATTERN = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$";
    public static final String PHONE_INVALID = "Phone Number is not valid";
    public static final String TOKEN_INVALID = "refreshToken is not valid";


    private ValidationConstants() {

    }
}
