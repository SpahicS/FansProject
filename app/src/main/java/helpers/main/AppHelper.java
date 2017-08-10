package helpers.main;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;
import android.widget.Toast;

import digitalbath.fansproject.R;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

                if (diffDays == 1) {
                    newsTimeStamp = Long.toString(diffDays) + (" day ago");
                } else {
                    newsTimeStamp = Long.toString(diffDays) + (" days ago");
                }
            } else if (diffHours > 0) {
                if (diffMinutes > 29)
                    diffHours += 1;

                if (diffHours == 1) {
                    newsTimeStamp = Long.toString(diffHours) + (" hour ago");
                } else {
                    newsTimeStamp = Long.toString(diffHours) + (" hours ago");
                }
            } else if (diffMinutes == 1) {
                newsTimeStamp = Long.toString(diffMinutes).replace("-", "") + (" minute ago");
            } else {
                newsTimeStamp = Long.toString(diffMinutes).replace("-", "") + (" minutes ago");
            }

        }

        return newsTimeStamp;
    }

    public static String getDomainName(String url) {

        String domain = "";

        URI uri;

        try {
            uri = new URI(url);
            domain = uri.getHost();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return domain.startsWith("www.") ? domain.substring(4) : domain;
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

    public static String findUrl(String string) {

        String url = "";

        Pattern urlPattern = Pattern.compile(
            "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
                + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

        Matcher matcher = urlPattern.matcher(string);

        while (matcher.find()) {
            int matchStart = matcher.start(1);
            int matchEnd = matcher.end();

            url = string.substring(matchStart, matchEnd);
        }

        return url;
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

    public static String getNewsEditionCode(Context context, String countryCode){

        List<String> editionCodes;
        editionCodes = Arrays.asList(context.getResources().getStringArray(R.array.country_codes));
        String editionCode = "us";

        int codesSize = editionCodes.size();
        for (int i = 0; i < codesSize; i++) {
            if (editionCodes.get(i).equals(countryCode)) {
                editionCode = editionCodes.get(i);
                break;
            } else if (editionCodes.get(i).contains(countryCode)) {
                editionCode = editionCodes.get(i);
            }
        }

        return editionCode;
    }

    public static String getNewsEditionCode(Context context) {

        String newsEdition = Locale.getDefault().getLanguage().toLowerCase();
        String country = Locale.getDefault().getCountry().toLowerCase();
        String editionCode;

        if (newsEdition.equals(country)) {
            editionCode = newsEdition;
        } else {
            editionCode = newsEdition + "_" + country;
        }

        String edition = AppHelper.getNewsEditionCode(context, editionCode);

        if (edition.equals("us")) {
            edition = AppHelper.getNewsEditionCode(context, country);
        }

        return edition;
    }

    public static void animateItemAppearance(View view, int position) {

        AnimationSet set = new AnimationSet(true);

        ScaleAnimation scale =  new ScaleAnimation(0.9f, 1f, 0.9f, 1f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        AlphaAnimation alpha = new AlphaAnimation(0, 1);

        set.addAnimation(scale);
        set.addAnimation(alpha);

        switch (position) {

            case 0:
               set.setStartOffset(0);
                break;
            case 1:
                set.setStartOffset(300);
                break;
            case 2:
                set.setStartOffset(600);
                break;
            case 3:
                set.setStartOffset(900);
                break;
            default:
                set.setStartOffset(0);
                break;

        }

        set.setDuration(500);

        view.startAnimation(set);

    }


}
