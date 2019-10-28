package com.example.anrdoijsonprasing;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private customListViewAllGuides customListViewAdapter;
    public String currentView = "LISTVIEW";
    int scrollY = -1;

    HashMap<String, JSONArray> all_guides_list = new HashMap<>();
    HashMap<String, JSONArray> full_guid = new HashMap<>();
    HashMap<String, String> guide_basic_info = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        currentView = "LISTVIEW";
        getJSON("http://justdo.co.il/android/return_guides_list.php");

        setContentView(R.layout.activity_main);


        // super.onCreate(savedInstanceState);


    }

    public HashMap<String, String> get_guide_basic_info() {
        return guide_basic_info;
    }

    public HashMap<String, JSONArray> get_full_guid() {
        return full_guid;
    }

    private void getJSON(final String urlWebService) {
        /*
         * As fetching the json string is a network operation
         * And we cannot perform a network operation in main thread
         * so we need an AsyncTask
         * The constrains defined here are
         * Void -> We are not passing aanything
         * Void -> Nothing at progress update as well
         * String -> After completion it should return a string and it will be the json string
         * */
        class GetJSON extends AsyncTask<Void, Void, String> {

            //this method will be called before execution
            //you can display a progress bar or something
            //so that user can understand that he should wait
            //as network operation may take some time
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            //this method will be called after execution
            //so here we are displaying a toast with the json string
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //  Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                if (currentView.equals("LISTVIEW")) {
                    buildJsonForList(s);
                } else if (currentView.equals("GUIDVIEW")) {
                    buildJsonForGuide(s);
                }
            }

            //in this method we are fetching the json string
            @Override
            protected String doInBackground(Void... voids) {


                try {
                    //creating a URL
                    URL url = new URL(urlWebService);

                    //Opening the URL using HttpURLConnection
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    //StringBuilder object to read the string from the service
                    StringBuilder sb = new StringBuilder();

                    //We will use a buffered reader to read the string from service
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    //A simple string to read values from each line
                    String json;

                    //reading until we don't find null
                    while ((json = bufferedReader.readLine()) != null) {

                        //appending it to string builder
                        // sb.append(json + "\n");
                        System.out.println("***" + json);
                        return json;
                    }

                    //finally returning the read string
                    return sb.toString().trim();
                } catch (Exception e) {
                    e.getMessage();
                    return null;
                }

            }
        }

        //creating asynctask object and executing it
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    public void startBuildList() {
        listView = findViewById(R.id.main_cont);
        //// crate an hash array list
        ArrayList<HashMap<String, String>> guidesList = new ArrayList<>();


        for (int i = 0; i < all_guides_list.get("title").length(); i++) {
            HashMap<String, String> data = new HashMap<>();
            try {
                data.put("id", all_guides_list.get("id").getString(i));
                data.put("title", all_guides_list.get("title").getString(i));
                data.put("subtitle", all_guides_list.get("subtitle").getString(i));
                data.put("active", all_guides_list.get("active").getString(i));
                if (all_guides_list.get("user").get(i) != null) {
                    data.put("user", all_guides_list.get("user").getString(i));
                } else {
                    data.put("user", "");
                }
                String tempImagesArrayList = all_guides_list.get("images_fix").get(i).toString();
                //  char[] charArray = tempImagesArrayList.toCharArray();
                //  charArray.g
                data.put("image", tempImagesArrayList);
            } catch (JSONException e) {

            }

          /*  data.put("pages", bookPages[i]);
            data.put("author", authors[i]);*/
            guidesList.add(data);

        }
        /// convert the array hash list to list view
        customListViewAdapter = new customListViewAllGuides(getApplicationContext(), guidesList, this);

        /// bind -> adapt the listView to the main activity xml list view
        listView.setAdapter(customListViewAdapter);
        /// set listeners to the list buttons view
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int intPosition = position;
                TextView id_element = view.findViewById(R.id.get_item_id);
                String curret_id = id_element.getText().toString();
                //String clickedValue = listView.getItemAtPosition(intPosition).toString();
                Toast.makeText(getApplicationContext(), curret_id, Toast.LENGTH_LONG).show();
                currentView = "GUIDVIEW";
                getJSON("http://justdo.co.il/android/guid_pull.php?guide=" + curret_id);
                scrollY = listView.getSelectedItemPosition();
                //  displayGuid(curret_id);
            }

        });
        // if (scrollY != -1) listView.setSelection(scrollY);
    }

    public void startBuildGuid(/*String guid_id*/) {
        listView = findViewById(R.id.main_cont);
        //// crate an hash array list
        //ArrayList<HashMap<String, String>> guidesList = new ArrayList<>();

        Fragment guidFragment;
        guidFragment = new customViewGuid();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.place_holder_for_guid, guidFragment).addToBackStack("xxxxxx")
                .commit();


        View main_list_view = findViewById(R.id.main_cont);
        main_list_view.setVisibility(View.GONE);

    }

    public void buildJsonForList(String s) {
        try {
            JSONObject jObj = new JSONObject(s);
            JSONArray id_array = jObj.getJSONArray("id");
            JSONArray active_array = jObj.getJSONArray("active");
            JSONArray subject_array = jObj.getJSONArray("subject");
            JSONArray user_array = jObj.getJSONArray("user");
            JSONArray key_array = jObj.getJSONArray("guide_key");
            JSONArray title_array = jObj.getJSONArray("guide_title");
            JSONArray subtitle_array = jObj.getJSONArray("guide_subtitle");
            JSONArray images_array = jObj.getJSONArray("guide_images_array");
            JSONArray images_array_fix = jObj.getJSONArray("guide_images_array_fix");


            ArrayList id_array_list = new ArrayList();
            ArrayList active_array_list = new ArrayList();
            ArrayList subject_array_list = new ArrayList();
            ArrayList user_array_list = new ArrayList();
            ArrayList key_array_list = new ArrayList();
            ArrayList title_array_list = new ArrayList();
            ArrayList subtitle_array_list = new ArrayList();
            ArrayList images_array_list = new ArrayList();
            ArrayList images_array_fix_list = new ArrayList();
            //remove not active guides
            boolean toShowCurrentGuide;

            for (int x = 0; x < title_array.length(); x++) {
                toShowCurrentGuide = active_array.get(x).equals("1");
                if (toShowCurrentGuide) {
                    id_array_list.add(id_array.getString(x));
                    active_array_list.add(active_array.getString(x));
                    subject_array_list.add(subject_array.getString(x));
                    user_array_list.add(user_array.getString(x));
                    key_array_list.add(key_array.getString(x));
                    title_array_list.add(title_array.getString(x));
                    subtitle_array_list.add(subtitle_array.getString(x));
                    images_array_list.add(images_array.getString(x));
                    images_array_fix_list.add(images_array_fix.getString(x));
                    //id_array_list
                }
            }
            JSONArray id_array_new = new JSONArray(id_array_list);
            JSONArray active_array_new = new JSONArray(active_array_list);
            JSONArray subject_array_new = new JSONArray(subject_array_list);
            JSONArray user_array_new = new JSONArray(user_array_list);
            JSONArray key_array_new = new JSONArray(key_array_list);
            JSONArray title_array_new = new JSONArray(title_array_list);
            JSONArray subtitle_array_new = new JSONArray(subtitle_array_list);
            JSONArray images_array_new = new JSONArray(images_array_list);
            JSONArray images_array_fix_new = new JSONArray(images_array_fix_list);
            all_guides_list.put("id", id_array_new);
            all_guides_list.put("active", active_array_new);
            all_guides_list.put("subject", subject_array_new);
            all_guides_list.put("user", user_array_new);
            all_guides_list.put("key", key_array_new);
            all_guides_list.put("title", title_array_new);
            all_guides_list.put("subtitle", subtitle_array_new);
            all_guides_list.put("images", images_array_new);
            all_guides_list.put("images_fix", images_array_fix_new);
                    /*JSONArray loop_array = jObj.getJSONArray("loop");
                    all_guides_list.put("loop", loop_array);*/

            startBuildList();


        } catch (JSONException e) {

        }

    }

    public void buildJsonForGuide(String s) {
        try {
            JSONObject jObj = new JSONObject(s);

            guide_basic_info.put("title", jObj.get("guide_title").toString());
            guide_basic_info.put("subtitle", jObj.get("guide_subtitle").toString());
            guide_basic_info.put("user", jObj.get("user").toString());
            guide_basic_info.put("subject", jObj.get("subject").toString());
            guide_basic_info.put("id", jObj.get("id").toString());


            full_guid.put("steps", jObj.getJSONArray("type_of_steps_array"));
            full_guid.put("images", jObj.getJSONArray("guide_images_array"));
            full_guid.put("texts", jObj.getJSONArray("guide_text_array"));
            full_guid.put("videos", jObj.getJSONArray("guide_videos_array"));
            full_guid.put("textarea", jObj.getJSONArray("guide_textarea_array"));

            startBuildGuid();


        } catch (JSONException e) {

        }

    }


    public void onBackPressed() {
        //super.onBackPressed();
        backScreen();


    }


    public void sendImageToFrontFragment(String img_src) {

        Bundle bundle = new Bundle();
        bundle.putString("img_src", img_src);
        Fragment bigImgFragment;
        bigImgFragment = new fragment_large_img();
        bigImgFragment.setArguments(bundle);
        currentView = "IMAGEVIEW";
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.place_holder_for_guid, bigImgFragment, "imageInFront").addToBackStack(null)
                .commit();
    }

    public void backScreen() {
        super.onBackPressed();
        if (currentView == "IMAGEVIEW") {
            currentView = "GUIDVIEW";
            System.out.println("remove image view ");
        } else {
            View main_list_view = findViewById(R.id.main_cont);
            main_list_view.setVisibility(View.VISIBLE);

            currentView = "LISTVIEW";
            //  getJSON("http://justdo.co.il/android/return_guides_list.php");

        }
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

    public String fixImageUrl(String bad_string) {

        String fixed_string = bad_string;
        fixed_string = fixed_string.replaceAll("\\[", "");
        fixed_string = fixed_string.replaceAll("]", "");
        fixed_string = fixed_string.replaceAll("\\\\", "");
        fixed_string = fixed_string.replaceAll("\\\"", "");
        //  fixed_string = fixed_string.replaceAll("\\/", "\\");
        fixed_string = "http://justdo.co.il/" + fixed_string;
        return fixed_string;
    }

    public String clearHtmlTags(String old_sting) {

        String output;

        String regex_a = "<a([^\\[>]+).*([<]).{3}";
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

        //remove a


        //
        output = fixLinks(output);
        output = output.replaceAll(regex_a, "***");


        output = output.replaceAll("<u>", "");
        output = output.replaceAll("<ol>", "");
        output = output.replaceAll("<p>", "");
        output = output.replaceAll("</br>", "");
        output = output.replaceAll("<br>", "");


        return output;
    }

    public String fixLinks(String input) {

        List<String> allMatches = new ArrayList<String>();
        int arraySize = 0;
        Matcher m = Pattern.compile("<a([^\\[>]+).*([<]).{3}").matcher(input);
        while (m.find()) {
            allMatches.add(m.group());
        }
        String onlyUrlString = "";
        for (String s : allMatches) {
            String match_clean_url = "\"([^\\[>]+).*([\"])";
            //onlyUrlString = s.replaceAll(s,match_clean_url);
            Pattern pattern = Pattern.compile(match_clean_url);

            Matcher matcher = pattern.matcher(s);
            //input = input.replaceAll(s, onlyUrlString);
            if (matcher.find()) {
                String theGroup = matcher.group(0);
                System.out.println(theGroup);
                theGroup = theGroup.replaceAll("\"","");
                input = input.replace(s, theGroup);
            }

        }
        return input;
    }
}
