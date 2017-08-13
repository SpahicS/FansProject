package helpers.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import digitalbath.fansproject.R;

/**
 * Created by unexpected_err on 06/08/2017.
 */

public class FansTextView extends android.support.v7.widget.AppCompatTextView {

    String fontStyle = "regular";

    public FansTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public FansTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.font_style);
        CharSequence s = a.getString(R.styleable.font_style_font_style);

        if (s != null) {
            this.fontStyle = s.toString();
        }

        a.recycle();

        init(context);
    }

    public FansTextView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {

        switch (fontStyle) {

            case "light":
                Typeface tf = Typeface.createFromAsset(context.getAssets(),
                    "fonts/opensanslight.ttf");
                setTypeface(tf);
                break;
            case "regular":
                Typeface tf2 = Typeface.createFromAsset(context.getAssets(),
                    "fonts/opensansregular.ttf");
                setTypeface(tf2);
                break;
            case "bold":
                Typeface tf1 = Typeface.createFromAsset(context.getAssets(),
                    "fonts/opensansboldd.ttf");
                setTypeface(tf1);
                break;
        }

    }
}