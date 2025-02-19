package com.example.INFO.domain.user.dto;

import com.example.INFO.domain.auth.model.constant.OAuthProvider;
import com.example.INFO.domain.user.model.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor(staticName = "of")
public class UserInfoMeDto {

    String nickname;
    String email;
    OAuthProvider oAuthProvider;

    public static UserInfoMeDto fromEntity(UserEntity userEntity) {
        OAuthProvider oAuthProvider = null;
        if (userEntity.getOAuthDetailsEntity() != null) {
            oAuthProvider = userEntity.getOAuthDetailsEntity().getProvider();
        }

        return UserInfoMeDto.of(
                userEntity.getNickname(),
                userEntity.getEmail(),
                oAuthProvider
        );
    }
}
