package hu.esamu.rft.esamurft.api;

import hu.esamu.rft.esamurft.api.exception.SendingException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class EsamuClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(EsamuClient.class);

    private static final String REGISTER_URL = "http://localhost:8080/esamu/register";
    private static final String LOGIN_URL = "http://localhost:8080/esamu/login";
    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";

    private String username;
    private String password;
    private String facebookId;
    private String googleId;

    public enum SendType {
        REGISTER, LOGIN
    }

    public void send(SendType sendType) throws IOException, SendingException {
        if (!StringUtils.isNoneBlank(username, password, facebookId, googleId)) {
            throw new SendingException("All parameters are blank!");
        }
        // TODO csoportokba szedni őket
        // TODO blank logika javítása

        // TODO ha semmit se állítunk be, dobjunk kivételt
        URL registerUrl = sendType.equals(SendType.REGISTER) ? new URL(REGISTER_URL) : new URL(LOGIN_URL);
        LOGGER.info(registerUrl.toString());
        HttpURLConnection connection = (HttpURLConnection) registerUrl.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("charset", "utf-8");
        connection.setDoOutput(true);

        String usernameParam = USERNAME_KEY + "=" + username;
        String passwordParam = PASSWORD_KEY + "=" + password;

        try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
            wr.write(usernameParam.getBytes(StandardCharsets.UTF_8));
            wr.write("&".getBytes(StandardCharsets.UTF_8));
            wr.write(passwordParam.getBytes(StandardCharsets.UTF_8));
        }

        int responseCode = connection.getResponseCode();
        LOGGER.info("HTTP status code " + responseCode);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }
}
