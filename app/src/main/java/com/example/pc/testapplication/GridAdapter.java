package com.example.pc.testapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by pc on 24-Jul-17.
 */

public class GridAdapter extends BaseAdapter{

    Context context;
    //private final  String [] values;
    //private final int [] images;

    private List<Picture> pictures;
    View view;
    LayoutInflater layoutInflater;

    public GridAdapter(Context context, List<Picture> pictures){
        this.context = context;
        //this.values = values;
        this.pictures = pictures;

    }

    @Override
    public int getCount() {
        return pictures.size();
    }

    @Override
    public Object getItem(int i) {
        return pictures.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(view == null) {
            view = new View(context);
            view = layoutInflater.inflate(R.layout.single_item, null);

        }

            //call for each photo here and load photo

            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);

            //https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}.jpg

            String photoId = pictures.get(i).getId();
            String photoFarm = pictures.get(i).getFarm();
            String photoSecret = pictures.get(i).getSecret();
            String photoServer = pictures.get(i).getServer();

            String urlPath = "https://farm" + photoFarm + ".staticflickr.com/" + photoServer + "/" + photoId + "_" + photoSecret + ".jpg";
            Picasso.with(context).load(urlPath).into(imageView);


        return  view;
    }
}
