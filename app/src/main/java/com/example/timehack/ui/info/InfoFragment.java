package com.example.timehack.ui.info;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.timehack.R;
import com.example.timehack.databinding.FragmentInfoBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


//Class to take care of the Info fragment in the application.
public class InfoFragment extends Fragment {


    //initialize the buttons
    Button pdf_button1, pdf_button2, pdf_button3, clickable;

    //initialize the adapter
    ArrayAdapter<CharSequence> adapter;

    private FragmentInfoBinding binding;

    // Request code for selecting a PDF document.
    private static final int PICK_PDF_FILE = 2;
    
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //show action bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        binding = FragmentInfoBinding.inflate(inflater, container, false);

        //for the info fragment view when it starts up
        View v = inflater.inflate(R.layout.fragment_info, container, false);

        //start up button views
        pdf_button1 = v.findViewById(R.id.button1);
        pdf_button2 = v.findViewById(R.id.button2);
        pdf_button3 = v.findViewById(R.id.button3);

        clickable = v.findViewById(R.id.websiteClick);


        //set pdf names
        String pdf1 = "QuickHelp_FAQs";
        String pdf2 = "SINCGARS Operation TM";
        String pdf3 = "SINCGARS Pocket Guide";

        /*
        Citations for Technical Manuals supplied in the application
        -TM_11-5820-890-10-3:
        Headquarters, Dept. of the Army, Operator’s manual: Sincgars Ground Combat Net Radio,
        non-ICOM: Manpack Radio, AN/PRC-119 (NSN 5820-01-151-9915) (EIC: L2a), short range vehicular
        radio, AN/VRC-87 (NSN 5820-01-151-9916) (EIC: L2T) ... long range/long range vehicular radio,
        AN/VRC-92 (NSN 5820-01-151-9921) (EIC: L2y) (1997). Washington, D.C.
        -TM_11-5820-890-10-6:
        Headquarters, Dept. of the Army, SINCGARS ICOM ground radios used with Automated Net Control
         Device (ANCD) AN/CYZ-10: Operator’s pocket knife: Radio sets, Manpack Radio (AN/PRC-119A)
         ... (NSN N/A) (EIC n/A) (1994). Washington, D.C.
         */
        //create file name for uri to find the file
        String fileName1 = "QuickHelp_TM_V1.pdf";
        String fileName2 = "TM_11-5820-890-10-3_TimeHack.pdf";
        String fileName3 = "TM_11-5820-890-10-6_Timehack.pdf";

        //set on click listener for all one by one
        button(v,pdf_button1, pdf1,fileName1);
        button(v,pdf_button2, pdf2, fileName2);
        button(v,pdf_button3, pdf3, fileName3);

        clickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"));


                String title = "Go to site with:";

                Intent chooser = Intent.createChooser(intent, title);
                startActivity(chooser);
            }
        });

        //return view
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /*
     *Set up button for being pressed
     * Input Variables:
     * v for view
     * y for button name
     * pdfName for pdf name in toast
     * f for pdf filepath
     */

    public void button(View x, Button y, String pdfName, String f) {
        y.setOnClickListener(new View.OnClickListener() {
            public void onClick(View x) {
                final Toast toast = Toast.makeText(getContext(), pdfName, Toast.LENGTH_SHORT);
                toast.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 500);

                pdfOpen(f);
            }
        });
    }
    /*
     *Function to Open a pdf file with intent and open pdf file from assets folder with external app
     *
     * Variable in is the file name with .pdf or whatever extension needed
     */
    public void pdfOpen(String pdfFile) {
        //get file object
        File file = getAssetsFile(pdfFile);
        //pass asset file to Uri and then through File provider
        Uri fileUri = FileProvider.getUriForFile(requireContext(), requireContext().getPackageName() + ".provider", file);
        //Create intent to open external app
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //set the type to pdf
        intent.setType("application/.pdf");
        //set data from Uri
        intent.setData(fileUri);
        //grant permissions to open file from external application
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //try to open from an external application
        try {
            startActivity(intent);
        }
        //if no application found there is an error
        catch(ActivityNotFoundException e){
            Toast.makeText(getContext(), "Error opening PDF - Install a PDF Viewer.", Toast.LENGTH_SHORT).show();
        }
    }
    /*
    *Function to get the assets file from the assets directory and copy the file for use
    *
    * Variable in is the file name with .pdf or whatever extension needed
    */
    private File getAssetsFile(String fName) {
        //create file object from assets folder and if the file exists create a new file and copy
        File file = new File(requireContext().getFilesDir(), fName);
        if (!file.exists()) {
            try {
                file.createNewFile();
                copyAssetsFile(fName, file);
            } catch (IOException e) {
                Toast.makeText(getContext(), "Error file not found.", Toast.LENGTH_SHORT).show();
            }
        }
        return file;
    }
    /*
     *Function copy assets file from the assets directory and copy the file for use
     *
     * Variable in is the file name with .pdf or whatever extension needed and the file object copying into
     */
    private void copyAssetsFile(String fName, File file) {

        try {
            InputStream inputStream = requireContext().getAssets().open(fName);
            OutputStream outputStream = new FileOutputStream(file);
            //create buffer
            byte[] buffer = new byte[1024];
            //initialize length
            int length;
            //iterate the buffer
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            //Flushes the output stream and forces any buffered output bytes to be written out.
            outputStream.flush();
            //close streams
            outputStream.close();
            inputStream.close();
        }
        catch(IOException e){
            Toast.makeText(getContext(), "Error - file not copied for use.", Toast.LENGTH_SHORT).show();
        }
    }
}
