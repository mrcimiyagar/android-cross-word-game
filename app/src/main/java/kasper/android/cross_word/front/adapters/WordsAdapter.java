package kasper.android.cross_word.front.adapters;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import kasper.android.cross_word.R;
import kasper.android.cross_word.back.models.memory.Word;
import kasper.android.cross_word.front.activities.MainActivity;
import kasper.android.cross_word.front.activities.WordDetailsActivity;
import kasper.android.cross_word.front.activities.WordsActivity;

/**
 * Created by keyhan1376 on 11/18/2017.
 */

public class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.WordVH> {

    private AppCompatActivity activity;
    private List<Word> words;

    public WordsAdapter(AppCompatActivity activity, List<Word> words) {
        this.activity = activity;
        this.words = words;
        this.notifyDataSetChanged();
    }

    @Override
    public WordVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WordVH(LayoutInflater.from(parent.getContext()).inflate
                (R.layout.adapter_words, parent, false));
    }

    @Override
    public void onBindViewHolder(final WordVH holder, int position) {
        final Word word = this.words.get(position);
        holder.wordTV.setText(word.getWord());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((WordsActivity) activity).notifyOpeningWindow();
                activity.startActivity(new Intent(activity, WordDetailsActivity.class)
                        .putExtra("word", word));
                activity.overridePendingTransition(R.anim.anim_alpha_in, R.anim.nothing);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.words.size();
    }

    class WordVH extends RecyclerView.ViewHolder {

        TextView wordTV;

        WordVH(View itemView) {
            super(itemView);
            this.wordTV = itemView.findViewById(R.id.adapter_words_content_text_view);
        }
    }
}
