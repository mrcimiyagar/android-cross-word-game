package kasper.android.cross_word.front.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import kasper.android.cross_word.R;
import kasper.android.cross_word.back.models.memory.WordInfo;

/**
 * Created by keyhan1376 on 11/30/2017.
 */

public class GameQuestsAdapter extends RecyclerView.Adapter<GameQuestsAdapter.GameQuestVH> {

    private List<WordInfo> wordInfos;
    private boolean[] wordsFound;

    public GameQuestsAdapter(List<WordInfo> wordInfos, boolean[] wordsFound) {
        this.wordInfos = wordInfos;
        this.wordsFound = wordsFound;
        this.notifyDataSetChanged();
    }

    @Override
    public GameQuestVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GameQuestVH(LayoutInflater.from(parent.getContext()).inflate(R.layout
                .adapter_game_quests, parent, false));
    }

    @Override
    public void onBindViewHolder(GameQuestVH holder, int position) {

        WordInfo wordInfo = this.wordInfos.get(position);

        if (wordInfo.getQuestion().length() > 0) {
            holder.questTV.setText(wordInfo.getQuestion());
        }
        else {
            holder.questTV.setText("بدون سوال");
        }

        if (wordsFound[position]) {
            holder.aEmptyTV.setText(wordInfo.getAnswer());
        } else {
            StringBuilder questEmptyStr = new StringBuilder();
            for (int counter = 0; counter < wordInfo.getAnswer().length(); counter++) {
                questEmptyStr.append("_ ");
            }
            if (questEmptyStr.length() > 0) {
                questEmptyStr.deleteCharAt(questEmptyStr.length() - 1);
            }
            holder.aEmptyTV.setText(questEmptyStr.toString());
        }
    }

    @Override
    public int getItemCount() {
        return this.wordInfos.size();
    }

    class GameQuestVH extends RecyclerView.ViewHolder {

        TextView questTV;
        TextView aEmptyTV;

        GameQuestVH(View itemView) {
            super(itemView);
            this.questTV = itemView.findViewById(R.id.adapter_game_quest_question_text_view);
            this.aEmptyTV = itemView.findViewById(R.id.adapter_game_quest_answer_empty_text_view);
        }
    }
}
