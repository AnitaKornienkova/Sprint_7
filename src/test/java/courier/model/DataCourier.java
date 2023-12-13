package courier.model;

public class DataCourier {
    public final String login;
    public final String password;
    public final String firstName;

    public DataCourier(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }
}
