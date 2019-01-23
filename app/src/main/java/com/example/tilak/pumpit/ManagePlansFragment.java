package com.example.tilak.pumpit;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

public class ManagePlansFragment extends Fragment {
    ScrollView scrollView;
    TextView addplan;
    Dialog addplanDialog;
    Button addnewplan;
    GridView gridview;
    ImageView cancel;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SavedInstanceState){
        View mngplans = inflater.inflate(R.layout.fragment_manage_plans, container, false);
        scrollView = mngplans.findViewById(R.id.plansscroll);
        addplan = mngplans.findViewById(R.id.addplantitle);
        addplanDialog = new Dialog(mngplans.getContext(), android.R.style.Theme_Light_NoTitleBar);
        addplanDialog.setContentView(R.layout.addplan_popup);
        cancel = addplanDialog.findViewById(R.id.cancelplanpopup);
        addnewplan = addplanDialog.findViewById(R.id.addnewplan);
        gridview = addplanDialog.findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(getContext()));
        return mngplans;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        addplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addplanDialog.show();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addplanDialog.dismiss();
            }
        });
        addnewplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get data from dialog and then
                addplanDialog.dismiss();
            }
        });

    }
}
