package recoveryportal.adityagupta.recoveryportal.Fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.relex.circleindicator.CircleIndicator;
import recoveryportal.adityagupta.recoveryportal.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchMain extends Fragment {

    ViewPager viewPager;
    CircleIndicator circleIndicator;

    public static Fragment newInstance() {
        return new SearchMain();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        circleIndicator = view.findViewById(R.id.indicator_custom);
        viewPager = view.findViewById(R.id.search_fg);

        viewPager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));

        circleIndicator.setViewPager(viewPager);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_fragment_main, container, false);

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
}
