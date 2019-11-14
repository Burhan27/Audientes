package dk.sens.android.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by morten on 12/03/2018.
 */

public class ModalHelper
{
    static private Context mContext;

    public static void init(Context context)
    {
        ModalHelper.mContext = context;
    }

    public static void AlertError(Activity activity, String title, String message)
    {
        AlertDialog aDialog = new AlertDialog.Builder(activity)
                .setMessage(message)
                .setTitle(title)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog,
                                        final int which) {
                    }
                }).create();
        aDialog.show();
    }

}
