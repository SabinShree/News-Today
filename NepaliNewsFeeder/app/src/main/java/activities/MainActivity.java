package activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import nepalinewsfeeder.sabinkharel.com.fragment.R;
import fragments.AllChannel;
import fragments.RecycleViewFragment;
import fragments.SavedFragment;
import fragments.SavedFragment.OnFragmentInteractionListener;

public class MainActivity extends AppCompatActivity implements AllChannel.OnFragmentInteractionListener, OnFragmentInteractionListener, RecycleViewFragment.OnFragmentInteractionListener {
    private static final String TAG = "MainActivity";
    private FragmentTransaction transaction;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.tabs);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = new RecycleViewFragment();
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment1 = null;
                switch (tab.getPosition()) {
                    case 0:
                        fragment1 = new RecycleViewFragment();
                        break;
                    case 1:
                        fragment1 = new AllChannel();
                        break;
                    case 2:
                        fragment1 = new SavedFragment();
                        break;
                }
                FragmentManager fragmentManager1 = getSupportFragmentManager();
                fragmentManager1.beginTransaction().replace(R.id.fragment_container, fragment1).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


}

