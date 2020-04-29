package sjtu.iiot.wi_fi_scanner_iiot;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Vector;

public class CollectActivity extends Activity implements View.OnTouchListener {

    private RelativeLayout llTouch;
    //private TextView touchShow;
    Vector<String> RSSList = null;
    private String testlist=null;

    private SuperWiFi1 rss_scan =null;

    public static float posx=0;
    public static float posy=0;

    public static int recordID = 0;//The ID of the test result

    /*public Canvas canvas;
    public Paint p;
    private Bitmap bitmap;
    int color= Color.RED;
    float strokeWidth=10.0f;*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);

        llTouch = (RelativeLayout) findViewById(R.id.ll_touch);
        llTouch.setOnTouchListener(this);

        final EditText posText = (EditText)findViewById(R.id.posText);

        verifyStoragePermissions(this);
        rss_scan=new SuperWiFi1(this);
        testlist="";
        //ouchShow = (TextView) findViewById(R.id.touch_show);
        //final ImageView imageView = (ImageView) findViewById(R.id.imageView);
        //imageView.setVisibility(View.VISIBLE);
        //imageView.setImageResource(R.drawable.pic2);
        final Button returnbtn=findViewById(R.id.buttonrt);
        returnbtn.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent=new Intent(CollectActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        /*bitmap = BitmapFactory
                .decodeResource(getResources(), R.drawable.pic2).copy(Bitmap.Config.ARGB_8888,true);
        canvas=new Canvas();
        canvas.setBitmap(bitmap);
        p= new Paint(Paint.DITHER_FLAG);
        //p.setStyle(Style.STROKE);
        p.setAntiAlias(true);
        p.setColor(color);
        p.setStrokeWidth(strokeWidth);
        p.setStrokeJoin(Paint.Join.ROUND);*/
    }
    //@Override
    public boolean onTouch(View v,MotionEvent event) {
        switch (event.getAction()) {
            /**
             * 点击的开始位置
             */
            case MotionEvent.ACTION_DOWN:
               // tvTouchShowStart.setText("起始位置：(" + event.getX() + "," + event.getY());
                break;
            /**
             * 触屏实时位置
             */
            case MotionEvent.ACTION_MOVE:
                //tvTouchShow.setText("实时位置：(" + event.getX() + "," + event.getY());
                break;
            /**
             * 离开屏幕的位置
             */
            case MotionEvent.ACTION_UP:
                //touchShow.setText("Current Position：(" + event.getX() + "," + event.getY()+")");
                recordID = recordID + 1;
                //rss_scan.ScanRss();
                posx=event.getX();
                posy=event.getY();

                //canvas.drawPoint(posx, posy, p);                //画点
                //v.invalidate();

                rss_scan.scanAndRecordPos();
                while(rss_scan.isscan())
                {//Wait for the end
                }
                RSSList=rss_scan.getRSSlist();//Get the test result

                final EditText posText = (EditText)findViewById(R.id.posText);
                testlist="CurrentPos:("+ posx + "," + posy+"),\nWiFiSignal:"+RSSList.toString();
                posText.setText(testlist);//Display the result in the textlist

                break;
            default:
                break;
        }
        /**
         *  注意返回值
         *  true：view继续响应Touch操作；
         *  false：view不再响应Touch操作，故此处若为false，只能显示起始位置，不能显示实时位置和结束位置
         */
        return true;
    }

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_MULTICAST_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_FINE_LOCATION };
    /**
     * Checks if the app has permission to write to device storage
     * If the app does not has permission then the user will be prompted to
     * grant permissions
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
// Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
// We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }



}
