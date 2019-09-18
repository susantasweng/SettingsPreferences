package com.example.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.Preference.OnPreferenceClickListener;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.SwitchPreferenceCompat;

import com.example.androidsettingsexample.R;

/**
 * A simple {@link PreferenceFragmentCompat} subclass.
 */
public class PrefsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener,
        Preference.OnPreferenceChangeListener, OnPreferenceClickListener {

    private final String TAG = PrefsFragment.class.getSimpleName();
    private SharedPreferences sharedPreferences;
    private OnPreferenceSelectListener mListener;
    private ListPreference mListPreference;
    private final String NAME_PREFERENCE = "full_name";
    private final String EMAIL_PREFERENCE = "email_address";
    private final String SYNC_INTERVAL_PREFERENCE = "sync_interval";
    private final String GENERAL_PREFERENCE = "general_preference";
    private final String NOTIFICATION_PREFERENCE = "notification_preference";
    private final String SYNC_PREFERENCE = "sync_preference";
    private final String SOUND_PREFERENCE = "sound_preference";
    private Preference namePreference;
    private Preference emailPreference;
    private Preference syncIntervalPreference;

    public interface OnPreferenceSelectListener {
        void onPreferenceSelected(Class<? extends Fragment> clazz);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        namePreference = findPreference(NAME_PREFERENCE);
        emailPreference = findPreference(EMAIL_PREFERENCE);
        syncIntervalPreference = findPreference(SYNC_INTERVAL_PREFERENCE);

        findPreference(GENERAL_PREFERENCE).setOnPreferenceClickListener(this);
        findPreference(NOTIFICATION_PREFERENCE).setOnPreferenceClickListener(this);
        findPreference(SYNC_INTERVAL_PREFERENCE).setOnPreferenceClickListener(this);
        findPreference(SOUND_PREFERENCE).setOnPreferenceClickListener(this);

        setDefaults();
    }

    private void setDefaults() {
        updateValueFromPreference(sharedPreferences, NAME_PREFERENCE);
        updateValueFromPreference(sharedPreferences, EMAIL_PREFERENCE);
        updateValueFromPreference(sharedPreferences, SYNC_INTERVAL_PREFERENCE);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updateValueFromPreference(sharedPreferences, key);
    }

    private void updateValueFromPreference(SharedPreferences sharedPreferences, String key) {
        String value = "";
        switch (key) {
            case NAME_PREFERENCE:
                value = sharedPreferences.getString(NAME_PREFERENCE, "NULL");
                if (!value.equals("")) {
                    namePreference.setSummary(value);
                }
                break;

            case EMAIL_PREFERENCE:
                value = sharedPreferences.getString(EMAIL_PREFERENCE, "NULL");
                if (!value.equals("")) {
                    emailPreference.setSummary(value);
                }
                break;

            case SYNC_INTERVAL_PREFERENCE:
                value = sharedPreferences.getString(SYNC_INTERVAL_PREFERENCE, "NULL");
                syncIntervalPreference.setSummary(value);
                break;
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HomeFragment.OnFragmentInteractionListener) {
            mListener = (OnPreferenceSelectListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case GENERAL_PREFERENCE:
                startActivityForResult(new Intent(Settings.ACTION_DEVICE_INFO_SETTINGS), 0);
                break;
            case NOTIFICATION_PREFERENCE:
                startActivityForResult(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS), 0);
                break;
            case SYNC_PREFERENCE:
                startActivityForResult(new Intent(Settings.ACTION_SYNC_SETTINGS), 0);
                break;
            case SOUND_PREFERENCE:
                //startActivityForResult(new Intent(android.provider.Settings.ACTION_SOUND_SETTINGS), 0);
                mListener.onPreferenceSelected(SoundPrefsFragment.class);
                break;
        }
        return false;
    }


    //TODO: Add the Preferences through Java
    public void addNotificationPreferences() {
        Context context = getPreferenceManager().getContext();
        PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(context);

        SwitchPreferenceCompat notificationPreference = new SwitchPreferenceCompat(context);
        notificationPreference.setKey("notifications");
        notificationPreference.setTitle("Enable message notifications");

        PreferenceCategory notificationCategory = new PreferenceCategory(context);
        notificationCategory.setKey("notifications_category");
        notificationCategory.setTitle("Notifications");
        screen.addPreference(notificationCategory);
        notificationCategory.addPreference(notificationPreference);

        Preference feedbackPreference = new Preference(context);
        feedbackPreference.setKey("feedback");
        feedbackPreference.setTitle("Send feedback");
        feedbackPreference.setSummary("Report technical issues or suggest new features");

        PreferenceCategory helpCategory = new PreferenceCategory(context);
        helpCategory.setKey("help");
        helpCategory.setTitle("Help");
        screen.addPreference(helpCategory);
        helpCategory.addPreference(feedbackPreference);

        setPreferenceScreen(screen);
    }


    // No support for RingtonePreference in support library
    // https://issuetracker.google.com/issues/37057453#c2

    //In the preferences XML resource, change RingtonePreference to Preference. Then, here is the implementation Ringtone Preference
    //@Override
    /*public boolean onPreferenceTreeClick(Preference preference) {
        if (preference.getKey().equals(KEY_RINGTONE_PREFERENCE)) {
            Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI, Settings.System.DEFAULT_NOTIFICATION_URI);

            String existingValue = getRingtonePreferenceValue(); // TODO
            if (existingValue != null) {
                if (existingValue.length() == 0) {
                    // Select "Silent"
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
                } else {
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, Uri.parse(existingValue));
                }
            } else {
                // No ringtone has been selected, set to the default
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, Settings.System.DEFAULT_NOTIFICATION_URI);
            }

            startActivityForResult(intent, REQUEST_CODE_ALERT_RINGTONE);
            return true;
        } else {
            return super.onPreferenceTreeClick(preference);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_ALERT_RINGTONE && data != null) {
            Uri ringtone = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            if (ringtone != null) {
                setRingtonPreferenceValue(ringtone.toString()); // TODO
            } else {
                // "Silent" was selected
                setRingtonPreferenceValue(""); // TODO
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }*/
}
