package com.comesfullcircle.crash.service;

import com.comesfullcircle.crash.exception.user.UserAlreadyExistsException;
import com.comesfullcircle.crash.exception.user.UserNotFoundException;
import com.comesfullcircle.crash.model.entity.UserEntity;
import com.comesfullcircle.crash.model.user.User;
import com.comesfullcircle.crash.model.user.UserAuthenticationResponse;
import com.comesfullcircle.crash.model.user.UserLoginRequestBody;
import com.comesfullcircle.crash.model.user.UserSignUpRequestBody;
import com.comesfullcircle.crash.repository.UserEntityCacheRepository;
import com.comesfullcircle.crash.repository.UserEntityRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private UserEntityCacheRepository userEntityCacheRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired private JwtService jwtService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserEntityByUsername(username);
    }

    public User signUp(@Valid UserSignUpRequestBody userSignUpRequestBody) {
        userEntityRepository.findByUsername(userSignUpRequestBody.username())
                .ifPresent(
                        user -> {
                            throw new UserAlreadyExistsException();
                        }
                );

        var userEntity =
            userEntityRepository.save(
                UserEntity.of(
                        userSignUpRequestBody.username(),
                        passwordEncoder.encode(userSignUpRequestBody.password()),
                        userSignUpRequestBody.name(),
                        userSignUpRequestBody.email()));

        return User.from(userEntity);
    }

    public UserAuthenticationResponse authenticate(UserLoginRequestBody userLoginRequestBody) {
        var userEntity = getUserEntityByUsername(userLoginRequestBody.username());

        if (passwordEncoder.matches(userLoginRequestBody.password(), userEntity.getPassword())) {
            var accessToken = jwtService.generateAccessToken(userEntity);
            return new UserAuthenticationResponse(accessToken);
        } else {
            throw new UserNotFoundException();
        }

    }

    private UserEntity getUserEntityByUsername(String username) {
        return userEntityCacheRepository
                .getUserEntityCache(username)
                .orElseGet(
                        () -> {
                            var userEntity =
                                    userEntityRepository
                                    .findByUsername(username)
                                    .orElseThrow(()-> new UserNotFoundException(username));
                    userEntityCacheRepository.setUserEntityCache(userEntity);
                    return userEntity;
                });
    }
}
