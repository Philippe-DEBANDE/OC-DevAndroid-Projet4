package com.lamzone.mareu.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.lamzone.mareu.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static java.util.Calendar.YEAR;
import static java.util.Calendar.getInstance;

public class FilterFragment extends AppCompatDialogFragment {
    @BindView(R.id.dateFilter_It) TextInputEditText dateFilter_It;
    @BindView(R.id.dateFilter_Lyt) TextInputLayout dateFilter_Lyt;
    @BindView(R.id.roomNameFilter_spinner) Spinner roomNameFilter_Sp;
    @BindView(R.id.dateFilter_Iv) ImageView dateFilter_Iv;

    private List<String> mRooms;
    public Calendar actualDate = getInstance();
    public Calendar mDate, selectedDate, dateValue;
    private String mRoom;
    private OnButtonClickedListener listener;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private DatePickerDialog mDatePickerDialog;
    public FilterFragment(List<String> rooms) {
        mRooms = rooms;
    }

    @NonNull
    @Override
     public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.fragment_filter, null);
        ButterKnife.bind(this, view);
        //Spinner roomName Init
        //Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,mRooms){
            //simple_spinner_dropdown_item
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {// Disable the first item from Spinner, first item will be use for hint
                    return false;}
                else
                {return true;}
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0)
                {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomNameFilter_Sp.setAdapter(spinnerArrayAdapter);
        roomNameFilter_Sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (roomNameFilter_Sp.getSelectedItemPosition()>0) {
                    dateFilter_Iv.setEnabled(false);
                    dateFilter_Iv.setClickable(false);
                    dateFilter_It.setFocusable(false);
                    dateFilter_It.setText("");
                }

            }

            @Override
                  public void onNothingSelected(AdapterView<?> parent) {

                  }
     });

        builder.setView(view);
        builder.setTitle("Sélectionner un filtre");
        builder.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            if (roomNameFilter_Sp.getSelectedItemPosition()!=0) {
                            mRoom=roomNameFilter_Sp.getSelectedItem().toString();                            }
                            if (dateValue!=null) {mDate=dateValue;
                            System.out.println("Valeur de la date : "+mDate.getTime()
                            );}

                            listener.applyTexts(mDate, mRoom);
                    }

    });

        builder.setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss());
        //builder.setNeutralButton(android.R.string.ok, (dialog, which) -> mCallback.onButtonClicked(mDate, mRoom, true));

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorWhite));
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorWhite));
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        Button btn = ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) btn.getLayoutParams();
        //params.topMargin = yourValue;//Enter your desired margin value here.
        //params.bottomMargin = yourValue;
        params.leftMargin = 50;
        params.rightMargin = 190;
        btn.setLayoutParams(params);
    }

    @OnClick({R.id.dateFilter_Iv})
    public void displayDatePicker(View v){
        //actualDate = getInstance();
        actualDate.get(YEAR);
        actualDate.get(Calendar.MONTH);
        actualDate.get(Calendar.DAY_OF_MONTH);
        // To remove -> Check
        //System.out.println(calendar.get(Calendar.DAY_OF_MONTH)+"/"+(calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(YEAR) );
        mDatePickerDialog = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog,
                //android.R.style.Theme_Holo_Light_Dialog
                (view, year, month, dayOfMonth) -> {
                    selectedDate = Calendar.getInstance();
                    selectedDate.set(year, month, dayOfMonth);
                    dateFilter_It.setText(dateFormat.format(selectedDate.getTime()));
                    dateValue = (Calendar) selectedDate.clone();
                    roomNameFilter_Sp.setEnabled(false);
                    roomNameFilter_Sp.setClickable(false);
                    // Testing the date is not before -> error + emptying DateInputText
                    if (selectedDate.before(actualDate)) {
                        dateFilter_Lyt.setError("La date est passée !");
                        dateFilter_It.setText("");
                  }

                },
                actualDate.get(YEAR),
                actualDate.get(Calendar.MONTH),
                actualDate.get(Calendar.DAY_OF_MONTH));
        // Disable Date IntputLayout Error
        dateFilter_Lyt.setErrorEnabled(false);
        dateFilter_It.setText("");
        mDatePickerDialog.setMessage("Sélectionner une date : ");
        mDatePickerDialog.show();
    }

    private void setContentView(int activity_main) {
    }

    public interface OnButtonClickedListener {
        //void onButtonClicked(String room);
        void applyTexts(Calendar date, String roomName);
        //Calendar date
        //boolean reset
    }

    // intercept casting error when not implemented in main activity
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (OnButtonClickedListener) context;

    }
}

