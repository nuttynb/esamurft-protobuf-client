package hu.esamu.rft.esamurft.api.util;

import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;

@UtilityClass
public class Constants {

    public final String POST_METHOD_NAME = "POST";
    public final byte[] POST_PARAMETER_SEPARATOR = "&".getBytes(StandardCharsets.UTF_8);
    public final String EQUAL_SIGN = "=";
    public final String REGISTER_URL = "http://localhost:8080/esamu/register";
    public final String LOGIN_URL = "http://localhost:8080/esamu/login";
    public final String MESSAGE_URL = "http://localhost:8080/esamu/message";
    public final String USERNAME_KEY = "username";
    public final String PASSWORD_KEY = "password";
    public final String FACEBOOK_ID_TOKEN = "facebookIdToken";
    public final String GOOGLE_ID_TOKEN = "googleIdToken";
}
