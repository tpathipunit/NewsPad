package com.example.root.newspad.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.root.newspad.Constants;
import com.example.root.newspad.R;
import com.example.root.newspad.models.News;
import com.example.root.newspad.ui.NewsDetailActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by root on 9/26/17.
 */

public class FirebaseNewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final int MAX_WIDTH = 200;
    private static final int MAX_HEIGHT = 200;
    @Bind(R.id.imageView) ImageView mNewsImage;
    @Bind(R.id.newsTitle) TextView mNewsTitle;

    View mView;
    Context mContext;
    //constructor
    public FirebaseNewsViewHolder(View itemView){
        super(itemView);
        mView=itemView;
        mContext=itemView.getContext();
        itemView.setOnClickListener(this);
    }

    //bind the views
    public void bindNews(News news){
        TextView newsTitle=(TextView) mView.findViewById(R.id.newsTitle);
        TextView newsDescription=(TextView) mView.findViewById(R.id.newsDescription);
        TextView newsAuthor=(TextView) mView.findViewById(R.id.newsAuthor);
        ImageView newsImage=(ImageView) mView.findViewById(R.id.newsImage);

        Picasso.with(mContext)
                .load(news.getWebsite())
                .resize(MAX_WIDTH, MAX_HEIGHT)
                .centerCrop()
                .into(newsImage);

        newsTitle.setText(news.getDescription());
        newsDescription.setText(news.getAuthor());
    }
    @Override
    public void onClick(View view){
        //create arraylist of news
        final ArrayList<News> news=new ArrayList<>();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_CHILD_RESTAURANTS);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    news.add(snapshot.getValue(News.class));
                }
                int itemPosition=getLayoutPosition();
                Intent intent=new Intent(mContext, NewsDetailActivity.class);
                intent.putExtra("position", itemPosition + "");
                //wrap the array of news
                intent.putExtra("news", Parcels.wrap(news));

                mContext.startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
