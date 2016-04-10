package com.example.om.myapplicationreg_new.Activities;

/**
 * Created by om on 4/8/2016.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import java.util.Date;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.om.myapplicationreg_new.ContentProvider.User;
import com.example.om.myapplicationreg_new.ContentProvider.UserLocalStore;
import com.example.om.myapplicationreg_new.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kshivang on 08/04/16.
 */
public class Home extends AppCompatActivity {
    UserLocalStore userLocalStore;
    final Context context = this;
    // flag for Internet connection status
    Boolean isInternetPresent = false;

    // Connection detector class
    ConnectionDetector cd;

    double latitude;
    double longitude;
    ImageView imgView;
    static final int REQUEST_TAKE_PHOTO = 1;
    String mCurrentPhotoPath,y,new_date;
    String xyz = "null";
    TextView tvAddress,tv1,tv2,tvv,tvm,tve,tvcredit,tv;
    private ProgressDialog loading,loading_new;
    private ProgressBar spinner;
    int photo_bill = 0;

    Date d = null;

    AppLocationService appLocationService;

    static String strSDCardPathName = Environment.getExternalStorageDirectory() + "/temp_picture" + "/";
    static String strURLUpload = "http://www.learnhtml.provisor.in/android/uploadFile.php";

    static String strSDCardPathNamemorning = Environment.getExternalStorageDirectory() + "/temp_picture" + "/";
    static String strURLUploadmorning = "http://www.learnhtml.provisor.in/android/uploadFilemorning.php";

    static String strSDCardPathNameevening = Environment.getExternalStorageDirectory() + "/temp_picture" + "/";
    static String strURLUploadevening = "http://www.learnhtml.provisor.in/android/uploadFileevening.php";
    final DBAdapter db1 = new DBAdapter(Home.this);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        userLocalStore = new UserLocalStore(this);

