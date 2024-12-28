package com.comesfullcircle.crash.service;

import com.comesfullcircle.crash.model.registration.Registration;
import com.comesfullcircle.crash.model.slack.SlackNotificationBlock;
import com.comesfullcircle.crash.model.slack.SlackNotificationMessage;
import com.comesfullcircle.crash.model.slack.SlackNotificationText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class SlackService {
    private static final Logger logger = LoggerFactory.getLogger(SlackService.class);
    private static final RestClient restClient = RestClient.create();

    public void sendSlackNotification(Registration registration){

        var linkText = getRegistrationPageLinkText(registration);

        var slackNotificationMessage = new SlackNotificationMessage(
                List.of(
                        new SlackNotificationBlock("section", new SlackNotificationText("mrkdwn",linkText))));

        var response = restClient
                .post()
                .uri("https://hooks.slack.com/services/T080SLYH2DC/B0861441F7F/본인거")
                .body(slackNotificationMessage)
                .retrieve()
                .body(String.class);

        logger.info(response);
    }

    private String getRegistrationPageLinkText(Registration registration){
        var baseLink = "https://dev-jayce.github.io/crash/registration.html?registration=";
        var registrationId = registration.registrationId();
        var username = registration.user().username();
        var sessionId = registration.session().sessionId();
        var link = baseLink + registrationId+"," +username+","+sessionId;
        return  "SEOL: CONFERENCE <" + link + " |Registration Details>";
    }
}
