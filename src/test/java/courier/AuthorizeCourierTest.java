package courier;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

public class AuthorizeCourierTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    public static class AuthorizationCredentials {
        public final String login;
        public final String password;

        public AuthorizationCredentials(String login, String password) {
            this.login = login;
            this.password = password;
        }
    }

    //курьер может авторизоваться;
    //успешный запрос возвращает id
    @Test
    @Step("Курьер может авторизоваться успешно и возвращается id")
    public void testAuthorize() {
        AuthorizationCredentials authorizationCredentials = new AuthorizationCredentials(
                "anitkabandirka2",
                "anitkabandirka12342"
        );
        given()
                .header("Content-type", "application/json")
                .body(authorizationCredentials)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(200)
                .body("id", is(241192));
    }

    //для авторизации нужно передать все обязательные поля, если какого-то поля нет, запрос возвращает ошибку;
    @Test
    @Step("для авторизации нужно передать все обязательные поля, если какого-то поля нет, запрос возвращает ошибку")
    public void testAuthorizeWithoutLogin() {
        AuthorizationCredentials authorizationCredentials = new AuthorizationCredentials(
                null,
                "anitkabanditkakorn"
        );
        given()
                .header("Content-type", "application/json")
                .body(authorizationCredentials)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(400)
                .body("message", is("Недостаточно данных для входа"));
    }

    //если авторизоваться под несуществующим пользователем, запрос возвращает ошибку;
    //система вернёт ошибку, если неправильно указать логин;
    @Test
    @Step("если авторизоваться под несуществующим пользователем/неправильно указан логин, запрос возвращает ошибку")
    public void testAuthorizeNonExistenUser() {
        AuthorizationCredentials authorizationCredentials = new AuthorizationCredentials(
                "anitkanonexisten",
                "anitkabanditkakorn"
        );
        given()
                .header("Content-type", "application/json")
                .body(authorizationCredentials)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(404)
                .body("message", is("Учетная запись не найдена"));
    }

    //система вернёт ошибку, если неправильно указать пароль;
    @Test
    @Step("система вернёт ошибку, если неправильно указать пароль")
    public void testAuthorizeWrongPassword() {
        AuthorizationCredentials authorizationCredentials = new AuthorizationCredentials(
                "anitkabanditkakorn1",
                "anitkabanditkakorn3"
        );
        given()
                .header("Content-type", "application/json")
                .body(authorizationCredentials)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(404)
                .body("message", is("Учетная запись не найдена"));
    }
}
