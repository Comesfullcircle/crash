package com.comesfullcircle.crash.model.crashsession;

import com.comesfullcircle.crash.model.entity.CrashSessionEntity;
import com.comesfullcircle.crash.model.sessionspeaker.SessionSpeaker;

import java.time.ZonedDateTime;

public record CrashSession(
        Long sessionId,
        String title,
        String body,
        CrashSessionCategory category,
        ZonedDateTime dateTime,
        SessionSpeaker speaker
) {
    public static CrashSession from(CrashSessionEntity crashSessionEntity){
        return new CrashSession(
                crashSessionEntity.getSessionId(),
                crashSessionEntity.getTitle(),
                crashSessionEntity.getBody(),
                crashSessionEntity.getCategory(),
                crashSessionEntity.getDateTime(),
                SessionSpeaker.from(crashSessionEntity.getSpeaker()));
    }
}
