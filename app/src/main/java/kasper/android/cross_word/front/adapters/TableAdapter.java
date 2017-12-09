package kasper.android.cross_word.front.adapters;

import android.graphics.Rect;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import kasper.android.cross_word.R;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.TableHolder> {

    private String[] contentArr;
    private View[] itemsViews;
    private float[] scaleArr;

    private int tableSize;

    public TableAdapter(String[] contentArr) {
        this.contentArr = contentArr;
        this.tableSize = (int)Math.sqrt(contentArr.length);
        this.itemsViews = new View[contentArr.length];
        this.scaleArr = new float[contentArr.length];

        for (int counter = 0; counter < contentArr.length; counter++) {
            this.scaleArr[counter] = 1f;
        }

        this.notifyDataSetChanged();
    }

    @Override
    public TableHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_table, parent, false);
        itemView.getLayoutParams().height = (int)(parent.getLayoutParams().height / this.tableSize
                - 4 * parent.getContext().getResources().getDisplayMetrics().density);
        return new TableHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TableHolder holder, final int position) {

        itemsViews[position] = holder.itemView;

        holder.contentTV.setText(this.contentArr[position]);

        if (position == 0) {
            holder.contentTV.setBackgroundResource(R.drawable.rectound_white_tl);
        }
        else if (position == 2) {
            holder.contentTV.setBackgroundResource(R.drawable.rectound_white_tr);
        }
        else if (position == 6) {
            holder.contentTV.setBackgroundResource(R.drawable.rectound_white_bl);
        }
        else if (position == 8) {
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

    private Rect rect;

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