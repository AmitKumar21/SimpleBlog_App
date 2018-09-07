package com.example.amk52.mymusic;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Post extends AppCompatActivity {
  ImageButton uplaodimage;
  EditText title;
  EditText desc;
 StorageReference mStorage;
 DatabaseReference mrefrence;
    ProgressBar mbar;
    Button button;
    private  Uri uri=null;
  private static final int GALLERY_CODE=2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_item);
        mrefrence= FirebaseDatabase.getInstance().getReference().child("Blog");
        mStorage= FirebaseStorage.getInstance().getReference();
        uplaodimage=(ImageButton)findViewById(R.id.imageButton);
        title=(EditText)findViewById(R.id.post_Title);
        desc=(EditText)findViewById(R.id.post_Desc);
        button=(Button)findViewById(R.id.button);
        mbar=(ProgressBar)findViewById(R.id.progressBar2);
        uplaodimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery=new Intent(Intent.ACTION_GET_CONTENT);
               gallery.setType("image/*");
                startActivityForResult(gallery,GALLERY_CODE);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mbar.setVisibility(View.VISIBLE);
                startupload();
            }
        });
    }


    private  void startupload() {

      final String post_Title = title.getText().toString().trim();
       final String post_Desc = desc.getText().toString().trim();
        if (!TextUtils.isEmpty(post_Title) && !TextUtils.isEmpty(post_Desc) && uri != null) {
            final StorageReference fileupload = mStorage.child("BlogImage").child(uri.getLastPathSegment());
            UploadTask uploadTask = fileupload.putFile(uri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileupload.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        DatabaseReference mrefchild=mrefrence.push();
                        mrefchild.child("title").setValue(post_Title);
                        mrefchild.child("desc").setValue(post_Desc);
                        mrefchild.child("imageurl").setValue(uri.toString());
                        mbar.setVisibility(View.INVISIBLE);
                        Toast.makeText(Post.this, uri.toString(), Toast.LENGTH_SHORT).show();



                    } else {
                        // Handle failures
                    }
                }
            });
        }

    }
@Override
    public void onActivityResult( int requestCode,int resultcode,Intent data) {
        super.onActivityResult(requestCode,resultcode,data);
        if(requestCode==GALLERY_CODE&&resultcode==RESULT_OK)
        {
            uri=data.getData();
            uplaodimage.setImageURI(uri);
        }
    }
}
