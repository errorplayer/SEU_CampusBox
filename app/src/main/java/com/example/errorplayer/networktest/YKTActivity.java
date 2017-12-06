package com.example.errorplayer.networktest;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

import static android.view.KeyEvent.KEYCODE_BACK;


public class YKTActivity extends AppCompatActivity {
   public static String TAG = "YKT_message";
   public String Card_id_cache = "?";
    WebView webview ;
    public static int  value_group;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ykt);
        value_group = 0;
        webview =(WebView)findViewById(R.id.ykt_webview);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
        webview.setWebViewClient(new WebViewClient(){
            @Override
                  public boolean shouldOverrideUrlLoading(WebView view, String url) {
                      view.loadUrl(url);
                      return true;
                  }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webview.loadUrl("javascript:HTMLOUT.processHTML(document.documentElement.outerHTML);");
                //String name = sendRequest_getCardno("http://58.192.115.47:8088/wechat-web/service/profile.html");


            }
        });

       // webview.loadUrl("http://58.192.115.47:8088/wechat-web/login/initlogin.html");
        if (readTxtFile("cckb")!="404")
            Card_id_cache = readTxtFile("cckb");
        final EditText password = new EditText(this);
        final EditText recharge_value  = new EditText(this);

        final RadioButton rmb_10 = new RadioButton(this);
        final RadioButton rmb_30 = new RadioButton(this);
        final RadioButton rmb_8 = new RadioButton(this);
        final RadioButton rmb_20 = new RadioButton(this);

        final TextView text_money = new TextView(this);
        final TextView text_password = new TextView(this);

        final RadioGroup rg_value_select ;
        LinearLayout layout = new LinearLayout(this);
        LinearLayout layout_1 = new LinearLayout(this);
        LinearLayout layout_2 = new LinearLayout(this);
        rg_value_select = new RadioGroup(this);

        rmb_8.setText("8元" );
        rmb_8.setPadding(0, 0, 0, 0);
        rg_value_select.addView(rmb_8);


        rmb_10.setText("10元" );
        rmb_10.setPadding(0, 0, 0, 0);
        rg_value_select.addView(rmb_10);

        rmb_20.setText("20元" );
        rmb_20.setPadding(0, 0, 0, 0);
        rg_value_select.addView(rmb_20);
        rg_value_select.setOrientation(LinearLayout.HORIZONTAL);


        rmb_30.setText("30元" );
        rmb_30.setPadding(0, 0, 0, 0);
        rg_value_select.addView(rmb_30);
        rg_value_select.setPadding(60,0,0,0);
        layout.addView(rg_value_select);



        //recharge_value.setPadding(18,0,0,0);
        text_money.setText("金额");
        text_money.setTextSize(29);
        text_money.setPadding(18,0,8,0);
        text_password.setText("密码");
        text_password.setTextSize(29);
        text_password.setPadding(18,0,8,0);
        layout_2.addView(text_money);
        layout_2.addView(recharge_value);
        recharge_value.setWidth(460);
        layout_2.setOrientation(LinearLayout.HORIZONTAL);
        layout_2.setPadding(90,0,0,0);
        layout.addView(layout_2);

        layout_1.addView(text_password);
        password.setWidth(460);
        layout_1.addView(password);
        layout_1.setOrientation(LinearLayout.HORIZONTAL);
        layout_1.setPadding(90,0,0,0);
        layout.addView(layout_1);

        password.setTransformationMethod(PasswordTransformationMethod.getInstance());

        layout.setOrientation(LinearLayout.VERTICAL);
        //layout.se



        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("充值窗口").setIcon(R.drawable.charge).setView(layout)
                .setNegativeButton("不充了！", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        YKTActivity.this.finish();
                    }
                });

        builder.setPositiveButton("爽充！", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                String PW = "";
                String money = "";
                money = recharge_value.getText().toString();
                PW = password.getText().toString();
                if (!TextUtils.isEmpty(money)&&!TextUtils.isEmpty(PW))
                {
                    webview.loadUrl("http://58.192.115.47:8088/WechatEcardInterfaces/wechatweb/chongzhi.html?jsoncallback=jsonp1511410119540&value="+money+","+PW+"&cardno="+Card_id_cache+"&acctype=1");
                }
            }
        });
        builder.setCancelable(false);


        rg_value_select.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
               if (checkedId == rmb_10.getId())
                {
                    recharge_value.setText("10");
                }
                if (checkedId == rmb_30.getId())
                {
                    recharge_value.setText("30");
                }
                if (checkedId == rmb_8.getId())
                {
                    recharge_value.setText("8");
                }
                if (checkedId == rmb_20.getId())
                {
                    recharge_value.setText("20");
                }
            }
        });

        if (!Card_id_cache.equals("?"))
        {
            builder.show();
            Toast.makeText(YKTActivity.this,Card_id_cache,Toast.LENGTH_LONG).show();

        }
        else
        {
            webview.loadUrl("http://58.192.115.47:8088/wechat-web/login/initlogin.html");

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private String sendRequest_getCardno(final String address) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;

                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null)
                    {
                        response.append(line);
                    }

                    String context= response.toString() ;
