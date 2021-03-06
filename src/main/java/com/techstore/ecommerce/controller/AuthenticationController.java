package com.techstore.ecommerce.controller;

import com.techstore.ecommerce.object.dto.mapper.UserMapper;
import com.techstore.ecommerce.object.dto.request.AuthRequest;
import com.techstore.ecommerce.object.dto.response.TokenResponse;
import com.techstore.ecommerce.object.dto.response.UserResponse;
import com.techstore.ecommerce.object.model.RefreshToken;
import com.techstore.ecommerce.object.entity.jpa.User;
import com.techstore.ecommerce.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/oauth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserMapper mapper;

    @PostMapping("/login")
    ResponseEntity<?> createAccessToken(@RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                ));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();

        String accessToken = jwtService.generateAccessToken(user);
        RefreshToken refreshToken = jwtService.generateRefreshToken(user);

        TokenResponse response = new TokenResponse(accessToken, refreshToken.getToken(),
                                                   mapper.toResponseModel(user));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token/{token}")
    ResponseEntity<?> getNewAccessToken(@PathVariable String token) {
        RefreshToken refreshToken = jwtService.verifyExpiration(token);

        String newAccessToken = jwtService.generateAccessToken(refreshToken.getUser());
        UserResponse userInfo = mapper.toResponseModel(refreshToken.getUser());

        TokenResponse response = new TokenResponse(newAccessToken,
                                                   refreshToken.getToken(), userInfo);

        return ResponseEntity.ok(response);
    }
}
