package shaketheworld.com.trainalarm;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import shaketheworld.com.MessagePop.AlarmMessageClass;

public class TrainAlarmActivity extends AppCompatActivity {
private Logger logger = Logger.getLogger("TrainAlarmActivity");
private static final String messageNoNetwork= "You need to be connected to the internet";
private static final String textNoNetwork = "NONETWORK";
private static final String dataInputError = "Error happened while reading the input... try to give correct input";
private static final String textDataInput="DATAINPUTERROR";

    public void setAlarm(View view)
    {
        int trainNumber=0;
        int minutes=15;
        String destinationCode="CSTM";
        boolean errorHappened = false;
        try {

            trainNumber = new Integer((((TextView) findViewById(R.id.trainNumber)) == null) ? "0" : ((TextView) findViewById(R.id.trainNumber)).getText().toString());
            logger.log(Level.INFO, "Printing the train number received" + trainNumber);

        }catch(Exception e){
            errorHappened = true;
            logger.log(Level.SEVERE, "Cannot convert the train number to Integer");
        }
        try {
             minutes = new Integer((((Spinner) findViewById(R.id.timeBefore)) != null) ?(((Spinner) findViewById(R.id.timeBefore)).getSelectedItem().toString()):"15");
            logger.log(Level.INFO, "Printing the minutes received" + minutes);
        }catch(Exception e){
            errorHappened = true;
            logger.log(Level.SEVERE, "Issue with spinner minutes");
        }
        try{
            destinationCode = ((Spinner)findViewById(R.id.destinationCode)).getSelectedItem().toString();
            logger.log(Level.INFO, "Printing the destinationCode received" + destinationCode);
        }catch(Exception e){
            errorHappened = true;
            logger.log(Level.SEVERE, "Issue with destination code");
        }

        if(errorHappened){
            AlarmMessageClass messageClass = new AlarmMessageClass();
            messageClass.createMessageOkBox(findViewById(R.id.parentLayoutAlarm), dataInputError, textDataInput);
            logger.log(Level.INFO, "Got an error creating snackBar");
        }else {
            logger.log(Level.INFO, "Forwarding to create Intent");
            int res = startAlarmSet(trainNumber, minutes, destinationCode, this.getApplicationContext());
        }

    }


    private int startAlarmSet(int trainNumber, int minutes, String dest, Context context)
    {
        RailwayServices services = new RailwayServices();
        Calendar estReachTime = services.calculateEstTim(trainNumber, minutes, dest);
        Intent alarmIntent = new Intent(context, AlarmBroadcast.class);

        alarmIntent.putExtra("trainNumber","");
        alarmIntent.putExtra("minutes","");
        alarmIntent.putExtra("destCode","");
        alarmIntent.putExtra("timeAlalrmSet",estReachTime.getTimeInMillis());
        PendingIntent pendInt = PendingIntent.getBroadcast(context,0,alarmIntent,0);
        return 0;
    }

    private void createSpinners()
    {
        logger.info("Inside Spinner");
        List<String> list = new ArrayList<String>();
        list.add("15");
        list.add("30");
        list.add("60");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        //dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner timeSpin =(Spinner) findViewById(R.id.timeBefore);
        timeSpin.setAdapter(dataAdapter);

        List<String> listCodes = new ArrayList<String>();
        listCodes.add("CSTM");
        listCodes.add("LKO");
        listCodes.add("CNB");
        ArrayAdapter<String> dataAdapterDest = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        //dataAdapterDest.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner destSpin =(Spinner) findViewById(R.id.destinationCode);
        destSpin.setAdapter(dataAdapterDest);

        logger.info("created both the Spinner");

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        System.out.println("Atleast onCreate is called");
        logger.info("Inside on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_alarm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //If network is not available do not proceed

        logger.info("Checking mobile network");
        if(!mobileDataEnabled((Context)this.getApplicationContext())){

            logger.info("Network not available, creatingSnackBar");
            AlarmMessageClass messageClass = new AlarmMessageClass();
            messageClass.createMessageOkBox(findViewById(R.id.parentLayoutAlarm,message), messageNoNetwork);

        }//FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        logger.info("After mobile network check, network available");
        // The spinners are going to create the drop down values
        createSpinners();
        }

    private boolean mobileDataEnabled(Context context){
        logger.info("Inside mobileDataEnabled");
        boolean mobileDataEnabled = false; // Assume disabled
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            Class cmClass = Class.forName(cm.getClass().getName());
            Method method = cmClass.getDeclaredMethod("getActiveNetwork");
            method.setAccessible(true); // Make the method callable
            // get the setting for "mobile data"
            mobileDataEnabled = method.invoke(cm) == null?false:true;

        } catch (Exception e) {
            // Some problem accessible private API
            // TODO do whatever error handling you want here
        }
        logger.info("Before Exiting mobileDataEnabled" + mobileDataEnabled);
        return mobileDataEnabled;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_train_alarm, menu);
        return true;
    }

    public void checkDataServices()
    {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
