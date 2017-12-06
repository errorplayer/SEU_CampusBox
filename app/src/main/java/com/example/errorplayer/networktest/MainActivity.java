package com.example.errorplayer.networktest;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public String TAG = "TETE";
    TextView SW_;
    TextView XW_;
    TextView WS_;
    TextView WEEK_;
    TextView WEEK_order_;
    EditText StudentID;
    HashMap<Integer,String> curriculum_table = new HashMap<>();
    String cache_ID = "";
    Button xuehao_yikatong;
    private RadioGroup Weekday_select;
    private RadioButton mon;
    private RadioButton tue;
    private RadioButton wed;
    private RadioButton thu;
    private RadioButton fri;
    public static  String id;
    FloatingActionButton actionButton ;
    SubActionButton.Builder  itemBuilder;

    SubActionButton button2;
    SubActionButton button3;
    SubActionButton button1;
    SubActionButton button4;
    SubActionButton button5;
    SubActionButton button6;
    SubActionButton button7;
    SubActionButton button8;
    public static  int c_xqj_int; //current selected weekday
    public static  String c_xqj_string;

//    private RadioButton pollution;
//    private RadioButton love;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            List<String> permissionList = new ArrayList<>();
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        }
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Button sendRequest = (Button) findViewById(R.id.send_request);
        //Button CLEAN_FILES = (Button) findViewById(R.id.clean_files);


        xuehao_yikatong =(Button)findViewById(R.id.xhykt);

        Weekday_select = (RadioGroup) findViewById(R.id.weekday_select);
        mon = (RadioButton) findViewById(R.id.mon);
        tue = (RadioButton) findViewById(R.id.tue);
        wed = (RadioButton) findViewById(R.id.wed);
        thu = (RadioButton) findViewById(R.id.thu);
        fri = (RadioButton) findViewById(R.id.fri);

        //actionButton = (FloatingActionButton)findViewById(R.id.fab);



        StudentID = (EditText)findViewById(R.id.student_id_code);
        SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
        cache_ID = pref.getString("id","");
        StudentID.setText(cache_ID);
        SW_ = (TextView) findViewById(R.id.AM_class);
        XW_ = (TextView) findViewById(R.id.PM_class);
        WS_ = (TextView) findViewById(R.id.NT_class);
        WEEK_ = (TextView) findViewById(R.id.WEEK_show);
        WEEK_order_ = (TextView) findViewById(R.id.WEEK_order_show);
        int[] a = MatchWeek_limit("[12-16周]5-24节");
        Log.d("AA", String.valueOf(a[1]+a[0]));
        sendRequest.setOnClickListener(this);
        //xuehao_yikatong.setOnClickListener(this);
        //CLEAN_FILES.setOnClickListener(this);


        xuehao_yikatong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, YKTActivity.class);
                Log.d(TAG, "Click->search 2");
                startActivity(intent);
            }
        });
        Weekday_select.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if ( mon.getId() ==checkedId) {
                    showResponse(curriculum_table.get(0), curriculum_table.get(5), curriculum_table.get(10),"周一");
                    c_xqj_int = 0;
                    c_xqj_string = "周一";
                } else if (checkedId == tue.getId()) {
                    showResponse(curriculum_table.get(1), curriculum_table.get(6), curriculum_table.get(11),"周二");
                    c_xqj_int = 1;
                    c_xqj_string = "周二";
                } else if (checkedId == wed.getId()) {
                    showResponse(curriculum_table.get(2), curriculum_table.get(7), curriculum_table.get(12),"周三");
                    c_xqj_int = 2;
                    c_xqj_string = "周三";
                } else if (checkedId == thu.getId()) {
                    showResponse(curriculum_table.get(3), curriculum_table.get(8), curriculum_table.get(13),"周四");
                    c_xqj_int = 3;
                    c_xqj_string = "周四";
                } else if (checkedId == fri.getId()) {
                    showResponse(curriculum_table.get(4), curriculum_table.get(9), curriculum_table.get(14),"周五");
                    c_xqj_int = 4;
                    c_xqj_string = "周五";
                }
            }
        });

        actionButton = findViewById(R.id.fab);
        itemBuilder = new SubActionButton.Builder(this);
       // repeat many times:

        com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton.LayoutParams lp =
                new com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton.LayoutParams(
                        110,110);

        ImageView itemIcon1 = new ImageView(this);
        itemIcon1.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.charge));
        button1 = itemBuilder.setContentView(itemIcon1).setLayoutParams(lp).build();

        ImageView itemIcon2 = new ImageView(this);
        itemIcon2.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.change_stud));
        button2 = itemBuilder.setContentView(itemIcon2).setLayoutParams(lp).build();
        button2.setVisibility(View.INVISIBLE);

        ImageView itemIcon3 = new ImageView(this);
        itemIcon3.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.delete));
        button3 = itemBuilder.setContentView(itemIcon3).setLayoutParams(lp).build();

        ImageView itemIcon4 = new ImageView(this);
        itemIcon4.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.library));
        button4 = itemBuilder.setContentView(itemIcon4).setLayoutParams(lp).build();


        ImageView itemIcon5 = new ImageView(this);
        itemIcon5.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.list));
        button5 = itemBuilder.setContentView(itemIcon5).setLayoutParams(lp).build();


        ImageView itemIcon6 = new ImageView(this);
        itemIcon6.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.task));
        button6 = itemBuilder.setContentView(itemIcon6).setLayoutParams(lp).build();

        ImageView itemIcon7 = new ImageView(this);
        itemIcon7.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.done));
        button7 = itemBuilder.setContentView(itemIcon7).setLayoutParams(lp).build();



        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)

                .addSubActionView(button3)


                .addSubActionView(button4)
                .addSubActionView(button2)
                //.addSubActionView(button5)
                .addSubActionView(button6)
                .addSubActionView(button1)
                //.addSubActionView(button7)
                .attachTo(actionButton)
                .build();
        actionButton.setCompatElevation(30);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, YKTActivity.class);
                Log.d(TAG, "Click->search 2");
                startActivity(intent);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String target =  changeTo_nextStudent_kb();
                Log.d(TAG, "stdid->"+target);
                StudentID.setText(target);
                Log.d(TAG, "button2-> 1 "+c_xqj_string);
                OnCCKB_Button_Pressed(c_xqj_string);
