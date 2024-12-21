package com.comesfullcircle.crash.model.slack;

public record SlackNotificationBlock(
        String type,
        Object text
) {
}
