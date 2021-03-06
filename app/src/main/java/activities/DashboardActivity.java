package activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.google.firebase.database.FirebaseDatabase;
import adapters.DashboardPagerAdapter;
import dashboard.FragmentFeed;
import dashboard.FragmentNews;
import dashboard.FragmentProfile;
import digitalbath.fansproject.R;
import helpers.main.AppHelper;

public class DashboardActivity extends AppCompatActivity {

    private DashboardPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private AppBarLayout appBarLayout;
    private boolean exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        initializeContent();

    }

    private void initializeContent() {

        mPagerAdapter = new DashboardPagerAdapter(getSupportFragmentManager());

        initializePager();
        initializeTabs();

    }

    private void initializePager() {

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < mTabLayout.getTabCount(); i++) {

                    if (position == i) {

                        mTabLayout.getTabAt(i).getIcon().setTint
                                (getResources().getColor(R.color.tab_selected));

                    } else {

                        mTabLayout.getTabAt(i).getIcon().setTint
                                (getResources().getColor(R.color.tab_unselected));
                    }
                }
            }
        });
    }

    private void initializeTabs() {

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            switch (i) {
                case 0:
                    mTabLayout.getTabAt(i).setIcon(R.drawable.tab_icon1);
                    break;
                case 1:
                    mTabLayout.getTabAt(i).setIcon(R.drawable.tab_icon2);
                    break;
                case 2:
                    mTabLayout.getTabAt(i).setIcon(R.drawable.tab_icon3);
                    break;
                case 3:
                    mTabLayout.getTabAt(i).setIcon(R.drawable.tab_icon4);
                    break;
            }
        }

        mTabLayout.getTabAt(0).getIcon().setTint
                (getResources().getColor(R.color.tab_selected));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        Fragment fragment = mPagerAdapter.getRegisteredFragment(mViewPager.getCurrentItem());

        if (fragment instanceof FragmentFeed) {
            if (((FragmentFeed) fragment).closeCommentsCont())
                return;
        } else if (fragment instanceof FragmentNews) {
            if (((FragmentNews) fragment).closeCommentsCont())
                return;
        }

        if (exit) {
            moveTaskToBack(true);
        } else {
            FragmentProfile frag = (FragmentProfile) getSupportFragmentManager().getFragments().get(3);
            frag.hideCountriesRecycler();
            AppHelper.showToast(DashboardActivity.this, "Press Back again to exit.");
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (FirebaseDatabase.getInstance() != null)
        {
            FirebaseDatabase.getInstance().goOnline();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if(FirebaseDatabase.getInstance()!=null)
        {
            FirebaseDatabase.getInstance().goOffline();
        }
    }
}
