package recoveryportal.adityagupta.recoveryportal.Views;

import android.content.Context;
import androidx.cardview.widget.CardView;
import android.util.AttributeSet;

/**
 * Created by adityagupta on 16/11/17.
 */

public class SquareCardView extends CardView {
    public SquareCardView(Context context) {
        super(context);
    }

    public SquareCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredHeight();
        setMeasuredDimension(width, width);
    }
}
