package com.iiitd.dsavisualizer.runapp.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iiitd.dsavisualizer.R;
import com.iiitd.dsavisualizer.constants.AppSettings;
import com.iiitd.dsavisualizer.datastructures.graphs.GraphActivity;
import com.iiitd.dsavisualizer.runapp.others.ActivityItemData;
import com.iiitd.dsavisualizer.utility.UtilUI;

public class GraphsActivity extends AppCompatActivity {

    Context context;
    LinearLayout linearLayout;
    ImageButton btn_report;
    ImageButton btn_about;
    ImageButton btn_themes;
    ImageButton btn_back;

    ActivityItemData[] activityItemData = new ActivityItemData[]{
            new ActivityItemData(GraphActivity.class.getName(), "BFS", R.drawable.dsa_bfs),
            new ActivityItemData(GraphActivity.class.getName(), "DFS", R.drawable.dsa_dfs),
            new ActivityItemData(GraphActivity.class.getName(), "Dijkstra", R.drawable.dsa_dijkstra),
            new ActivityItemData(GraphActivity.class.getName(), "Bellman Ford", R.drawable.dsa_bellmanford),
            new ActivityItemData(GraphActivity.class.getName(), "Kruskal's", R.drawable.dsa_kruskal),
            new ActivityItemData(GraphActivity.class.getName(), "Prim's", R.drawable.dsa_prim)
    };

    int theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        theme = UtilUI.getCurrentAppTheme(getApplicationContext());
        setTheme(theme);

        super.onCreate(savedInstanceState);
        UtilUI.setWindowSettings(getWindow());
        setContentView(R.layout.activity_graphs);
        context = this;

        linearLayout = findViewById(R.id.ll_parent);
        btn_report = findViewById(R.id.btn_report);
        btn_about = findViewById(R.id.btn_about);
        btn_themes = findViewById(R.id.btn_themes);
        btn_back = findViewById(R.id.btn_back);

        initToolTipTexts();

        int width = (int) UtilUI.dpToPx(context, AppSettings.ACTIVITY_ITEM_WIDTH);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width,
                ViewGroup.LayoutParams.MATCH_PARENT);

        for (final ActivityItemData activityItemData : activityItemData) {
            View view = getLayoutInflater().inflate(R.layout.layout_activity_item, null);
            ImageView imageView = view.findViewById(R.id.iv_sorticon);
            TextView textView = view.findViewById(R.id.tv_sortname);

            imageView.setImageDrawable(ContextCompat.getDrawable(context, activityItemData.drawable));
            textView.setText(activityItemData.text);

            layoutParams.setMargins(15, 0, 15, 0);
            view.setLayoutParams(layoutParams);

            linearLayout.addView(view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Class<?> aClass = Class.forName(activityItemData.activityClassName);
                        UtilUI.startActivity(context, aClass);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });

        }

        btn_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UtilUI.startActivity(context, AboutActivity.class);
            }
        });

        btn_themes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ThemesActivity.class);
                startActivityForResult(intent, AppSettings.THEMES_REQ_CODE);
            }
        });

        btn_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // REPORT CODE HERE
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{AppSettings.REPORT_BUG_EMAIL});
                email.putExtra(Intent.EXTRA_SUBJECT, "Bug/Feedback");
                email.putExtra(Intent.EXTRA_TEXT, "");

                //need this to prompts email client only
                email.setType("message/rfc822");

                startActivity(Intent.createChooser(email, "Choose an Email client :"));
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAfterTransition();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AppSettings.THEMES_REQ_CODE){
            if(resultCode == RESULT_OK){
                recreate();
            }
        }
    }

    @Override
    protected void onResume() {
        int currentAppTheme = UtilUI.getCurrentAppTheme(getApplicationContext());
        if(theme != currentAppTheme){
            recreate();
        }
        super.onResume();
    }

    protected void initToolTipTexts() {
        TooltipCompat.setTooltipText(btn_report, "Report Bug/Feedback");
        TooltipCompat.setTooltipText(btn_about, "About Developers");
        TooltipCompat.setTooltipText(btn_themes, "Change Theme");
        TooltipCompat.setTooltipText(btn_back, "Go Back");
    }

}