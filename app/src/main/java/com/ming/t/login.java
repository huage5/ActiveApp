package com.ming.t;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

//login.java主要就是实现login界面的功能
public class login extends AppCompatActivity {
    private Button TopApp;
    public static Context mContext;
    EditText appPackage;
    String appPackageText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        appPackage=(EditText)super.findViewById(R.id.appPackage);//获取用户输入的包名

        TopApp=(Button)findViewById(R.id.TopApp);
        TopApp.setOnClickListener(new View.OnClickListener()//侦听登录点击事件
                                 {
                                     public void onClick(View v)
                                     {
                                         appPackageText =  appPackage.getText().toString();
                                         if(appPackageText == null || appPackageText.equals(""))
                                         {
                                             Toast.makeText(getApplicationContext(), "包名必须填写", Toast.LENGTH_SHORT).show();//提示包名必须填写
                                         }else{
                                             int x = 1;
                                             while( x < 3600 ) {
                                                 System.out.print("value of x : " + x );
                                                 x++;
                                                 try {
                                                     Thread.sleep(1000);//休眠1秒
                                                 } catch (InterruptedException e) {
                                                     e.printStackTrace();
                                                 }
                                                 openAppWithPackageName(appPackage.getText().toString());
                                                 System.out.print("\n");
                                             }
                                         }

                                     }
                                 }
        );
    }

    private void openAppWithPackageName(String packagename) {
        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            return;
        }

        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);
        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = getPackageManager()
                .queryIntentActivities(resolveIntent, 0);

        if (!resolveinfoList.iterator().hasNext()){
            return ;
        }
        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packagename = 参数packname
            String packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);//重点是加这个

            // 设置ComponentName参数1:packagename参数2:MainActivity路径
            ComponentName cn = new ComponentName(packageName, className);
            intent.setComponent(cn);
            //startActivity(intent);
            mContext = getBaseContext();
            mContext.startActivity(intent);
        }
    }
}