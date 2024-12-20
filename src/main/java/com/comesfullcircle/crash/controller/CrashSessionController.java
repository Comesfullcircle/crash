package com.comesfullcircle.crash.controller;

import com.comesfullcircle.crash.model.crashsession.CrashSession;
import com.comesfullcircle.crash.model.crashsession.CrashSessionPatchRequestBody;
import com.comesfullcircle.crash.model.crashsession.CrashSessionPostRequestBody;
import com.comesfullcircle.crash.model.sessionspeaker.SessionSpeaker;
import com.comesfullcircle.crash.model.sessionspeaker.SessionSpeakerPatchRequestBody;
import com.comesfullcircle.crash.model.sessionspeaker.SessionSpeakerPostRequestBody;
import com.comesfullcircle.crash.service.CrashSessionService;
import com.comesfullcircle.crash.service.SessionSpeakerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/crash-sessions")
public class CrashSessionController {

    @Autowired
    private CrashSessionService crashSessionService;

   @GetMapping
    public ResponseEntity<List<CrashSession>> getCrashSessions() {
       var crashSessions = crashSessionService.getCrashSessions();
       return ResponseEntity.ok(crashSessions);
   }

    @GetMapping("/{sessionId}")
    public ResponseEntity<CrashSession> getSessionSpeakerBySpeakerId(@PathVariable Long sessionId)
    {
        var crashSession = crashSessionService.getCrashSessionBySessionId(sessionId);
        return ResponseEntity.ok(crashSession);
    }

    @PostMapping
    public ResponseEntity<CrashSession> createCrashSession(
            @Valid @RequestBody
            CrashSessionPostRequestBody crashSessionPostRequestBody
    ) {
        var crashSession = crashSessionService.createCrashSession(crashSessionPostRequestBody);
        return ResponseEntity.ok(crashSession);
    }

    @PatchMapping("/{sessionId}")
    public ResponseEntity<CrashSession> updateCrashSession(
            @PathVariable Long sessionId,
            @RequestBody CrashSessionPatchRequestBody crashSessionPatchRequestBody)
    {
        var crashSession = crashSessionService.updateCrashSession(sessionId, crashSessionPatchRequestBody);
        return ResponseEntity.ok(crashSession);
    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> deleteCrashSession(@PathVariable Long sessionId)
    {
        crashSessionService.deleteCrashSession(sessionId);
        return ResponseEntity.noContent().build();
    }

}
