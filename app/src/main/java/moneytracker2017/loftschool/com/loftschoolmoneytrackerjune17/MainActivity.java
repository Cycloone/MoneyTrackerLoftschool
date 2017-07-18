package moneytracker2017.loftschool.com.loftschoolmoneytrackerjune17;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


public class MainActivity extends AppCompatActivity {

    private TabLayout tabs;
    private ViewPager pages;
    private String[] types = {Item.TYPE_EXPENSE, Item.TYPE_INCOME};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabs = (TabLayout) findViewById(R.id.tabs);
        pages = (ViewPager) findViewById(R.id.pages);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!((LSApp) getApplication()).isLoggedIn())
            startActivity(new Intent(this, AuthActivity.class));
        else {
            initUi();
        }
    }

    private class MainPagerAdapter extends FragmentPagerAdapter {
        private final String[] tittles;

        MainPagerAdapter() {
            super(getSupportFragmentManager());
            tittles = getResources().getStringArray(R.array.main_pager_titles);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == getCount() - 1)
                return new BalanceFragment();

            final ItemsFragment fragment = new ItemsFragment();
            String type = (position == 0) ? Item.TYPE_EXPENSE : Item.TYPE_INCOME;
            Bundle args = new Bundle();
            args.putString(ItemsFragment.ARG_TYPE, types[position]);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tittles[position];
        }


        @Override
        public int getCount() {
            return tittles.length;
        }

    }

    private void initUi() {
        if (pages.getAdapter() != null)
            return;
        pages.setAdapter(new MainPagerAdapter());
        tabs.setupWithViewPager(pages);
    }

}
