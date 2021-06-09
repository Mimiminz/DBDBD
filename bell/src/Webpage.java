import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URISyntaxException;


public class Webpage {
    public static void main(String args[]){

        String urlLink = "http://map.naver.com/index.nhn" +
                "?slng=127.0475211" +
                "&slat=37.27944776" + "&stext=출발지" +
                "&elng=127.05271438629" +
                "&elat=37.593882286327" + "&etext=목적지" +
                "&menu=route&pathType=1";

            try{
                Desktop.getDesktop().browse(new URI(urlLink));
            }catch(URISyntaxException e){
                e.printStackTrace();;
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    };

