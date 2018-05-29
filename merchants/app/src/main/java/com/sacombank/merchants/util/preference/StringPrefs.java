package com.sacombank.merchants.util.preference;

import android.content.Context;
import android.content.SharedPreferences;

public class StringPrefs extends PrefsItem {

    public StringPrefs(Context context, String name, String defValue) {
        super(context, name);
        this.defValue = defValue;
    }

    @Override
    protected Object getPrefValue() {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        String v = settings.getString(getName(), defValue);
        return v;
    }

    @Override
    protected void setPrefValue(Object value) {
        String v = (String) value;
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(getName(), v);
        editor.commit();
    }


    public String getValue() {
        String s = (String) getPrefValue();
        return s;
    }

    public void setValue(String value) {
        setPrefValue(value);
    }

    private String defValue;
}
