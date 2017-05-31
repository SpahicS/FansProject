package listeners;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Spaja on 31-May-17.
 */

public class OnCountrySelectorClickListener implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private boolean isRecyclerVisible;

    public OnCountrySelectorClickListener(RecyclerView mRecyclerView) {
        this.mRecyclerView = mRecyclerView;
    }

    @Override
    public void onClick(View v) {

        if (!isRecyclerVisible) {
            mRecyclerView.setVisibility(View.VISIBLE);
            isRecyclerVisible = true;
        } else {
            mRecyclerView.setVisibility(View.GONE);
            isRecyclerVisible = false;
        }
    }
}
