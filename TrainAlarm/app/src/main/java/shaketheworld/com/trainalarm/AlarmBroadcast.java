package shaketheworld.com.trainalarm;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Anand Mishra on 8/25/2016.
 */
public class AlarmBroadcast extends BroadcastReceiver {
Logger logger = Logger.getLogger("AlarmBroadcast");
    @Override
    public void onReceive(Context context, Intent intent) {
        logger.log(Level.INFO, "Inside the onReceive method");
        // For our recurring task, we'll just display a message
        Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();
        logger.info("trainNumber.." + intent.getStringExtra("trainNumber") + "minutes.." + intent.getStringExtra("minutes") + "destCode.."+intent.getStringExtra("destCode"));

    }


}
