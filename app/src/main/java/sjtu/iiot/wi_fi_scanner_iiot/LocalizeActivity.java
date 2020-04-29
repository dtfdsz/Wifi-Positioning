package sjtu.iiot.wi_fi_scanner_iiot;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;

public class LocalizeActivity extends AppCompatActivity {

    //final RelativeLayout relativeLayout_add_imageview = findViewById(R.id.rl_touch);
    RelativeLayout relative;
    StringBuilder sb;
    private String FileLabelName="xystest1";
    private String testlist=null;
    float x=0,y=0;
    private SuperWiFi2 rss_scan =null;
    private Vector<Integer> RSSList = null;
    private Vector<Integer> CmpList=null;
    private int NumberOfWiFi=9;
    private String str;
    //final EditText outText = (EditText)findViewById(R.id.outText);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localize);

        final Button returnbtn1=findViewById(R.id.buttonrt1);
        returnbtn1.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent=new Intent(LocalizeActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        verifyStoragePermissions(this);

        final Button locbtn=findViewById(R.id.buttonloc);
        final EditText outText = (EditText)findViewById(R.id.outText);
        this.CmpList = new Vector<Integer>();
        locbtn.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View v)
            {

                float min=Integer.MAX_VALUE;
                float tmpx=0,tmpy=0;
                float res=0;
                rss_scan=new SuperWiFi2(LocalizeActivity.this);
                rss_scan.scanForLoc();
                while(rss_scan.isscan())
                {//Wait for the end
                }
                RSSList=rss_scan.getRSSlist();//Get the test result



                int j=0;
                String strx="";
                String stry="";
                String strsg="";
                float x1,y1;
                int k,tmp;
                str=loadFromSDFile(FileLabelName+".txt");
                //final EditText outText = (EditText)findViewById(R.id.outText);

             while(j<str.length())
                {

                    strx="";
                    stry="";

                    while(str.charAt(j)!='\r') {
                        ++j;
                    }
                    j+=2;
                    while(str.charAt(j)!='\r')
                    {
                        //strx+=str.charAt(j);
                        strx=strx+str.charAt(j);
                        ++j;
                    }
                    j+=2;
                    while(str.charAt(j)!='\r')
                    {
                        stry+=str.charAt(j);
                        ++j;
                    }
                    j+=2;
                    x1 = Float.valueOf(strx);
                    y1 = Float.valueOf(stry);

                    //testlist=testlist+x1+" "+y1+" ";
                    CmpList.clear();

                    for(k=0;k<NumberOfWiFi;++k)
                    {
                        strsg="";
                        while(str.charAt(j)!='\r')
                        {
                            strsg+=str.charAt(j);
                            ++j;
                        }
                        j+=2;
                        tmp=Integer.valueOf(strsg).intValue();
                        CmpList.add(tmp);
                        //testlist=testlist+tmp+" ";
                    }
                    res=0;

                    for (int i = 0; i < NumberOfWiFi; ++i)
                    {
                        res += (RSSList.get(i) - CmpList.get(i)) * (RSSList.get(i) - CmpList.get(i));
                   }
                    if (res <= min) {
                       min = res;
                       tmpx = x1;
                        tmpy = y1;
                    }

                }
                testlist="Your position:("+tmpx+","+tmpy+")"+","+"Signal strength:"+RSSList.get(0)+","+RSSList.get(1)
                +RSSList.get(2)+","+RSSList.get(3)+","+RSSList.get(4)+","+RSSList.get(5)+","+RSSList.get(6)+","+RSSList.get(7)+","
                        +RSSList.get(8);
                //CmpList.clear();
                //testlist="Signal Strength:"+RSSList.get(0)+","+RSSList.get(1);
                outText.setText(testlist);
                /*
               while(str.charAt(j)!='\r')
                   ++j;
               j+=2;
               while(str.charAt(j)!='\r') {
                   strx = strx + str.charAt(j);
                   ++j;
               }
               j+=2;
                while(str.charAt(j)!='\r') {
                    stry = stry + str.charAt(j);
                    ++j;
                }
                x1 = Float.valueOf(strx);
                y1=Float.valueOf(stry);
                testlist=""+x1+" "+y1;
                j+=2;
                while(str.charAt(j)!='\r')
                {
                    strsg+=str.charAt(j);
                    ++j;
                }
                tmp=Integer.valueOf(strsg).intValue();
                testlist+=" "+tmp;
                j+=2;
                while(str.charAt(j)!='\r')
                {
                    strsg+=str.charAt(j);
                    ++j;
                }
                tmp=Integer.valueOf(strsg).intValue();
                testlist+=" "+tmp;

                final EditText outText = (EditText)findViewById(R.id.outText);
                outText.setText(testlist);
                */

                /*final EditText outText = (EditText)findViewById(R.id.outText);
               testlist=""+str.length()+str.charAt(0)+str.charAt(1)+str.charAt(2)+str.charAt(3)+str.charAt(4)+str.charAt(5);
               outText.setText(testlist);
               */


                /*testlist=str;
                final EditText outText = (EditText)findViewById(R.id.outText);
                outText.setText(testlist);
                */
                //testlist="test";
                //outText.setText(testlist);

                x=tmpx;
                y=tmpy;
                relative = (RelativeLayout) findViewById(R.id.rl_touch);
                relative.addView(new IconView(LocalizeActivity.this,x,y));
            }
        });
        //relative = (RelativeLayout) findViewById(R.id.rl_touch);
        //relative.addView(new IconView(this,x,y));
    }


    private String loadFromSDFile(String fname) {
        fname="/"+fname;
        String result=null;
        try {
            File f=new File(Environment.getExternalStorageDirectory().getPath()+fname);
            int length=(int)f.length();
            byte[] buff=new byte[length];
            FileInputStream fin=new FileInputStream(f);
            fin.read(buff);
            fin.close();
            result=new String(buff,"UTF-8");
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(LocalizeActivity.this,"没有找到指定文件",Toast.LENGTH_SHORT).show();
        }
        return result;
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
