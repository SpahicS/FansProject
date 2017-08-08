package viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import digitalbath.fansproject.R;

/**
 * Created by Spaja on 06-Aug-17.
 */

public class LeagueTableViewHolder extends RecyclerView.ViewHolder {

    public TextView teamName, points, draws, loses, wins;
    public ImageView teamLogoMini;

    public LeagueTableViewHolder(View itemView) {
        super(itemView);

        teamName = (TextView) itemView.findViewById(R.id.team_name);
        points = (TextView) itemView.findViewById(R.id.points);
        draws = (TextView) itemView.findViewById(R.id.draws);
        wins = (TextView) itemView.findViewById(R.id.wins);
        loses = (TextView) itemView.findViewById(R.id.loses);
        teamLogoMini = (ImageView) itemView.findViewById(R.id.team_logo_mini);
    }
}
