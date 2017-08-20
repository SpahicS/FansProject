package listeners;

import android.view.View;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

import helpers.main.AppController;
import models.news.FeedItem;
import models.news.ArticleItem;
import helpers.other.NewsItemDataService;

/**
 * Created by Spaja on 27-Jul-17.
 */

public class OnDislikeClickListener implements View.OnClickListener {

    private FeedItem mFeedItem;
    private ArticleItem mArticleItem;
    private NewsItemDataService mNewsItemDataService;
    private DatabaseReference mDataBaseReference;
    private String uid;

    public OnDislikeClickListener(FeedItem mFeedItem, DatabaseReference mDataBaseReference) {
        this.mFeedItem = mFeedItem;
        this.mDataBaseReference = mDataBaseReference;
        this.uid = AppController.getUser().getUid();
    }

    public OnDislikeClickListener(ArticleItem articleItem, NewsItemDataService newsItemDataService) {
        this.mArticleItem = articleItem;
        this.mNewsItemDataService = newsItemDataService;
        this.uid = AppController.getUser().getUid();
    }

    @Override
    public void onClick(View v) {

        if (mFeedItem != null) {
            if (mFeedItem.getDislikes().get(uid) == null) {

                Map<String, Object> taskMap = new HashMap<>();
                taskMap.put(uid, true);

                mDataBaseReference.child(mFeedItem.getId())
                        .child("dislikes").updateChildren(taskMap);

            } else {

                mDataBaseReference.child(mFeedItem.getId())
                        .child("dislikes").child(uid).removeValue();
            }
        } else {
            if (mArticleItem.getDislikes().containsKey(uid)) {
                mNewsItemDataService.removeDislike(mArticleItem.getId(), uid);
            } else {
                mNewsItemDataService.saveDislike(mArticleItem.getId(), uid);
            }
        }
    }
}
