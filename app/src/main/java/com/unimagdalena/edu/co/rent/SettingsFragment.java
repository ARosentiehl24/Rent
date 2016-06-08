package com.unimagdalena.edu.co.rent;


import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;

import es.dmoral.prefs.Prefs;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        EditTextPreference preferenceIp = (EditTextPreference) findPreference("ip");
        String ip = Prefs.with(getActivity()).read("ip", "192.168.0.16");
        preferenceIp.setSummary(ip);
        preferenceIp.setOnPreferenceChangeListener(this);

        EditTextPreference preferencePort = (EditTextPreference) findPreference("port");
        String port = Prefs.with(getActivity()).read("port", "80");
        preferencePort.setSummary(port);
        preferencePort.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String value = newValue.toString();
        Prefs.with(getActivity()).write(preference.getKey(), value);

        return true;
    }
}
