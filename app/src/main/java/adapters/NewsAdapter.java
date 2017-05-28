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
import helpers.other.GetMetaDataFromUrl;
import listeners.OnArticleClickListener;
import models.ResponseData;
import com.bumptech.glide.Glide;

/**
 * Created by Spaja on 26-Apr-17.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ArticleViewHolder> {

    private ResponseData mDataSet;
    private Activity mActivity;

    public NewsAdapter(Activity activity, ResponseData mDataSet) {
        this.mDataSet = mDataSet;
        this.mActivity = activity;
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.article_item, parent, false);
        return new ArticleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {

        holder.title.setText(mDataSet.getChannel().getNewsList().get(position).getTitle());

        String url = mDataSet.getChannel().getNewsList().get(position).getLink().split("url=")[1];
        GetMetaDataFromUrl worker = new GetMetaDataFromUrl(mActivity, holder, null, false);
        worker.execute(url);

        String domain = AppHelper.getDomainName(url);

        holder.publisher.setText(domain);

        Glide.with(mActivity)
                .load("http://" + domain + "/favicon.ico")
                .placeholder(R.drawable.ic_rss_feed)
                .into(holder.favIcon);

        holder.pubDate.setText(AppHelper.getTimeDifference(mDataSet.getChannel()
            .getNewsList().get(position).getPubDate()));

        holder.title.setOnClickListener(new OnArticleClickListener(mActivity, url));
        holder.image.setOnClickListener(new OnArticleClickListener(mActivity, url));

    }

    @Override
    public int getItemCount() {
        return mDataSet.getChannel().getNewsList().size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder {

        public ImageView image, favIcon;
        public TextView title, publisher, pubDate;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.news_item_image);
            title = (TextView) itemView.findViewById(R.id.news_item_title);
            favIcon = (ImageView) itemView.findViewById(R.id.fav_icon);
            publisher = (TextView) itemView.findViewById(R.id.publisher);
            pubDate = (TextView) itemView.findViewById(R.id.pub_date);
        }
    }
}
