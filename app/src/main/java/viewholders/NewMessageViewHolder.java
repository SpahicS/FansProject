package viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import digitalbath.fansproject.R;


/**
 * Created by Spaja on 27-Jul-17.
 */

public class NewMessageViewHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public EditText message;
    public CircleImageView avatar;
    public ImageView image;
    public LinearLayout post;
    public LinearLayout discard;
    public LinearLayout actions;

    public RelativeLayout articlePreview;
    public TextView articleTitle;
    public TextView articlePublisher;
    public ImageView articlePublisherIcon;
    public ImageView articleImage;
    public ProgressBar progressBar;

    public NewMessageViewHolder(View itemView) {
        super(itemView);

        title = (TextView) itemView.findViewById(R.id.title);
        message = (EditText) itemView.findViewById(R.id.message);
        avatar = (CircleImageView) itemView.findViewById(R.id.avatar);
        image = (ImageView) itemView.findViewById(R.id.image);
        post = (LinearLayout) itemView.findViewById(R.id.post_btn);
        discard = (LinearLayout) itemView.findViewById(R.id.discard_btn);
        actions = (LinearLayout) itemView.findViewById(R.id.actions_cont);

        articlePreview = (RelativeLayout) itemView.findViewById(R.id.preview_article);
        articleTitle = (TextView) itemView.findViewById(R.id.preview_title);
        articlePublisher = (TextView) itemView.findViewById(R.id.preview_publisher_name);
        articlePublisherIcon = (ImageView) itemView.findViewById(R.id.preview_publisher_icon);
        articleImage = (ImageView) itemView.findViewById(R.id.article_image);

        progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);

    }
}

