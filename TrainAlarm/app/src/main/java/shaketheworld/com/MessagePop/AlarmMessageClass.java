package shaketheworld.com.MessagePop;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;

import java.util.logging.Logger;

import shaketheworld.com.trainalarm.R;

/**
 * Created by Anand Mishra on 8/23/2016.
 */
public class AlarmMessageClass implements View.OnClickListener {
private static final String noNetwork = "NONETWORK";
private Logger logger = Logger.getLogger("AlarmMessageClass");
    @Override
    public void onClick(View v) {
        Button popUpButton = (Button) v;
        popUpButton.getText();
        if( popUpButton.getText().equals(noNetwork)){
            // Terminate the application
            logger.info("Terminating the application since network was not found");
            System.exit(0);
        }
    }

    public void createMessageOkBox(View v, String message, String text){
        Snackbar noNetworkSnackBar = Snackbar.make(v,message, Snackbar.LENGTH_INDEFINITE );
        noNetworkSnackBar.setAction("Ok",new AlarmMessageClass());
        noNetworkSnackBar.setText(text);
    }
}
