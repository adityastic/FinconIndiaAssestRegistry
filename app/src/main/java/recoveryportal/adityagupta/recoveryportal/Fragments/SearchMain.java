package recoveryportal.adityagupta.recoveryportal.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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

import me.relex.circleindicator.CircleIndicator;
import recoveryportal.adityagupta.recoveryportal.Data.SearchResults;
import recoveryportal.adityagupta.recoveryportal.DetailsActivity;
import recoveryportal.adityagupta.recoveryportal.Placeholders.NOInternetPlaceHolder;
import recoveryportal.adityagupta.recoveryportal.R;
import recoveryportal.adityagupta.recoveryportal.Utils.Common;
import recoveryportal.adityagupta.recoveryportal.Utils.ReadJsonFile;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchMain extends Fragment {

    ViewPager viewPager;
    CircleIndicator circleIndicator;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        circleIndicator = view.findViewById(R.id.indicator_custom);
        viewPager = (ViewPager) view.findViewById(R.id.search_fg);

        viewPager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));

        circleIndicator.setViewPager(viewPager);
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override

        public Fragment getItem(int pos) {
            switch (pos) {

                case 0:
                    return SearchFragment.newInstance();
                case 1:
                    return SearchByEngFragment.newInstance();
                default:
                    return SearchFragment.newInstance();

            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_fragment_main, container, false);

    }

    public static Fragment newInstance() {
        return new SearchMain();
    }
}
