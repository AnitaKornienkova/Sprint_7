package courier;

import courier.model.Order;
import io.qameta.allure.Step;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import io.restassured.RestAssured;
import org.junit.Before;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private final List<String> color;

    private final Matcher<Integer> expected;

    public CreateOrderTest(List<String> color, Matcher<Integer> expected) {
        this.color = color;
        this.expected = expected;
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @Step("Создание заказа")
    public void testCreateOrder() {
        Order order = new Order(
                "Иван",
                "Иванов",
                "Москва.15",
                "м.Пушкина",
                "+79668854201",
                12,
                "2023-12-11",
                "позвонить заранее",
                color
        );

        given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post("/api/v1/orders")
                .then()
                .statusCode(201)
                .body("track", is(expected));
    }

    @Parameterized.Parameters
    public static Object[][] getOrder() {
        return new Object[][]{
                {List.of("BLACK"), new TrackNumberMatcher()},
                {List.of("GREY"), new TrackNumberMatcher()},
                {List.of(), new TrackNumberMatcher()},
                {null, new TrackNumberMatcher()},
                {List.of("BLACK", "GREY"), new TrackNumberMatcher()},
        };
    }

    private static class TrackNumberMatcher extends TypeSafeMatcher<Integer> {
        @Override
        protected boolean matchesSafely(Integer integer) {
            return integer > 0;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("is integer");
        }
    }
}

