package helpers.other;

import adapters.FeedAdapter;
import adapters.NewsAdapter;
import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bumptech.glide.Glide;

import digitalbath.fansproject.R;
import helpers.main.AppHelper;
import models.FeedItem;
import models.MetaTag;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by Spaja on 25-Apr-17.
 */

public class GetMetaDataFromUrl extends AsyncTask<String, Void, Void> {

    private Activity mActivity;
    private MetaTag metaTag;
    private RecyclerView.ViewHolder mHolder;
    private FeedItem mFeedItem;
    private boolean mIsPreviewCreated;

    public GetMetaDataFromUrl(Activity activity, RecyclerView.ViewHolder holder, FeedItem item,
        boolean isPreviewCreated) {

        this.mActivity = activity;
        this.mHolder = holder;
        this.mFeedItem = item;
        this.mIsPreviewCreated = isPreviewCreated;

        metaTag = new MetaTag();
    }

    @Override
    public Void doInBackground(String... params) {

        try {

            Document document = Jsoup.connect(params[0]).get();
            metaTag.setTitle(document.select("meta[property=og:title]").attr("content"));
            metaTag.setImageUrl(document.select("meta[property=og:image]").attr("content"));
            metaTag.setArticleUrl(document.select("meta[property=og:url]").attr("content"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onPostExecute(Void aVoid) {

        if (!mActivity.isDestroyed()) {

            if (mHolder instanceof NewsAdapter.ArticleViewHolder &&
                metaTag.getImageUrl() != null) {

                Glide.with(mActivity)
                    .load(metaTag.getImageUrl())
                    .into(((NewsAdapter.ArticleViewHolder) mHolder).image);

            } else if (mHolder instanceof FeedAdapter.NewMessageViewHolder) {

                FeedAdapter.NewMessageViewHolder holder = ((FeedAdapter.NewMessageViewHolder) mHolder);

                holder.articlePreview.setVisibility(View.VISIBLE);
                holder.articleTitle.setText(metaTag.getTitle());

                if (metaTag.getImageUrl() != null) {

                    Glide.with(mActivity)
                        .load(metaTag.getImageUrl())
                        .into(holder.articleImage);

                }

                if (metaTag.getArticleUrl() != null) {

                    String domain = AppHelper.getDomainName(metaTag.getArticleUrl());

                    holder.articlePublisher.setText(domain);

                    Glide.with(mActivity)
                        .load("http://www." + domain + "/favicon.ico")
                        .placeholder(R.drawable.ic_rss_feed)
                        .into(holder.articlePublisherIcon);

                }

                mFeedItem.setArticleTitle(metaTag.getTitle());
                mFeedItem.setArticleImageUrl(metaTag.getImageUrl());
                mFeedItem.setArticleUrl(metaTag.getArticleUrl());
                mFeedItem.setIsArticleItem(true);


                mIsPreviewCreated = true;
                holder.progressBar.setVisibility(View.GONE);
            }
        }
    }
}
