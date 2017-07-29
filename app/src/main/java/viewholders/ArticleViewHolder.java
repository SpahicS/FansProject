package viewholders;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import digitalbath.fansproject.R;

/**
 * Created by Spaja on 21-Jun-17.
 */

public class ArticleViewHolder extends RecyclerView.ViewHolder {

    public ImageView image;
    public CircleImageView publisherIcon;
    public TextView title, publisherNameAndTime;
    public TextView numberOfLikes;
    public TextView numberOfDislikes;
    public LinearLayout likeCont;
    public LinearLayout dislikeCont;
    public TextView like;
    public TextView dislike;
    public ImageView likeIcon;
    public ImageView dislikeIcon;
    public LinearLayout commentsCont;
    public TextView numberOfComments;

    public ArticleViewHolder(View itemView) {
        super(itemView);
        image = (ImageView) itemView.findViewById(R.id.image);
        title = (TextView) itemView.findViewById(R.id.title);
        publisherIcon = (CircleImageView) itemView.findViewById(R.id.publisher_icon);
        publisherNameAndTime = (TextView) itemView.findViewById(R.id.publisher_name_and_time);

        numberOfLikes = (TextView) itemView.findViewById(R.id.likes);
        numberOfDislikes = (TextView) itemView.findViewById(R.id.unlikes);
        numberOfComments = (TextView) itemView.findViewById(R.id.comments);

        likeCont = (LinearLayout) itemView.findViewById(R.id.like_cont);
        dislikeCont = (LinearLayout) itemView.findViewById(R.id.unlike_cont);
        commentsCont = (LinearLayout) itemView.findViewById(R.id.comment_cont);

        like = (TextView) itemView.findViewById(R.id.like);
        dislike = (TextView) itemView.findViewById(R.id.unlike);

        likeIcon = (ImageView) itemView.findViewById(R.id.like_icon);
        dislikeIcon = (ImageView) itemView.findViewById(R.id.unlike_icon);
    }

    public void setLikeButtonOn(Activity mActivity) {
        likeIcon.setColorFilter(mActivity.getResources().getColor(R.color.colorAccent));
        like.setTextColor(mActivity.getResources().getColor(R.color.colorAccent));
    }

    public void setLikeButtonOff(Activity mActivity) {
        likeIcon.setColorFilter(mActivity.getResources().getColor(R.color.light_gray_with_tr));
        like.setTextColor(mActivity.getResources().getColor(R.color.main_color_dark));
    }

    public void setDislikeButtonOn(Activity mActivity) {
        dislikeIcon.setColorFilter(mActivity.getResources().getColor(R.color.colorAccent));
        dislike.setTextColor(mActivity.getResources().getColor(R.color.colorAccent));
    }

    public void setDislikeButtonOff(Activity mActivity) {
        dislikeIcon.setColorFilter(mActivity.getResources().getColor(R.color.light_gray_with_tr));
        dislike.setTextColor(mActivity.getResources().getColor(R.color.main_color_dark));
    }
}

