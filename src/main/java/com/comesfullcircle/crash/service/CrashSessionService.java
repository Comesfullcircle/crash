package com.comesfullcircle.crash.service;

import com.comesfullcircle.crash.exception.crashsession.CrashSessionNotFoundException;
import com.comesfullcircle.crash.model.crashsession.CrashSession;
import com.comesfullcircle.crash.model.crashsession.CrashSessionPatchRequestBody;
import com.comesfullcircle.crash.model.crashsession.CrashSessionPostRequestBody;
import com.comesfullcircle.crash.model.entity.CrashSessionEntity;
import com.comesfullcircle.crash.repository.CrashSessionEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class CrashSessionService {
    @Autowired
    private CrashSessionEntityRepository crashSessionEntityRepository;
    @Autowired
    private SessionSpeakerService sessionSpeakerService;

    public List<CrashSession> getCrashSessions(){
        return crashSessionEntityRepository.findAll().stream().map(CrashSession::from).toList();
    }

    public CrashSession getCrashSessionBySessionId(Long sessionId){
        var crashSessionEntity = getCrashSessionEntityBySessionId(sessionId);
        return CrashSession.from(crashSessionEntity);
    }

    public CrashSession createCrashSession(CrashSessionPostRequestBody crashSessionPostRequestBody){
        var sessionSpeakerEntity =
                sessionSpeakerService.getSessionSpeakerEntityBySpeakerId(
                        crashSessionPostRequestBody.speakerId());

        var crashSessionEntity =
                CrashSessionEntity.of(
                        crashSessionPostRequestBody.title(),
                        crashSessionPostRequestBody.body(),
                        crashSessionPostRequestBody.category(),
                        crashSessionPostRequestBody.dateTime(),
                        sessionSpeakerEntity
                );

        return CrashSession.from(
                crashSessionEntityRepository.save(crashSessionEntity)
        );
    }

    public CrashSession updateCrashSession(
            Long sessionId,
            CrashSessionPatchRequestBody crashSessionPatchRequestBody){
       var crashSessionEntity = getCrashSessionEntityBySessionId(sessionId);

       if(!ObjectUtils.isEmpty(crashSessionPatchRequestBody.title())){
           crashSessionEntity.setTitle(
                   crashSessionPatchRequestBody.title());
       }

        if(!ObjectUtils.isEmpty(crashSessionPatchRequestBody.body())){
            crashSessionEntity.setBody(
                    crashSessionPatchRequestBody.body());
        }

        if(!ObjectUtils.isEmpty(crashSessionPatchRequestBody.category())){
            crashSessionEntity.setCategory(
                    crashSessionPatchRequestBody.category());
        }

        if(!ObjectUtils.isEmpty(crashSessionPatchRequestBody.dateTime())){
            crashSessionEntity.setDateTime(
                    crashSessionPatchRequestBody.dateTime());
        }

        if(!ObjectUtils.isEmpty(crashSessionPatchRequestBody.speakerId())){
           var sessionSpeakerEntity =
                   sessionSpeakerService.getSessionSpeakerEntityBySpeakerId(
                   crashSessionPatchRequestBody.speakerId()
           );

           crashSessionEntity.setSpeaker(sessionSpeakerEntity);
        }

        return CrashSession.from(crashSessionEntityRepository.save(crashSessionEntity));
    }

    public void deleteCrashSession(Long sessionId){
        var crashSessionEntity = getCrashSessionEntityBySessionId(sessionId);
        crashSessionEntityRepository.delete(crashSessionEntity);
    }

    public CrashSessionEntity getCrashSessionEntityBySessionId(Long sessionId){
        return crashSessionEntityRepository
                .findById(sessionId)
                .orElseThrow(() -> new CrashSessionNotFoundException(sessionId));
    }
}
