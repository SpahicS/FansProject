package helpers.other;

import adapters.FeedAdapter;
import adapters.NewsAdapter;
import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ImageView;
import digitalbath.fansproject.R;
import java.io.IOException;
import me.angrybyte.goose.Article;
import me.angrybyte.goose.Configuration;
import me.angrybyte.goose.ContentExtractor;

/**
 * Created by Spaja on 25-Apr-17.
 */

public class GooseArticleDataParser extends AsyncTask<String, Void, Article> {

    private Activity mActivity;
    private Article article;

    public GooseArticleDataParser(Activity activity) {

        this.mActivity = activity;
    }

    @Override
    public Article doInBackground(String... params) {

        try {

            Configuration config = new Configuration(mActivity.getCacheDir().getAbsolutePath());
            ContentExtractor extractor = new ContentExtractor(config);
            article = extractor.extractContent(params[0]);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return article;
    }

    @Override
    public void onPostExecute(Article article) {

        if (!mActivity.isDestroyed()) {

            int asd = 0;
        }
    }
}
