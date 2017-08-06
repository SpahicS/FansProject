package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import digitalbath.fansproject.R;
import models.team_data.LeagueTable;
import viewholders.LeagueTableViewHolder;
import viewholders.NewMessageViewHolder;

/**
 * Created by Spaja on 06-Aug-17.
 */

public class LeagueTableAdapter extends RecyclerView.Adapter<LeagueTableViewHolder> {

    private Context mContext;
    private LeagueTable mLeagueTable;

    public LeagueTableAdapter(Context context, LeagueTable leagueTable) {

        this.mContext = context;
        this.mLeagueTable = leagueTable;
    }

    @Override
    public LeagueTableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.league_table_item, parent, false);

        return new LeagueTableViewHolder(v);

    }

    @Override
    public void onBindViewHolder(LeagueTableViewHolder holder, int position) {

        holder.teamName.setText(mLeagueTable.getStanding().get(position).getTeamName());
    }

    @Override
    public int getItemCount() {
        return mLeagueTable.getStanding().size();
    }
}
