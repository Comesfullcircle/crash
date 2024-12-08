package com.comesfullcircle.crash.model.user;

import com.comesfullcircle.crash.model.entity.UserEntity;

public record User(Long userId, String username, String name, String email) {
    public static User from(UserEntity userEntity){
        return new User(
                userEntity.getUserId(),
                userEntity.getUsername(),
                userEntity.getName(),
                userEntity.getEmail());
    }
}