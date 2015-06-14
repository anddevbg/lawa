package com.anddevbg.lawa;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;


public class WeatherActivity extends ActionBarActivity {
    ViewPager viewPager=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

/*
Instantiate viewpager and fragment manager +
set adapter to the viewpager by passing it a fragment manager object
*/

        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new MyAdapter(fragmentManager));


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_weather, menu);
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

}

// create adapter to handle the views
class MyAdapter extends FragmentStatePagerAdapter {

    public MyAdapter(FragmentManager fm) {
        super(fm);
    }

    // Check where we are and show fragment at each position

    @Override
    public Fragment getItem(int position) {
        WeatherFragment fragment = new WeatherFragment();
        if(position==0)
        {
            return fragment;
        }
        if(position==1)
        {
            SecondCityFragment fragment2 = new SecondCityFragment();
            return fragment2;
        }
        if(position==2)
        {
            return fragment;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
