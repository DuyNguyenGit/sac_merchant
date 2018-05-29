package com.sacombank.merchants.util.preference;

import android.content.Context;
import android.content.SharedPreferences;

public class IntegerPrefs extends PrefsItem {
    public IntegerPrefs(Context context, String name, int defValue) {
        super(context, name);
        this.defValue = defValue;
    }

    @Override
    public void setPrefValue(Object value) {
        Integer v = (Integer) value;
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(getName(), v.intValue());
        editor.commit();
    }

    @Override
    public Object getPrefValue() {
        // TODO Auto-generated method stub
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        int v = settings.getInt(getName(), defValue);
        return v;
    }

    public int getValue() {
        Integer i = (Integer) getPrefValue();
        return i;
    }

    public void setValue(int value) {
        setPrefValue(value);
    }

    private int defValue;
}
