package radesh.example.twosideseekbar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

public class test  {

    public void Test(){
        String test = "A:::";
        String[] data  = test.split(":",-1);
        Log.e("test",data[0]);
        Log.e("test",data[1]);
    }

    public static String GetCarrierName(Context c) {

        TelephonyManager manager = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
        String car = manager.getNetworkOperatorName();
        if (car.contains("irancell") || car.contains("Irancell")) {
            return "irancell";
        } else if (car.contains("mci") || car.contains("MCI") || car.equals("IR MCI") || car.equals("IR-MCI") || car.contains("TCI")) {
            return "mci";
        } else {
            return "n/a " + car;
        }
    }

}


