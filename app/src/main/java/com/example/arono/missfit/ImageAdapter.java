package com.example.arono.missfit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by arono on 13/02/2016.
 */
public class ImageAdapter extends BaseAdapter implements Filterable{

    Context context;
    LayoutInflater inflater;
    ArrayList filteredData;
    ArrayList<Item> allData;
    public ImageAdapter(Context c,ArrayList data){
        this.context = c;
        //inflater = (LayoutInflater) c.getSystemService(c.LAYOUT_INFLATER_SERVICE);
        this.allData = data;
    }
    public ImageAdapter(Context c){
        this.context = c;
    }
    public int getCount() {
        return filteredData.size();
    }

    public void setItems(ArrayList data) {
        this.allData= data;
        this.filteredData = data;
    }

    @Override
    public Object getItem(int i) {
        return filteredData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout  row = (RelativeLayout)inflater.inflate(R.layout.single_row,viewGroup,false);
        TextView tvName = (TextView)row.findViewById(R.id.itemName);
        TextView tvPrice = (TextView)row.findViewById(R.id.itemPrice);
        ImageView imageView = (ImageView)row.findViewById(R.id.imageView);
        Item item = allData.get(i);
        tvName.setText(item.getName());
        tvPrice.setText(""+item.getPrice());
        //imageView.setImageResource(R.drawable.im1);
       // imageView.setImageBitmap(item.getPicture()[0]);
       ImageDownload downloadTask = new ImageDownload(item.getPhotoOne(),imageView,(ProgressBar)row.getChildAt(1));
       downloadTask.execute();
        return row;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                filteredData = new ArrayList<String>();

                for (Item s : (ArrayList<Item>) allData) {
                    if(s.getName().contains(constraint))
                        filteredData.add(s);
                }

                FilterResults results = new FilterResults();
                results.values = filteredData;
                results.count = filteredData.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                //filteredData = (ArrayList<String>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
