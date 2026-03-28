package com.store.storeauthservice.service;

public interface PasswordService {
    boolean matches(String rawPassword, String encodedPassword);
}
