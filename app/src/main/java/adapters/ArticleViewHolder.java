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
    public LinearLayout commentsCont;
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

