package com.comesfullcircle.crash.model.registration;

import com.comesfullcircle.crash.model.crashsession.CrashSession;
import com.comesfullcircle.crash.model.entity.RegistrationEntity;
import com.comesfullcircle.crash.model.user.User;

public record Registration(
        Long registrationId,
        User user,
        CrashSession session
) {
    public static Registration from(RegistrationEntity registrationEntity){
        return new Registration(
                registrationEntity.getRegistrationId(),
                User.from(registrationEntity.getUser()),
                CrashSession.from(registrationEntity.getSession()));
    }
}
