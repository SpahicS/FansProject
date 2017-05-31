package listeners;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

/**
 * Created by Spaja on 31-May-17.
 */

public class OnCountryCodeClickListener implements View.OnClickListener {

    private String mCountryCode;
    private Context mContext;

    public OnCountryCodeClickListener(Context context, String countryCode) {
        this.mCountryCode = countryCode;
        this.mContext = context;
    }

    @Override
    public void onClick(View v) {
        SharedPreferences prefs = mContext.getSharedPreferences("COUNTRY_CODES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("COUNTRY_CODE", mCountryCode);
        editor.apply();
    }
}