//                    if (context.contains("error")||context.contains("404"))
//                    {
//                        showResponse("请确认已经连接校园网络");
//                        return;
//                    }
                    if (!context.isEmpty())
                        Log.d("YYY", context);
                    if (context.contains("姓名"))
                    {
                        String name = "";
                        int start_index = context.indexOf("姓名");
                        start_index = context.indexOf("dd>",start_index);
                        int end_index =  context.indexOf("</dd>",start_index);
                        name = context.substring(start_index,end_index);


                        Log.d("YYY", "cardno->"+name);
//                        return name;
                    }
//                    context = context.replaceAll("&nbsp;","");
//                    ArrayList<String> shangwukecheng = new ArrayList<String>();
//                    int start_index= 0;
//                    String addstr= "<br>";
//
//                    for (int index = 0 ;index != 5;index++)
//                    {
//                        {
//                            start_index = context.indexOf("<td class=\"line_topleft\" rowspan=\"2\" ",start_index);
//                            String item = context.substring(start_index,context.indexOf("</td>",start_index));
//                            item = item.substring("<td class=\"line_topleft\" rowspan=\"2\" align=\"center\">".length()+2,item.length());
//                            item = addstr +item;
//                            item.replaceAll("</td>","<br>");
//                            item=processRex(item);
//                            if (item .equals("%") )
//                                item = "%null%";
//                            item = item + "==";
//                            shangwukecheng.add(item);
//                            start_index =context.indexOf("</td>",start_index);
//                        }
//
//
//                    }
//
//                    String filePath="";
//                    boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
//                    String content = "";
//                    String fp = "";
//                    if (hasSDCard) { // SD卡根目录的hello.text
//                        filePath = Environment.getExternalStorageDirectory().toString() + File.separator + id+"_c.txt";
//                    } else  // 系统下载缓存根目录的hello.text
//                        filePath = Environment.getDownloadCacheDirectory().toString() + File.separator + id+"_c.txt";
//                    try{
//                        File file=new File(filePath);
//                        if(file.isFile() && file.exists()){
//                            content = readTxtFile(id);
//                        }else
//                        {
//                            fp = saveFile(shangwukecheng,id);
//                            content = readTxtFile(id);
////                            Toast.makeText(getApplicationContext(), "create table successfully!", Toast.LENGTH_SHORT).show();
//                        }
//                    }catch (Exception e)
//                    {
//                        e.printStackTrace();
//                    }
//
//
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
//

        }).start();
        return "aa";
    }


    class MyJavaScriptInterface {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processHTML(String context) {


            if (context.contains("sum="))
            {
                String cardno = "";
                int start_index = context.indexOf("sum=");
                cardno = context.substring(start_index+4,start_index+10);


                ArrayList<String> l_cardno = new ArrayList<>();
                l_cardno.add(cardno);
                saveFile(l_cardno,"cckb");
               // Log.d("YYY", "cardno->"+name);
                return;
            }
        }
    }

    public String saveFile(List<String> str, final String cardno_file) {
        String filePath = null;
        boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (hasSDCard) { // SD卡根目录的hello.text
            filePath = Environment.getExternalStorageDirectory().toString() + File.separator + cardno_file+"_cardnofile.txt";
        } else  // 系统下载缓存根目录的hello.text
            filePath = Environment.getDownloadCacheDirectory().toString() + File.separator + cardno_file+"_cardnofile.txt";

        try {
            File file = new File(filePath);
            if (!file.exists()) {
                File dir = new File(file.getParent());
                dir.mkdirs();
                file.createNewFile();
            }
            FileOutputStream outStream = new FileOutputStream(file);
            for (int index =0;index!=str.size();index++)
                outStream.write(str.get(index).getBytes());
            Log.d("AA", "write finished!");

            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;
    }

    public  String  readTxtFile(final String cardno_file){
        String filePath = "";

        boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        StringBuilder content_result = new StringBuilder();
        if (hasSDCard) { // SD卡根目录的hello.text
            filePath = Environment.getExternalStorageDirectory().toString() + File.separator + cardno_file+"_cardnofile.txt";
        } else  // 系统下载缓存根目录的hello.text
            filePath = Environment.getDownloadCacheDirectory().toString() + File.separator + cardno_file+"_cardnofile.txt";

        try {
            String encoding="UTF-8";
            File file=new File(filePath);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                int count = 0;
                while((lineTxt = bufferedReader.readLine()) != null){
                    content_result.append(lineTxt);
                    //Log.d("AA", lineTxt+"\n");
                    count += 1;
                }
                Log.d("AA", "count:"+String.valueOf(count));
                read.close();
                return content_result.toString();
            }else{
                // return "404";
            }
        } catch (Exception e) {
            //System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        return "404";
    }
}





