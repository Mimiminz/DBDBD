import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;

public class GetLocation {
    public static void main(String[] args) {
        try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));

            String ip = in.readLine(); //you get the IP as a String
            System.out.println(ip);
            URL url = new URL("http://api.ipstack.com/"+ ip + "?access_key=d9352d4c38b5a09e0b35f0211126df15&format=1");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer stringBuffer = new StringBuffer();
            String inputLine;
            while ((inputLine = bufferedReader.readLine()) != null)  {
                stringBuffer.append(inputLine);
                stringBuffer.append("\n");
            }
            bufferedReader.close();

            String response = stringBuffer.toString();
            System.out.println(response);

            JSONObject jsonObject = new JSONObject(response);
            BigDecimal lat = jsonObject.getBigDecimal("latitude");
            BigDecimal lng = jsonObject.getBigDecimal("longitude");
            System.out.println(lat);
            System.out.println(lng);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}