package com.worldtechq.frebse;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ViewTarget;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Second extends AppCompatActivity implements ImageAdapter.OnItemClickListener{
    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private DatabaseReference databaseReference;
    private FirebaseStorage mstorage;
    private  ValueEventListener valueEventListener;
    private ProgressBar progressBar;
    private List<Upload> muploads;
    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_second);
        progressBar=findViewById (R.id.pbbar);
        recyclerView=findViewById (R.id.recyclerView);
        recyclerView.setHasFixedSize (true);
        recyclerView.setLayoutManager (new LinearLayoutManager (this));
        muploads= new ArrayList<> ();
        imageAdapter=new ImageAdapter (Second.this,muploads);
        recyclerView.setAdapter (imageAdapter);
        imageAdapter.setOnItemClickListener (Second.this);
        mstorage=FirebaseStorage.getInstance ();
        databaseReference= FirebaseDatabase.getInstance ().getReference ("uploads");

        valueEventListener=databaseReference.addValueEventListener (new ValueEventListener ( ) {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                muploads.clear ();
               for(DataSnapshot postSnapShot:dataSnapshot.getChildren ())
               {    Upload uploades= postSnapShot.getValue (Upload.class);
                    uploades.setkey (postSnapShot.getKey ());
               muploads.add (uploades);

               }
               imageAdapter.notifyDataSetChanged ();
               progressBar.setVisibility (View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText (Second.this, databaseError.getMessage (), Toast.LENGTH_SHORT).show ( );
                progressBar.setVisibility (View.INVISIBLE);

            }
        });


    }
    /*this method return the position of image*/

    @Override
    public void onItemLick(int position) {

        Upload upload=this.muploads.get(position);
        Intent intent=new Intent (Second.this,ImageOpen.class);

        Log.e ("InmageUrl", upload.getmImageUrl () );
        intent.putExtra ("imagename", upload.getMname ());
        intent.putExtra ("imageid", upload.getmImageUrl ());


        startActivity (intent);
        /*Toast.makeText (this, "click at position"+position, Toast.LENGTH_SHORT).show ( );*/
    }

    /*this method also return the position of image you can modify if according to your need*/
    @Override
    public void onWhateverClick(int position) {
        Toast.makeText (this, "whatever click at position"+position, Toast.LENGTH_SHORT).show ( );

    }

    /*this method is used to delete image from database */
    @Override
    public void onDeleteClick(int position) {
        Upload setItem=muploads.get (position);
        final String selectKey=setItem.getKey ();
        StorageReference ImageR= mstorage.getReferenceFromUrl (setItem.getmImageUrl ());
        ImageR.delete ().addOnSuccessListener (new OnSuccessListener<Void> ( ) {
            @Override
            public void onSuccess(Void aVoid) {
                databaseReference.child (selectKey).removeValue ();
                Toast.makeText (Second.this, "Item Deletd", Toast.LENGTH_SHORT).show ( );

            }
        });

    }
}
