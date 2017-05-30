package helpers.other;

import adapters.FeedAdapter;
import adapters.NewsAdapter;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bumptech.glide.Glide;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
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
    private RecyclerView.Adapter mAdapter;
    private FeedItem mFeedItem;
    private boolean mIsPreviewCreated;
    private int mPosition;

    public GetMetaDataFromUrl(Activity activity, RecyclerView.Adapter adapter,
        RecyclerView.ViewHolder holder, int position, FeedItem item, boolean isPreviewCreated) {

        this.mActivity = activity;
        this.mAdapter = adapter;
        this.mHolder = holder;
        this.mFeedItem = item;
        this.mPosition = position;
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

            if (mAdapter instanceof NewsAdapter) {

                ((NewsAdapter) mAdapter).onMetaTagsLoaded(mHolder, mPosition, metaTag);

            } else if (mAdapter instanceof FeedAdapter) {

                ((FeedAdapter) mAdapter).onMetaTagsLoaded(mHolder, mPosition, metaTag);
            }
        }
    }
}