        User user = userLocalStore.getLoggedInUser();






        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Demo");
        query2.getInBackground("QNGkwKjpjt", new GetCallback<ParseObject>() {
            public void done(ParseObject gameScore, ParseException e) {
                if (e == null) {
                    gameScore.put("Demo", "Demo");
                    gameScore.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Demo");
                            query1.getInBackground("QNGkwKjpjt", new GetCallback<ParseObject>() {
                                public void done(ParseObject object, ParseException e) {

                                    if (e == null) {
                                        d = object.getUpdatedAt();
                                    }
                                    if (d != null) {
                                        Toast.makeText(getApplicationContext(), d.toString(), Toast.LENGTH_LONG).show();

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });













tv = (TextView)findViewById(R.id.textView2);
       tv.setText(user.contact);
String newstring;
        newstring = tv.getText().toString();
        tv.setVisibility(View.GONE);


        // creating connection detector class instance
        cd = new ConnectionDetector(getApplicationContext());
        appLocationService = new AppLocationService(
                Home.this);

        //Showing the current logged in email to textview
        //Fetching email from shared preferences

        tv1 = (TextView) findViewById(R.id.tv_amount);
        tv2 = (TextView) findViewById(R.id.textView3);
        tv2.setVisibility(View.GONE);
        tvv = (TextView) findViewById(R.id.tv1);
        tvcredit = (TextView) findViewById(R.id.credit_total);
        String z = "0";
        final String y = "1";
        tv2.setText(y);
        tvv.setText(z);
        tvm = (TextView)findViewById(R.id.tvmorning);
        tve = (TextView) findViewById(R.id.textView1);
        spinner=(ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);









        isInternetPresent = cd.isConnectingToInternet();

        // check for Internet status
        if (isInternetPresent) {
            // Internet Connection is Present
            // make HTTP requests
         //   getData();
        } else {
            // Internet connection is not present
            // Ask user to connect to Internet

            Toast.makeText(Home.this,"No Internet Connection, Please enable your internet connection",Toast.LENGTH_LONG).show();


        }

        final Location location = appLocationService.getLocation(LocationManager.GPS_PROVIDER);





        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
        else {
            showSettingsAlert();
        }
        tvAddress = (TextView)findViewById(R.id.textView);
        tvAddress.setVisibility(View.GONE);






// Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
// *** Create Folder
        createFolder();
        createFoldermorning();
        createFolderevening();
// *** ImageView
        imgView = (ImageView) findViewById(R.id.imgview123);
        imgView.setVisibility(View.GONE);
        ImageView camera = (ImageView)findViewById(R.id.camera123);





        ImageView cameraevening = (ImageView) findViewById(R.id.imageViewevening);
        cameraevening.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Location gpsLocation = appLocationService
                        .getLocation(LocationManager.GPS_PROVIDER);


                if (gpsLocation != null) {

                    double latitude = gpsLocation.getLatitude();
                    double longitude = gpsLocation.getLongitude();
                    String result = " Latitude: " + gpsLocation.getLatitude() +
                            " Longitude: " + gpsLocation.getLongitude();

                    tvAddress.setText(result);


                    xyz = tvAddress.getText().toString();





                    if(xyz != "null") {
                        photo_bill = 3;
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
// Ensure that there's a camera activity to handle the intent
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
// Create the File where the photo should go
                            File photoFile = null;
                            try {
                                photoFile = createImageFileevening();
                            } catch (IOException ex) {
                            }
// Continue only if the File was successfully created
                            if (photoFile != null) {
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

                            }
                        }
                    }

                }






                else  {

                    final Toast tag = Toast.makeText(getBaseContext(), "Please Wait Fetching your address",Toast.LENGTH_SHORT);

                    tag.show();

                    new CountDownTimer(21000, 1000)
                    {

                        public void onTick(long millisUntilFinished) {tag.show();}
                        public void onFinish() {tag.show();}

                    }.start();
                }







            }


        });

        ImageView cameramorning = (ImageView)findViewById(R.id.morning_button);


        cameramorning.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Location gpsLocation = appLocationService
                        .getLocation(LocationManager.GPS_PROVIDER);


                if (gpsLocation != null) {

                    double latitude = gpsLocation.getLatitude();
                    double longitude = gpsLocation.getLongitude();
                    String result = " Latitude: " + gpsLocation.getLatitude() +
                            " Longitude: " + gpsLocation.getLongitude();

                    tvAddress.setText(result);


                    xyz = tvAddress.getText().toString();





                    if(xyz != "null") {
                    photo_bill = 2;
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
// Ensure that there's a camera activity to handle the intent
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
// Create the File where the photo should go
                            File photoFile = null;
                            try {
                                photoFile = createImageFilemorning();
                            } catch (IOException ex) {
                            }
// Continue only if the File was successfully created
                            if (photoFile != null) {
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

                            }
                        }
                    }

                }






                else  {

                    final Toast tag = Toast.makeText(getBaseContext(), "Please Wait Fetching your address",Toast.LENGTH_SHORT);

                    tag.show();

                    new CountDownTimer(21000, 1000)
                    {

                        public void onTick(long millisUntilFinished) {tag.show();}
                        public void onFinish() {tag.show();}

                    }.start();
                }







            }


        });



        Button logout = (Button) findViewById(R.id.buttonlogout);
        logout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);
                startActivity(new Intent(Home.this, Login.class));
                finish();
            }
        });


// *** Take Photo

// Perform action on click
        camera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {









                Location gpsLocation = appLocationService
                        .getLocation(LocationManager.GPS_PROVIDER);


                if (gpsLocation != null) {

                    double latitude = gpsLocation.getLatitude();
                    double longitude = gpsLocation.getLongitude();
                    String result = " Latitude: " + gpsLocation.getLatitude() +
                            " Longitude: " + gpsLocation.getLongitude();

                    tvAddress.setText(result);


                    xyz = tvAddress.getText().toString();





                    if(xyz != "null") {
                     photo_bill = 1;
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
// Ensure that there's a camera activity to handle the intent
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
// Create the File where the photo should go
                            File photoFile = null;
                            try {
                                photoFile = createImageFile();
                            } catch (IOException ex) {
                            }
// Continue only if the File was successfully created
                            if (photoFile != null) {
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

                            }
                        }
                    }

                }






                else  {

                    final Toast tag = Toast.makeText(getBaseContext(), "Please Wait Fetching your address",Toast.LENGTH_SHORT);

                    tag.show();

                    new CountDownTimer(21000, 1000)
                    {

                        public void onTick(long millisUntilFinished) {tag.show();}
                        public void onFinish() {tag.show();}

                    }.start();
                }







            }


        });

    }



    private File createImageFileevening()throws IOException {

// Create an image file name

        y =  tvAddress.getText().toString();
        String user123 = tv.getText().toString();
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date());

        String imageFileName = "JPEG_" + d + "_" + y + "_" + user123 + "_" + "evening_bill";

        y = "1";

        File storageDir = new File(strSDCardPathNamemorning);

        File image = File.createTempFile(imageFileName, /* prefix */
                ".jpg", /* suffix */

                storageDir /* directory */



        );


// Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void createFolderevening() {

        File folder = new File(strSDCardPathNameevening);
        try {
// Create folder
            if (!folder.exists()) {
                folder.mkdir();
            }
        } catch (Exception ex) {
        }
    }

    private File createImageFilemorning() throws IOException {

// Create an image file name

        y =  tvAddress.getText().toString();
        String user123 = tv.getText().toString();
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date());

        String imageFileName = "JPEG_" + d + "_" + y + "_" + user123 + "_" + "morning_bill";

        y = "1";

        File storageDir = new File(strSDCardPathNamemorning);

        File image = File.createTempFile(imageFileName, /* prefix */
                ".jpg", /* suffix */

                storageDir /* directory */



        );


// Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
            imgView.setImageBitmap(bitmap);

            isInternetPresent = cd.isConnectingToInternet();

            if(photo_bill == 1) {
                // check for Internet status
                if (isInternetPresent) {
                    // Internet Connection is Present
                    // make HTTP requests
                    new UploadAsync(Home.this).execute();
                } else {
                    // Internet connection is not present
                    // Ask user to connect to Internet
                    Toast.makeText(getBaseContext(), "No Internet Connection.Please Check Your Internet Connection", Toast.LENGTH_LONG).show();

                }
            }
            else if(photo_bill == 2){
                if (isInternetPresent) {
                    // Internet Connection is Present
                    // make HTTP requests
                    new UploadAsyncmorning(Home.this).execute();
                } else {
                    // Internet connection is not present
                    // Ask user to connect to Internet
                    Toast.makeText(getBaseContext(), "No Internet Connection.Please Check Your Internet Connection", Toast.LENGTH_LONG).show();

                }
            }
            else if(photo_bill == 3){
                if (isInternetPresent) {
                    // Internet Connection is Present
                    // make HTTP requests
                    new UploadAsyncevening(Home.this).execute();
                } else {
                    // Internet connection is not present
                    // Ask user to connect to Internet
                    Toast.makeText(getBaseContext(), "No Internet Connection.Please Check Your Internet Connection", Toast.LENGTH_LONG).show();

                }
            }



        }
    }
    private void createFoldermorning() {
        File folder = new File(strSDCardPathNamemorning);
        try {
// Create folder
            if (!folder.exists()) {
                folder.mkdir();
            }
        } catch (Exception ex) {
        }
    }

    public static void clearFoldermorning(){
        File dir = new File(strSDCardPathNamemorning);
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int n = 0; n < children.length; n++)
            {
                new File(dir, children[n]).delete();
            }
        }
    }


    private void getData() {
        String id = tv.getText().toString();
        if (id.equals("")) {
            Toast.makeText(this, "Please enter your Email", Toast.LENGTH_LONG).show();
            return;
        }
        loading = ProgressDialog.show(this,"Please wait...","Fetching...",false,false);

        String url = Config.DATA_URL+id;

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Home.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response){
        String name="";
        String address="";
        String vc = "";
        String credit = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
            JSONObject collegeData = result.getJSONObject(0);
            name = collegeData.getString(Config.KEY_NAME);
            address = collegeData.getString(Config.KEY_ADDRESS);
            vc = collegeData.getString(Config.KEY_VC);
            credit = collegeData.getString(Config.KEY_CREDIT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tv1.setText(name);
        tvm.setText(address);
        tve.setText(vc);
        tvcredit.setText(credit);
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                Home.this);
        alertDialog.setTitle("SETTINGS");
        alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        Home.this.startActivity(intent);
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    public class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            tvAddress.setText(locationAddress);

            y =  tvAddress.getText().toString();
        }
    }

