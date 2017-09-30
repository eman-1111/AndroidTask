package ides.link.androidtask.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import ides.link.androidtask.LoginActivity;
import ides.link.androidtask.R;

/**
 * Created by Eman on 9/28/2017.
 */

public class CommonUtilities {


    /*
    * Check if String is Null/empty or not
    */
    public static boolean validateUserName(String str) {
        if (str.isEmpty() || str.length() < 2)
            return false;

        return true;
    }

    public static boolean validateEmail(String str) {
        if (str.isEmpty() || android.util.Patterns.EMAIL_ADDRESS.matcher(str).matches()) {
            return false;
        }
        return true;
    }

    public static boolean validatePassword(String str) {
        if (str.isEmpty() || str.length() < 2 ) {
            return false;
        }
        return true;
    }


    public static void showPopupMessage(final Activity activity, String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(msg);
        builder.setTitle(title);
        builder.setNegativeButton(activity.getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //do things
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public static String getDistance(LatLng my_latlong, LatLng frnd_latlong) {
        Location l1 = new Location("One");
        l1.setLatitude(my_latlong.latitude);
        l1.setLongitude(my_latlong.longitude);

        Location l2 = new Location("Two");
        l2.setLatitude(frnd_latlong.latitude);
        l2.setLongitude(frnd_latlong.longitude);

        float distance = l1.distanceTo(l2);
        String dist = distance + " M";

        if (distance > 1000.0f) {
            distance = distance / 1000.0f;
            dist = distance + " KM";
        }
        return dist;
    }


}
