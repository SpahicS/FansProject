package adapters;

import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import dashboard.FragmentFeed;
import dashboard.FragmentNews;
import dashboard.FragmentProfile;
import dashboard.FragmentTeam;

/**
 * Created by unexpected_err on 29/04/2017.
 */

public class DashboardPagerAdapter extends FragmentPagerAdapter {

    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

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

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}