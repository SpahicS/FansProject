package listeners;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import activities.ArticleActivity;


/**
 * Created by Spaja on 28-Apr-17.
 */

public class OnArticleClickListener implements View.OnClickListener {

    private Activity mActivity;
    private String mArticleUrl;

    public OnArticleClickListener(Activity mActivity, String articleUrl) {
        this.mActivity = mActivity;
        this.mArticleUrl = articleUrl;
    }

    @Override
    public void onClick(View v) {

        Intent i = new Intent(mActivity, ArticleActivity.class);
        i.putExtra("URL", mArticleUrl);
        mActivity.startActivity(i);
    }
}
