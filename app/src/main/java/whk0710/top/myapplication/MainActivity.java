package whk0710.top.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import whk0710.top.myapplication.constants.Constants;
import whk0710.top.myapplication.fee.PhoneInfo;
import whk0710.top.myapplication.fee.SMSReceiver;
import whk0710.top.myapplication.utils.Utils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String ACTION_SMS_RECEIVER = "android.provider.Telephony.SMS_RECEIVED";
    private CollapsingToolbarLayoutState state;
    private PhoneInfo phoneInfo;
    public static final String LOGTAG = Utils.getLogTag(MainActivity.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //判断当前Activity是否获得了该权限
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                //没有授权,判断权限申请是否曾经被拒绝过
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_SMS)) {
                    Toast.makeText(MainActivity.this, "你曾经拒绝过此权限,需要重新获取", Toast.LENGTH_SHORT).show();
                    //进行权限请求
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_SMS},
                            0x11);
                } else {
                    //进行权限请求
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_SMS},
                            0x11);
                }
            }
        }
        SMSReceiver receiver = new SMSReceiver();
        IntentFilter receiverFilter = new IntentFilter(ACTION_SMS_RECEIVER);
        registerReceiver(receiver, receiverFilter);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initView();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.nav_fee: getFee();break;
            default:;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //初始化
    public void initView(){
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        //toolbar.setTitle("关于");
        //setSupportActionBar(toolbar);
        //扩张时的标题颜色
        //collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        //设置CollapsingToolbarLayout收缩时的标题颜色
        //collapsingToolbarLayout.setCollapsedTitleTextColor(Color.BLACK);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(verticalOffset == 0){
                    if(state != CollapsingToolbarLayoutState.EXPANDED){
                        state = CollapsingToolbarLayoutState.EXPANDED;
                        collapsingToolbarLayout.setTitle("看一看");
                        change();
                    }
                }else if(Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()){
                    if (state != CollapsingToolbarLayoutState.COLLAPSED) {
                        state = CollapsingToolbarLayoutState.COLLAPSED;
                        collapsingToolbarLayout.setTitle("好了");
                        change();
                    }
                }else{
                    if (state != CollapsingToolbarLayoutState.INTERNEDIATE) {
                        state = CollapsingToolbarLayoutState.INTERNEDIATE;
                        collapsingToolbarLayout.setTitle("我在动");
                        change();
                    }
                }
            }
        });
    }

    private void change(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            if(state == CollapsingToolbarLayoutState.COLLAPSED){
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
            }else{
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }
    }
    //CollapsingToolbarLayout的三种状态
    private enum CollapsingToolbarLayoutState {
        EXPANDED,//展开
        COLLAPSED,//折叠
        INTERNEDIATE//过程中
    }
    private void startActivity(Class activity){
        Intent intent = new Intent(MainActivity.this,activity);
        startActivity(intent);
    }
    //查话费
    private void getFee(){
        //版本号大于M要运行时获取权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //判断当前Activity是否获得了该权限
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                //没有授权,判断权限申请是否曾经被拒绝过
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_PHONE_STATE)) {
                    Toast.makeText(MainActivity.this, "你曾经拒绝过此权限,需要重新获取", Toast.LENGTH_SHORT).show();
                    //进行权限请求
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_PHONE_STATE},
                            0x11);
                } else {
                    //进行权限请求
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_PHONE_STATE},
                            0x11);
                }
            }else{
                phoneInfo = new PhoneInfo(this.getApplicationContext());
                Log.d(LOGTAG,"phone number is " + phoneInfo.getNativePhoneNumber());
                Log.d(LOGTAG,"service company is " + phoneInfo.getProvidersName());
                if(!phoneInfo.getProvidersName().equals(Constants.PROVIDER_NONE)){
                    switch(phoneInfo.getProvidersName()){
                        case Constants.PROVIDER_UNICOM:;break;
                        case Constants.PROVIDER_CHINAMOBILE:;break;
                        case Constants.PROVIDER_CHINATELECOM:;break;
                        default:;
                    }
                }
            }
        }else{

        }
    }
}
