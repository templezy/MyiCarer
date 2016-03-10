package au.edu.adelaide.sensorlog.app;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import au.edu.adelaide.sensorlog.app.data.SensorLogService;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class SaveDialogFragment extends DialogFragment {

//    private String time;
    private SharedPreferences prefs;

//    public SaveDialogFragment() {
//        super();
//        Calendar c = Calendar.getInstance();
//        time = c.get(Calendar.HOUR_OF_DAY) + "-" + c.get(Calendar.MINUTE) + "-" + c.get(Calendar.SECOND) + " "
//                + c.get(Calendar.DAY_OF_MONTH) + "-" + c.get(Calendar.MONTH) + "-" + c.get(Calendar.YEAR);
//    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.fragment_save_dialog, null);
        final EditText editText = (EditText) dialogView.findViewById(R.id.edittext_filename);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final Intent serviceIntent = new Intent(getActivity().getApplicationContext(), SensorLogService.class);
        builder.setView(dialogView)
                .setTitle(R.string.dialog_title)
                .setPositiveButton(R.string.dialog_save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String fileName = editText.getText().toString();
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString(getString(R.string.pref_filename), fileName);
                        editor.apply();
                        getActivity().stopService(serviceIntent);
//                        ((MainActivity) getActivity()).logManager.stopReadingSensorData(fileName + " " + time + ".csv");
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString(getString(R.string.pref_filename), null);
                        editor.apply();
                        getActivity().stopService(serviceIntent);
//                        ((MainActivity) getActivity()).logManager.stopReadingSensorData(null);
                    }
                });
        editText.setText(prefs.getString(getString(R.string.pref_activity_label), getString(R.string.pref_activity_other)));
        Dialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

}
