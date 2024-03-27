package com.example.timehack.ui.clock;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.timehack.R;

import org.joda.time.DateTime;

import java.util.Locale;


//Class to take care of the Clock fragment in the application.
public class ClockFragment extends Fragment {

    Dialog dialog;

    String tzPosition = null;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        //for the clock fragment view when it starts up
        View v = inflater.inflate(R.layout.fragment_clock, container, false);

        //show action bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();


        //creating dialog searchable spinner
        TextView searchTZ = v.findViewById(R.id.timeZone_spinner);

        //set up text clock
        TextClock tz = (TextClock) v.findViewById(R.id.tz_display);

        //set timezone to previous position of timezone selected & dropdown to previous selected tz
        tz.setTimeZone(tzPosition);
        searchTZ.setText(tzPosition);

        searchTZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initialize dialog
                dialog = new Dialog(getContext());

                // set custom dialog
                dialog.setContentView(R.layout.searchable_spinner);

                // set custom height and width for the dialog box
                //80% for width
                //50% for height
                int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.80);
                int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.50);
                dialog.getWindow().setLayout(width, height);

                // set transparent background for the whole screen behind the dialog box
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                // show dialog box
                dialog.show();

                // Initialize and assign variable
                EditText search_tz = dialog.findViewById(R.id.tz_searchbox);
                AbsListView listView = dialog.findViewById(R.id.tz_list_view);

                // Initialize array adapter
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Popular_Time_Zones));

                // set adapter for listview
                listView.setAdapter(adapter);

                //setup the listener for the dialog search box
                search_tz.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        //nothing is done before the text is changed
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        //nothing is done after the text is changed
                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // set selected item on textView
                        searchTZ.setText(adapter.getItem(position));


                        // Showing selected spinner item
                        final Toast toast = Toast.makeText(getContext(), "TimeZone: " + adapter.getItem(position), Toast.LENGTH_SHORT);
                        toast.show();

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                toast.cancel();
                            }
                        }, 750);

                        //set position
                        tzPosition = adapter.getItem(position);
                        //set timezone
                        tz.setTimeZone(adapter.getItem(position));
                        // Dismiss dialog
                        dialog.dismiss();
                    }
                });
            }
        });


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
    }


    //Get Julian day for setting text
    String julian_day() {
        DateTime dt = new DateTime();
        //get day of year from datetime
        int day = dt.getDayOfYear();

        //convert to string
        String jday = String.valueOf(day);
        //get length
        int len = jday.length();

        //do not want a three char day number just 2 digits last two
        //so when it is day 101 we want 01 as the output
        if (len >= 3) {
            jday = jday.substring(1);

            return jday;
        } else {
            //if it is only 2 digits if we have just 1-9 days we want also a 0 leading
            //format string, local, put in 0 and width of string is 2, number is julianday
            String formatted = String.format((Locale) null, "%02d", day);

            return formatted;
        }
    }


}


