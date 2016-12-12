package hu.esamu.rft.esamurft.app;

import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;
import hu.esamu.rft.esamurft.api.EsamuRFTMessages;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;

public class Main {
    private static final String REGISTER_URL = "http://localhost:8080/esamu/register";
    private static final String MESSAGE_URL = "http://localhost:8080/esamu/message";
    private static final String IMAGE_EXAMPLE = "images/image.png";

    private static final String USERNAME_KEY = "username";

    private static List<AbstractMap.SimpleEntry> users = new ArrayList<>();
    private static List<String> messages = new ArrayList<>();

    private static Logger LOG = LoggerFactory.getLogger(Main.class);

    private static Random r = new Random();

    public static byte[] imageConverter(String image) throws IOException {
        InputStream is = Main.class.getClassLoader().getResourceAsStream(image);
        return IOUtils.toByteArray(is);
    }

    public static void main(String[] args) throws IOException {

        URL messageUrl = new URL(MESSAGE_URL);
        URL registerUrl = new URL(REGISTER_URL);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Number of usernames: ");
        int numberOfUsers = Integer.parseInt(scanner.next());
        System.out.println("Number of messages: ");
        int numberOfMessages = Integer.parseInt(scanner.next());
        for (int i = 0; i < numberOfUsers; i++) {
            String username = RandomStringUtils.random(r.nextInt(15), true, true);
            String id = registerUser((HttpURLConnection) registerUrl.openConnection(), username);
            LOG.info("User id: " + id);
            LOG.info("------------" + (i + 1) + ".------------");
            users.add(new AbstractMap.SimpleEntry(id, username));
        }

        for (int i = 0; i < numberOfMessages; i++) {
            sendMessage((HttpURLConnection) messageUrl.openConnection(), r.nextInt(numberOfUsers));
        }
    }

    private static String registerUser(HttpURLConnection connection, String user) throws IOException {
        connection.setRequestMethod("POST");

        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("charset", "utf-8");
        connection.setDoOutput(true);

        LOG.info("Sending 'POST' request to URL : " + REGISTER_URL);
        String param = USERNAME_KEY + "=" + user;

        try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
            wr.write(param.getBytes(StandardCharsets.UTF_8));
        }

        int responseCode = connection.getResponseCode();

        LOG.info("Post parameters: " + param);
        LOG.info("Response code: " + responseCode);

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuffer result = new StringBuffer();
        String line;
        while ((line = br.readLine()) != null) {
            result.append(line);
        }

        return result.toString();
    }

    private static void sendMessage(HttpURLConnection connection, int userIndex) throws IOException {
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/octet-stream");

        ByteString bs = ByteString.copyFrom(imageConverter(IMAGE_EXAMPLE));
        Instant time = Instant.now();
        Timestamp timestamp = Timestamp.newBuilder().setSeconds(time.getEpochSecond())
                .setNanos(time.getNano()).build();
        AbstractMap.SimpleEntry user = users.get(userIndex);
        EsamuRFTMessages.item message = EsamuRFTMessages.item.newBuilder().setId((String) user.getKey()).setName((String) user.getValue()).setLatitude(0).setLongitude(0).setDescription("Description").setTimestamp(timestamp.getSeconds()).setImage(bs).build();

        connection.setDoOutput(true);

        LOG.info("Sending 'POST' request to URL : " + MESSAGE_URL);

        try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
            wr.write(message.toByteArray());
        }

        int responseCode = connection.getResponseCode();

        LOG.info("Post parameters : \n" + message.toString());
        LOG.info("Response Code : " + responseCode);
    }
}
