package com.example.androidsettingsexample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.fragments.HomeFragment;
import com.example.fragments.HomeFragment.OnFragmentInteractionListener;
import com.example.fragments.PrefsFragment;
import com.example.fragments.PrefsFragment.OnPreferenceSelectListener;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener, OnPreferenceSelectListener {

    private final String TAG = MainActivity.class.getSimpleName();
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        switchFragment(HomeFragment.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void switchFragment(Class<? extends Fragment> toClazz) {
        Fragment fragment = null;
        try {
            fragment = toClazz.newInstance();

        } catch (Exception e) {
            Log.e(TAG, "could not instantiate fragment: " + e, e);
        }

        if (fragment != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


            if (toClazz.equals(HomeFragment.class)) {
                fragmentTransaction.add(R.id.fragment_container, fragment, toClazz.getName());
            } else {
                fragmentTransaction.remove(getCurrentFragment());
                fragmentTransaction.add(R.id.fragment_container, fragment, toClazz.getName());
            }
            fragmentTransaction.commit();
        }
    }

    public Fragment getCurrentFragment() {
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if(currentFragment != null) {
            return currentFragment;
        }
        return null;
    }

    @Override
    public void onFragmentInteraction() {
        switchFragment(PrefsFragment.class);
    }

    @Override
    public void onBackPressed() {
        if (getCurrentFragment() instanceof HomeFragment) {
            this.finish();
        }
        super.onBackPressed();
    }

    @Override
    public void onPreferenceSelected(Class<? extends Fragment> clazz) {
        switchFragment(clazz);
    }
}
