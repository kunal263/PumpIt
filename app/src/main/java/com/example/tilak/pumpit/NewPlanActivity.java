package com.example.tilak.pumpit;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.shuhart.stepview.StepView;

import java.util.HashMap;
import java.util.Map;

public class NewPlanActivity extends AppCompatActivity implements NewPlanFrag1.NextBtnListener, NewPlanFrag2.NextBtnListener, NewPlanFrag3.NextBtnListener {

    StepView stepView;
    FrameLayout newmembcont;
    NewPlanFrag1 newPlanFrag1 = new NewPlanFrag1();
    NewPlanFrag2 newPlanFrag2 = new NewPlanFrag2();
    NewPlanFrag3 newPlanFrag3 = new NewPlanFrag3();

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String GymName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_plan);
        stepView = findViewById(R.id.stepViewPlan);
        newmembcont = findViewById(R.id.newPlan_container);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.newPlan_container, newPlanFrag1).commit();
    }

    @Override
    public void onNewPlanBtnClicked1(Boolean result, String ptitle) {
        if(result)
        {
            stepView.go(1, true);
            newPlanFrag2.getPlanTitle(ptitle);
            getSupportFragmentManager().beginTransaction().replace(R.id.newPlan_container,
                    newPlanFrag2).commit();
        }
    }

    @Override
    public void onNewPlanBtnClicked2(Boolean result) {
        if(result){
            stepView.go(2, true);
            getSupportFragmentManager().beginTransaction().replace(R.id.newPlan_container,
                    newPlanFrag3).commit();

        }
    }

    @Override
    public void onNewPlanBtnClicked3(Boolean result) {
        stepView.done(true);
        finish();
    }
}
