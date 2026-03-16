package com.aimentor.domain.user.dto.response;

import com.aimentor.domain.user.entity.Role;
import java.time.LocalDateTime;

public record AuthTokenResponse(
        Long userId,
        String email,
        Role role,
        String accessToken,
        LocalDateTime accessTokenExpiresAt,
        String refreshToken,
        LocalDateTime refreshTokenExpiresAt
) {
}
