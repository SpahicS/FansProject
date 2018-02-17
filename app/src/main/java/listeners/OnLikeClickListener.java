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

public class OnLikeClickListener implements View.OnClickListener {

    private FeedItem mFeedItem;
    private ArticleItem mArticleItem;
    private NewsItemDataService mNewsItemDataService;
    private DatabaseReference mDatabaseReference;
    private String uid;

    public OnLikeClickListener(FeedItem feedItem, DatabaseReference databaseReference) {
        this.mFeedItem = feedItem;
        this.mDatabaseReference = databaseReference;
        this.uid = AppController.getUser().getUid();
    }

    public OnLikeClickListener(ArticleItem articleItem, NewsItemDataService newsItemDataService) {
        this.mArticleItem = articleItem;
        this.mNewsItemDataService = newsItemDataService;
        this.uid = AppController.getUser().getUid();
    }

    @Override
    public void onClick(View v) {

        if (mFeedItem != null) {

            if (mFeedItem.getLikes().get(uid) == null) {

                Map<String, Object> taskMap = new HashMap<>();
                taskMap.put(uid, true);

                mDatabaseReference.child(mFeedItem.getId())
                        .child("likes").updateChildren(taskMap);

            } else {

                mDatabaseReference.child(mFeedItem.getId())
                        .child("likes").child(uid).removeValue();
            }

        } else {

            if (mArticleItem.getLikes().containsKey(uid)) {
                mNewsItemDataService.removeLike(mArticleItem.getId(), uid);
            } else {
                mNewsItemDataService.saveLike(mArticleItem.getId(), uid);
            }
        }
    }
}
