package com.comesfullcircle.crash.config;

import com.comesfullcircle.crash.model.sessionspeaker.SessionSpeaker;
import com.comesfullcircle.crash.model.sessionspeaker.SessionSpeakerPostRequestBody;
import com.comesfullcircle.crash.model.user.UserSignUpRequestBody;
import com.comesfullcircle.crash.service.SessionSpeakerService;
import com.comesfullcircle.crash.service.UserService;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.IntStream;

@Configuration
public class ApplicationConfiguration {

    private static final Faker faker = new Faker();

    @Autowired
    private UserService userService;

    @Autowired
    private SessionSpeakerService sessionSpeakerService;

    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {
                // TODO : 유저 및 세션스피커 생성
                createTestUsers();
                createTestSessionSpeakers(10);
            }
        };
    }

    private void createTestUsers(){
        userService.signUp(new UserSignUpRequestBody("seol", "1234", "YUN SEOL","seol@crash.com"));
        userService.signUp(new UserSignUpRequestBody("sul", "1234", "YUN SUL","sul@crash.com"));
        userService.signUp(new UserSignUpRequestBody("green", "1234", "YUN GREEN","green@crash.com"));
        userService.signUp(new UserSignUpRequestBody("blue", "1234", "YUN BLUE","blue@crash.com"));

       /* userService.signUp(
                new UserSignUpRequestBody(
                        faker.name().name(), "1234", faker.name().fullName(),faker.internet().emailAddress()));
        userService.signUp(
                new UserSignUpRequestBody(
                        faker.name().name(), "1234", faker.name().fullName(),faker.internet().emailAddress()));
        userService.signUp(
                new UserSignUpRequestBody(
                        faker.name().name(), "1234", faker.name().fullName(),faker.internet().emailAddress()));
        */
    }

    private void createTestSessionSpeakers(int numberOfSpeakers) {
      var sessionSpeakers =
        IntStream.range(0, numberOfSpeakers).mapToObj(i -> createTestSessionSpeaker()).toList();
    }


    private SessionSpeaker createTestSessionSpeaker(){
        var name = faker.name().fullName();
        var company = faker.company().name();
        var description = faker.shakespeare().romeoAndJulietQuote();

        return sessionSpeakerService.createSessionSpeaker(
                new SessionSpeakerPostRequestBody(company, name, description));
    }
}
