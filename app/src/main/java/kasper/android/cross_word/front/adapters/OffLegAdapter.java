package kasper.android.cross_word.front.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import kasper.android.cross_word.R;
import kasper.android.cross_word.back.models.memory.GameLevel;
import kasper.android.cross_word.front.activities.GameSceneActivity;

public class OffLegAdapter extends RecyclerView.Adapter<OffLegAdapter.OffLegHolder> {

    private Context context;
    private List<GameLevel> gameLevels;

    public OffLegAdapter(Context context, List<GameLevel> gameLevels) {
        this.context = context;
        this.gameLevels = gameLevels;
        this.notifyDataSetChanged();
    }

    @Override
    public OffLegHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OffLegHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_offleg, parent, false));
    }

    @Override
    public void onBindViewHolder(OffLegHolder holder, int position) {

        final GameLevel gameLevel = this.gameLevels.get(position);

        holder.titleBar.setText("مرحله ی " + (position + 1));
        holder.ratingBar.setRating(gameLevel.getDoneScore());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, GameSceneActivity.class).putExtra("game-level-id", gameLevel.getId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return gameLevels.size();
    }

    class OffLegHolder extends RecyclerView.ViewHolder {

        TextView titleBar;
        RatingBar ratingBar;

        OffLegHolder(View itemView) {
            super(itemView);
            this.titleBar = (TextView) itemView.findViewById(R.id.adapter_offleg_title_bar);
            this.ratingBar = (RatingBar) itemView.findViewById(R.id.adapter_offleg_rating_bar);
        }
    }
}