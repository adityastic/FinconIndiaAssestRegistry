package recoveryportal.adityagupta.recoveryportal.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
public class SearchFragment extends Fragment {

    // TextView tvName,tvEngine,tvReg,tvModel;
    EditText editText, editText2, editText3, editText4;
    //  CardView cd;

    boolean clicked = false;
    Button search;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        editText = view.findViewById(R.id.editText);
        editText2 = view.findViewById(R.id.editText2);
        editText3 = view.findViewById(R.id.editText3);
        editText4 = view.findViewById(R.id.editText4);

        search = view.findViewById(R.id.search);

        editText.setText("MH");
        editText.setClickable(false);
        editText.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(2)});
        editText2.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(2)});
        editText3.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(2)});
        editText4.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(4)});

        editText2.requestFocus();
        editText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 2) {
                    editText3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        editText3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 2) {
                    editText4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        editText4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 4) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText4.getWindowToken(), 0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        File jsonFolder = new File(getContext().getCacheDir(), "/alljsons/");
        if (!jsonFolder.exists()) {
            jsonFolder.mkdirs();
        }

        search.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onClick(View v) {
                if (Common.isNetworkAvailable(getActivity())) {
                    if (!clicked ) {
                        if(!editText.getText().toString().equals("") && !editText2.getText().toString().equals("") && !editText3.getText().toString().equals("") && !editText4.getText().toString().equals(""))
                        {
                            clicked = true;
                            Common.startDownload(getContext(), String.format(Common.SEARCH_LINK, (editText.getText().toString() + editText2.getText().toString() + editText3.getText().toString() + editText4.getText().toString()), Common.loginDetails.Email_Id, Common.loginDetails.Password), "Search.json", new Handler() {

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
                        }
                        else
                        {
                            Toast.makeText(getContext(), "Enter Registration Complete Number", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    startActivity(new Intent(getActivity(), NOInternetPlaceHolder.class));
                }

            }
        });

    }

    public SearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);

    }

    public static Fragment newInstance() {
        return new SearchFragment();
    }
}
