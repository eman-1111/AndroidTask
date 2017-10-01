package ides.link.androidtask;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import ides.link.androidtask.fragment.CountriesFragment;
import ides.link.androidtask.fragment.MapFragment;
import ides.link.androidtask.fragment.PhoneContactFragment;
import ides.link.androidtask.utilities.CommonUtilities;
import ides.link.androidtask.utilities.Constant;
import ides.link.androidtask.utilities.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.viewpager) ViewPager viewPager;
    @BindView(R.id.tabs) TabLayout tabLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        CommonUtilities.isDeviceOnline(viewPager ,this);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        MapFragment mapFragment = new MapFragment();
        PhoneContactFragment phoneContactFragment = new PhoneContactFragment();
        CountriesFragment countriesFragment = new CountriesFragment();

        adapter.addFragment(mapFragment, getResources().getString(R.string.map));
        adapter.addFragment(countriesFragment, getResources().getString(R.string.countries));
        adapter.addFragment(phoneContactFragment, getResources().getString(R.string.contact));
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            userLogOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void userLogOut() {
        SharedPreferences.Editor editor = getSharedPreferences(Constant.MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(Constant.USER_NAME, "");
        editor.putString(Constant.USER_PASSWORD, "");
        editor.apply();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
