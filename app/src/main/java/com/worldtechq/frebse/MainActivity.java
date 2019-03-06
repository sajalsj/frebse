package com.worldtechq.frebse;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final int pickid = 1;
    private Button upload;

    private Button showImage;
   /* private TextView showUplaod;*/
    private EditText filename;
    private ImageView imageView;
    private ProgressBar progressBar;
    private Uri muri;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult (requestCode, resultCode, data);
        if (requestCode == pickid && resultCode == RESULT_OK && data != null && data.getData ( ) != null)
            ;

        muri = data.getData ( );

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap (getContentResolver ( ), muri);
            imageView.setImageBitmap (bitmap);
        } catch (IOException e) {
            e.printStackTrace ( );


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        /*typecasting*/

        upload = findViewById (R.id.upload);
        showImage = findViewById (R.id.chooseimg);
        /*showUplaod = findViewById (R.id.shw);*/
        filename = findViewById (R.id.filenmae);
        imageView = findViewById (R.id.imv);
        progressBar = findViewById (R.id.pbar);
        /*give path of firebase database or storage where image will be store*/

        storageReference = FirebaseStorage.getInstance ( ).getReference ("uploads");
        databaseReference = FirebaseDatabase.getInstance ( ).getReference ("uploads");

        /*select image from mobile */
        showImage.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {

                OpenFile ( );

            }
        });

        /* upload image on firebase*/

        upload.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                UploadFile ( );


            }
        });




    }

    /*this method select image feom mobile*/
    private void OpenFile() {
        Intent intent = new Intent ( );
        intent.setType ("image/*");
        intent.setAction (Intent.ACTION_GET_CONTENT);
        startActivityForResult (intent, pickid);
    }

    /*this method return the image xtension*/

    private String getFileEx(Uri uri) {

        ContentResolver cn = getContentResolver ( );
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton ( );
        return mimeTypeMap.getExtensionFromMimeType (cn.getType (uri));
    }


    /*  this method contains the actual code of upload image on firebase databse*/

    private void UploadFile() {
        if (muri != null) {
            final StorageReference filerefrence = storageReference.child ("uploads/" + System.currentTimeMillis ( ) + "." + getFileEx (muri));
            filerefrence.putFile (muri)
                    .addOnSuccessListener (new OnSuccessListener<UploadTask.TaskSnapshot> ( ) {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler ( );
                            handler.postDelayed (new Runnable ( ) {
                                @Override
                                public void run() {
                                    progressBar.setProgress (0);
                                }
                            }, 500);
                            Toast.makeText (MainActivity.this, "Successful Upload", Toast.LENGTH_SHORT).show ( );


                            Task<Uri> urlTask = taskSnapshot.getStorage ( ).getDownloadUrl ( );

                            while (!urlTask.isSuccessful ( )) ;
                            Uri downloadUrl = urlTask.getResult ( );
                            final String sdownload_url = String.valueOf (downloadUrl);
                            Log.d (sdownload_url, "onSuccess: firebase download url: " + downloadUrl.toString ( ));

                            Upload upload = new Upload (filename.getText ( ).toString ( ).trim ( )
                                    , downloadUrl.toString ( ));
                            String uploadid = databaseReference.push ( ).getKey ( );
                            databaseReference.child (uploadid).setValue (upload);
                            imageView.setImageDrawable (null);
                            filename.getText ().clear ();


                        }
                    })
                    .addOnFailureListener (new OnFailureListener ( ) {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText (MainActivity.this, e.getMessage ( ), Toast.LENGTH_SHORT).show ( );

                        }
                    })
                    .addOnProgressListener (new OnProgressListener<UploadTask.TaskSnapshot> ( ) {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred ( ) / taskSnapshot.getTotalByteCount ( ));
                            progressBar.setProgress ((int) progress);
                        }
                    });

        } else {
            Toast.makeText (this, "No file selected", Toast.LENGTH_SHORT).show ( );
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater ( );
        menuInflater.inflate (R.menu.show, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId ( )) {
            case R.id.i1:
                Intent i = new Intent (MainActivity.this, Second.class);
                this.startActivity (i);
                return true;
            default:
                return super.onOptionsItemSelected (item);

        }
    }
}
