package com.comesfullcircle.crash.controller;

import com.comesfullcircle.crash.model.sessionspeaker.SessionSpeaker;
import com.comesfullcircle.crash.model.sessionspeaker.SessionSpeakerPatchRequestBody;
import com.comesfullcircle.crash.model.sessionspeaker.SessionSpeakerPostRequestBody;
import com.comesfullcircle.crash.model.user.User;
import com.comesfullcircle.crash.model.user.UserAuthenticationResponse;
import com.comesfullcircle.crash.model.user.UserLoginRequestBody;
import com.comesfullcircle.crash.model.user.UserSignUpRequestBody;
import com.comesfullcircle.crash.service.SessionSpeakerService;
import com.comesfullcircle.crash.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/session-speakers")
public class SessionSpeakerController {

    @Autowired
    private SessionSpeakerService sessionSpeakerService;

   @GetMapping
    public ResponseEntity<List<SessionSpeaker>> getSessionSpeakers() {
       var sessionSpeakers = sessionSpeakerService.getSessionSpeakers();
       return ResponseEntity.ok(sessionSpeakers);
   }

    @GetMapping("/{speakerId}")
    public ResponseEntity<SessionSpeaker> getSessionSpeakerBySpeakerId(@PathVariable Long speakerId)
    {
        var sessionSpeaker = sessionSpeakerService.getSessionSpeakerBySpeakerId(speakerId);
        return ResponseEntity.ok(sessionSpeaker);
    }

    @PostMapping
    public ResponseEntity<SessionSpeaker> createSessionSpeaker(
            @Valid @RequestBody
            SessionSpeakerPostRequestBody sessionSpeakerPostRequestBody
    ) {
        var sessionSpeaker = sessionSpeakerService.createSessionSpeaker(sessionSpeakerPostRequestBody);
        return ResponseEntity.ok(sessionSpeaker);
    }

    @PatchMapping("/{speakerId}")
    public ResponseEntity<SessionSpeaker> updateSessionSpeaker(
            @PathVariable Long speakerId,
            @RequestBody SessionSpeakerPatchRequestBody sessionSpeakerPatchRequestBody)
    {
        var sessionSpeaker = sessionSpeakerService.updateSessionSpeaker(speakerId, sessionSpeakerPatchRequestBody);
        return ResponseEntity.ok(sessionSpeaker);
    }

    @DeleteMapping("/{speakerId}")
    public ResponseEntity<Void> deleteSessionSpeaker(@PathVariable Long speakerId)
    {
        sessionSpeakerService.deleteSessionSpeaker(speakerId);
        return ResponseEntity.noContent().build();
    }

}
