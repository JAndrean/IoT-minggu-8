package com.example.iot_71160036;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    ToggleButton buttonLampu1;
    ToggleButton buttonLampu2;

    TextView ldr;
    TextView statusBtn;

    String valueLdr;
    String valueBtn;
    String valueLampu1;
    String valueLampu2;

    DatabaseReference dref;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonLampu1 = (ToggleButton) findViewById(R.id.toggleButton_lampu1);
        buttonLampu2 = (ToggleButton) findViewById(R.id.toggleButton_lampu2);

        statusBtn = (TextView) findViewById(R.id.txtView_valueBtn);
        ldr = (TextView) findViewById(R.id.txtView_valueLdr);


        dref = FirebaseDatabase.getInstance().getReference();
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                valueBtn = dataSnapshot.child("Node1/btn").getValue().toString();
                if(valueBtn.equals("0"))
                    statusBtn.setText("Not Pressed");
                else
                    statusBtn.setText("Pressed");

                valueLdr = dataSnapshot.child("Node1/ldr").getValue().toString();
                ldr.setText(valueLdr);

                valueLampu1 = dataSnapshot.child("Node1/lampu1").getValue().toString();
                if(valueLampu1.equals("0"))
                    buttonLampu1.setChecked(false);
                else
                    buttonLampu1.setChecked(true);

                valueLampu2 = dataSnapshot.child("Node1/lampu2").getValue().toString();
                if(valueLampu2.equals("0"))
                    buttonLampu2.setChecked(false);
                else
                    buttonLampu2.setChecked(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        buttonLampu1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    DatabaseReference lampu1Ref = FirebaseDatabase.getInstance().getReference("Node1/lampu1");
                    lampu1Ref.setValue(1);
                }
                else
                {
                    DatabaseReference lampu1Ref = FirebaseDatabase.getInstance().getReference("Node1/lampu1");
                    lampu1Ref.setValue(0);
                }
            }
        });

        buttonLampu2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    DatabaseReference lampu2Ref = FirebaseDatabase.getInstance().getReference("Node1/lampu2");
                    lampu2Ref.keepSynced(true);

                    lampu2Ref.setValue(1);
                }
                else
                {
                    DatabaseReference lampu2Ref = FirebaseDatabase.getInstance().getReference("Node1/lampu2");
                    lampu2Ref.keepSynced(true);

                    lampu2Ref.setValue(0);
                }
            }
        });
    }
}