package com.example.pc.testapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    GridView gridView;
    ProgressDialog prg;
    List<Picture> allPictures = null;
    GridAdapter gridAdapter;
    EditText searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        allPictures = new ArrayList<Picture>();
        searchButton = (EditText) findViewById(R.id.searchButton);
        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GridAdapter(this, allPictures);
        gridView.setAdapter(gridAdapter);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        JsonTask tsk = new JsonTask();
        try {
            tsk.execute("https://api.flickr.com/services/rest/?method=flickr.photos.getRecent&api_key=7397c4181be23a786ac9efff0cb1d55d&format=json&nojsoncallback=1").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        searchButton.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN && (i == KeyEvent.KEYCODE_ENTER)){

                    String query =searchButton.getText().toString();
                    //allPictures = null;
                    allPictures.clear();
                    //gridView.setAdapter(null);

                    //search button is clicked make call here.
                    JsonTask tsk = new JsonTask();
                    try {
                        tsk.execute("https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=7397c4181be23a786ac9efff0cb1d55d&tags=" +
                                query + "&format=json&nojsoncallback=1").get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    gridAdapter = new GridAdapter(getApplicationContext(), allPictures);
                    gridView.setAdapter(gridAdapter);

                    return true;
                }
                return false;
            }
        });


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(MainActivity.this, "item clicked" + i, Toast.LENGTH_SHORT).show();
                //Picture pictureClicked = (Picture) gridView.getAdapter().getItem(i);
                Picture pictureClicked = (Picture) gridView.getItemAtPosition(i);
                //String farm = pictureClicked.getFarm();

                //Toast.makeText(MainActivity.this, farm, Toast.LENGTH_SHORT).show();
                String path = "https://farm"
                        + pictureClicked.getFarm()
                        + ".staticflickr.com/"
                        + pictureClicked.getServer()
                        + "/" + pictureClicked.getId()
                        + "_" + pictureClicked.getSecret()
                        + ".jpg";

                showImage(path);

            }
        });

        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem + visibleItemCount >= totalItemCount){
                    // End has been reached
                    //Toast.makeText(MainActivity.this, "Request new data for pagination here...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void showImage(String image) {
        Dialog builder = new Dialog(this);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });

        ImageView imageView = new ImageView(this);
        Picasso.with(getApplicationContext()).load(image).into(imageView);

       // imageView.setImageResource(R.mipmap.ic_launcher);
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        builder.show();
    }
    class JsonTask extends AsyncTask<String,Void,JSONObject> {


        @Override
        protected void onPreExecute() {
            prg = new ProgressDialog(MainActivity.this);
            prg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prg.setTitle("Loading..");
            prg.setMessage("Please wait");

            prg.show();

        }



        @Override
        protected JSONObject doInBackground(String... params) {

            String urlStr = params[0];
            HttpURLConnection conn = null;
            JSONObject retObj = null;

            try {
                URL url = new URL(urlStr);
                conn = (HttpURLConnection)url.openConnection();
                //conn.setDoOutput(true);
                //conn.setDoInput(true);
                conn.setRequestProperty("Content-Type","application/json");
                conn.connect();

                if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                    InputStream in = conn.getInputStream();

                    BufferedReader reader
                            = new BufferedReader(new InputStreamReader(in));



                    String line = "";
                    StringBuilder strBuffer = new StringBuilder();
                    while((line = reader.readLine())!=null){
                        Log.v("Test", line);
                        strBuffer.append(line);

                    }

                    retObj = new JSONObject(strBuffer.toString());

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
            }


            return retObj;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {



            prg.dismiss();

            try {
                JSONObject response = jsonObject.getJSONObject("photos");

                JSONArray photos = response.getJSONArray("photo"); //this has all my news object as children.

                for(int i = 0; i < photos.length(); i++){
                    JSONObject singlePhotoItem = photos.getJSONObject(i);
                    String photoId = singlePhotoItem.getString("id");
                    String photoSecret = singlePhotoItem.getString("secret");
                    String photoServer = singlePhotoItem.getString("server");
                    String photoFarm = singlePhotoItem.getString("farm");
                    //Toast.makeText(MainActivity.this,photoId + " " + photoSecret + " " + photoServer + " " + photoFarm, Toast.LENGTH_SHORT).show();
                    Picture picture = new Picture();
                    picture.setId(photoId);
                    picture.setSecret(photoSecret);
                    picture.setServer(photoServer);
                    picture.setFarm(photoFarm);

                    allPictures.add(picture);
                    //gridAdapter.notifyDataSetChanged();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