//                Log.d(TAG, "button2-> 2"+c_xqj_string);
                showResponse(curriculum_table.get(c_xqj_int), curriculum_table.get(c_xqj_int+5), curriculum_table.get(c_xqj_int+10),c_xqj_string);

            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OnCleanFile_Button_Pressed();
                button2.setVisibility(View.INVISIBLE);
                Snackbar.make(view,"信息已被抹去",Snackbar.LENGTH_LONG)
                        .setAction("撤销", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Snackbar.make(view,"对不起，世上没有后悔药！",Snackbar.LENGTH_LONG).show();
                            }
                        }).show();
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view,"更多功能敬请期待",Snackbar.LENGTH_SHORT).show();


            }
        });
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view,"更多功能敬请期待",Snackbar.LENGTH_SHORT).show();


            }
        });




    }

    @Override
    public void onClick(View v) {


        if (v.getId() == R.id.send_request) {
            Log.d(TAG, "Click->search ");

            OnCCKB_Button_Pressed("-1");
            StudentID.clearFocus();
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(StudentID.getWindowToken(),0);
        }
//        if (v.getId() == R.id.clean_files) {
//
//            OnCleanFile_Button_Pressed();
//        }
    }

    private void sendRequestWithHttpURLConnection() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                String id = StudentID.getText().toString();
                try {
                    URL url = new URL("http://xk.urp.seu.edu.cn/jw_service/service/stuCurriculum.action?queryAcademicYear=17-18-2&queryStudentId="+id);
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
                    if (context.contains("value=\"查询\""))
                    {
                        showResponse("不存在的  ^+^");
                        Log.d("AA", "发现不存在的");
                        return;
                    }
                    context = context.replaceAll("&nbsp;","");
                    ArrayList<String> shangwukecheng = new ArrayList<String>();
                    int start_index= 0;
                    String addstr= "<br>";
                    for (int index = 0 ;index != 11;index++)
                    {
                        if (index != 3)
                        {
                            start_index = context.indexOf("<td rowspan=\"5\" class=\"line_topleft\" align=\"center\"",start_index);
                            String item = context.substring(start_index,context.indexOf("</td>",start_index));
                            item = item.substring("<td rowspan=\"5\" class=\"line_topleft\" align=\"center\">".length(),item.length());// align=\"center\">",String.valueOf(index));
                            item = addstr +item;
                            item.replaceAll("</td>","<br>");
                            item=processRex(item);
                            if (item .equals("%") )
                                item = "%null%";
                            item = item + "==";
                            shangwukecheng.add(item);
                            start_index =context.indexOf("</td>",start_index);
                        }
                        if (index == 3)
                        {
                            start_index = context.indexOf("<td rowspan=\"5\"",start_index);
                            String item = context.substring(start_index,context.indexOf("</td>",start_index));
                            item = item.substring("<td rowspan=\"5\" class=\"line_topleft\"\" align=\"center\">".length(),item.length());// align=\"center\">",String.valueOf(index));
                            item = addstr +item;
                            item.replaceAll("</td>","<br>");
                            item=processRex(item);
                            if (item .equals("%"))
                                item = "%null%";
                            item = item + "==";
                            shangwukecheng.add(item);
                            start_index =context.indexOf("</td>",start_index);
                        }

                    }
                    for (int index = 0 ;index != 5;index++)
                    {
                        {
                            start_index = context.indexOf("<td class=\"line_topleft\" rowspan=\"2\" ",start_index);
                            String item = context.substring(start_index,context.indexOf("</td>",start_index));
                            item = item.substring("<td class=\"line_topleft\" rowspan=\"2\" align=\"center\">".length()+2,item.length());
                            item = addstr +item;
                            item.replaceAll("</td>","<br>");
                            item=processRex(item);
                            if (item .equals("%") )
                                item = "%null%";
                            item = item + "==";
                            shangwukecheng.add(item);
                            start_index =context.indexOf("</td>",start_index);
                        }


                    }

                    String filePath="";
                    boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
                    String content = "";
                    String fp = "";
                    if (hasSDCard) { // SD卡根目录的hello.text
                        filePath = Environment.getExternalStorageDirectory().toString() + File.separator + id+"_c.txt";
                    } else  // 系统下载缓存根目录的hello.text
                        filePath = Environment.getDownloadCacheDirectory().toString() + File.separator + id+"_c.txt";
                    try{
                        File file=new File(filePath);
                        if(file.isFile() && file.exists()){
                            content = readTxtFile(id);
                        }else
                        {
                            fp = saveFile(shangwukecheng,id);
                            content = readTxtFile(id);
//                            Toast.makeText(getApplicationContext(), "create table successfully!", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    //for (int index = 0 ;index != 10;index++)
                    //Toast.makeText(String.valueOf(shangwukecheng.size()),Toast.LENGTH_SHORT).show();



                    String[] group = {};
                    group = content.split("==");
                    ArrayList<String> GROUP_CURRI = new ArrayList<String>();
                    for (int i = 0 ; i != group.length;i++)
                        if (!TextUtils.isEmpty(group[i]))
                            GROUP_CURRI.add(group[i]);
//                    for(int index = 0;index != GROUP_CURRI.size();index+=1)
//                    Log.d("AA", GROUP_CURRI.get(index));
                    curriculum_table = getFinalCurriculumTable(GROUP_CURRI);
                    //for(int index = 0;index != curriculum_table.size();index+=1)

//                      Log.d("AA", curriculum_table.get(7));


//                    if (getWeek()<=5)
//                        showResponse(curriculum_table.get(getWeek()-1),curriculum_table.get(getWeek()+4));
//                    else
//                    {
//                       showResponse("没有课程","尽情玩耍");
//                    }
//                    Log.d("AA", getCurrentDay());
//                    Log.d("AA",String.valueOf(daysBetween("2017-09-24 00:00:00",getCurrentDay())));

                    //showResponse(String.valueOf(shangwukecheng.size()));
                    //readFileOnLine();
//                    parseXMLWithPull(content);
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


        }).start();
    }

    private HashMap<Integer,String> getFinalCurriculumTable(ArrayList<String> group_curri) {
        HashMap<Integer,String> result = new HashMap<>(10);
        int count = 0;
        for (int index = 0;index != group_curri.size();index++)
        {
            if (!group_curri.get(index).contains("||"))
            {
                result.put(count,group_curri.get(index));
                count++;
            }

        }
        return result;
    }
    private void showResponse(final String warning)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WEEK_.setText(warning);
                WEEK_order_.setText("");
                SW_.setText("");
                XW_.setText("");
                WS_.setText("");
            }
        });
    }
    private void showResponse_delete_info(final String warning)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WEEK_.setText("");
                WEEK_order_.setText("");
                SW_.setText(warning);
                XW_.setText("");
                WS_.setText("");
            }
        });
    }
    private void showResponse(final String s1,final String s2,final String s3,final String WEEK) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String w_o_ = "";
                String week = "";
                try{
                if (daysBetween("2017-09-24 00:00:00",getCurrentDay()) % 7 != 0 )
                    w_o_ = String.valueOf(daysBetween("2017-09-24 00:00:00",getCurrentDay())/7+1);
                else
                    w_o_ = String.valueOf(daysBetween("2017-09-24 00:00:00",getCurrentDay())/7);}
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                if (WEEK.equals(""))
                    week = NumToWeek(getWeek());
                else
                    week = WEEK;
                WEEK_.setText(week);
                c_xqj_string = week;
                Log.d(TAG, "show-> c_string  "+c_xqj_string);


                WEEK_order_.setText("第"+w_o_+"周");

                SW_.setText("上午没课啦啦啦");
                XW_.setText("下午没课啦啦啦");
                WS_.setText("晚上没课啦啦啦");
                int week_odd_even = 0;
                int dijizhou_ = 0;
                dijizhou_ = Integer.parseInt(w_o_);
                week_odd_even =  Integer.parseInt(w_o_)%2;

                String[] temp1 = s1.split("%");
                ArrayList<String> temp11  = new ArrayList<String>();
                for (int i = 0 ; i != temp1.length;i++)
                    if (!TextUtils.isEmpty(temp1[i])) {
                        if (temp1[i].contains("(双)") && week_odd_even == 1) {
                            temp11.remove(temp11.size() - 1);
                            temp11.remove(temp11.size() - 1);
//                            i++;
//                            temp11.add(temp1[i]);
                        }
                        else if(temp1[i].contains("(单)") && week_odd_even == 0) {
                            temp11.remove(temp11.size() - 1);
                            temp11.remove(temp11.size() - 1);
//                            i++;
//                            temp11.add(temp1[i]);
                        } else if((MatchWeek_limit(temp1[i])[0]<=dijizhou_) &&(MatchWeek_limit(temp1[i])[1]>=dijizhou_))
                        {
                            temp11.add(temp1[i]);
                        } else{
                            temp11.remove(temp11.size()-1);
                            if (i != temp1.length-1)
                            i = i+1;

                        }

                    }
                String S1="==================="+"\n";
                int row_line1 = 0;
                for (int i = 0 ;i!= temp11.size();i++)
                {
                    if (row_line1 ==3)
                    {
                        S1 = S1+"==================="+"\n";
                        row_line1=0;
                    }
                    S1 = S1+temp11.get(i)+"\n";
                    row_line1++;
                }
                if(temp11.size() ==1)
                {
                    if (temp11.get(0).equals("null"))
                        S1 = "==================="+"\n";
                }
                S1 = S1+"\n";
                SW_.setText(S1);


                String[] temp2 = s2.split("%");
                ArrayList<String> temp22  = new ArrayList<String>();
                for (int i = 0 ; i != temp2.length;i++)
                    if (!TextUtils.isEmpty(temp2[i])) {
                        if (temp2[i].contains("(双)") && week_odd_even == 1) {
                            temp22.remove(temp22.size() - 1);
                            temp22.remove(temp22.size() - 1);
//                            i++;
//                            temp22.add(temp2[i]);
                        }
                        else if(temp2[i].contains("(单)") && week_odd_even == 0) {
                            temp22.remove(temp22.size() - 1);
                            temp22.remove(temp22.size() - 1);
//                            i++;
//                            temp22.add(temp2[i]);
                        }else if((MatchWeek_limit(temp2[i])[0]<=dijizhou_) &&(MatchWeek_limit(temp2[i])[1]>=dijizhou_))
                        {
                            temp22.add(temp2[i]);
                        } else{
                            temp22.remove(temp22.size()-1);
                            if (i != temp2.length-1)
                            i = i+1;

                        }

                    }
                String S2="==================="+"\n";
                int row_line2 = 0;
                for (int i = 0 ;i!= temp22.size();i++)
                {
                    if (row_line2 ==3)
                    {
                        S2 = S2+"==================="+"\n";
                        row_line2=0;
                    }
                    S2 = S2+temp22.get(i)+"\n";
                    row_line2++;
                }
                if(temp22.size() ==1)
                {
                    if (temp22.get(0).equals("null"))
                        S2 = "==================="+"\n";
                }
                S2 = S2+"\n";
                XW_.setText(S2);

                String[] temp3 = s3.split("%");
                ArrayList<String> temp33  = new ArrayList<String>();
                for (int i = 0 ; i != temp3.length;i++)
                    if (!TextUtils.isEmpty(temp3[i])) {
                        if (temp3[i].contains("(双)") && week_odd_even == 1) {
                            temp33.remove(temp33.size() - 1);
                            temp33.remove(temp33.size() - 1);
                            i++;
//                            temp33.add(temp3[i]);
                        }
                        else if(temp3[i].contains("(单)") && week_odd_even == 0) {
                            temp33.remove(temp33.size() - 1);
                            temp33.remove(temp33.size() - 1);
//                            i++;
//                            temp33.add(temp3[i]);
                        }else if((MatchWeek_limit(temp3[i])[0]<=dijizhou_) &&(MatchWeek_limit(temp3[i])[1]>=dijizhou_))
                        {
                            temp33.add(temp3[i]);
                        } else{
                            temp33.remove(temp33.size()-1);
                            if (i != temp3.length-1)
                            i = i+1;

                        }

                    }
                String S3="==================="+"\n";
                int row_line3 = 0;
                for (int i = 0 ;i!= temp33.size();i++)
                {
                    if (row_line3 ==3)
                    {
                        S3 = S3+"==================="+"\n";
                        row_line3=0;
                    }
                    S3 = S3+temp33.get(i)+"\n";
                    row_line3++;
                }
                if(temp33.size() ==1)
                {
                    if (temp33.get(0).equals("null"))
                        S3 = "==================="+"\n";
                }
                S3 = S3+"\n";
                WS_.setText(S3);

            }
        });
        Weekday_select.setVisibility(View.VISIBLE);
        button2.setVisibility(View.VISIBLE);
        Log.d(TAG, "show-> c_string 2 "+c_xqj_string);


    }

    private void PreshowResponse(final String s1,final String s2,final String s3,final String WEEK) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String w_o_ = "";
                String week = "";
                try{
                    if (daysBetween("2017-09-24 00:00:00",getCurrentDay()) % 7 != 0 )
                        w_o_ = String.valueOf(daysBetween("2017-09-24 00:00:00",getCurrentDay())/7+2);
                    else
                        w_o_ = String.valueOf(daysBetween("2017-09-24 00:00:00",getCurrentDay())/7+1);}
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                if (WEEK.equals(""))
                    week = NumToWeek(getWeek());
                else week = WEEK;
                WEEK_order_.setText("第"+w_o_+"周");
                WEEK_.setText(week);
                SW_.setText("上午没课啦啦啦");
                XW_.setText("下午没课啦啦啦");
                WS_.setText("晚上没课啦啦啦");
                int week_odd_even = 0;
                int dijizhou_ = 0;
                dijizhou_ = Integer.parseInt(w_o_);
                week_odd_even =  Integer.parseInt(w_o_)%2;

                String[] temp1 = s1.split("%");
                ArrayList<String> temp11  = new ArrayList<String>();
                for (int i = 0 ; i != temp1.length;i++)
                    if (!TextUtils.isEmpty(temp1[i])) {
                        if (temp1[i].contains("(双)") && week_odd_even == 1) {
                            temp11.remove(temp11.size() - 1);
                            temp11.remove(temp11.size() - 1);
//                            i++;
//                            temp11.add(temp1[i]);
                        }
                        else if(temp1[i].contains("(单)") && week_odd_even == 0) {
                            temp11.remove(temp11.size() - 1);
                            temp11.remove(temp11.size() - 1);
//                            i++;
//                            temp11.add(temp1[i]);
                        } else if((MatchWeek_limit(temp1[i])[0]<=dijizhou_) &&(MatchWeek_limit(temp1[i])[1]>=dijizhou_))
                        {
                            temp11.add(temp1[i]);
                        } else{
                            temp11.remove(temp11.size()-1);
                            if (i != temp1.length-1)
                                i = i+1;

                        }

                    }
                String S1="==================="+"\n";
                int row_line1 = 0;
                for (int i = 0 ;i!= temp11.size();i++)
                {
                    if (row_line1 ==3)
                    {
                        S1 = S1+"==================="+"\n";
                        row_line1=0;
                    }
                    S1 = S1+temp11.get(i)+"\n";
                    row_line1++;
                }
                if(temp11.size() ==1)
                {
                    if (temp11.get(0).equals("null"))
                        S1 = "==================="+"\n";
                }
                S1 = S1+"\n";
                SW_.setText(S1);


                String[] temp2 = s2.split("%");
                ArrayList<String> temp22  = new ArrayList<String>();
                for (int i = 0 ; i != temp2.length;i++)
                    if (!TextUtils.isEmpty(temp2[i])) {
                        if (temp2[i].contains("(双)") && week_odd_even == 1) {
                            temp22.remove(temp22.size() - 1);
                            temp22.remove(temp22.size() - 1);
//                            i++;
//                            temp22.add(temp2[i]);
                        }
                        else if(temp2[i].contains("(单)") && week_odd_even == 0) {
                            temp22.remove(temp22.size() - 1);
                            temp22.remove(temp22.size() - 1);
//                            i++;
//                            temp22.add(temp2[i]);
                        }else if((MatchWeek_limit(temp2[i])[0]<=dijizhou_) &&(MatchWeek_limit(temp2[i])[1]>=dijizhou_))
                        {
                            temp22.add(temp2[i]);
                        } else{
                            temp22.remove(temp22.size()-1);
                            if (i != temp2.length-1)
                                i = i+1;

                        }

                    }
                String S2="==================="+"\n";
                int row_line2 = 0;
                for (int i = 0 ;i!= temp22.size();i++)
                {
                    if (row_line2 ==3)
                    {
                        S2 = S2+"==================="+"\n";
                        row_line2=0;
                    }
                    S2 = S2+temp22.get(i)+"\n";
                    row_line2++;
                }
                if(temp22.size() ==1)
                {
                    if (temp22.get(0).equals("null"))
                        S2 = "==================="+"\n";
                }
                S2 = S2+"\n";
                XW_.setText(S2);

                String[] temp3 = s3.split("%");
                ArrayList<String> temp33  = new ArrayList<String>();
                for (int i = 0 ; i != temp3.length;i++)
                    if (!TextUtils.isEmpty(temp3[i])) {
                        if (temp3[i].contains("(双)") && week_odd_even == 1) {
                            temp33.remove(temp33.size() - 1);
                            temp33.remove(temp33.size() - 1);
                            i++;
//                            temp33.add(temp3[i]);
                        }
                        else if(temp3[i].contains("(单)") && week_odd_even == 0) {
                            temp33.remove(temp33.size() - 1);
                            temp33.remove(temp33.size() - 1);
//                            i++;
//                            temp33.add(temp3[i]);
                        }else if((MatchWeek_limit(temp3[i])[0]<=dijizhou_) &&(MatchWeek_limit(temp3[i])[1]>=dijizhou_))
                        {
                            temp33.add(temp3[i]);
                        } else{
                            temp33.remove(temp33.size()-1);
                            if (i != temp3.length-1)
                                i = i+1;

                        }

                    }
                String S3="==================="+"\n";
                int row_line3 = 0;
                for (int i = 0 ;i!= temp33.size();i++)
                {
                    if (row_line3 ==3)
                    {
                        S3 = S3+"==================="+"\n";
                        row_line3=0;
                    }
                    S3 = S3+temp33.get(i)+"\n";
                    row_line3++;
                }
                if(temp33.size() ==1)
                {
                    if (temp33.get(0).equals("null"))
                        S3 = "==================="+"\n";
                }
                S3 = S3+"\n";
                WS_.setText(S3);

            }
        });
        Weekday_select.setVisibility(View.INVISIBLE);
        button2.setVisibility(View.VISIBLE);
    }

    public String saveFile(List<String> str,final String file_userID) {
        String filePath = null;
        boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (hasSDCard) { // SD卡根目录的hello.text
            filePath = Environment.getExternalStorageDirectory().toString() + File.separator + file_userID+"_c.txt";
        } else  // 系统下载缓存根目录的hello.text
            filePath = Environment.getDownloadCacheDirectory().toString() + File.separator + file_userID+"_c.txt";

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
    public  String  readTxtFile(final String file_userID){
        String filePath = "";

        boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        StringBuilder content_result = new StringBuilder();
        if (hasSDCard) { // SD卡根目录的hello.text
            filePath = Environment.getExternalStorageDirectory().toString() + File.separator + file_userID+"_c.txt";
        } else  // 系统下载缓存根目录的hello.text
            filePath = Environment.getDownloadCacheDirectory().toString() + File.separator + file_userID+"_c.txt";

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

    public String NumToWeek(int s )
    {
        switch (s)
        {
            case 1:return "周一";
            case 2:return "周二";
            case 3:return "周三";
            case 4:return "周四";
            case 5:return "周五";
            case 6:return "礼拜六";
            case 7:return "星期天";
            default:return "星期九";
        }


    }
    public String getCurrentDay()
    {
        SimpleDateFormat    sDateFormat    =   new    SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String    date    =    sDateFormat.format(new    java.util.Date());
        return date;
    }
    public String processRex (String  str)
    {
        String result = str.replaceAll("<br>","%");
        result = result.replaceAll("下午","||%");

        return result;
    }


    public int  getWeek() {
        Calendar cal = Calendar.getInstance();
        int i = cal.get(Calendar.DAY_OF_WEEK);
        switch (i) {
            case 1:
                return 7;
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
                return 3;
            case 5:
                return 4;
            case 6:
                return 5;
            case 7:
                return 6;

        }
        return 1;
    }

    public  int daysBetween(String smdate,String bdate) throws ParseException {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    public int[] MatchWeek_limit (String s)
    {
        int[] result = {1,16};
        Pattern datePatt = Pattern.compile("\\[(.*)\\-(.*)周\\](.*)");

        Matcher m = datePatt.matcher(s);
        if (m.matches()) {
            result[0]   = Integer.parseInt(m.group(1)); // get values inside the first (..)
            result[1] = Integer.parseInt(m.group(2)); // get values inside the second (..)

        }

        return result;

    }

    public boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }

    public  ArrayList<String> search_store()
    {
        ArrayList<String> install_stud_list = new ArrayList<String>();
        String filePath = "";
        try {

            boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

            if (hasSDCard) { // SD卡根目录的hello.text
                filePath = Environment.getExternalStorageDirectory().toString() + File.separator ;
            } else  // 系统下载缓存根目录的hello.text
                filePath = Environment.getDownloadCacheDirectory().toString() + File.separator ;
//            try {
//                File file = new File(filePath);
//                boolean t_f = true;
//                if (file.isFile() && file.exists()) {
//                     t_f = deleteFile(filePath);
//                    return t_f;
//                }}
//            catch(Exception e)
//            {
//                e.printStackTrace();
//            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        install_stud_list = refreshFileList(filePath);
        return  install_stud_list;


    }

    public  ArrayList<String> refreshFileList(String strPath) {
        ArrayList<String> install_stud_list = new ArrayList<String>();
        File dir = new File(strPath);
        File[] files = dir.listFiles();


        if (files == null)
            return null;
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                //refreshFileList(files[i].getAbsolutePath());
            } else {
                if(files[i].getName().toLowerCase().endsWith("_c.txt"))
                    install_stud_list.add(files[i].getName());
            }
        }
        return install_stud_list;
    }

    public String changeTo_nextStudent_kb()
    {
        String target_stud_id = "";
        HashMap<Integer,String> index_stdID = new HashMap<>();
        HashMap<String,Integer> stdID_index = new HashMap<>();
        ArrayList<String> install_stud_list = new ArrayList<String>();
        install_stud_list = search_store();
        int id_int = 0;
        if (!install_stud_list.isEmpty())
        {

            Pattern studentIDPatt = Pattern.compile("(.*)\\_c.txt");
            for (String stdID:install_stud_list) {
                Matcher m = studentIDPatt.matcher(stdID);
                if (m.matches()) {

                    index_stdID.put(id_int,m.group(1));
                    stdID_index.put(m.group(1),id_int);
                    id_int++;
                 }
            }
            Log.d(TAG, "install_stud_list.size");
        }
        int current_index = stdID_index.get(id);
        int next_index = -1;
        if (current_index == index_stdID.size()-1)
        {
            next_index = 0;

        }
        else
        {
            next_index = current_index+1;
        }
        target_stud_id = index_stdID.get(next_index);
        return target_stud_id;



    }

    public void OnCCKB_Button_Pressed(String specified_weekday)
    {
        curriculum_table.clear();
        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
        editor.putString("id", StudentID.getText().toString());
        editor.apply();
        id = StudentID.getText().toString();
        showResponse("请确认已经连接校园网络和输入的正确性");
        try {
            String filePath = "";
            boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
            String content = "";
            String fp = "";
            if (hasSDCard) { // SD卡根目录的hello.text
                filePath = Environment.getExternalStorageDirectory().toString() + File.separator + id + "_c.txt";
            } else  // 系统下载缓存根目录的hello.text
                filePath = Environment.getDownloadCacheDirectory().toString() + File.separator + id + "_c.txt";
            try {
                File file = new File(filePath);
                if (file.isFile() && file.exists()) {
                    content = readTxtFile(id);
                    String[] group = {};
                    group = content.split("==");
                    ArrayList<String> GROUP_CURRI = new ArrayList<String>();
                    for (int i = 0; i != group.length; i++)
                        if (!TextUtils.isEmpty(group[i]))
                            GROUP_CURRI.add(group[i]);
//                    for(int index = 0;index != GROUP_CURRI.size();index+=1)
//                    Log.d("AA", GROUP_CURRI.get(index));
                    curriculum_table = getFinalCurriculumTable(GROUP_CURRI);
                    if (getWeek() <= 5 && !curriculum_table.isEmpty()) {
                        if (specified_weekday.equals("-1"))
                        showResponse(curriculum_table.get(getWeek() - 1), curriculum_table.get(getWeek() + 4), curriculum_table.get(getWeek() + 9), "");
                        else
                         showResponse(curriculum_table.get(getWeek() - 1), curriculum_table.get(getWeek() + 4), curriculum_table.get(getWeek() + 9), specified_weekday);


                    }
                    else if(getWeek() > 5 && !curriculum_table.isEmpty()){
                        PreshowResponse(curriculum_table.get(0), curriculum_table.get(5), curriculum_table.get(10),"星期一");
                    }
                } else {
                    showResponse("请猛戳按键！");

                    sendRequestWithHttpURLConnection();

                    SystemClock.sleep(3000);
                    if (getWeek() <= 5 && !curriculum_table.isEmpty())
                        showResponse(curriculum_table.get(getWeek() - 1), curriculum_table.get(getWeek() + 4), curriculum_table.get(getWeek() + 9),"");
                    else if(getWeek() > 5 && !curriculum_table.isEmpty()){
                        PreshowResponse(curriculum_table.get(0), curriculum_table.get(5), curriculum_table.get(10),"星期一");
                    }
                    else if(curriculum_table.isEmpty()){
                        SW_.setText("");
                        XW_.setText("学校网络故障");
                        WS_.setText("");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //for (int index = 0 ;index != 10;index++)
            //Toast.makeText(String.valueOf(shangwukecheng.size()),Toast.LENGTH_SHORT).show();


//                if (content.contains("404")) {
//                    showResponse("请确认已经连接校园网络");
//                    return;
//                }

            //for(int index = 0;index != curriculum_table.size();index+=1)

//                      Log.d("AA", curriculum_table.get(7));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void OnCleanFile_Button_Pressed()
    {
        String filePath = "";
        boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

        if (hasSDCard) { // SD卡根目录的hello.text
            filePath = Environment.getExternalStorageDirectory().toString();
        } else  // 系统下载缓存根目录的hello.text
            filePath = Environment.getDownloadCacheDirectory().toString();
        Log.d("AA", filePath);
        try {
            File target_dir = new File(filePath);
            ArrayList<String> need_delete_filenames = new ArrayList<>();
            ArrayList<Boolean> delete_status = new ArrayList<>();
            boolean delete_flag = true;

            if (target_dir.isDirectory()) {
                File[] dir_files = target_dir.listFiles();
                for (int i = 0; i != dir_files.length; i++) {
                    if (dir_files[i].toString().endsWith("_c.txt")) {
                        need_delete_filenames.add(dir_files[i].toString());
                    }
                    if (dir_files[i].toString().endsWith("_cardnofile.txt")) {
                        need_delete_filenames.add(dir_files[i].toString());
                    }
                }
                for (int i = 0; i != need_delete_filenames.size(); i++) {
                    delete_flag = deleteFile(need_delete_filenames.get(i));
                    delete_status.add(delete_flag);
                }
            }
            String Clean_info = "";
            for (int i = 0; i != need_delete_filenames.size(); i++) {
                if (delete_status.get(i))
                {
                    Clean_info = Clean_info+need_delete_filenames.get(i)+"删除成功！"+"\n";
                }
                else
                {
                    Clean_info = Clean_info+need_delete_filenames.get(i)+"删除失败！"+"\n";
                }
                Log.d("AA", need_delete_filenames.get(i));

            }
            if(!need_delete_filenames.isEmpty())
                showResponse_delete_info(Clean_info);
            else
                showResponse_delete_info("都干净了，你还点人家 ->-");
        } catch (Exception e) {
            e.printStackTrace();
            showResponse_delete_info("未知错误");
        }
    }


}
