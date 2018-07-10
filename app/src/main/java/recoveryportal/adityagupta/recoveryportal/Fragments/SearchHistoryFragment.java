package recoveryportal.adityagupta.recoveryportal.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.util.ArrayList;
import java.util.List;

import recoveryportal.adityagupta.recoveryportal.Adapters.SearchAdapter;
import recoveryportal.adityagupta.recoveryportal.Data.SearchHistoryData;
import recoveryportal.adityagupta.recoveryportal.Data.SearchResults;
import recoveryportal.adityagupta.recoveryportal.DetailsActivity;
import recoveryportal.adityagupta.recoveryportal.Placeholders.NOInternetPlaceHolder;
import recoveryportal.adityagupta.recoveryportal.R;
import recoveryportal.adityagupta.recoveryportal.Utils.Common;
import recoveryportal.adityagupta.recoveryportal.Utils.ReadJsonFile;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchHistoryFragment extends Fragment {
    SwipeRefreshLayout mSwipeRefreshLayout;
    CardView noInternetCard;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mWallpaperAdapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        mRecyclerView =  view.findViewById(R.id.recycler_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mSwipeRefreshLayout.setRefreshing(true);

        mWallpaperAdapter = null;
        mRecyclerView.setAdapter(null);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mWallpaperAdapter = new SearchAdapter(getContext(), Common.list);
                mRecyclerView.setAdapter(mWallpaperAdapter);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        },1000);

        noInternetCard = view.findViewById(R.id.nointernet);
        if (Common.isNetworkAvailable(getContext())) {
            noInternetCard.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        } else {
            noInternetCard.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout();
            }
        });
    }

    public void refreshLayout()
    {
        Common.generateHistory(getContext());
        mWallpaperAdapter = null;
        mRecyclerView.setAdapter(null);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mWallpaperAdapter = new SearchAdapter(getContext(), Common.list);
                mRecyclerView.setAdapter(mWallpaperAdapter);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        },1000);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_history, container, false);

    }

    public static Fragment newInstance() {
        return new SearchHistoryFragment();
    }
}