// Upload Image in Background

    public class UploadAsync extends AsyncTask<String, Void, Void> {



// ProgressDialog

        //  private ProgressDialog mProgressDialog;



        public UploadAsync(Home activity) {

            //    mProgressDialog = new ProgressDialog(activity);

//            mProgressDialog.setMessage("Uploading please wait.....");

            //          mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            //        mProgressDialog.setCancelable(false);



        }



        protected void onPreExecute() {

            super.onPreExecute();

            //  mProgressDialog.show();

            spinner.setVisibility(View.VISIBLE);
            tvv.setVisibility(View.GONE);

        }



        @Override

        protected Void doInBackground(String... par) {



// *** Upload all file to Server

            File file = new File(strSDCardPathName);

            File[] files = file.listFiles();

            for (File sfil : files) {

                if (sfil.isFile()) {

                    uploadFiletoServer(sfil.getAbsolutePath(), strURLUpload);

                }

            }



//*** Clear Folder

            clearFolder();



            return null;

        }



        protected void onPostExecute(Void unused) {




            isInternetPresent = cd.isConnectingToInternet();

            // check for Internet status
            if (isInternetPresent) {

                //   mProgressDialog.dismiss();
                spinner.setVisibility(View.GONE);
                tvv.setVisibility(View.VISIBLE);


                Toast.makeText(getBaseContext(), "Photo Uploaded.", Toast.LENGTH_LONG).show();
            }

            else{
                Toast.makeText(getBaseContext(), "Failed.", Toast.LENGTH_LONG).show();
                spinner.setVisibility(View.GONE);
            }



        }



    }



    private File createImageFile() throws IOException {

// Create an image file name

        y =  tvAddress.getText().toString();
        String user123 = tv.getText().toString();
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date());

        String imageFileName = "JPEG_" + d + "_" + y + "_" + user123 + "_";








        y = "1";

        File storageDir = new File(strSDCardPathName);

        File image = File.createTempFile(imageFileName, /* prefix */
                ".jpg", /* suffix */

                storageDir /* directory */



        );


// Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public static boolean uploadFiletoServer(String strSDPath, String strUrlServer) {

        int bytesRead, bytesAvailable, bufferSize;

        byte[] buffer;

        int maxBufferSize = 1 * 1024 * 1024;

        int resCode = 0;

        String resMessage = "";

        String lineEnd = "\r\n";

        String twoHyphens = "--";

        String boundary = "*****";

        try {

            File file = new File(strSDPath);
            if (!file.exists()) {
                return false;
            }
            FileInputStream fileInputStream = new FileInputStream(new File(strSDPath));
            URL url = new URL(strUrlServer);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes(
                    "Content-Disposition: form-data; name=\"filUpload\";filename=\"" + strSDPath + "\"" + lineEnd);
            outputStream.writeBytes(lineEnd);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
// Read file
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }
            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
