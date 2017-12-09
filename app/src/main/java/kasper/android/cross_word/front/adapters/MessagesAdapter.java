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
import kasper.android.cross_word.back.models.memory.Message;
import kasper.android.cross_word.back.utils.PersianCalendar;
import kasper.android.cross_word.front.activities.MainActivity;
import kasper.android.cross_word.front.activities.MessagesActivity;
import kasper.android.cross_word.front.activities.PresentActivity;

/**
 * Created by keyhan1376 on 11/18/2017.
 */

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageVH> {

    private AppCompatActivity activity;
    private List<Message> messages;

    public MessagesAdapter(AppCompatActivity activity, List<Message> messages) {
        this.activity = activity;
        this.messages = messages;
        this.notifyDataSetChanged();
    }

    @Override
    public MessageVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MessageVH(LayoutInflater.from(parent.getContext()).inflate
                (R.layout.adapter_messages, parent, false));
    }

    @Override
    public void onBindViewHolder(final MessageVH holder, int position) {

        final Message msg = this.messages.get(position);
        holder.contentTV.setText(msg.getContent());
        PersianCalendar pc = new PersianCalendar(msg.getTime());
        String[] dateTime = pc.getPersianShortDateTime().split(" ");
        holder.timeTV.setText(dateTime[0] + "\n" + dateTime[1]);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MessagesActivity) activity).notifyOpeningWindow();
                activity.startActivity(new Intent(activity, PresentActivity.class)
                        .putExtra("present-title", "پیام")
                        .putExtra("present-content", holder.timeTV.getText()
                                + "\n\n" + msg.getContent()));
                activity.overridePendingTransition(R.anim.anim_alpha_in, R.anim.nothing);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.messages.size();
    }

    class MessageVH extends RecyclerView.ViewHolder {

        TextView contentTV;
        TextView timeTV;

        MessageVH(View itemView) {
            super(itemView);
            this.contentTV = itemView.findViewById(R.id.adapter_messages_content_text_view);
            this.timeTV = itemView.findViewById(R.id.adapter_messages_time_text_view);
        }
    }
}
