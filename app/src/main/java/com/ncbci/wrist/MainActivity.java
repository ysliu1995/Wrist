package com.ncbci.wrist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ncbci.wrist.Fragment.Activity;
import com.ncbci.wrist.Fragment.Home;
import com.ncbci.wrist.Fragment.Sleep;

public class MainActivity extends AppCompatActivity {

    static final String TAG = "MainActivity";
    private BottomNavigationView mBv;
    private ViewPager mVp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                }
            }
        }
    }

    private void initView() {
        mBv = findViewById(R.id.bv);
        mVp = findViewById(R.id.vp);
        mBv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        mVp.setCurrentItem(0);
                        return true;
                    case R.id.navigation_activity:
                        mVp.setCurrentItem(1);
                        return true;
                    case R.id.navigation_sleep:
                        mVp.setCurrentItem(2);
                        return true;
                }
                return false;
            }
        });

        setupViewPager(mVp);
        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBv.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private void setupViewPager(ViewPager viewPager) {
        BottomAdapter adapter = new BottomAdapter(getSupportFragmentManager());
        adapter.addFragment(new Home());
        adapter.addFragment(new Activity());
        adapter.addFragment(new Sleep());
        viewPager.setAdapter(adapter);
    }
}
