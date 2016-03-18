package com.example.juggernaut.police;

import android.content.Context;
import android.widget.Toast;

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
