package courier;

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
    private final Order order;

    private final Matcher<Integer> expected;

    public CreateOrderTest(Order order, Matcher<Integer> expected) {
        this.order = order;
        this.expected = expected;
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @Step("Создание заказа")
    public void testCreateOrder() {
        given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post("/api/v1/orders")
                .then()
                .statusCode(201)
                .body("track", is(expected));
    }

    @Parameterized.Parameters //
    public static Object[][] getOrder() {
        return new Object[][]{
                {new Order("Иван", "Иванов", "Москва.15", "м.Пушкина", "+79668854201", 12, "2023-12-11", "позвонить заранее", List.of("BLACK")), new TrackNumberMatcher()},
                {new Order("Иван", "Иванов", "Москва.15", "м.Пушкина", "+79668854201", 12, "2023-12-11", "позвонить заранее", List.of("GREY")), new TrackNumberMatcher()},
                {new Order("Иван", "Иванов", "Москва.15", "м.Пушкина", "+79668854201", 12, "2023-12-11", "позвонить заранее", List.of()), new TrackNumberMatcher()},
                {new Order("Иван", "Иванов", "Москва.15", "м.Пушкина", "+79668854201", 12, "2023-12-11", "позвонить заранее", null), new TrackNumberMatcher()},
                {new Order("Иван", "Иванов", "Москва.15", "м.Пушкина", "+79668854201", 12, "2023-12-11", "позвонить заранее", List.of("BLACK", "GREY")), new TrackNumberMatcher()},
        };
    }

    public static class Order {
        public final String firstName;
        public final String lastName;
        public final String address;
        public final String metroStation;
        public final String phone;
        public final int rentTime;
        public final String deliveryDate;
        public final String comment;
        public final List<String> color;

        public Order(String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate, String comment, List<String> color) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.address = address;
            this.metroStation = metroStation;
            this.phone = phone;
            this.rentTime = rentTime;
            this.deliveryDate = deliveryDate;
            this.comment = comment;
            this.color = color;
        }
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

