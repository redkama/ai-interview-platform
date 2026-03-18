package com.aimentor.domain.user.service;

import com.aimentor.domain.user.dto.request.SignupRequest;
import com.aimentor.domain.user.dto.response.AuthTokenResponse;
import com.aimentor.domain.user.entity.Role;
import com.aimentor.domain.user.entity.User;
import com.aimentor.domain.user.repository.UserRepository;
import com.aimentor.common.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthService authService;

    @Test
    void signupShouldPersistUserWithUserRoleAndEncodedPassword() {
        SignupRequest request = new SignupRequest("Demo User", "demo@example.com", "password1");
        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            return User.builder()
                    .email(user.getEmail())
                    .name(user.getName())
                    .password(user.getPassword())
                    .role(user.getRole())
                    .build();
        });
        when(jwtTokenProvider.createAccessToken(any(), eq(request.email()), eq(Role.USER))).thenReturn("access-token");
        when(jwtTokenProvider.createRefreshToken(any(), eq(request.email()), eq(Role.USER))).thenReturn("refresh-token");
        when(jwtTokenProvider.getAccessTokenExpirationSeconds()).thenReturn(1800L);
        when(jwtTokenProvider.getRefreshTokenExpirationSeconds()).thenReturn(1209600L);

        AuthTokenResponse response = authService.signup(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        org.mockito.Mockito.verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertThat(savedUser.getRole()).isEqualTo(Role.USER);
        assertThat(savedUser.getPassword()).isEqualTo("encoded-password");
        assertThat(response.email()).isEqualTo(request.email());
        assertThat(response.role()).isEqualTo(Role.USER);
        assertThat(response.accessToken()).isEqualTo("access-token");
    }
}
