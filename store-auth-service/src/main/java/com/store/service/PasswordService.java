package com.store.service;

public interface PasswordService {
    boolean matches(String rawPassword, String encodedPassword);
}
