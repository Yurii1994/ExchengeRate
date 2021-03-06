package muv.exchangerate;

import android.content.Context;
import android.content.SharedPreferences;

public class SaveLoadPreferences
{
    public void saveStringPreferences(String nameFile, String nameVariable, String value, Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(nameFile, 1);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(nameVariable, value);
        editor.apply();
    }

    public String loadStringPreferences(String nameFile, String nameVariable, Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(nameFile, Context.MODE_PRIVATE);
        String pr;
        pr = preferences.getString(nameVariable, "");
        return pr;
    }

    public void saveIntegerPreferences(String nameFile, String nameVariable, int value, Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(nameFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(nameVariable, value);
        editor.apply();
    }

    public int loadIntegerPreferences(String nameFile, String nameVariable, Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(nameFile, Context.MODE_PRIVATE);
        int pr;
        pr = preferences.getInt(nameVariable, 0);
        return pr;
    }

    public void clearPreferences(String nameFile, Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(nameFile, Context.MODE_PRIVATE);
        preferences.edit().clear().apply();
    }
}
