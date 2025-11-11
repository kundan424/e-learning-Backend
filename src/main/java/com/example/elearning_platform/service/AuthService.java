package com.example.elearning_platform.service;

import com.example.elearning_platform.dto.AuthResponse;
import com.example.elearning_platform.dto.LoginRequest;
import com.example.elearning_platform.dto.RegisterRequest;
import com.example.elearning_platform.entity.User;
import com.example.elearning_platform.repository.UserRepository;
import com.example.elearning_platform.util.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthResponse register (RegisterRequest request){
        // check: if user already exists
        if (userRepository.findByUsername(request.getUsername()).isPresent()){
            throw new IllegalArgumentException("Username already taken");
        }

        // create new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        userRepository.save(user);

        /// 3. Generate and return token
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtUtil.generateToken(userDetails);

        return  new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request){
        // authenticate the user , return exception if credential is bad
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtUtil.generateToken(userDetails);
        return new AuthResponse(token);
    }
}
