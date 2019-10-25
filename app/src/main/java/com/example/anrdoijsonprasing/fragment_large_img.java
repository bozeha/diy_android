package com.example.anrdoijsonprasing;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

public class fragment_large_img extends Fragment {
    View current_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ImageView current_img = container.findViewById(R.id.large_image);
        View currentView = inflater.inflate(R.layout.large_image_fragment, container, false);
        //currentView.findViewById(R.id.large_image).setBackgroundColor(getResources().getColor(R.color.DarkOrchid));
        current_view = currentView;
        updateImageSrc(this.getArguments().getString("img_src"), currentView);
        return currentView;
    }

    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

    }

    public void updateImageSrc(String src, View v) {
        ImageView current_img = v.findViewById(R.id.large_image);
        RelativeLayout main_large_img_layout = v.findViewById(R.id.large_image_fragment);
        Picasso.with(v.getContext()).load(src).error(android.R.drawable.sym_contact_card).placeholder(android.R.drawable.sym_contact_card).into(current_img);
   /*     current_img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Your code.
                ((MainActivity) getActivity()).backScreen();
            }
        });*/
        main_large_img_layout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Your code.
                System.out.println("parentttttttttttttttttttttttttttttttttttttt");
                ((MainActivity) getActivity()).backScreen();
            }
        });
    }


}
