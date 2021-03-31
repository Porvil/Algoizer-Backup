package com.iiitd.dsavisualizer.runapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.iiitd.dsavisualizer.R;
import com.iiitd.dsavisualizer.runapp.others.OnBoardingPopUp;
import com.iiitd.dsavisualizer.utility.UtilUI;

public abstract class BaseActivity extends AppCompatActivity {

    public Context context;
    public LayoutInflater layoutInflater;

    public DrawerLayout dl_main;
    public ViewStub vs_main;
    public ViewStub vs_menu_left;
    public ViewStub vs_menu_right;
    public View v_main;
    public View v_menu_left;
    public View v_menu_right;

    public int LAYOUT_MAIN;
    public int LAYOUT_LEFT;
    public int LAYOUT_RIGHT;
    public String ONBOARDING_KEY;
    public boolean isConfigured = false;

    /** Subclasses are obligated to call this before calling super.onCreate() */
    protected void configure(int LAYOUT_MAIN, int LAYOUT_LEFT, int LAYOUT_RIGHT, String ONBOARDING_KEY) {
        this.LAYOUT_MAIN =  LAYOUT_MAIN;
        this.LAYOUT_LEFT =  LAYOUT_LEFT;
        this.LAYOUT_RIGHT = LAYOUT_RIGHT;
        this.ONBOARDING_KEY = ONBOARDING_KEY;

        isConfigured = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(!isConfigured) {
            throw new IllegalStateException("configure() not called prior to onCreate()");
        }

        int theme = UtilUI.getCurrentAppTheme(getApplicationContext());
        setTheme(theme);

        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_base);

        context = this;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dl_main = findViewById(R.id.dl_main);
        vs_main = findViewById(R.id.vs_main);
        vs_menu_left = findViewById(R.id.vs_menu_left);
        vs_menu_right = findViewById(R.id.vs_menu_right);

        vs_main.setLayoutResource(LAYOUT_MAIN);
        vs_menu_left.setLayoutResource(LAYOUT_LEFT);
        vs_menu_right.setLayoutResource(LAYOUT_RIGHT);
    }

    // id = 0 => both, 1 => left, 2 => right
    public boolean isDrawerOpen(int id){
        if(id == 0){
            return (dl_main.isDrawerOpen(GravityCompat.START) || dl_main.isDrawerOpen(GravityCompat.END));
        }
        else if (id == 1) {
            return dl_main.isDrawerOpen(GravityCompat.START);
        }
        else if (id == 2) {
            return dl_main.isDrawerOpen(GravityCompat.END);
        }

        return false;
    }

    // id = 0 => both, 1 => left, 2 => right
    public void openDrawer(int id){
        if(id == 0){
            dl_main.openDrawer(GravityCompat.START);
            dl_main.openDrawer(GravityCompat.END);
        }
        else if (id == 1) {
            dl_main.openDrawer(GravityCompat.START);
        }
        else if (id == 2) {
            dl_main.openDrawer(GravityCompat.END);
        }
    }

    // id = 0 => both, 1 => left, 2 => right
    public void closeDrawer(int id){
        if(id == 0){
            dl_main.closeDrawer(GravityCompat.START);
            dl_main.closeDrawer(GravityCompat.END);
        }
        else if (id == 1) {
            dl_main.closeDrawer(GravityCompat.START);
        }
        else if (id == 2) {
            dl_main.closeDrawer(GravityCompat.END);
        }
    }

    // must be called only after v_main is laid out
    public OnBoardingPopUp showOnBoarding(){
        closeDrawer(0);
        OnBoardingPopUp onBoardingPopUp = OnBoardingPopUp.getInstance(context,
                v_main.getWidth(), v_main.getHeight(),
                v_main, ONBOARDING_KEY);
        onBoardingPopUp.show();
        return onBoardingPopUp;
    }

    @Override
    public void onBackPressed() {
        if (isDrawerOpen(0)){
            closeDrawer(0);
        }
        else{
            back();
        }
    }

    public void initOnBoarding() {
        v_main.post(new Runnable() {
            @Override
            public void run() {
                boolean tutorialState = UtilUI.getTutorialState(context, ONBOARDING_KEY);
                if(!tutorialState) {
                    showOnBoarding();
                }
            }
        });
    }

//    public void showBackDialog(){
//        View view = getLayoutInflater().inflate(R.layout.layout_back_confirmation, null);
//
//        Button btn_cancel = view.findViewById(R.id.btn_cancel);
//        Button btn_yes = view.findViewById(R.id.btn_yes);
//
//        final Dialog dialog = new Dialog(context);
//        dialog.setContentView(view);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.show();
//
//        btn_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                btn_menu.setEnabled(true);
//                dl_main.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
//                dialog.dismiss();
//            }
//        });
//
//        btn_yes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                finish();
//            }
//        });
//
//        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                System.out.println("Dismissed");
//                btn_menu.setEnabled(true);
//                btn_nav.setEnabled(true);
//                btn_info.setEnabled(true);
//                dl_main.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
//            }
//        });
//    }


    protected abstract void initPseudoCode();
    protected abstract void initViews();
    protected abstract void initNavigation();
    protected abstract void back();
    protected abstract void disableUI();
    protected abstract void enableUI();

}