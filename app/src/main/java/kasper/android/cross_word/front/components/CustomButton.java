package kasper.android.cross_word.front.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import kasper.android.cross_word.R;

/**
 * Created by keyhan1376 on 11/24/2017.
 */

public class CustomButton extends CardView {

    public CustomButton(Context context) {
        super(context);
        this.init(context, null);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context, attrs);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context, attrs);
    }

    @SuppressLint("InflateParams")
    private void init(Context context, AttributeSet attrs) {
        this.setCardElevation(getResources().getDisplayMetrics().density * 4);
        this.setRadius(getResources().getDisplayMetrics().density * 24);

        View contentView = LayoutInflater.from(context).inflate(R.layout.custom_button, null, false);
        contentView.setLayoutParams(new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.MATCH_PARENT));

        this.addView(contentView);

        TextView textTV = this.findViewById(R.id.custom_button_text_text_view);
        ImageView drawableIV = this.findViewById(R.id.custom_button_drawable_image_view);

        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomButton, 0, 0);
            try {
                textTV.setText(ta.getString(R.styleable.CustomButton_text));
                drawableIV.setImageDrawable(ta.getDrawable(R.styleable.CustomButton_drawable));
                contentView.setBackgroundDrawable(ta.getDrawable(R.styleable.CustomButton_backimage));
                this.setCardBackgroundColor(ta.getColor(R.styleable.CustomButton_backcolor, 0));
            } finally {
                ta.recycle();
            }
        }
    }
}
