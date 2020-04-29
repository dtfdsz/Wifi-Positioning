package sjtu.iiot.wi_fi_scanner_iiot;

import android.content.Context;
import android.widget.ImageView;

public class IconView extends android.support.v7.widget.AppCompatImageView {
    public IconView(Context context,float x,float y) {
        super(context);
        this.setImageResource(R.drawable.loc1);
        this.setX(x);
        this.setY(y);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(100, 100);

    }
}
