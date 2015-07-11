package com.javiergonzalez.exercise.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.javiergonzalez.exercise.R;
import com.javiergonzalez.exercise.interfaces.DateListener;

import java.util.Calendar;

/**
 * Created by javiergonzalezcabezas on 11/7/15.
 */
public class ListDialogAdapter extends ArrayAdapter<String>  {

    private final int QUANTITY = 0;
    private final int VISUAL_CONDITION = 1;
    private final int VISUAL_CONDITION_NOTE = 2;
    private final int OBSERVATIONS = 3;
    private final int INSTALL_DATE = 4;
    private final int MANUFACTURER = 5;
    private final int MODEL = 6;
    private final int SERIAL_NUMBER = 7;

    private Activity mContext;

    private String[] mList;
    private DateListener mListener;
    String mDate;

    public ListDialogAdapter(Activity context, String[] list,String date,DateListener dateListener) {
        super(context, R.layout.row_list_dialog, list);
        mContext = context;
        mList = list;
        mDate = date;
        mListener = dateListener;
    }




    static class ViewHolder {
        public TextView titleTextView;
        public TextView detailTextView;
        public Button button;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.row_list_dialog, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.titleTextView = (TextView) rowView.findViewById(R.id.row_list_dialog_title);
            viewHolder.detailTextView = (TextView) rowView.findViewById(R.id.row_list_dialog_detail);
            viewHolder.button = (Button) rowView.findViewById(R.id.row_list_dialog_button);
            rowView.setTag(viewHolder);
        }

        final ViewHolder holder = (ViewHolder) rowView.getTag();
        holder.titleTextView.setText(mList[position]);

        if (position==INSTALL_DATE && !mDate.equals("")){
            holder.detailTextView.setText(mDate);
        }

        final View view = mContext.getLayoutInflater().inflate(R.layout.dialog_number, null);
        final EditText etInput = (EditText) view.findViewById(R.id.dialog_number_edit_text);
        final View viewManufacturer = mContext.getLayoutInflater().inflate(R.layout.dialog_text, null);
        final EditText etInputEdtittext = (EditText) viewManufacturer.findViewById(R.id.dialog_text_edit_text);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position) {
                    case QUANTITY:
                        customDialogEdit(view, etInput, holder, position);
                        break;
                    case VISUAL_CONDITION:
                        simpleList(holder);
                        break;
                    case VISUAL_CONDITION_NOTE:
                        radioButtonList(holder);
                        break;
                    case OBSERVATIONS:
                        radioButtonList(holder);
                        break;
                    case INSTALL_DATE:
                        showDatePickerDialog(v);

                        break;
                    case MANUFACTURER:
                        customDialogEdit(viewManufacturer, etInputEdtittext, holder, position);
                        break;
                    case MODEL:
                        customDialogEdit(viewManufacturer, etInputEdtittext, holder, position);
                        break;
                    case SERIAL_NUMBER:
                        customDialogEdit(view, etInput, holder, position);
                        break;
                }
            }
        });

        return rowView;
    }


    public void customDialogEdit(View view, final EditText etInput, final ViewHolder holder, final int position) {

        final AlertDialog alert = new AlertDialog.Builder(mContext)
                .setTitle(mList[position])
                .setView(view)
                .setPositiveButton(R.string.row_list_signin_save, null)
                .setCancelable(true)
                .create();
        alert.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface arg0) {
                Button okButton = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {

                        if (!etInput.getText().equals("")) {
                            holder.detailTextView.setText(etInput.getText());
                        }
                        alert.cancel();
                    }
                });
            }
        });
        alert.show();
    }

    public void simpleList(final ViewHolder holder) {
        final CharSequence[] items = {"Android OS", "iOS", "Windows Phone", "Meego"};
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("your preferred OS?");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                holder.detailTextView.setText(items[item]);
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void radioButtonList(final ViewHolder holder) {
        final CharSequence[] items = {"Android OS", "iOS", "Windows Phone", "Meego"};
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("your preferred OS?");
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                holder.detailTextView.setText(items[item]);
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment(mListener);
        newFragment.show(mContext.getFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        public DatePickerFragment(DateListener mListener) {
            this.mListener = mListener;
        }

        DateListener mListener;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);


            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            mListener.getDate(year, month, day);

        }
    }

}
