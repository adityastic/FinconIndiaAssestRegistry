package recoveryportal.adityagupta.recoveryportal.Fragments;

import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import recoveryportal.adityagupta.recoveryportal.Adapters.SearchAdapter;
import recoveryportal.adityagupta.recoveryportal.R;
import recoveryportal.adityagupta.recoveryportal.Utils.Common;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchHistoryFragment extends Fragment {
    SwipeRefreshLayout mSwipeRefreshLayout;
    CardView noInternetCard;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mWallpaperAdapter;

    public static Fragment newInstance() {
        return new SearchHistoryFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        mRecyclerView = view.findViewById(R.id.recycler_list);
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
        }, 1000);

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

    public void refreshLayout() {
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
        }, 1000);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_history, container, false);

    }
}
