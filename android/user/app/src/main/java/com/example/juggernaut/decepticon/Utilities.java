package com.example.juggernaut.decepticon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Toast;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

/**
 * Created by juggernaut on 15/3/16.
 */
public class Utilities {
    public static void message (Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

    public static String json_clean(String s){
        String keyword = s.replaceAll("\"", "");
        return keyword;
    }


}
