package com.incabinteractive.justus;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private ListView contacts;
    private ArrayAdapter<String>arrayAdapter;
    private String name;
    private ArrayList<String>list_of_contacts=new ArrayList<>();
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp=MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor ed= sp.edit();
        setContentView(R.layout.activity_main);
        if(sp.getString("n","").isEmpty()){
            uname();
        }
            contacts=(ListView)findViewById(R.id.lv_contacts);
            arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list_of_contacts);
            contacts.setAdapter(arrayAdapter);

            root.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Set<String> set=new HashSet<String>();
                    Iterator i=dataSnapshot.getChildren().iterator();
                    while(i.hasNext()){
                        set.add(((DataSnapshot)i.next()).getKey());
                    }
                    list_of_contacts.clear();
                    list_of_contacts.addAll(set);

                    arrayAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            contacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    SharedPreferences sp=MainActivity.this.getPreferences(Context.MODE_PRIVATE);
                    Intent it = new Intent(MainActivity.this,chatroom.class);
                    it.putExtra("room_name",((TextView)view).getText().toString());
                    it.putExtra("user_name",sp.getString("n",""));
                    startActivity(it);
                    finish();
                }
            });
    }

    private void uname() {

        AlertDialog.Builder b=new AlertDialog.Builder(this);
        b.setTitle("Enter a Username: ");

        final EditText nm=new EditText(this);

        b.setView(nm);
        b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                name=nm.getText().toString();
                SharedPreferences sp=MainActivity.this.getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor ed= sp.edit();
                ed.putString("n",name);
                ed.commit();
            }
        });
        b.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        b.show();
    }
}
