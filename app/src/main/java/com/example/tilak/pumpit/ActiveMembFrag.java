package com.example.tilak.pumpit;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ActiveMembFrag extends Fragment implements MemberAdapter.ItemclickListener{
    RecyclerView activeRv;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    CollectionReference memberref;

    TelephonyManager telephonyManager;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String GymName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SavedInstanceState){
        View activeFragV = inflater.inflate(R.layout.fragment_activememb, container, false);
        activeRv = activeFragV.findViewById(R.id.activemembRecyclerview);
        GymName = user.getDisplayName();
        Log.d("GymNameActive", GymName);
        memberref = db.collection("Gyms/"+GymName+"/Members");
        return activeFragV;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setUpActiveRecyclerView();
    }

    private void setUpActiveRecyclerView() {
        Query query = memberref;
        FirestoreRecyclerOptions<Member> options = new FirestoreRecyclerOptions.Builder<Member>().setQuery(query, Member.class).build();
        MemberAdapter adapter = new MemberAdapter(options, getContext(), this);
        activeRv.setLayoutManager(new LinearLayoutManager(getContext()));
        activeRv.setAdapter(adapter);
        Log.d("ActiveRv", "finished rv");
    }

    @Override
    public void onItemClick(String name, String plan, String payment, String profileurl, String phone) {
        Intent i = new Intent(getActivity(), MemberDetails.class);
        i.putExtra("name", name);
        i.putExtra("plan", plan);
        i.putExtra("payment", payment);
        i.putExtra("profileurl", profileurl);
        i.putExtra("phone", phone);
        startActivity(i);
    }

    @Override
    public void callItem(String phone) {
        String phonenum = String.format("tel: %s", phone);
        telephonyManager = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
        // Set the data for the intent as the phone number.
        dialIntent.setData(Uri.parse(phonenum));
        startActivity(dialIntent);
        // If package resolves to an app, send intent.
    }
}
