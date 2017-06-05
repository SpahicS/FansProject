package adapters;

import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import dashboard.FragmentFeed;
import dashboard.FragmentNews;
import dashboard.FragmentProfile;
import dashboard.FragmentTeam;

/**
 * Created by unexpected_err on 29/04/2017.
 */

public class DashboardPagerAdapter extends FragmentPagerAdapter {


    public DashboardPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return FragmentFeed.newInstance(position + 1);
            case 1:
                return FragmentNews.newInstance(position + 1);
            case 2:
                return FragmentTeam.newInstance(position + 1);
            case 3:
                return FragmentProfile.newInstance(position + 1);
        }

        return FragmentFeed.newInstance(position + 1);
    }

    @Override public int getCount() {
        return 4;
    }
}