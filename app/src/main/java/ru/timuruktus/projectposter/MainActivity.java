package ru.timuruktus.projectposter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private String urlToImage;
    private DatabaseReference mDatabase;
    private EditText title,text;
    private TextView tagView;
    private Button chooseImg, push, addTag, removeTag;
    private ArrayList<String> tags = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tags = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        title = (EditText) findViewById(R.id.title);
        text = (EditText) findViewById(R.id.text);
        chooseImg = (Button) findViewById(R.id.chooseImg);
        push = (Button) findViewById(R.id.push);
        addTag = (Button) findViewById(R.id.addTag);
        removeTag = (Button) findViewById(R.id.removeTag);
        tagView = (TextView) findViewById(R.id.tagView);

        chooseImg.setOnClickListener(this);
        push.setOnClickListener(this);
        addTag.setOnClickListener(this);
        removeTag.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.chooseImg) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();

            final View view = inflater.inflate(R.layout.dialog_url, null);
            builder.setView(view)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            EditText et = (EditText) view.findViewById(R.id.push_url_link);
                            urlToImage = et.getText().toString();
                        }
                    });
            builder.create();
            builder.show();

        }else if(id == R.id.push){
            Post post = new Post(text.getText().toString(), title.getText().toString(), urlToImage,
                    tags);
            String postID = mDatabase.child("Posts").push().getKey();
            mDatabase.child("Posts").child(postID).setValue(post);

        }else if(id == R.id.addTag){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();

            final View view = inflater.inflate(R.layout.dialog_tag, null);
            builder.setView(view)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            EditText et = (EditText) view.findViewById(R.id.tag);
                            if(et.getText().toString() == "") return;
                            tags.add(et.getText().toString());
                            String allTags = "";
                            for(int i = 0; i < tags.size(); i++){
                                allTags += tags.get(i) + ",";
                            }
                            tagView.setText(allTags);
                        }
                    });
            builder.create();
            builder.show();

        }else if(id == R.id.removeTag){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();

            final View view = inflater.inflate(R.layout.dialog_tag, null);
            builder.setView(view)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            if(tags.size() == 0 || tags == null){
                                Toast.makeText(MainActivity.this, "Нет тегов!", Toast.LENGTH_SHORT)
                                        .show();
                                return;
                            }
                            EditText et = (EditText) view.findViewById(R.id.tag);
                            int index = tags.indexOf(et.getText().toString());
                            if(index < 0) return;
                            tags.remove(index);
                            String allTags = "";
                            for(int i = 0; i < tags.size(); i++){
                                allTags += tags.get(i) + ",";
                            }
                            tagView.setText(allTags);
                        }
                    });
            builder.create();
            builder.show();
        }



    }
}
