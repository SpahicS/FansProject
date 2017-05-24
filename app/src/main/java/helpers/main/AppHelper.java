package helpers.main;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;
import digitalbath.fansproject.R;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by unexpected_err on 20/05/2017.
 */

public class AppHelper {

    public static String getTimeDifference(String timePublished) {

        String newsTimeStamp = "* minutes ago";

        if (timePublished == null)
            return newsTimeStamp;

        if (timePublished.contains("CET"))
            timePublished = timePublished.replace("CET", "GMT+01:00");

        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");

        Date now;

        try {
            now = dateFormatLocal.parse(dateFormatGmt.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
            return newsTimeStamp;
        }

        Date articleDate;

        try {
            articleDate = dateFormatLocal.parse(dateFormatGmt.format(new Date(timePublished)));
        } catch (ParseException e) {
            e.printStackTrace();
            return newsTimeStamp;
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            return newsTimeStamp;
        }

        if (articleDate != null) {

            long diff = now.getTime() - articleDate.getTime();

            if (diff <= 0)
                return "0 minutes ago";

            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            if (diffDays > 0) {
                if (diffHours > 11)
                    diffDays += 1;
                newsTimeStamp = Long.toString(diffDays) + (" days ago");
            } else if (diffHours > 0) {
                if (diffMinutes > 29)
                    diffHours += 1;
                newsTimeStamp = Long.toString(diffHours) + (" hours ago");
            } else
                newsTimeStamp = Long.toString(diffMinutes).replace("-", "") + (" minutes ago");

        }

        return newsTimeStamp;
    }

    public static void showToast(Context context, String text) {

        if (context == null)
            return;

        LayoutInflater inflater = (LayoutInflater)
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_toast, null);

        TextView textView = (TextView) layout.findViewById(R.id.textToShow);
        textView.setText(text);

        Toast toast = new Toast(context.getApplicationContext());
        toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM, 0, 150);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();

    }

    public static Typeface getRobotoLight(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/robotolight.ttf");
    }

    public static Typeface getRalewayLight(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/ralewaylight.ttf");
    }

    public static Animation getAnimationUp(Context context) {
        return AnimationUtils.loadAnimation(context, R.anim.abc_grow_fade_in_from_bottom);
    }

    public static Animation getAnimationDown(Context context) {
        return AnimationUtils.loadAnimation(context, R.anim.abc_shrink_fade_out_from_bottom);
    }

    public static void show(View view, Context context) {
        view.setVisibility(View.VISIBLE);
        view.startAnimation(getAnimationUp(context));
    }

    public static void hide(View view, Context context) {
        view.setVisibility(View.GONE);
        view.startAnimation(getAnimationDown(context));
    }
}
