package com.worldtechq.frebse;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import java.util.List;

/*  adapter it fetch data from databse and show it on second activity*/

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
     private Context mcontext;
     private List<Upload> muploads;
     public OnItemClickListener mlistener;


     public  ImageAdapter(Context context, List<Upload> uploads)
     {
         this.mcontext=context;
         this.muploads=uploads;
     }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from (mcontext).inflate (R.layout.imagesdata,parent,false);
        return new ImageViewHolder (v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
         Upload uploadfile= muploads.get(position);
         holder.textView.setText ( uploadfile.getMname ());
        RequestOptions options = new RequestOptions()

                .fitCenter ()
                .placeholder(R.drawable.ic_launcher_background);
        Glide.with(mcontext).load (uploadfile.getmImageUrl ()).apply (options).into (holder.imageView);




    }

    @Override
    public int getItemCount() {
        return muploads.size ();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener,MenuItem.OnMenuItemClickListener{
        TextView textView;
        ImageView imageView;

        public ImageViewHolder(View itemView) {
            super (itemView);
            textView=itemView.findViewById (R.id.imagetext);
            imageView=itemView.findViewById (R.id.imageview);
            itemView.setOnClickListener (this);
            itemView.setOnCreateContextMenuListener (this);

        }

        @Override
        public void onClick(View v) {
            if (mlistener != null)
            {
                int position=getAdapterPosition ();
                if (position != RecyclerView.NO_POSITION);
                {
                    mlistener.onItemLick (position);

                }
            }
        }
        /*this method give option to select when longpress on image*/

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle ("select Action");
            MenuItem doWhatever=menu.add (menu.NONE,1,1,"Noting to do");
            MenuItem delete=menu.add (menu.NONE,2,2,"delete");
            doWhatever.setOnMenuItemClickListener (this);
            delete.setOnMenuItemClickListener (this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mlistener != null)
            {
                int position=getAdapterPosition ();
                if (position != RecyclerView.NO_POSITION);
                {
                   switch (item.getItemId ())
                   {
                       case 1:
                           mlistener.onWhateverClick (position);
                           return  true;
                       case 2:
                           mlistener.onDeleteClick (position);
                           return true;
                   }

                }
            }
              return  false;
        }
    }
    /*this interface is allow you to click on images*/
    public interface OnItemClickListener{
          void onItemLick(int position);

         void onWhateverClick(int position);

         void  onDeleteClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
         mlistener=onItemClickListener;

    }
}
