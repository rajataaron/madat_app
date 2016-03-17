package com.example.juggernaut.decepticon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

/**
 * Created by juggernaut on 17/3/16.
 */
public class Dashboard extends Activity {

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        drawer();
    }

    private void drawer() {
        toolbar = (Toolbar) findViewById(R.id.tb);
        toolbar.setTitle("Apraadh");
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName("Emergency");
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withName("Lodge FIR");
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withName("Check Status");
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withName("Exit");
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.drawerbackground).build();



        Drawer result = new DrawerBuilder().withToolbar(toolbar).withAccountHeader(headerResult).withActivity(Dashboard.this).addDrawerItems(item1, item2, item3, item4).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int i, IDrawerItem iDrawerItem) {
                switch (i) {
                    case 1:
                        emergency();
                        break;
                    case 3:
                        checkStatus();
                        break;
                    case 4:
                        finish();
                        break;
                    case 2:
                        lodgeFir();
                        break;
                }
                return false;
            }
        }).build();

    }

    private void checkStatus() {

        Utilities.message(this,"check status");
    }

    private void emergency(){
        Intent intent = new Intent(this,Dashboard.class);
        startActivity(intent);
    }

    private void lodgeFir(){
        Intent intent = new Intent(this,LodgeFir.class);
        startActivity(intent);
    }

}
