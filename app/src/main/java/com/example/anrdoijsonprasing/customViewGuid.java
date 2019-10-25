package com.example.anrdoijsonprasing;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;

public class customViewGuid extends Fragment {
    HashMap<String, String> guide_basic_info = new HashMap<>();
    HashMap<String, JSONArray> full_guid = new HashMap<>();
    int scrollY;
    ScrollView sv = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("customViewGuid created @@@@@@@@@@@@@@@@@@@@@@@@@@@@@21");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_guid, container, false);
        System.out.println("customViewGuid create view !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1");

        guide_basic_info = ((MainActivity) this.getActivity()).get_guide_basic_info();
        full_guid = ((MainActivity) this.getActivity()).get_full_guid();
        updateView(view, inflater, container);
        return view;
    }

    public void updateView(View v, LayoutInflater inflater, ViewGroup container) {
        String current_img_src;
        //// start create dynamic objects to add it to scrolling object
        LinearLayout cont_steps = v.findViewById(R.id.fill_steps);
        LinearLayout temp_lenear_layout = new LinearLayout(getActivity());
        System.out.println("");
        sv = new ScrollView(getActivity());
        sv.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                scrollY = sv.getScrollY();
                System.out.println("!!!!!!!!!!!!!!!" + scrollY);
            }
        });

        TextView main_title = new TextView(this.getActivity());
        TextView main_sub_title = new TextView(this.getActivity());
        ImageView main_img = new ImageView(this.getActivity());
        main_title.setText(guide_basic_info.get("title"));
        main_title.setGravity(Gravity.CENTER);
        main_title.setTextSize(20);
        main_sub_title.setText(guide_basic_info.get("subtitle"));
        main_sub_title.setGravity(Gravity.CENTER);
        main_sub_title.setTextSize(17);

        try {
            String main_img_src = ((MainActivity) this.getActivity()).fixImageUrl(full_guid.get("images").getString(0));
            Picasso.with(v.getContext()).load(main_img_src).error(android.R.drawable.sym_contact_card).placeholder(android.R.drawable.sym_contact_card).into(main_img);
        } catch (JSONException e) {

        }
        temp_lenear_layout.setOrientation(LinearLayout.VERTICAL);
        temp_lenear_layout.addView(main_title);
        temp_lenear_layout.addView(main_sub_title);
        temp_lenear_layout.addView(main_img);
        main_img.setAdjustViewBounds(true);
        main_img.setMaxHeight(300);
        int steps_length = full_guid.get("steps").length();
        int loop_textarea = 0;
        int loop_text_and_img = 0;
        for (int loop = 0; loop < steps_length; loop++) {


            full_guid.get("steps").toString();
            TextView child = new TextView(this.getActivity());

            /// create a separate between every step
            LinearLayout temp_visual_linear_layout_block = new LinearLayout(getActivity());
            temp_visual_linear_layout_block.setPadding(0, 10, 0, 10);
            temp_visual_linear_layout_block.setOrientation(LinearLayout.VERTICAL);
            GradientDrawable drawable = new GradientDrawable();
            /// create a border by shape for every step
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setStroke(3, Color.BLACK);
            drawable.setCornerRadius(8);
            drawable.setColor(getResources().getColor(R.color.BackgroundGray));


            temp_visual_linear_layout_block.setBackgroundDrawable(drawable);
            try {
//                child.setText(full_guid.get("steps").getString(loop));
                String current_steps = full_guid.get("steps").getString(loop);
                switch (current_steps) {
                    case "textarea":
                        String current_textarea = clearHtmlTags(full_guid.get("textarea").getString(loop_textarea));
                        current_textarea = fixChars(current_textarea);
                        child.setText(current_textarea);
                        child.setTextSize(20);
                        child.setGravity(View.TEXT_ALIGNMENT_CENTER);
                        child.setHeight(100);
                        child.setMaxWidth(100);
                        child.setSingleLine(false);
                        child.setGravity(Gravity.CENTER_HORIZONTAL);
                        loop_textarea++;
                        temp_lenear_layout.addView(child);

                        break;
                    case "text_and_img":
                        ImageView child_img = new ImageView(this.getActivity());
                        String get_img_url_from_api = full_guid.get("images").getString(loop_text_and_img + 1);
                        /// image is +1 because of img 0 is for the header
                        if (!get_img_url_from_api.equals("")) {
                            final String fixed_img_src = ((MainActivity) this.getActivity()).fixImageUrl(get_img_url_from_api);
                            Picasso.with(v.getContext()).load(fixed_img_src).error(android.R.drawable.sym_contact_card).placeholder(android.R.drawable.sym_contact_card).into(child_img);

                            child_img.setAdjustViewBounds(true);
                            child_img.setMaxHeight(200);
                            child_img.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ((MainActivity) getActivity()).sendImageToFrontFragment(fixed_img_src);

                                }
                            });
                        }
                        String current_text = fixChars(full_guid.get("texts").getString(loop_text_and_img));
                        child.setText(current_text);
                        child.setTextSize(20);
                        child.setGravity(View.TEXT_ALIGNMENT_CENTER);
                        child.setHeight(100);
                        child.setMaxWidth(100);
                        child.setSingleLine(false);
                        child.setGravity(Gravity.CENTER_HORIZONTAL);
                        loop_text_and_img++;
                        //Adding child to the room


                        temp_visual_linear_layout_block.addView(child);
                        if (!get_img_url_from_api.equals("")) {
                            temp_visual_linear_layout_block.addView(child_img);
                        }
                        temp_lenear_layout.addView(temp_visual_linear_layout_block);
                        break;

                    case "youtube":
                        String current_video_path = fixChars(full_guid.get("videos").getString(loop_textarea));

                        if (current_video_path.contains("avi") || current_video_path.contains("mp4") || current_video_path.contains("flv")) {
                            current_video_path = ((MainActivity) getActivity()).fixImageUrl(current_video_path);
                            VideoView video_view = new VideoView(this.getContext());
                            final VideoView videoview = (VideoView) video_view;
                            Uri uri = Uri.parse(current_video_path);
                            videoview.setVideoURI(uri);
                            /// set video view params
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(800, 400);
                            params.gravity = Gravity.CENTER_HORIZONTAL;
                            videoview.setLayoutParams(params);

                            videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mp) {
                                    videoview.start();
                                }
                            });
                            temp_lenear_layout.addView(video_view);
                        } else {
                            WebView video_view = new WebView(getContext());
                            String frameVideo = "<iframe width='100%' height='500px' src='https://www.youtube.com/embed/" + current_video_path + "' frameborder='0' allowfullscreen></iframe>";
                            video_view.loadData(frameVideo, "text/html", "utf-8");
                            WebSettings webSettings = video_view.getSettings();
                            webSettings.setJavaScriptEnabled(true);
                            webSettings.setDomStorageEnabled(true);
                            video_view.clearCache(true);
                            temp_lenear_layout.addView(video_view);
                        }


                        break;
                }
                //              child.setText(full_guid.get("steps").getString(loop));
            } catch (JSONException e) {
            }


        }

        sv.setId(R.id.guid_scroll_view);
        sv.addView(temp_lenear_layout);

        //sv.setPadding(0, 0, 0, 0);
        System.out.println("!!!!!!!!!!!!!!!!!!" + String.valueOf(sv.getHeight()));
        cont_steps.addView(sv);


    }

    public String clearHtmlTags(String old_sting) {

        String output;
        String regex_span = "<span([^>]+).";
        String regex_p = "<p([^>]+).";
        String regex_div = "<div([^>]+).";
        String regex_li = "<li([^>]+).";
        String regex_ul = "<ul([^>]+).";
        String regex_ol = "<ol([^>]+).";
        String regex_u = "<u([^>]+).";

        //remove span
        output = old_sting.replaceAll(regex_span, "");
        output = output.replaceAll("</span>", "");

        //remove p
        output = output.replaceAll(regex_p, "");
        output = output.replaceAll("</p>", "");
        //remove div
        output = output.replaceAll(regex_div, "");
        output = output.replaceAll("</div>", "");
        //remove li
        output = output.replaceAll(regex_li, "* ");
        output = output.replaceAll("</li>", "");
        //remove ul
        output = output.replaceAll(regex_ul, "");
        output = output.replaceAll("</ul>", "");
        //remove ol
        output = output.replaceAll(regex_ol, "");
        output = output.replaceAll("</ol>", "");
        //remove u
        output = output.replaceAll(regex_u, "");
        output = output.replaceAll("</u>", "");


        output = output.replaceAll("<u>", "");
        output = output.replaceAll("<ol>", "");
        output = output.replaceAll("<p>", "");
        output = output.replaceAll("</br>", "");
        output = output.replaceAll("<br>", "");


        return output;
    }

    public String fixChars(String string_to_fix) {
        string_to_fix = string_to_fix.replaceAll("&#41;", ")");
        string_to_fix = string_to_fix.replaceAll("&#40;", "(");
        string_to_fix = string_to_fix.replaceAll("&#39;", "'");
        string_to_fix = string_to_fix.replaceAll("&#47;", "/");
        string_to_fix = string_to_fix.replaceAll("&#44;", ",");
        string_to_fix = string_to_fix.replaceAll("&#34;", "\"");
        //string_to_fix = string_to_fix.replaceAll("<p>", "");
        //string_to_fix = string_to_fix.replaceAll("</p>", "");
        //string_to_fix = string_to_fix.replaceAll("<p/>", "");
        string_to_fix = string_to_fix.replaceAll("&nbsp;", " ");

        return string_to_fix;
    }

    @Override
    public void onResume() {
        super.onResume();
        sv.scrollTo(scrollY, scrollY);

    }
}

