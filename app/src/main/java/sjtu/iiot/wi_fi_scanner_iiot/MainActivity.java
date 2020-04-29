package sjtu.iiot.wi_fi_scanner_iiot;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import java.util.Vector;
import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
    private SuperWiFi rss_scan =null;
    Vector<String> RSSList = null;
    private String testlist=null;
    public static int testID = 0;//The ID of the test result
    //private CollectData collectdata=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //textlayout= (EditText)findViewById(R.id.ipText);
        //collectdata = (CollectData) findViewById(R.id.collect_data);
       //final ImageView imageView = (ImageView) findViewById(R.id.imageView);

        final EditText ipText = (EditText)findViewById(R.id.ipText);//The textlist of the average of the result
        final Button changactivity = (Button)findViewById(R.id.button1);//The start button
        final Button cleanlist = (Button)findViewById(R.id.button2);//Clear the textlist
        final Button collectbtn=findViewById(R.id.button3);
        final Button localizebtn=findViewById(R.id.button4);

        //imageView.setImageResource(R.drawable.pic0);
        //imageView.setVisibility(View.GONE);
        verifyStoragePermissions(this);
        rss_scan=new SuperWiFi(this);
        testlist="";
        testID=0;
        localizebtn.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View v)
            {
                //linearLayout.setVisibility(View.GONE);
                //selectActivity.setVisibility(View.VISIBLE);
                //imageView.setVisibility(View.VISIBLE);
                //imageView.setImageResource(R.drawable.pic2);
                //collectdata.setVisibility(View.VISIBLE);
                Intent intent=new Intent(MainActivity.this,LocalizeActivity.class);
                startActivity(intent);
            }
        });

        collectbtn.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View v)
            {
                //linearLayout.setVisibility(View.GONE);
                //selectActivity.setVisibility(View.VISIBLE);
                //imageView.setVisibility(View.VISIBLE);
                //imageView.setImageResource(R.drawable.pic2);
                //collectdata.setVisibility(View.VISIBLE);
                Intent intent=new Intent(MainActivity.this,CollectActivity.class);
                startActivity(intent);
            }
        });
        changactivity.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                testID = testID + 1;
                rss_scan.ScanRss();
                while(rss_scan.isscan()){//Wait for the end
                }
                RSSList=rss_scan.getRSSlist();//Get the test result
                final EditText ipText = (EditText)findViewById(R.id.ipText);
                testlist=testlist+"testID:"+testID+"\n"+RSSList.toString()+"\n";
                ipText.setText(testlist);//Display the result in the textlist
            }
        });
        cleanlist.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                testlist="";
                ipText.setText(testlist);//Clear the textlist
                testID=0;
            }
        });
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