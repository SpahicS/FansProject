package listeners;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import activities.ArticleActivity;
import models.news.ArticleItem;

/**
 * Created by Spaja on 28-Apr-17.
 */

public class OnArticleClickListener implements View.OnClickListener {

    private Activity mActivity;
    private ArticleItem mArticle;

    public OnArticleClickListener(Activity mActivity, ArticleItem article) {
        this.mActivity = mActivity;
        this.mArticle = article;
    }

    @Override
    public void onClick(View v) {

        Intent i = new Intent(mActivity, ArticleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("article", mArticle);
        i.putExtras(bundle);
        mActivity.startActivity(i);
    }
}