// Response Code and Message
            resCode = conn.getResponseCode();
            if (resCode == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int read = 0;
                while ((read = is.read()) != -1) {
                    bos.write(read);
                }
                byte[] result = bos.toByteArray();
                bos.close();
                resMessage = new String(result);
            }
            fileInputStream.close();
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (Exception ex) {
// Exception handling
            return false;
        }
    }
    public static void createFolder() {
        File folder = new File(strSDCardPathName);
        try {
// Create folder
            if (!folder.exists()) {
                folder.mkdir();
            }
        } catch (Exception ex) {
        }
    }
    public static void clearFolder(){
        File dir = new File(strSDCardPathName);
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int n = 0; n < children.length; n++)
            {
                new File(dir, children[n]).delete();
            }
        }
    }

    public class UploadAsyncmorning extends AsyncTask<String, Void, Void> {



// ProgressDialog

        //  private ProgressDialog mProgressDialog;



        public UploadAsyncmorning(Home activity) {

            //    mProgressDialog = new ProgressDialog(activity);

//            mProgressDialog.setMessage("Uploading please wait.....");

            //          mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            //        mProgressDialog.setCancelable(false);



        }



        protected void onPreExecute() {

            super.onPreExecute();

            //  mProgressDialog.show();

            spinner.setVisibility(View.VISIBLE);
            tvv.setVisibility(View.GONE);

        }



        @Override

        protected Void doInBackground(String... par) {



// *** Upload all file to Server

            File file = new File(strSDCardPathNamemorning);

            File[] files = file.listFiles();

            for (File sfil : files) {

                if (sfil.isFile()) {

                    uploadFiletoServermorning(sfil.getAbsolutePath(), strURLUploadmorning);

                }

            }



//*** Clear Folder

            clearFoldermorning();



            return null;

        }



        protected void onPostExecute(Void unused) {




            isInternetPresent = cd.isConnectingToInternet();

            // check for Internet status
            if (isInternetPresent) {

                //   mProgressDialog.dismiss();
                spinner.setVisibility(View.GONE);
                tvv.setVisibility(View.VISIBLE);


                Toast.makeText(getBaseContext(), "Photo Uploaded.", Toast.LENGTH_LONG).show();
            }

            else{
                Toast.makeText(getBaseContext(), "Failed.", Toast.LENGTH_LONG).show();
                spinner.setVisibility(View.GONE);
            }



        }



    }

    public static boolean uploadFiletoServermorning(String strSDPath, String strUrlServer) {

        int bytesRead, bytesAvailable, bufferSize;

        byte[] buffer;

        int maxBufferSize = 1 * 1024 * 1024;

        int resCode = 0;

        String resMessage = "";

        String lineEnd = "\r\n";

        String twoHyphens = "--";

        String boundary = "*****";

        try {

            File file = new File(strSDPath);
            if (!file.exists()) {
                return false;
            }
            FileInputStream fileInputStream = new FileInputStream(new File(strSDPath));
            URL url = new URL(strUrlServer);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes(
                    "Content-Disposition: form-data; name=\"filUpload\";filename=\"" + strSDPath + "\"" + lineEnd);
            outputStream.writeBytes(lineEnd);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
// Read file
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }
            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
// Response Code and Message
            resCode = conn.getResponseCode();
            if (resCode == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int read = 0;
                while ((read = is.read()) != -1) {
                    bos.write(read);
                }
                byte[] result = bos.toByteArray();
                bos.close();
                resMessage = new String(result);
            }
            fileInputStream.close();
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (Exception ex) {
// Exception handling
            return false;
        }
    }

    public class UploadAsyncevening extends AsyncTask<String, Void, Void> {



// ProgressDialog

        //  private ProgressDialog mProgressDialog;



        public UploadAsyncevening(Home activity) {

            //    mProgressDialog = new ProgressDialog(activity);

//            mProgressDialog.setMessage("Uploading please wait.....");

            //          mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            //        mProgressDialog.setCancelable(false);



        }



        protected void onPreExecute() {

            super.onPreExecute();

            //  mProgressDialog.show();

            spinner.setVisibility(View.VISIBLE);
            tvv.setVisibility(View.GONE);

        }



        @Override

        protected Void doInBackground(String... par) {



// *** Upload all file to Server

            File file = new File(strSDCardPathNameevening);

            File[] files = file.listFiles();

            for (File sfil : files) {

                if (sfil.isFile()) {

                    uploadFiletoServerevening(sfil.getAbsolutePath(), strURLUploadevening);

                }

            }



//*** Clear Folder

            clearFolderevening();



            return null;

        }



        protected void onPostExecute(Void unused) {




            isInternetPresent = cd.isConnectingToInternet();

            // check for Internet status
            if (isInternetPresent) {

                //   mProgressDialog.dismiss();
                spinner.setVisibility(View.GONE);
                tvv.setVisibility(View.VISIBLE);


                Toast.makeText(getBaseContext(), "Photo Uploaded.", Toast.LENGTH_LONG).show();
            }

            else{
                Toast.makeText(getBaseContext(), "Failed.", Toast.LENGTH_LONG).show();
                spinner.setVisibility(View.GONE);
            }



        }



    }

    public static boolean uploadFiletoServerevening(String strSDPath, String strUrlServer) {

        int bytesRead, bytesAvailable, bufferSize;

        byte[] buffer;

        int maxBufferSize = 1 * 1024 * 1024;

        int resCode = 0;

        String resMessage = "";

        String lineEnd = "\r\n";

        String twoHyphens = "--";

        String boundary = "*****";

        try {

            File file = new File(strSDPath);
            if (!file.exists()) {
                return false;
            }
            FileInputStream fileInputStream = new FileInputStream(new File(strSDPath));
            URL url = new URL(strUrlServer);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes(
                    "Content-Disposition: form-data; name=\"filUpload\";filename=\"" + strSDPath + "\"" + lineEnd);
            outputStream.writeBytes(lineEnd);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
// Read file
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }
            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
// Response Code and Message
            resCode = conn.getResponseCode();
            if (resCode == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int read = 0;
                while ((read = is.read()) != -1) {
                    bos.write(read);
                }
                byte[] result = bos.toByteArray();
                bos.close();
                resMessage = new String(result);
            }
            fileInputStream.close();
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (Exception ex) {
// Exception handling
            return false;
        }
    }
    private void clearFolderevening() {
        File dir = new File(strSDCardPathNameevening);
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int n = 0; n < children.length; n++)
            {
                new File(dir, children[n]).delete();
            }
        }
    }
}