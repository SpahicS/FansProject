package adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import de.hdodenhof.circleimageview.CircleImageView;
import digitalbath.fansproject.R;
import helpers.main.AppHelper;
import helpers.other.GetMetaDataFromUrl;
import helpers.other.MetaTagsLoad;
import listeners.OnArticleClickListener;
import models.Item;
import models.MetaTag;
import models.ResponseData;
import com.bumptech.glide.Glide;

/**
 * Created by Spaja on 26-Apr-17.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>
    implements MetaTagsLoad{

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
    public void onBindViewHolder(final ArticleViewHolder holder, int position) {

        Item articleItem = mDataSet.getChannel().getNewsList().get(position);

        holder.title.setText(articleItem.getTitle());

        String url = articleItem.getLink().split("url=")[1];

        if (articleItem.getImageUrl() == null) {

            holder.image.setVisibility(View.VISIBLE);

            GetMetaDataFromUrl worker = new GetMetaDataFromUrl
                (mActivity, this, holder, position, null, false);

            worker.execute(url);

        } else if (TextUtils.isEmpty(articleItem.getImageUrl())) {

            holder.image.setVisibility(View.GONE);

        } else {

            loadImage(holder.image, articleItem.getImageUrl(), false);

        }

        String domain = AppHelper.getDomainName(url);

        holder.publisherNameAndTime.setText(domain + " · " + AppHelper
            .getTimeDifference(articleItem.getPubDate()));

        SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>() {

            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {

                holder.publisherIcon.setImageBitmap(bitmap);

            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);

                holder.publisherIcon.setImageResource(R.drawable.publisher_icon);
            }
        };

        Glide.with(mActivity)
            .load("http://www." + domain + "/favicon.ico")
            .asBitmap()
            .into(target);

        holder.itemView.setOnClickListener(new OnArticleClickListener(mActivity, url));

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

    private void loadImage(final ImageView image, String imageUrl, final boolean animate) {

        SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>() {

            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {

                if (animate) {

                    TransitionDrawable td = new TransitionDrawable(new Drawable[] {
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

    public class ArticleViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public CircleImageView publisherIcon;
        public TextView title, publisherNameAndTime;
        public TextView numberOfLikes;
        public TextView numberOfUnlikes;
        public LinearLayout likeCont;
        public LinearLayout unlikeCont;
        public TextView like;
        public TextView unlike;
        public ImageView likeIcon;
        public ImageView unlikeIcon;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
            publisherIcon = (CircleImageView) itemView.findViewById(R.id.publisher_icon);
            publisherNameAndTime = (TextView) itemView.findViewById(R.id.publisher_name_and_time);

            numberOfLikes = (TextView) itemView.findViewById(R.id.likes);
            numberOfUnlikes = (TextView) itemView.findViewById(R.id.unlikes);

            likeCont = (LinearLayout) itemView.findViewById(R.id.like_cont);
            unlikeCont = (LinearLayout) itemView.findViewById(R.id.unlike_cont);

            like = (TextView) itemView.findViewById(R.id.like);
            unlike = (TextView) itemView.findViewById(R.id.unlike);

            likeIcon = (ImageView) itemView.findViewById(R.id.like_icon);
            unlikeIcon = (ImageView) itemView.findViewById(R.id.unlike_icon);
        }
    }

}
