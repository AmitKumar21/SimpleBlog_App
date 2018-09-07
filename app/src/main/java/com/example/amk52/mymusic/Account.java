package com.example.amk52.mymusic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class Account extends AppCompatActivity{
    RecyclerView mBlogList;
    public DatabaseReference mRootref;
    FirebaseRecyclerAdapter adapter;
private FirebaseAuth mAuth;
private FirebaseAuth.AuthStateListener mAuthListner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        mAuth=FirebaseAuth.getInstance();
        mRootref = FirebaseDatabase.getInstance().getReference("Blog");
        mRootref.keepSynced(true);
        mBlogList=(RecyclerView)findViewById(R.id.blog_List);

        mBlogList.setHasFixedSize(true);

        Query query = mRootref; //= FirebaseDatabase.getInstance().getReferenceFromUrl("https://mymusic-21ac3.firebaseio.com/User");
        FirebaseRecyclerOptions<Blog> options =
                new FirebaseRecyclerOptions.Builder<Blog>()
                        .setQuery(query, Blog.class)
                        .build();
        adapter = new FirebaseRecyclerAdapter<Blog,BlogViewHolder>(options) {
            @Override
            public BlogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.blog_row, parent, false);

                return new BlogViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(BlogViewHolder holder, int position, Blog model) {
                holder.setTitle(model.getTitle()); // Bind the Chat object to the ChatHolder

                holder.setDesc(model.getDesc());
                holder.setImage(model.getImageurl());
            }
        };
        mBlogList.setAdapter(adapter);
        mBlogList.setLayoutManager(new LinearLayoutManager(this));
   mAuthListner=new FirebaseAuth.AuthStateListener() {
    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if(firebaseAuth.getCurrentUser()==null){
            Intent account=new Intent(Account.this,MainActivity.class);
            startActivity(account);
        }
    }
};

    }
    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        View mview;

        public BlogViewHolder(View itemView) {
            super(itemView);
            mview = itemView;
        }

        public void setTitle(String title) {
            TextView posttitle = (TextView) mview.findViewById(R.id.post_Title);
            posttitle.setText(title);

        }
        public  void setImage(final String image){
            final ImageView postImage=(ImageView)mview.findViewById(R.id.post_image);
            Picasso.get().load(image).networkPolicy(NetworkPolicy.OFFLINE).into(postImage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
              Picasso.get().load(image).into(postImage);
                }
            });
        }
        public void setDesc(String desc) {
            TextView posrDesc = (TextView) mview.findViewById(R.id.post_Desc);
            posrDesc.setText(desc);


        }


    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
        adapter.startListening();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.add){
            startActivity(new Intent(Account.this,Post.class));
        }
        if(item.getItemId()==R.id.action_LogIN){
            mAuth.signOut();
        }
        return super.onOptionsItemSelected(item);
    }
}
