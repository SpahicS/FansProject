package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import digitalbath.fansproject.R;
import listeners.OnCountryCodeClickListener;

/**
 * Created by Spaja on 31-May-17.
 */

public class CountryCodesAdapter extends RecyclerView.Adapter<CountryCodesAdapter.MyViewHolder> {

    private String[] mDataSet;
    private Context mContext;

    public CountryCodesAdapter(Context context, String[] stringArray) {

        this.mDataSet = stringArray;
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.country_code_item, parent, false);
        return new CountryCodesAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.countryCode.setText(mDataSet[position]);
        holder.countryCode.setOnClickListener(new OnCountryCodeClickListener(mContext, holder.countryCode.getText().toString()));

    }

    @Override
    public int getItemCount() {
        return mDataSet.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView countryCode;

        public MyViewHolder(View itemView) {

            super(itemView);
            countryCode = (TextView) itemView.findViewById(R.id.country_code);
        }
    }
}
