package helpers.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by unexpected_err on 06/08/2017.
 */

public class FansTextView extends android.support.v7.widget.AppCompatTextView {

    public FansTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public FansTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FansTextView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
            "fonts/ralewaymedium.ttf");
        setTypeface(tf);
    }
}