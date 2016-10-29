package hu.esamu.rft.esamurft.app;

import com.google.protobuf.ByteString;
import hu.esamu.rft.esamurft.protos.EsamuRFTMessages;
import org.apache.commons.io.IOUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main {
    private static final String URL = "http://localhost:8080/esamu";
    private static final String IMAGE_EXAMPLE = "images/image.png";

    public static byte[] imageConverter(String image) throws IOException {
        InputStream is = Main.class.getClassLoader().getResourceAsStream(image);

        return IOUtils.toByteArray(is);
    }

    public static void main(String[] args) throws IOException {
        ByteString bs = ByteString.copyFrom(imageConverter(IMAGE_EXAMPLE));

        URL obj = new URL(URL);

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/octet-stream");

        EsamuRFTMessages.item message = EsamuRFTMessages.item.newBuilder().setName("soma").setLatitude(1).setLongitude(2).setDescription("asd").setTimestamp(234).setImage(bs).build();

        con.setDoOutput(true);

        System.out.println("\nSending 'POST' request to URL : " + URL);

        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.write(message.toByteArray());
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();

        System.out.println("Post parameters : \n" + message.toString());
        System.out.println("Response Code : " + responseCode);
    }
}
