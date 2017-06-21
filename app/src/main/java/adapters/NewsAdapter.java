package adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import digitalbath.fansproject.R;
import helpers.other.MetaTagsLoad;
import models.Item;
import models.MetaTag;
import models.NewsItem;
import models.ResponseData;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Spaja on 26-Apr-17.
 */

public class NewsAdapter extends RecyclerView.Adapter<ArticleViewHolder>
        implements MetaTagsLoad {

    private ResponseData mDataSet;
    private Activity mActivity;
    private DatabaseReference mNewsRef;

    public NewsAdapter(Activity activity, ResponseData mDataSet) {

        this.mDataSet = mDataSet;
        this.mActivity = activity;
        FirebaseDatabase mNewsDatabase = FirebaseDatabase.getInstance();
        mNewsRef = mNewsDatabase.getReference().child("news");

    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.article_item, parent, false);
        return new ArticleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ArticleViewHolder holder, final int position) {

        holder.render(this, position, mDataSet.getChannel().getNewsList().get(position),
                mActivity);
    }

    @Override
    public int getItemCount() {
        return mDataSet.getChannel().getNewsList().size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onMetaTagsLoaded(RecyclerView.ViewHolder holder, int position, MetaTag metatag) {

        final ArticleViewHolder articleViewHolder = (ArticleViewHolder) holder;

        Item articleItem = mDataSet.getChannel().getNewsList().get(position);

        articleItem.setImageUrl(metatag.getImageUrl());

        loadImage(articleViewHolder.image, articleItem.getImageUrl(), true);

    }

    void loadImage(final ImageView image, String imageUrl, final boolean animate) {

        SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>() {

            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {

                if (animate) {

                    TransitionDrawable td = new TransitionDrawable(new Drawable[]{
                            new ColorDrawable(Color.TRANSPARENT),
                            new BitmapDrawable(mActivity.getResources(), bitmap)
                    });

                    image.setImageDrawable(td);

                    td.startTransition(300);

                } else {

                    image.setImageBitmap(bitmap);
                }
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);

                image.setVisibility(View.GONE);

            }
        };

        Glide.with(mActivity)
                .load(imageUrl)
                .asBitmap()
                .into(target);

    }
}
