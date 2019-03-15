package com.example.tilak.pumpit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shuhart.stepview.StepView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewMemberActivity extends AppCompatActivity implements NewMembFrag1.NextBtnListener, NewMembFrag2.NextBtnListener, NewMembFrag3.NextBtnListener{

    StepView stepView;
    FrameLayout newmembcont;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String GymName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_member);
        stepView = findViewById(R.id.stepViewMemb);
        newmembcont = findViewById(R.id.newMemb_container);

        GymName = user.getDisplayName();

        Log.d("GymMetainfo_nma", GymName);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.newMemb_container, new NewMembFrag1()).commit();
    }

    @Override
    public void onNewMembBtnClicked1(Boolean result, String membName) {
        if(result){
            setupPlansWithCount();
            NewMembFrag2 newMembFrag2 = new NewMembFrag2();
            Bundle bundle =  new Bundle();
            bundle.putString("membName", membName);
            newMembFrag2.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.newMemb_container,
                    newMembFrag2).addToBackStack(null).commit();
        }
    }

    @Override
    public void onNewMembBtnClicked2(Boolean result) {
        if(result){
            getSupportFragmentManager().beginTransaction().replace(R.id.newMemb_container,
                    new NewMembFrag3()).addToBackStack(null).commit();
        }
    }

    public void setupPlansWithCount(){
        final ArrayList<String> planNameList = new ArrayList<>();
        final ArrayList<Integer> planCountList = new ArrayList<>();
        CollectionReference cr = FirebaseFirestore.getInstance().collection("/Gyms/"+GymName+"/Plans");
        cr.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    //Log.d("planArri", document.getId());
                    if(!planNameList.contains(document.getId())){
                        planNameList.add(document.getId());
                    }
                }
                CollectionReference membRef = FirebaseFirestore.getInstance().collection("/Gyms/"+GymName+"/Members");
                membRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            Log.d("planNamelst", String.valueOf(planNameList.size()));
                            for(int i = 0; i<planNameList.size(); i++){
                                planCountList.add(0);
                            }
                            Log.d("plancntsize", String.valueOf(planCountList.size()));
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                for(int i = 0; i<planNameList.size(); i++){
                                    if(planNameList.get(i).equals(document.getString("planName"))){
                                        Integer cnt = planCountList.get(i);
                                        cnt = cnt+1;
                                        planCountList.set(i, cnt);
                                    }
                                }
                            }
                            for(int j = 0; j<planCountList.size();j++){
                                Log.d("plancntval", String.valueOf(planCountList.get(j)));
                            }
                        }
                    }
                });
            }
        });
        for(int i =0; i<planNameList.size(); i++){
            DocumentReference writeRef = FirebaseFirestore.getInstance().document("Gyms/"+GymName+"/Plans/"+planNameList.get(i));
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("planMembCount", planCountList.get(i));
            writeRef.set(data);
        }
    }

    @Override
    public void onNewMembBtnClicked3() {
        Log.d("next3check", "entered next3");
        final DocumentReference dr = FirebaseFirestore.getInstance().document("/Gyms/"+GymName+"/MetaData/members");
        dr.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String cnt = documentSnapshot.getString("allmembcount");
                Integer cnti = Integer.valueOf(cnt);
                cnti = cnti + 1;
                dr.update("allmembcount", cnti.toString());
            }
        });
        Log.d("next3click", "nextfinish");
        finish();
    }
}
