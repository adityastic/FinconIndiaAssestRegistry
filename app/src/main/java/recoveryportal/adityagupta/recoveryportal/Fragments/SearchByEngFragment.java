package recoveryportal.adityagupta.recoveryportal.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;

import recoveryportal.adityagupta.recoveryportal.Data.SearchResults;
import recoveryportal.adityagupta.recoveryportal.DetailsActivity;
import recoveryportal.adityagupta.recoveryportal.Placeholders.NOInternetPlaceHolder;
import recoveryportal.adityagupta.recoveryportal.R;
import recoveryportal.adityagupta.recoveryportal.Utils.Common;
import recoveryportal.adityagupta.recoveryportal.Utils.ReadJsonFile;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchByEngFragment extends Fragment {

    // TextView tvName,tvEngine,tvReg,tvModel;
    EditText editText2;
    //  CardView cd;

    boolean clicked = false;
    Button search;

    public SearchByEngFragment() {
    }

    public static Fragment newInstance() {
        return new SearchByEngFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        editText2 = view.findViewById(R.id.editText2);
        search = view.findViewById(R.id.search);

        editText2.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        editText2.requestFocus();

        File jsonFolder = new File(getContext().getCacheDir(), "/alljsons/");
        if (!jsonFolder.exists()) {
            jsonFolder.mkdirs();
        }

        search.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onClick(View v) {
                if (Common.isNetworkAvailable(getActivity())) {
                    if (!clicked) {
                        if (!editText2.getText().toString().equals("") && editText2.getText().toString().length() == 5) {
                            clicked = true;
                            Common.startDownload(getContext(), String.format(Common.SEARCH_LINK, editText2.getText().toString(), Common.loginDetails.Email_Id, Common.loginDetails.Password), "Search.json", new Handler() {

                                @Override
                                public void handleMessage(Message msg) {
                                    if (msg.toString().contains("arg1=1")) {
                                        try {
                                            JSONObject mainObject = new JSONObject(ReadJsonFile.getJSONObject(getContext().getCacheDir() + "/alljsons/Search.json").toString());

                                            if (mainObject.has("id")) {
                                                Common.searchResults = new SearchResults(
                                                        new SimpleDateFormat("hh:mm:ss aa").parse(mainObject.getString("time")),
                                                        mainObject.getString("status"),
                                                        mainObject.getString("id"),
                                                        mainObject.getString("customerName"),
                                                        mainObject.getString("vehicleModel"),
                                                        mainObject.getString("loanNo"),
                                                        mainObject.getString("engineNo"),
                                                        mainObject.getString("regNo"),
                                                        mainObject.getString("chassisNo"),
                                                        mainObject.getString("bucket"),
                                                        mainObject.getString("principalOS"),
                                                        mainObject.getString("emiAmount"),
                                                        mainObject.getString("bounceChargeDue"),
                                                        mainObject.getString("lppDue"),
                                                        mainObject.getString("firstEmiDueDate"),
                                                        mainObject.getString("loanEndDate"),
                                                        mainObject.getString("branch"),
                                                        mainObject.getString("client")
                                                );

                                                Intent i = new Intent(getActivity(), DetailsActivity.class);
                                                startActivity(i);
                                                getActivity().finish();
                                                clicked = false;
                                            } else {
                                                clicked = false;
                                                Common.searchResults = null;
                                                Toast.makeText(getContext(), "No Record Found", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (Exception e) {
                                            clicked = false;
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getContext(), "Enter Last 5 Digits of Engine or Chassis Number", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    startActivity(new Intent(getActivity(), NOInternetPlaceHolder.class));
                }

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_eng, container, false);

    }
}
