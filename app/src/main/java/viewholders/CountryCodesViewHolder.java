package viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import digitalbath.fansproject.R;
import helpers.main.AppHelper;

/**
 * Created by Spaja on 21-Jun-17.
 */

public class CountryCodesViewHolder extends RecyclerView.ViewHolder {

    public TextView countryCode;
    public ImageView checkMark;
    public RelativeLayout country;

    public CountryCodesViewHolder(View itemView) {

        super(itemView);

    }

    public void initializeViews(View itemView, Context mContext) {
        countryCode = (TextView) itemView.findViewById(R.id.country_code);
        countryCode.setTypeface(AppHelper.getRobotoLight(mContext));
        checkMark = (ImageView) itemView.findViewById(R.id.check_mark);
        country = (RelativeLayout) itemView.findViewById(R.id.country);
    }
}

