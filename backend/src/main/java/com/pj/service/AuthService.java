package com.pj.service;

import com.pj.domain.AuthResponse;
import com.pj.domain.LoginRequest;
import com.pj.domain.RegisterRequest;
import com.pj.domain.User;
import com.pj.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse register(RegisterRequest request) {
        User existing = userMapper.findByUsername(request.getUsername());
        if (existing != null) {
            return AuthResponse.fail("用户名已存在");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        userMapper.insert(user);
        return AuthResponse.ok(request.getUsername());
    }

    public AuthResponse login(LoginRequest request) {
        User user = userMapper.findByUsername(request.getUsername());
        if (user == null) {
            return AuthResponse.fail("用户不存在");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            return AuthResponse.fail("密码错误");
        }
        return AuthResponse.ok(user.getUsername());
    }
}
