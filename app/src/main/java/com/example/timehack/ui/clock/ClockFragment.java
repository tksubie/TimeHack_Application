package com.example.timehack.ui.clock;

import android.os.Bundle;
import android.os.Handler;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentClockBinding.inflate(inflater, container, false);

        //for the clock fragment view when it starts up
        View v = inflater.inflate(R.layout.fragment_clock, container, false);

        //show action bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();


        final TextView textView = binding.textClock;


        //creating spinner
        spinner = (Spinner) v.findViewById(R.id.timeZone_spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout.
        adapter = ArrayAdapter.createFromResource(
                this.getActivity(),
                R.array.Popular_Time_Zones,
                android.R.layout.simple_spinner_item
        );

        //set the spinner with adapter, spinner, and view
        spinnerSet(adapter, spinner, v);

        //get textview
        TextView julian = v.findViewById(R.id.jdate);


        //create a swipe down to refresh action
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //get day refreshed
                julian.setText(julian_day());
                // Showing refresh
                final Toast toast = Toast.makeText(getContext(), "Refreshed Julian Day", Toast.LENGTH_SHORT);
                toast.show();

                //show the toast in less time than the default Short
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 750);
                mSwipeRefreshLayout.setRefreshing(false);

            }
        });

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


    /*
    * create a spinner
    * Variables in
    * Char array adapter being adap
    * Spinner being spin
    * View for textclock being x
    */
    public void spinnerSet(ArrayAdapter<CharSequence> adap, Spinner spin, View x ) {
        // Specify the layout to use when the list of choices appears.
        adap.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);

        // Apply the adapter to the spinner.
        spin.setAdapter(adap);

        //when clicking on an item in the spinner, do something
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //set up text clock
            TextClock tz = (TextClock) x.findViewById(R.id.tz_display);

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // An item is selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos).
                // On selecting a spinner item

                String timezone_choice = spin.getSelectedItem().toString();

                // Showing selected spinner item
                final Toast toast = Toast.makeText(adapterView.getContext(), "TimeZone: " + timezone_choice, Toast.LENGTH_SHORT);
                toast.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 750);

                //set timezone
                tz.setTimeZone(timezone_choice);

            }

            //when spinner item is not selected
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
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


