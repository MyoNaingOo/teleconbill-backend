package com.mno.teleconbill.auth;

import com.mno.teleconbill.config.JwtService;
import com.mno.teleconbill.entity.Role;
import com.mno.teleconbill.entity.Token;
import com.mno.teleconbill.entity.TokenType;
import com.mno.teleconbill.entity.User;
import com.mno.teleconbill.otb.OtpRepo;
import com.mno.teleconbill.otb.OtpService;
import com.mno.teleconbill.reposity.TokenRepo;
import com.mno.teleconbill.reposity.UserRepo;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepo userRepo;
    private final TokenRepo tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final OtpService otpService;

    public AuthenticationResponse register(RegisterRequest request) {

        if(Objects.equals(request.email, "myonaingoo623@gmail.com")){
            System.out.println("Admin Email Login ");
            var user = User.builder()
                    .name(request.name)
                    .email(request.email)
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.ADMIN)
                    .build();

            var savedUser = userRepo.save(user);
            var jwtToken = jwtService.generateToken(user);
            saveUserToken(savedUser, jwtToken);
            otpService.sendOtp(request.getEmail(),jwtToken);
            return AuthenticationResponse.builder()
                    .user(savedUser)
                    .build();

        }else {

            var user = User.builder()
                    .name(request.name)
                    .email(request.email)
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.USER)
                    .build();

            var savedUser = userRepo.save(user);
            var jwtToken = jwtService.generateToken(user);
            saveUserToken(savedUser, jwtToken);
            otpService.sendOtp(request.getEmail(),jwtToken);
            return AuthenticationResponse.builder()
                    .user(savedUser)
                    .build();
        }

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepo.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        otpService.sendOtp(request.getEmail(),jwtToken);

        return AuthenticationResponse.builder()
                .user(user)
                .build();
    }

    public void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}

