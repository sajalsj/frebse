package com.worldtechq.frebse;

import com.google.firebase.database.Exclude;

public class Upload {
    private  String mname;
    private  String mImageUrl;
    private String mkey;

      public Upload()
      {

      }
      public  Upload( String name,String ImageUrl)
      {
          if (name.trim ().equals (""))
          {
              name="No Name";
          }
          this.mname=name;
          this.mImageUrl=ImageUrl;
      }

    public String getMname() {
        return mname;
    }

    public  void setMname(String name)
    {
        mname=name;
    }
    public String getmImageUrl() {
        return mImageUrl;
    }
    public  void setmImageUrl(String imageUrl)

    {
        mImageUrl=imageUrl;
    }
    @Exclude
    public String getKey()
    {
        return mkey;
    }
    @Exclude
    public void setkey(String key)
    {   mkey=key;

    }
}
