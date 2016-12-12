package hu.esamu.rft.esamurft.api;

import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

public class EsamuClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(EsamuClient.class);

    private static final byte[] POST_PARAMETER_SEPARATOR = "&".getBytes(StandardCharsets.UTF_8);
    private static final String EQUAL_SIGN = "=";

    private static final String REGISTER_URL = "http://localhost:8080/esamu/register";
    private static final String LOGIN_URL = "http://localhost:8080/esamu/login";
    private static final String MESSAGE_URL = "http://localhost:8080/esamu/message";
    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";
    private static final String FACEBOOK_ID_TOKEN = "facebookIdToken";
    private static final String GOOGLE_ID_TOKEN = "googleIdToken";

    private String username;
    private String password;
    private String facebookIdToken;
    private String googleIdToken;

    private EsamuRFTMessages.item.Builder message;

    public EsamuClient() {
        message = EsamuRFTMessages.item.newBuilder();
    }

    public enum SendType {
        REGISTER, LOGIN
    }

    public void send(SendType sendType) throws IOException {
        // TODO ha semmit se állítunk be, dobjunk kivételt
        URL registerUrl = sendType.equals(SendType.REGISTER) ? new URL(REGISTER_URL) : new URL(LOGIN_URL);
        LOGGER.info(registerUrl.toString());
        HttpURLConnection connection = (HttpURLConnection) registerUrl.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("charset", "utf-8");
        connection.setDoOutput(true);

        String usernameParam = USERNAME_KEY + EQUAL_SIGN + username;
        String passwordParam = PASSWORD_KEY + EQUAL_SIGN + password;
        String fbTokenParam = FACEBOOK_ID_TOKEN + EQUAL_SIGN + facebookIdToken;
        String googleTokenParam = GOOGLE_ID_TOKEN + EQUAL_SIGN + googleIdToken;

        try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
            wr.write(usernameParam.getBytes(StandardCharsets.UTF_8));
            wr.write(POST_PARAMETER_SEPARATOR);
            wr.write(passwordParam.getBytes(StandardCharsets.UTF_8));
            wr.write(POST_PARAMETER_SEPARATOR);
            wr.write(fbTokenParam.getBytes(StandardCharsets.UTF_8));
            wr.write(POST_PARAMETER_SEPARATOR);
            wr.write(googleTokenParam.getBytes(StandardCharsets.UTF_8));
        }

        int responseCode = connection.getResponseCode();
        LOGGER.info("HTTP status code " + responseCode);
    }

    public void sendMessage() throws IOException {
        URL messageUrl = new URL(MESSAGE_URL);
        HttpURLConnection connection = (HttpURLConnection) messageUrl.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/octet-stream");
        connection.setDoOutput(true);

        Instant time = Instant.now();
        Timestamp timestamp = Timestamp.newBuilder().setSeconds(time.getEpochSecond())
                .setNanos(time.getNano()).build();
        message.setTimestamp(timestamp.getSeconds());

        try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
            wr.write(message.build().toByteArray());
        }

        LOGGER.info("Sending 'POST' request to URL : " + MESSAGE_URL);
        int responseCode = connection.getResponseCode();
    }

    public String getMessageDescription() {
        return message.getDescription();
    }

    public String getMessageName() {
        return message.getName();
    }

    public String getMessageId() {
        return message.getId();
    }

    public void setMessageDescription(String description) {
        message.setDescription(description);
    }

    public void setMessageName(String name) {
        message.setName(name);
    }

    public void setMessageId(String id) {
        message.setId(id);

    }

    public int getMessageLatitude() {
        return message.getLatitude();
    }

    public int getMessageLongitude() {
        return message.getLongitude();
    }

    public void setMessageLatitude(int latitude) {
        message.setLatitude(latitude);
    }

    public void setMessageLongitude(int longitude) {
        message.setLongitude(longitude);
    }

    public ByteString getMessageImage() {
        return message.getImage();
    }

    public void setMessageImage(ByteString image) {
        message.setImage(image);
    }

    public void setMessageImage(byte[] image) {
        ByteString bs = ByteString.copyFrom(image);
        message.setImage(bs);
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

    public String getFacebookIdToken() {
        return facebookIdToken;
    }

    public void setFacebookIdToken(String facebookIdToken) {
        this.facebookIdToken = facebookIdToken;
    }

    public String getGoogleIdToken() {
        return googleIdToken;
    }

    public void setGoogleIdToken(String googleIdToken) {
        this.googleIdToken = googleIdToken;
    }
}
