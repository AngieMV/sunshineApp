package mx.saudade.sunshineapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.view.ActionProvider;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;

/**
 * Created by angelicamendezvega on 8/3/15.
 */
public final class ActionsUtils {

    private static final String TAG = ActionsUtils.class.getSimpleName();

    public static void showMap(Context context) {
        String code = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.pref_location_key), context.getString(R.string.pref_location_default));

        Uri geolocation = Uri.parse("geo:0,0?q="+code+"&zoom=23");

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(geolocation);

        if (i.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(i);
        }
    }

    public static void launchSettingsActivity(Context context) {
        Intent i = new Intent();
        i.setClass(context, SettingsActivity.class);
        context.startActivity(i);
    }

    public static void share(ShareActionProvider provider, String message) {

        if (message == null || provider == null) {
            return;
        } else {
            Log.e(TAG, "Error" + provider.toString() + " " + message );
        }

        Intent i = new Intent(Intent.ACTION_SEND);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT, message + " #sunshineApp");

        provider.setShareIntent(i);
    }

}
