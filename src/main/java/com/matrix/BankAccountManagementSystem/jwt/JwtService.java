package com.matrix.BankAccountManagementSystem.jwt;

import com.matrix.BankAccountManagementSystem.model.entity.security.User;
import io.jsonwebtoken.Claims;

public interface JwtService {
    String issueToken(User user);
    Claims verifyToken(String token);
}
