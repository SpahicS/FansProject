package viewholders;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import digitalbath.fansproject.R;

/**
 * Created by Spaja on 27-Jul-17.
 */

public class FeedItemViewHolder extends RecyclerView.ViewHolder {

    public TextView message;
    public TextView username;
    public TextView time;
    public TextView numberOfLikes;
    public TextView numberOfComments;
    public TextView numberOfDislikes;
    public CircleImageView avatar;
    public ImageView image;
    public LinearLayout likeCont;
    public LinearLayout dislikeCont;
    public LinearLayout commentCont;
    public TextView like;
    public TextView dislike;
    public ImageView likeIcon;
    public ImageView dislikeIcon;
    public RelativeLayout articleCont;
    public TextView articleTitle;
    public TextView articlePublisher;
    public CircleImageView articlePublisherIcon;
    public ImageView articleImage;

    public FeedItemViewHolder(View itemView) {
        super(itemView);

        message = (TextView) itemView.findViewById(R.id.message);
        username = (TextView) itemView.findViewById(R.id.username);
        time = (TextView) itemView.findViewById(R.id.time_published);
        avatar = (CircleImageView) itemView.findViewById(R.id.avatar);
        image = (ImageView) itemView.findViewById(R.id.image);

        numberOfLikes = (TextView) itemView.findViewById(R.id.likes);
        numberOfComments = (TextView) itemView.findViewById(R.id.comments);
        numberOfDislikes = (TextView) itemView.findViewById(R.id.unlikes);

        likeCont = (LinearLayout) itemView.findViewById(R.id.like_cont);
        dislikeCont = (LinearLayout) itemView.findViewById(R.id.unlike_cont);
        commentCont = (LinearLayout) itemView.findViewById(R.id.comment_cont);

        like = (TextView) itemView.findViewById(R.id.like);
        dislike = (TextView) itemView.findViewById(R.id.unlike);

        likeIcon = (ImageView) itemView.findViewById(R.id.like_icon);
        dislikeIcon = (ImageView) itemView.findViewById(R.id.unlike_icon);

        articleCont = (RelativeLayout) itemView.findViewById(R.id.article_cont);
        articleTitle = (TextView) itemView.findViewById(R.id.article_title);
        articlePublisher = (TextView) itemView.findViewById(R.id.article_publisher_name);
        articlePublisherIcon = (CircleImageView) itemView.findViewById(R.id.article_publisher_icon);
        articleImage = (ImageView) itemView.findViewById(R.id.article_image);
    }

    public void setLikeButtonOn(Activity mActivity) {
        like.setTextColor(mActivity.getResources().getColor(R.color.colorAccent));
        likeIcon.setColorFilter(ContextCompat.getColor(mActivity, R.color.colorAccent));
    }

    public void setLikeButtonOff(Activity mActivity) {
        like.setTextColor(mActivity.getResources().getColor(R.color.main_color_dark));
        likeIcon.setColorFilter(ContextCompat.getColor(mActivity, R.color.light_gray_with_tr));
    }

    public void setDislikeButtonOn(Activity mActivity) {
        dislike.setTextColor(mActivity.getResources().getColor(R.color.colorAccent));
        dislikeIcon.setColorFilter(ContextCompat.getColor(mActivity, R.color.colorAccent));
    }

    public void setDislikeButtonOff(Activity mActivity) {
        dislike.setTextColor(mActivity.getResources().getColor(R.color.main_color_dark));
        dislikeIcon.setColorFilter(ContextCompat.getColor(mActivity, R.color.light_gray_with_tr));
    }
}
