package com.incabinteractive.justus;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class chatroom extends AppCompatActivity {

    private Button send;
    ScrollView sv;
    private EditText msg;
    private LinearLayout ll_chat;
    private Button back;
    private Firebase mref;
    private TextView chats,rn;
    private String uname,rname,temp_key,chat_msg,chat_uname;
    private DatabaseReference root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        mref= new Firebase("https://vip-8c41d.firebaseio.com/");
        send=(Button)findViewById(R.id.send);
        msg=(EditText)findViewById(R.id.msg);
        ll_chat=(LinearLayout) findViewById(R.id.ll_chat);
        sv=(ScrollView) findViewById(R.id.sv);
        back=(Button) findViewById(R.id.back);

        uname=getIntent().getExtras().get("user_name").toString();
        rname=getIntent().getExtras().get("room_name").toString();

        setTitle(rname);

        root= FirebaseDatabase.getInstance().getReference().child(rname);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> map=new HashMap<String,Object>();
                temp_key=root.push().getKey();
                root.updateChildren(map);

                DatabaseReference msg_root=root.child(temp_key);
                Map<String,Object>map2=new HashMap<String,Object>();
                map2.put("name",uname);
                if(msg.getText().toString().isEmpty()){

                }
                else{
                    map2.put("msg",msg.getText().toString());

                    msg_root.updateChildren(map2);
                    msg.setText("");
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(chatroom.this,MainActivity.class));
                finish();
            }
        });
        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void append_chat_conversation(DataSnapshot dataSnapshot) {

        Iterator i=dataSnapshot.getChildren().iterator();
        while(i.hasNext()){
            TextView chats=new TextView(this);
            LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp2.weight = 1.0f;
            chat_msg=(((DataSnapshot)i.next()).getValue().toString());
            chat_uname=(((DataSnapshot)i.next()).getValue().toString());

            if(uname.equals(chat_uname)){
                ll_chat.setGravity(Gravity.RIGHT);
                chats.setGravity(Gravity.RIGHT);
                lp2.gravity = Gravity.RIGHT;
                lp2.bottomMargin=10;
                lp2.rightMargin=20;
              //  chats.setBackgroundResource(R.drawable.rightchat);
                chats.setRight(10);
                chats.setLayoutParams(lp2);
                chats.setText(chat_msg);
                chats.setTextSize(20f);
                ll_chat.addView(chats);
            }
            else{
                chats.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                ll_chat.setGravity(Gravity.LEFT);
                chats.setGravity(Gravity.LEFT);
                lp2.gravity = Gravity.LEFT;
                lp2.bottomMargin=10;
                lp2.leftMargin=20;
                chats.setTextSize(20f);
                chats.setLeft(10);
                chats.setLayoutParams(lp2);
                chats.setText(chat_msg);
                ll_chat.addView(chats);
            }
            sv.post(new Runnable() {
                @Override
                public void run() {
                    sv.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });

        }
    }

}
