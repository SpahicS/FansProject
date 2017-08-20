package helpers.other;

import android.app.Activity;
import android.os.AsyncTask;
import de.l3s.boilerpipe.extractors.ArticleExtractor;
import java.net.URL;
import me.angrybyte.goose.Article;
import me.angrybyte.goose.Configuration;
import me.angrybyte.goose.ContentExtractor;
import org.xml.sax.InputSource;

/**
 * Created by Spaja on 25-Apr-17.
 */

public class BoilerpipeArticleDataParser extends AsyncTask<String, Void, String> {

    private Activity mActivity;
    private String string;

    public BoilerpipeArticleDataParser(Activity activity) {

        this.mActivity = activity;


    }

    @Override
    public String doInBackground(String... params) {

        try {

            URL url = new URL(params[0]);

            InputSource is = new InputSource();
            is.setEncoding("UTF-8");
            is.setByteStream(url.openStream());

            string = ArticleExtractor.INSTANCE.getText(is);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return string;
    }

    @Override
    public void onPostExecute(String article) {

        if (!mActivity.isDestroyed()) {

            int asd = 0;
        }
    }
}
