package courier.model;

public class AuthorizationCredentials {
    public final String login;
    public final String password;

    public AuthorizationCredentials(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
