package com.example.timehack.ui.clock;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.timehack.R;
import com.example.timehack.databinding.FragmentClockBinding;

import org.joda.time.DateTime;

import java.util.Locale;


//Class to take care of the Clock fragment in the application.
public class ClockFragment extends Fragment {

    //initialize the spinner
    Spinner spinner;

    //initialize the adapter
    ArrayAdapter<CharSequence> adapter;



    private FragmentClockBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentClockBinding.inflate(inflater, container, false);

        //for the clock fragment view when it starts up
        View v = inflater.inflate(R.layout.fragment_clock, container, false);

        //hide action bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        final TextView textView = binding.textClock;

        //set up text clock
        TextClock tz = (TextClock) v.findViewById(R.id.tz_display);

        //creating spinner
        spinner = (Spinner) v.findViewById(R.id.timeZone_spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout.
        adapter = ArrayAdapter.createFromResource(
                this.getActivity(),
                R.array.Popular_Time_Zones,
                android.R.layout.simple_spinner_item
        );
        // Specify the layout to use when the list of choices appears.
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);

        // Apply the adapter to the spinner.
        spinner.setAdapter(adapter);

        //when clicking on an item in the spinner, do something
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // An item is selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos).
                // On selecting a spinner item

                String timezone_choice = spinner.getSelectedItem().toString();

                // Showing selected spinner item
                Toast.makeText(adapterView.getContext(), "TimeZone: " + timezone_choice, Toast.LENGTH_SHORT).show();

                //set timezone
                tz.setTimeZone(timezone_choice);

            }

            //when spinner item is not selected
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //get textview
        TextView julian = v.findViewById(R.id.jdate);

        //get day
        julian.setText(julian_day());

        //return the view
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //Get Julian day for setting text
    String julian_day(){
        DateTime dt = new DateTime();
        //get day of year from datetime
        int day = dt.getDayOfYear();

        //convert to string
        String jday = String.valueOf(day);
        //get length
        int len = jday.length();

        //do not want a three char day number just 2 digits last two
        //so when it is day 101 we want 01 as the output
        if (len >= 3){
            jday = jday.substring(1);
            return jday;
        }
        else{
            //if it is only 2 digits if we have just 1-9 days we want also a 0 leading
            //format string, local, put in 0 and width of string is 2, number is julianday
            String formatted = String.format((Locale) null,"%02d", day);

            return formatted;
        }
    }


}


//    // Declaring a layout (changes are to be made to this)
//    // Declaring a textview (which is inside the layout)
//    SwipeRefreshLayout swipeRefreshLayout = v.findViewById(R.id.refreshLayout);
////TextView refreshText = v.findViewById(R.id.tv1);
//
//
//// Refresh  the layout
//        swipeRefreshLayout.setOnRefreshListener(
//                new SwipeRefreshLayout.OnRefreshListener() {
//@Override
//public void onRefresh() {
//
//        //get textview
//        TextView julian = v.findViewById(R.id.jdate);
//        //get day
//        julian.setText(julian_day());
//
//        // Showing selected spinner item
//        Toast.makeText(getContext(), "Refreshed", Toast.LENGTH_SHORT).show();
//
//        //refreshText.setText("Refreshed");
//
//        // This line is important as it explicitly
//        // refreshes only once
//        // If "true" it implicitly refreshes forever
//        swipeRefreshLayout.setRefreshing(false);
//        }
//        }
//        );