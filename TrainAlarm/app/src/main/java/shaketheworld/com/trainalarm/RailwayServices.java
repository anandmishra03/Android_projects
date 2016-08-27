package shaketheworld.com.trainalarm;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

/**
 * Created by Anand Mishra on 8/25/2016.
 */
public class RailwayServices {

    public Calendar getEstTime(int trainNumber, int minutes, String dest){
     return null;
    }

    public list<String> getDestCodes()
    {
        return null;
    }

    public boolean validateTrainNumber()
    {
        return true;
    }

    public Calendar calculateEstTim(int trainNumber, int minutes, String dest)
    {
        // call irctc service to get the estimated time to reach the station....
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response = httpclient.execute(new HttpGet(URL));
        StatusLine statusLine = response.getStatusLine();
        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            String responseString = out.toString();
            out.close();
            //..more logic
        } else{
            //Closes the connection.
            response.getEntity().getContent().close();
            throw new IOException(statusLine.getReasonPhrase());
        }
        Calendar cal = railwayServices.getEstTime(trainNumber, minutes, dest);

        return cal;
    }
}
