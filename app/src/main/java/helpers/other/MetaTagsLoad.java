package helpers.other;

import android.support.v7.widget.RecyclerView;
import models.news.MetaTag;

/**
 * Created by unexpected_err on 31/05/2017.
 */

public interface MetaTagsLoad {

    void onMetaTagsLoaded(RecyclerView.ViewHolder holder, int position, MetaTag tag);

}
