package adapters;

import android.content.Context;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.caverock.androidsvg.SVG;

import java.io.InputStream;
import java.util.ArrayList;

import SvgHelper.SvgDecoder;
import SvgHelper.SvgDrawableTranscoder;
import SvgHelper.SvgSoftwareLayerSetter;
import digitalbath.fansproject.R;
import models.team_data.LeagueTable;
import models.team_data.TeamStanding;
import viewholders.LeagueTableViewHolder;

/**
 * Created by Spaja on 06-Aug-17.
 */

public class LeagueTableAdapter extends RecyclerView.Adapter<LeagueTableViewHolder> {

    private Context mContext;
    private LeagueTable mLeagueTable;
    private GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder;

    public LeagueTableAdapter(Context context, LeagueTable leagueTable) {

        this.mContext = context;
        this.mLeagueTable = leagueTable;
        requestBuilder = Glide.with(context)
                .using(Glide.buildStreamModelLoader(Uri.class, context), InputStream.class)
                .from(Uri.class)
                .as(SVG.class)
                .transcode(new SvgDrawableTranscoder(), PictureDrawable.class)
                .sourceEncoder(new StreamEncoder())
                .cacheDecoder(new FileToStreamDecoder<>(new SvgDecoder()))
                .decoder(new SvgDecoder())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .animate(android.R.anim.fade_in)
                .listener(new SvgSoftwareLayerSetter<Uri>());
    }

    @Override
    public LeagueTableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.league_table_item, parent, false);

        return new LeagueTableViewHolder(v);

    }

    @Override
    public void onBindViewHolder(LeagueTableViewHolder holder, int position) {

        ArrayList<TeamStanding> standing = mLeagueTable.getStanding();

        holder.teamName.setText(standing.get(position).getTeamName());
        holder.wins.setText(String.valueOf(standing.get(position).getWins()));
        holder.draws.setText(String.valueOf(standing.get(position).getDraws()));
        holder.loses.setText(String.valueOf(standing.get(position).getLoses()));
        holder.points.setText(String.valueOf(standing.get(position).getPoints()));

        if (standing.get(position).getCrestURI() != null) {

            Uri uri = Uri.parse(standing.get(position).getCrestURI());

            requestBuilder
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .load(uri)
                    .into(holder.teamLogoMini);
        }
    }

    @Override
    public int getItemCount() {
        return mLeagueTable.getStanding().size();
    }
}
