package com.example.fragments;

import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;

import com.example.androidsettingsexample.R;

/**
 * A simple {@link PreferenceFragmentCompat} subclass.
 */
public class SyncPrefsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {
    private final String TAG = PrefsFragment.class.getSimpleName();
    private OnPreferenceSelectListener mListener;

    public interface OnPreferenceSelectListener {
        void onPreferenceSelected(Preference preference);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.sync_preference, rootKey);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Log.d(TAG, preference.toString());
        return false;
    }
}
