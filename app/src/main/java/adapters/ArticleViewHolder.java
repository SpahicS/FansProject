package adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import de.hdodenhof.circleimageview.CircleImageView;
import digitalbath.fansproject.R;
import helpers.main.AppHelper;
import helpers.other.GetMetaDataFromUrl;
import listeners.OnArticleClickListener;
import models.NewsItem;
import persistance.NewsItemDataService;

/**
 * Created by Spaja on 21-Jun-17.
 */

class ArticleViewHolder extends RecyclerView.ViewHolder {

    ImageView image;
    private CircleImageView publisherIcon;
    private TextView title, publisherNameAndTime;
    TextView numberOfLikes;
    TextView numberOfUnlikes;
    LinearLayout likeCont;
    LinearLayout unlikeCont;
    private TextView like;
    private TextView unlike;
    private ImageView likeIcon;
    private ImageView unlikeIcon;
    LinearLayout commentsCont;
    public TextView numberOfComments;

    ArticleViewHolder(View itemView) {
        super(itemView);
        image = (ImageView) itemView.findViewById(R.id.image);
        title = (TextView) itemView.findViewById(R.id.title);
        publisherIcon = (CircleImageView) itemView.findViewById(R.id.publisher_icon);
        publisherNameAndTime = (TextView) itemView.findViewById(R.id.publisher_name_and_time);

        numberOfLikes = (TextView) itemView.findViewById(R.id.likes);
        numberOfUnlikes = (TextView) itemView.findViewById(R.id.unlikes);
        numberOfComments = (TextView) itemView.findViewById(R.id.comments);

        likeCont = (LinearLayout) itemView.findViewById(R.id.like_cont);
        unlikeCont = (LinearLayout) itemView.findViewById(R.id.unlike_cont);
        commentsCont = (LinearLayout) itemView.findViewById(R.id.comment_cont);

        like = (TextView) itemView.findViewById(R.id.like);
        unlike = (TextView) itemView.findViewById(R.id.unlike);

        likeIcon = (ImageView) itemView.findViewById(R.id.like_icon);
        unlikeIcon = (ImageView) itemView.findViewById(R.id.unlike_icon);
    }

    void render(final NewsAdapter newsAdapter, final int position, final NewsItem articleItem,
                final Activity mActivity, final NewsItemDataService newsItemDataService) {

        title.setText(articleItem.getTitle());

        String url = articleItem.getLink().split("url=")[1];

        if (articleItem.getImageUrl() == null) {

            image.setVisibility(View.VISIBLE);
            GetMetaDataFromUrl worker = new GetMetaDataFromUrl
                    (mActivity, newsAdapter, this, position, null, false);
            worker.execute(url);

        } else if (TextUtils.isEmpty(articleItem.getImageUrl())) {

            image.setVisibility(View.GONE);

        } else {

            newsAdapter.loadImage(image, articleItem.getImageUrl(), false);

        }

        String domain = AppHelper.getDomainName(url);

        publisherNameAndTime.setText(domain + " · " + AppHelper
                .getTimeDifference(articleItem.getPubDate()));

        SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>() {

            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {

                publisherIcon.setImageBitmap(bitmap);

            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);

                publisherIcon.setImageResource(R.drawable.publisher_icon);
            }
        };

        Glide.with(mActivity)
                .load("http://www." + domain + "/favicon.ico")
                .asBitmap()
                .into(target);

        image.setOnClickListener(new OnArticleClickListener(mActivity, url));
   }

    void setLikeButtonOn(Activity mActivity) {
        likeIcon.setColorFilter(mActivity.getResources().getColor(R.color.colorAccent));
        like.setTextColor(mActivity.getResources().getColor(R.color.colorAccent));
    }

    void setLikeButtonOff(Activity mActivity) {
        likeIcon.setColorFilter(mActivity.getResources().getColor(R.color.light_gray_with_tr));
        like.setTextColor(mActivity.getResources().getColor(R.color.main_color_dark));
    }

    void setDislikeButtonOn(Activity mActivity) {
        unlikeIcon.setColorFilter(mActivity.getResources().getColor(R.color.colorAccent));
        unlike.setTextColor(mActivity.getResources().getColor(R.color.colorAccent));
    }

    void setDislikeButtonOff(Activity mActivity) {
        unlikeIcon.setColorFilter(mActivity.getResources().getColor(R.color.light_gray_with_tr));
        unlike.setTextColor(mActivity.getResources().getColor(R.color.main_color_dark));
    }
}

