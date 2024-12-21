package com.comesfullcircle.crash.service;

import com.comesfullcircle.crash.exception.registration.RegistrationAlreadyExistsException;
import com.comesfullcircle.crash.exception.registration.RegistrationNotFoundException;
import com.comesfullcircle.crash.model.crashsession.CrashSessionPostRequestBody;
import com.comesfullcircle.crash.model.crashsession.CrashSessionRegistrationStatus;
import com.comesfullcircle.crash.model.entity.RegistrationEntity;
import com.comesfullcircle.crash.model.entity.UserEntity;
import com.comesfullcircle.crash.model.registration.Registration;
import com.comesfullcircle.crash.model.registration.RegistrationPostRequestBody;
import com.comesfullcircle.crash.repository.RegistrationEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegistrationService {

    @Autowired
    private RegistrationEntityRepository registrationEntityRepository;
    @Autowired
    private CrashSessionService crashSessionService;
    @Autowired
    private SlackService slackService;

    public List<Registration> getRegistrationsByCurrentUser(UserEntity currentUser) {
        var registrationEntities = registrationEntityRepository.findByUser(currentUser);
        return registrationEntities.stream().map(Registration::from).toList();
    }

    public Registration getRegistrationByRegistrationIdByCurrentUser(
            Long registrationId, UserEntity currentUser) {
        var registrationEntity =
                getRegistrationEntityByRegistrationIdAndUserEntity(registrationId, currentUser);
        return Registration.from(registrationEntity);
    }

    public RegistrationEntity getRegistrationEntityByRegistrationIdAndUserEntity(
            Long registrationId, UserEntity userEntity
    ){
        return registrationEntityRepository
                .findByRegistrationIdAndUser(registrationId, userEntity)
                .orElseThrow(
                        () -> new RegistrationNotFoundException(registrationId, userEntity)
                );
    }

    public Registration createRegistrationByCurrentUser(
            RegistrationPostRequestBody registrationPostRequestBody, UserEntity currentUser) {
        var crashSessionEntity =
                crashSessionService.getCrashSessionEntityBySessionId(
                        registrationPostRequestBody.sessionId());

        registrationEntityRepository
                .findByUserAndSession(currentUser, crashSessionEntity)
                .ifPresent(
                        registrationEntity -> {
                            throw new RegistrationAlreadyExistsException(
                                    registrationEntity.getRegistrationId(), currentUser
                            );
                        }
                );

        var registrationEntity = RegistrationEntity.of(currentUser, crashSessionEntity);
        var registration =  Registration.from(registrationEntityRepository.save(registrationEntity));

        slackService.sendSlackNotification(registration);

        return registration;
    }

    public void deleteRegistrationByRegistrationIdAndCurrentUser(
            Long registrationId, UserEntity currentUser) {
        var registrationEntity =
                getRegistrationEntityByRegistrationIdAndUserEntity(registrationId, currentUser);
        registrationEntityRepository.delete(registrationEntity);
    }

    public CrashSessionRegistrationStatus getCrashSessionRegistrationStatusBySessionIdAndCurrentUser(
            Long sessionId, UserEntity currentUser) {
        var crashSessionEntity = crashSessionService.getCrashSessionEntityBySessionId(sessionId);
        var registrationEntity =
                registrationEntityRepository.findByUserAndSession(currentUser, crashSessionEntity);

        return new CrashSessionRegistrationStatus(
                sessionId,
                registrationEntity.isPresent(),
                registrationEntity.map(RegistrationEntity::getRegistrationId).orElse(null));
    }
}
