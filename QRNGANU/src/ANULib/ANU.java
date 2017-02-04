/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ANULib;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import javax.json.*;

/**
 *
 * @author Sean
 */
public class ANU {
    public static int[] QRNG(int arrayLength, String dataType, int blockSize) throws Exception {
        if (arrayLength < 1){
            throw new BadRNGRequestException("arrayLength too small");
        }
        else if (arrayLength > 1024){
            throw new BadRNGRequestException("arrayLength too large");
        }
        else if (!"uint8".equals(dataType) && !"uint16".equals(dataType) && !"hex16".equals(dataType)){
            throw new BadRNGRequestException("Bad dataType");
        }
        else if ("hex16".equals(dataType) && blockSize < 1){
            throw new BadRNGRequestException("blockSize too small");
        }
        else if ("hex16".equals(dataType) && blockSize > 1024){
            throw new BadRNGRequestException("blockSize too large");
        }
        StringBuilder sb = new StringBuilder("https://qrng.anu.edu.au/API/jsonI.php?length=");
        sb.append(arrayLength);
        sb.append("&type=").append(dataType);
        if ("hex16".equals(dataType)){
            sb.append("&size=").append(blockSize);
        }
        URL url = new URL(sb.toString());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        JsonReader jsonReader;
        InputStream in = connection.getInputStream();
        jsonReader = Json.createReader(in);
        JsonObject jsonObject = jsonReader.readObject();
        jsonReader.close();
        in.close();
        JsonArray jsonArray = jsonObject.getJsonArray("data");
        int[] resultArray = new int[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); i++){
            resultArray[i] = jsonArray.getInt(i);
        }
        return resultArray;
    }
}

class BadRNGRequestException extends Exception {
    BadRNGRequestException() {
        super();
    }
    
    BadRNGRequestException(String message) {
        super(message);
    }
}
