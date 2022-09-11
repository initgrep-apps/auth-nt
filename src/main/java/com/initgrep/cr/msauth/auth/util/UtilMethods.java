package com.initgrep.cr.msauth.auth.util;

import static java.util.Objects.isNull;

public  final class UtilMethods {

    /**
     * @param s a String
     * @return true if the string is either null or empty or blank
     */
    public static  boolean isEmpty(String s){
        return isNull(s) || s.isEmpty() || s.isBlank();
    }

    private UtilMethods(){}
}
