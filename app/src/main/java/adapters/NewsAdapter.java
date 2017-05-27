package adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import digitalbath.fansproject.R;
import helpers.main.AppHelper;
import helpers.main.GetMetaDataFromUrl;
import listeners.OnArticleClickListener;
import models.ResponseData;
import com.bumptech.glide.Glide;

/**
 * Created by Spaja on 26-Apr-17.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    private ResponseData mDataSet;
    private Activity mActivity;

    public NewsAdapter(Activity activity, ResponseData mDataSet) {
        this.mDataSet = mDataSet;
        this.mActivity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_list_item, parent, false);
        return new NewsAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.newsTitle.setText(mDataSet.getChannel().getNewsList().get(position).getTitle());

        String url = mDataSet.getChannel().getNewsList().get(position).getLink().split("url=")[1];
        GetMetaDataFromUrl worker = new GetMetaDataFromUrl(mActivity, holder.newsImage);
        worker.execute(url);

        String publisher = url.split(".com/")[0] + ".com";
        String helper = publisher.split("//")[1];
        if (helper.contains("/") && helper.contains("www")) {

            holder.publisher.setText(helper.split("/")[0].substring(4));

        } else if (helper.contains("/")) {

            holder.publisher.setText(helper.split("/")[0]);

        } else {

            holder.publisher.setText(helper.substring(4));
        }

        Glide.with(mActivity)
                .load(publisher + "/favicon.ico")
                .placeholder(R.drawable.ic_rss_feed_24dp)
                .into(holder.favIcon);

        holder.pubDate.setText(AppHelper.getTimeDifference(mDataSet.getChannel().getNewsList().get(position).getPubDate()));

        holder.newsTitle.setOnClickListener(new OnArticleClickListener(mActivity, url));
        holder.newsImage.setOnClickListener(new OnArticleClickListener(mActivity, url));

    }

    @Override
    public int getItemCount() {
        return mDataSet.getChannel().getNewsList().size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView newsImage, favIcon;
        TextView newsTitle, publisher, pubDate;

        MyViewHolder(View itemView) {
            super(itemView);
            newsImage = (ImageView) itemView.findViewById(R.id.news_item_image);
            newsTitle = (TextView) itemView.findViewById(R.id.news_item_title);
            favIcon = (ImageView) itemView.findViewById(R.id.fav_icon);
            publisher = (TextView) itemView.findViewById(R.id.publisher);
            pubDate = (TextView) itemView.findViewById(R.id.pub_date);
        }
    }
}
