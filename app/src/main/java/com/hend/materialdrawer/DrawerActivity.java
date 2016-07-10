package com.hend.materialdrawer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DrawerActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);


        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.side_nav_bar)
                .addProfiles(
                        new ProfileDrawerItem().withName("Hend Mahmoud").withEmail("hend@grapesnberries.com").withIcon(getResources().getDrawable(R.drawable.ic_person))
                )
                .withOnAccountHeaderProfileImageListener(new AccountHeader.OnAccountHeaderProfileImageListener() {
                    @Override
                    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
                        Toast.makeText(DrawerActivity.this,"Open Profile",Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
                        return false;
                    }
                })
               .withOnAccountHeaderSelectionViewClickListener(new AccountHeader.OnAccountHeaderSelectionViewClickListener() {
                   @Override
                   public boolean onClick(View view, IProfile profile) {
                       Toast.makeText(DrawerActivity.this,"Open Profile From Header",Toast.LENGTH_SHORT).show();
                       //Return true closes switching accounts view
                       return true;
                   }
               })
                .build();



        //TODO: According to the returned user type build the correct drawer
        Drawer resultDrawer= buildChefDrawer(headerResult);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        resultDrawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);

    }

    Drawer buildChefDrawer(AccountHeader header){
        PrimaryDrawerItem itemMeals = new PrimaryDrawerItem().withIdentifier(1).withName("Meals").withIcon(R.drawable.ic_restaurant_menu);
        SecondaryDrawerItem itemSettings = (SecondaryDrawerItem) new SecondaryDrawerItem().withIdentifier(2).withName("Settings").withIcon(R.drawable.ic_setting);
        SecondaryDrawerItem itemLogout = (SecondaryDrawerItem) new SecondaryDrawerItem().withIdentifier(3).withName("Logout").withIcon(R.drawable.ic_logout);
        //create the drawer and remember the `Drawer` result object

        // Create the AccountHeader

        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(header)
                .addDrawerItems(
                        itemMeals,
                        new DividerDrawerItem(),
                        itemSettings,
                        itemLogout
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        Toast.makeText(DrawerActivity.this,"Click "+position,Toast.LENGTH_SHORT).show();

                        return true;
                    }
                })
                .build();
        return  result;

    }

}
