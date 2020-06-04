package com.reachout.passwordreset;

import com.reachout.passwordreset.UrlGenerator;

public class PasswordResetManager {

    private UrlGenerator ug;

    public PasswordResetManager() {
        this.ug = new UrlGenerator();
    }

    public String getUrl() {
        return "http://reachout.space/resetPassword?uid=" + getUniqueCode();
    }

    public String getUniqueCode() {
        return ug.randomString();
    }
    
}