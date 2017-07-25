package listeners;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.firebase.database.DatabaseReference;

import java.util.Date;

import digitalbath.fansproject.R;
import helpers.main.AppController;
import models.Comment;

/**
 * Created by Spaja on 12-Jul-17.
 */

public class OnPostCommentListener implements View.OnClickListener{

    private DatabaseReference mItemCommentsDatabase;
    private RelativeLayout mCommentsCont;

    public OnPostCommentListener(DatabaseReference itemCommentsDatabase,
        RelativeLayout commentsCont) {

        mItemCommentsDatabase = itemCommentsDatabase;
        mCommentsCont = commentsCont;

    }

    @Override
    public void onClick(View v) {

        EditText commentInput = (EditText) mCommentsCont.findViewById(R.id.comment_input);

        Comment comment = new Comment();

        comment.setUsername(AppController.getUser().getDisplayName());
        comment.setUserId(AppController.getUser().getUid());
        comment.setText(commentInput.getText().toString());
        comment.setDate(new Date().toString());

        if (AppController.getUser().getPhotoUrl() != null)
            comment.setAvatar(AppController.getUser().getPhotoUrl().toString());

        mItemCommentsDatabase.push().setValue(comment);

        commentInput.setText("");

    }
}
