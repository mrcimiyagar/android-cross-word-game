package kasper.android.cross_word.front.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import kasper.android.cross_word.R;

public class FilledTableAdapter extends RecyclerView.Adapter<FilledTableAdapter.TableHolder> {

    private String[] contentArr;
    private View[] itemsViews;
    private float[] scaleArr;
    private boolean[] used;
    private int tableSize;

    public FilledTableAdapter(String[] contentArr) {
        this.contentArr = contentArr;
        this.tableSize = (int)(Math.sqrt(contentArr.length));
        this.itemsViews = new View[contentArr.length];
        this.scaleArr = new float[contentArr.length];
        this.used = new boolean[contentArr.length];

        for (int counter = 0; counter < contentArr.length; counter++) {
            this.scaleArr[counter] = 1f;
        }

        for (int counter = 0; counter < contentArr.length; counter++) {
            this.used[counter] = false;
        }

        this.notifyDataSetChanged();
    }

    @Override
    public TableHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_filled_table, parent, false);
        int margin = (int)(2 * parent.getContext().getResources().getDisplayMetrics().density);
        itemView.getLayoutParams().width = (int)((parent.getLayoutParams().width - 8 * parent.getContext().getResources()
                .getDisplayMetrics().density) / tableSize - margin * 2);
        itemView.getLayoutParams().height = (int)((parent.getLayoutParams().height - 8 * parent.getContext().getResources()
                .getDisplayMetrics().density) / tableSize - margin * 2);
        ((RecyclerView.LayoutParams)itemView.getLayoutParams()).setMargins(margin, margin, margin, margin);
        return new TableHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TableHolder holder, final int position) {

        itemsViews[position] = holder.itemView;

        holder.contentTV.setText(this.contentArr[position]);

        if (position == 0) {
            holder.contentTV.setBackgroundResource(R.drawable.rectound_white_tl);
        }
        else if (position == tableSize - 1) {
            holder.contentTV.setBackgroundResource(R.drawable.rectound_white_tr);
        }
        else if (position == tableSize * (tableSize - 1)) {
            holder.contentTV.setBackgroundResource(R.drawable.rectound_white_bl);
        }
        else if (position == (tableSize * tableSize) - 1) {
            holder.contentTV.setBackgroundResource(R.drawable.rectound_white_br);
        }
        else {
            holder.contentTV.setBackgroundResource(R.drawable.rectangle_white);
        }
    }

    public void notifyItemSelected(int position) {
        if (position >= 0 && position < contentArr.length) {
            if (scaleArr[position] == 1) {
                scaleArr[position] = 0.75f;
                itemsViews[position].animate().scaleX(0.75f).scaleY(0.75f).setDuration(150).start();
            }
        }
    }

    public void notifySelectionReset() {
        for (int counter = 0; counter < itemsViews.length; counter++) {
            if (scaleArr[counter] < 1) {
                scaleArr[counter] = 1f;
                itemsViews[counter].animate().scaleX(1f).scaleY(1f).setDuration(150).start();
            }
        }
    }

    public void notifyWordFound(List<Integer> wordIndices) {
        Log.d("KasperLogger", "hello ! " + wordIndices.size());
        for (int charIndex : wordIndices) {
            this.used[charIndex] = true;
            itemsViews[charIndex].animate().scaleX(0.25f).scaleY(0.25f).alpha(0).setDuration(250).start();
        }
    }

    @Override
    public int getItemCount() {
        return this.contentArr.length;
    }

    class TableHolder extends RecyclerView.ViewHolder {

        TextView contentTV;

        TableHolder(View itemView) {
            super(itemView);
            contentTV = (TextView) itemView.findViewById(R.id.adapter_table_content_text_view);
        }
    }
}