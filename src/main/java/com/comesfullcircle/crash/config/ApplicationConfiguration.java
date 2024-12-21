package com.comesfullcircle.crash.config;

import com.comesfullcircle.crash.model.coinbase.PriceResponse;
import com.comesfullcircle.crash.model.crashsession.CrashSession;
import com.comesfullcircle.crash.model.crashsession.CrashSessionCategory;
import com.comesfullcircle.crash.model.crashsession.CrashSessionPostRequestBody;
import com.comesfullcircle.crash.model.entity.CrashSessionEntity;
import com.comesfullcircle.crash.model.exchange.ExchangeResponse;
import com.comesfullcircle.crash.model.sessionspeaker.SessionSpeaker;
import com.comesfullcircle.crash.model.sessionspeaker.SessionSpeakerPostRequestBody;
import com.comesfullcircle.crash.model.user.UserSignUpRequestBody;
import com.comesfullcircle.crash.service.CrashSessionService;
import com.comesfullcircle.crash.service.SessionSpeakerService;
import com.comesfullcircle.crash.service.UserService;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

@Configuration
public class ApplicationConfiguration {

    /*private static final WebClient webClient = WebClient.builder()
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .build();*/

    //Spring RestClient 공부 - API 실습 및 연동
    private static final RestClient restClient = RestClient.create();

    private static final Logger logger = LoggerFactory.getLogger(ApplicationConfiguration.class);

    private static final Faker faker = new Faker();

    @Autowired
    private UserService userService;

    @Autowired
    private SessionSpeakerService sessionSpeakerService;

    @Autowired
    private CrashSessionService crashSessionService;

    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {
           /*      // TODO : 유저 및 세션스피커 생성
                createTestUsers();
                createTestSessionSpeakers(10);*/
                // private Double getUsdToKrwExchangeRate(){
                var bitcoinUsdPrice = getBitcoinUsdPrice();
                //TODO : USD to KRW 환율 조회
                var usdToKrwExchangeRate = getUsdToKrwExchangeRate();
                //TODO : Bitcoin KRW 가격 계산
                var koreanPremium = 1.1;
                var bitcoinKrwPrice = bitcoinUsdPrice + usdToKrwExchangeRate;

                logger.info(String.format("BIT KRW: %.2f", bitcoinKrwPrice));
            }
        };
    }

    private Double getBitcoinUsdPrice(){
        var response =
                restClient
                .get()
                .uri("https://api.coinbase.com/v2/prices/BTC-USD/buy")
                .retrieve()
                        .onStatus(HttpStatusCode::is4xxClientError,
                                (req, res) -> {
                                // TODO: 클라이언트  에러 예외 처리
                                    logger.error(new String(res.getBody().readAllBytes(), StandardCharsets.UTF_8));
                                })
                .body(PriceResponse.class);

        assert response != null;
        return Double.parseDouble(response.data().amount());
    }



    private Double getUsdToKrwExchangeRate(){

        String redirectUrl = "https://www.koreaexim.go.kr/site/program/financial/exchangeJSON?authkey=AUTH키발급한거!!&searchdate=20241220&data=AP01";

        var response =
                restClient
                        .get()
                        .uri(redirectUrl)
                        .retrieve()
                        .onStatus(
                                HttpStatusCode::is4xxClientError,
                                (req, res) -> {
                                    logger.error(new String(res.getBody().readAllBytes(), StandardCharsets.UTF_8));
                                })
                        .body(ExchangeResponse[].class);


        if (response == null || response.length == 0) {
            throw new IllegalStateException("API response is null or empty");
        }

            var usdToKrwExchangeRate =
                    Arrays.stream(response).filter(
                            exchangeResponse -> exchangeResponse.cur_unit().equals("USD")
                    ).findFirst().orElseThrow();

            return Double.parseDouble(usdToKrwExchangeRate.deal_bas_r().replace(",", ""));


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

      sessionSpeakers.forEach(
              sessionSpeaker -> {
                  int numberOfSessions = new Random().nextInt(4)+1;
                  IntStream.range(0, numberOfSessions).forEach(i -> createTestCrashSession(sessionSpeaker));
              }
      );
    }


    private SessionSpeaker createTestSessionSpeaker(){
        var name = faker.name().fullName();
        var company = faker.company().name();
        var description = faker.shakespeare().romeoAndJulietQuote();

        return sessionSpeakerService.createSessionSpeaker(
                new SessionSpeakerPostRequestBody(company, name, description));
    }

    private void createTestCrashSession(SessionSpeaker sessionSpeaker){
        var title = faker.book().title();
        var body =
                faker.shakespeare().asYouLikeItQuote()
                + faker.shakespeare().hamletQuote()
                + faker.shakespeare().kingRichardIIIQuote()
                + faker.shakespeare().romeoAndJulietQuote();

        crashSessionService.createCrashSession(new CrashSessionPostRequestBody(
                title,
                body,
                getRandomCategory(),
                ZonedDateTime.now().plusDays(new Random().nextInt(2) +1),
                sessionSpeaker.speakerId()
        ));
    }

    private CrashSessionCategory getRandomCategory(){
        var categories = CrashSessionCategory.values();
        int randomIndex = new Random().nextInt(categories.length);
        return categories[randomIndex];
    }
}
