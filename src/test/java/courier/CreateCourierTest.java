package courier;

import courier.model.AuthorizationCredentials;
import courier.model.DataCourier;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

public class CreateCourierTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    //    курьера можно создать;
    //    успешный запрос возвращает ok: true;
    //    запрос возвращает правильный код ответа;
    @Test
    @Step("курьера можно создать; успешный запрос возвращает ok: true; запрос возвращает правильный код ответа;")
    public void testCreateNewCourier() {
        DataCourier dataCourier = new DataCourier(
                "anitkabanditkakorn1",
                "anitkabanditkakorn",
                "anitkabanditkakorn"
        );
        given()
                .header("Content-type", "application/json")
                .body(dataCourier)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(201)
                .body("ok", is(true));
    }

    @After
    public void deleteCreateNewCourier() {
        AuthorizationCredentials authorizationCredentials = new AuthorizationCredentials(
                "anitkabanditkakorn1",
                "anitkabanditkakorn"
        );

        Integer courierId = given()
                .header("Content-type", "application/json")
                .body(authorizationCredentials)
                .when()
                .post("/api/v1/courier/login")
                .getBody()
                .jsonPath()
                .get("id");

        if (courierId != null) {
            given().when().delete("/api/v1/courier/" + courierId).then().statusCode(200);
        }
    }

    //    нельзя создать двух одинаковых курьеров;
    //    если создать пользователя с логином, который уже есть, возвращается ошибка.
    @Test
    @Step("нельзя создать двух одинаковых курьеров; если создать пользователя с логином, который уже есть, возвращается ошибка")
    public void testDuplicate() {
        DataCourier dataCourier = new DataCourier(
                "anitkabanditkakorn1",
                "anitkabanditkakorn",
                "anitkabanditkakorn"
        );
        given()
                .header("Content-type", "application/json")
                .body(dataCourier)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(201)
                .body("ok", is(true));
        given()
                .header("Content-type", "application/json")
                .body(dataCourier)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(409)
                .body("message", is("Этот логин уже используется. Попробуйте другой."));
    }

    //    чтобы создать курьера, нужно передать в ручку все обязательные поля;
    //    если одного из полей нет, запрос возвращает ошибку;
    @Test
    @Step("чтобы создать курьера, нужно передать в ручку все обязательные поля; если одного из полей нет, запрос возвращает ошибку;")
    public void testWithoutLogin() {
        DataCourier dataCourier = new DataCourier(
                null,
                "anitkabanditkakorn",
                "anitkabanditkakorn"
        );

        given()
                .header("Content-type", "application/json")
                .body(dataCourier)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(400)
                .body("message", is("Недостаточно данных для создания учетной записи"));
    }
}



