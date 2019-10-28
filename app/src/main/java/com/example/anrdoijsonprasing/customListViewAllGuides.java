package com.example.anrdoijsonprasing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;


public class customListViewAllGuides extends BaseAdapter {

    private Context mContext;
    private Context mMainActivityContext;
    /// the hashMap let you create key for each string
    /// example {key , value } => (1 , "bob")
    private ArrayList<HashMap<String, String>> guidesListReceived;
    /// inflater is function to inflate the xml view to the java file
    private static LayoutInflater inflater = null;


    public customListViewAllGuides(Context context, ArrayList<HashMap<String, String>> temp_data, Context activity_context) {

        this.mContext = context;
        this.mMainActivityContext = activity_context;
        guidesListReceived = temp_data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    @Override
    /// count how meney item there is
    public int getCount() {
        return guidesListReceived.size();
    }

    @Override

    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        HashMap<String, String> json_object;
        json_object = guidesListReceived.get(position);
        if (convertView == null) {
            //view = inflater.inflate(R.layout.row_listview_item, null);
            view = inflater.inflate(R.layout.list_row_view, null);
        }
        TextView item_id = view.findViewById(R.id.get_item_id);
        TextView title = view.findViewById(R.id.title);
        TextView sub_title = view.findViewById(R.id.sub_title);
        TextView pages = view.findViewById(R.id.pages);
        ImageView image = view.findViewById(R.id.list_img);
        String temp_title = (json_object.get("title"));
        if (mMainActivityContext instanceof MainActivity) {
            temp_title = ((MainActivity) mMainActivityContext).fixChars(temp_title);
        }
        String temp_sub_title = json_object.get("subtitle");
        if (mMainActivityContext instanceof MainActivity) {
            temp_sub_title = ((MainActivity) mMainActivityContext).fixChars(temp_sub_title);
        }
        title.setText(temp_title);
        sub_title.setText(temp_sub_title);
        pages.setText(json_object.get("pages"));
        item_id.setText(json_object.get("id"));

        final String imgURL = ((MainActivity) mMainActivityContext).fixImageUrl(json_object.get("image"));
        Picasso.with(view.getContext()).load(imgURL).error(android.R.drawable.sym_contact_card).placeholder(android.R.drawable.sym_contact_card).into(image);
        return view;
    }


    public void loadImage(String url, ImageView imageView) {
        Picasso.with(imageView.getContext()).load(url).into(imageView);
    }

/*    public String fixImageUrl(String bad_string) {
        String fixed_string = bad_string;
        fixed_string = fixed_string.replaceAll("\\[", "");
        fixed_string = fixed_string.replaceAll("]", "");
        fixed_string = fixed_string.replaceAll("\\\\", "");
        fixed_string = fixed_string.replaceAll("\\\"", "");
        //  fixed_string = fixed_string.replaceAll("\\/", "\\");
        fixed_string = "http://justdo.co.il/" + fixed_string;
        return fixed_string;
    }*/

}
