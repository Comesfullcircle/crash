package com.comesfullcircle.crash.model.sessionspeaker;


public record SessionSpeakerPatchRequestBody(
        // 부분 수정이 가능해야하므로 @NotEmpty 가 붙어서는 안됨
        String company,
        String name,
        String description
) {
}
