package com.psl.fantasy.league.season2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.psl.fantasy.league.season2.R;;
import com.psl.classes.Config;
import com.psl.transport.Connection;

import org.ksoap2.serialization.SoapObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.dft.onyxcamera.licensing.License.TAG;
import static com.psl.fantasy.league.season2.AgentLocator.MY_PERMISSIONS_REQUEST_LOCATION;


/**
 * Created by yAQOOB on 23/01/18.
 */


public class Profile extends Fragment {
    Bitmap image;
    String encoded_image;
    ImageView imgProfileImage;
    FileOutputStream fileOutputStream;
    FileInputStream fileInputStream;
    SharedPreferences sharedPreferences;
    String image_name = "local_image";
    String user_id;
    String base64ToSave;
    boolean is_changed = false;
    public static final int WRITE_REQUEST_CODE = 111;
    String f_name, l_name, m_number, email;
    Intent CropIntent;
    View mView;
    boolean IS_CHANGES_MADE = false;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mView = view;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
        }

        try {
            sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF, MODE_PRIVATE);
            String fb_image = sharedPreferences.getString(Config.PICTURE, "");
            user_id = sharedPreferences.getString(Config.USER_ID, "");
            String base64 = sharedPreferences.getString(Config.IMAGE_DATA, "");
            base64ToSave = base64;
            imgProfileImage = (ImageView) view.findViewById(R.id.ivUserImage);
            byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);

            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            if (decodedByte != null)
                imgProfileImage.setImageBitmap(decodedByte);

            String name = sharedPreferences.getString(Config.NAME, "");
            if (name.equals("")) {
                ((EditText) view.findViewById(R.id.txt_firstname)).setText(sharedPreferences.getString(Config.FIRST_NAME, ""));
                ((EditText) view.findViewById(R.id.txt_lastname)).setText(sharedPreferences.getString(Config.LAST_NAME, ""));
            } else {
                ((EditText) view.findViewById(R.id.txt_firstname)).setText(name);
            }

            ((EditText) view.findViewById(R.id.txt_mobilenumber)).setText(sharedPreferences.getString(Config.CELL_NO, ""));
            ((EditText) view.findViewById(R.id.txt_email)).setText(sharedPreferences.getString(Config.EMAIL, ""));

            checkCameraPermission();
            checkLocationPermission();
            //checkWritePermission();

        } catch (Exception e) {
            e.printStackTrace();
        }

        imgProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            WRITE_REQUEST_CODE);
                    //selectImage();
                } else {
                    //selectImage();
                    Log.e("DB", "PERMISSION GRANTED");
                }

                selectImage();

                  /*if(isStoragePermissionGranted())
                    {*/

                //}
                //checkWritePermission();

                /*if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ){
                    ActivityCompat.requestPermissions(getActivity(),new String[]{ android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},101);
                    selectImage();
                    return;
                }else*/
                {
                    // selectImage();
                }
            }
        });

        ((Button) view.findViewById(R.id.iv_save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {


                    f_name = ((EditText) mView.findViewById(R.id.txt_firstname)).getText().toString();
                    l_name = ((EditText) mView.findViewById(R.id.txt_lastname)).getText().toString();
                    m_number = ((EditText) mView.findViewById(R.id.txt_mobilenumber)).getText().toString();
                    email = ((EditText) mView.findViewById(R.id.txt_email)).getText().toString();

                    if (f_name.equals(sharedPreferences.getString(Config.FIRST_NAME, ""))
                            && l_name.equals(sharedPreferences.getString(Config.LAST_NAME, ""))
                            && m_number.equals(sharedPreferences.getString(Config.CELL_NO, ""))
                            && email.equals(sharedPreferences.getString(Config.EMAIL, ""))) {
                        if (is_changed) {

                        } else {
                            is_changed = false;
                        }
                    } else {
                        is_changed = true;
                    }

                    if (is_changed) {

                    } else {
                        Config.getAlert(getActivity(), "No change to save");
                        return;
                    }

                    if (f_name.equals("")) {
                        Config.getAlert(getActivity(), "Please enter first name");
                    } else if (l_name.equals("")) {
                        Config.getAlert(getActivity(), "Please enter last name");
                    } else if (m_number.equals("")) {
                        Config.getAlert(getActivity(), "Please enter mobile number");
                    } else if (email.equals("")) {
                        Config.getAlert(getActivity(), "Please enter email");
                    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        Config.getAlert(getActivity(), "Please enter a valid email address");
                    } else {
                        //if (is_changed)
                        new UploadUserData().execute();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    public boolean isStoragePermissionGranted() {
        boolean bool = false;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);

            if (Build.VERSION.SDK_INT >= 23) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Permission is granted");
                    bool = true;
                } else {

                    Log.v(TAG, "Permission is revoked");
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
                    bool = false;
                }
            } else { //permission is automatically granted on sdk<23 upon installation
                Log.v(TAG, "Permission is granted");
                bool = true;
            }
        }
        return bool;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectImage();
            Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
            //resume tasks needing this permission
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles 
        getActivity().setTitle("Menu 1");
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    private static final int REQUEST_WRITE_STORAGE = 112;

    public boolean checkWritePermission() {
        boolean bool = false;
        try {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Asking user if explanation is needed
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_WRITE_STORAGE);


                } else {
                    // No explanation needed, we can request the permission.
                    requestPermissions(
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_WRITE_STORAGE);
                }


                bool = false;
            } else if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Asking user if explanation is needed
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    requestPermissions(
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_WRITE_STORAGE);


                } else {
                    // No explanation needed, we can request the permission.
                    requestPermissions(
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_WRITE_STORAGE);
                }

                bool = false;
            } else {
                bool = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bool;
    }

    public boolean checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.CAMERA)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                requestPermissions(
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                requestPermissions(
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    if (checkCameraPermission()) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 0);
                    }
                } else if (items[item].equals("Choose from Library")) {
                    if (checkWritePermission()) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Select File"), 1);
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }

    @SuppressLint("NewApi")
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    try {
                        image = (Bitmap) imageReturnedIntent.getExtras().get("data");
                        if (image != null) {
                            BitmapDrawable myBackground = new BitmapDrawable(image);
                            //BitmapDrawable myBackground = new BitmapDrawable(cropAndGivePointedShape(image));
                            imgProfileImage.setImageDrawable(myBackground);
                            try {
                                getActivity().deleteFile(image_name);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            BitmapDrawable drawable = (BitmapDrawable) imgProfileImage.getDrawable();
                            Bitmap bitmap = drawable.getBitmap();
                            base64ToSave = encodeTobase64(bitmap);

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(Config.IMAGE_DATA, base64ToSave);
                            editor.commit();
                            is_changed = true;


                           /* ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                            objectOutputStream.writeObject(base64);
                            objectOutputStream.close();*/
                        } else {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                break;
            case 1:
                String file_name = "";
                if (resultCode == RESULT_OK) {

                    try {
                        Uri selectedImage = imageReturnedIntent.getData();
                        //ImageCropFunction(selectedImage);
                        String wholeID = DocumentsContract.getDocumentId(selectedImage);

                        // Split at colon, use second item in the array
                        String id = wholeID.split(":")[1];

                        String[] column = {MediaStore.Images.Media.DATA};

                        // where id is equal to
                        String sel = MediaStore.Images.Media._ID + "=?";

                        Cursor cursor = getActivity().getContentResolver().
                                query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        column, sel, new String[]{id}, null);

                        String filePath = "";

                        int columnIndex = cursor.getColumnIndex(column[0]);

                        if (cursor.moveToFirst()) {
                            filePath = cursor.getString(columnIndex);
                        }
                        cursor.close();
                        String sss = filePath;

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(filePath, options);
                        final int REQUIRED_SIZE = 200;
                        int scale = 1;
                        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                            scale *= 2;
                        options.inSampleSize = scale;
                        options.inJustDecodeBounds = false;
                        image = BitmapFactory.decodeFile(filePath, options);

                        BitmapDrawable myBackground = new BitmapDrawable(image);
                        //cropAndGivePointedShape(image);
                        imgProfileImage.setImageDrawable(myBackground);


                        BitmapDrawable drawable = (BitmapDrawable) imgProfileImage.getDrawable();
                        Bitmap bitmap = drawable.getBitmap();
                        base64ToSave = encodeTobase64(bitmap);
                    } catch (Exception e) {
                        Config.getAlert(getActivity(), "Selected picture is too large, please select another picture");
                        e.printStackTrace();
                    }

                    try {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Config.IMAGE_DATA, base64ToSave);
                        editor.commit();
                        is_changed = true;
                        //*ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                        /*objectOutputStream.writeObject(base64);
                        objectOutputStream.close();*//**//**/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;

            /*case 2:
            {

                if (imageReturnedIntent != null) {

                    Bundle bundle = imageReturnedIntent.getExtras();

                    Bitmap bitmap = bundle.getParcelable("data");

                    //imgProfileImage.setImageDrawable(myBackground);
                    imgProfileImage.setImageBitmap(bitmap);

                }
            }*/

        }
    }

 /*   public String getPath(Uri uri) {
        // just some safety built in
        try {
            if (uri == null) {
                // TODO perform some logging or show user feedback
                return null;
            }
            // try to retrieve the image from the media store first
            // this will only work for images selected from gallery
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
            if (cursor != null) {
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String path = cursor.getString(column_index);
                cursor.close();
                return path;
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        // this is our fallback here
        return uri.getPath();
    }*/


    private class UploadUserData extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;
        String mResult;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = ProgressDialog.show(getActivity(), "Saving data", "Please wait...");
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection connection = new Connection("saveImage2", getActivity());

                connection.addProperties("userid", user_id);
                connection.addProperties("imageBase64", base64ToSave);
                connection.addProperties("f_name", f_name);
                connection.addProperties("l_name", l_name);
                connection.addProperties("email", email);
                connection.addProperties("mobile", m_number);
                connection.addProperties("w_username", Config.w1);
                connection.addProperties("w_password", Config.w2);
                connection.ConnectForSingleNode();

                SoapObject object = connection.Result();
                mResult = object.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (pDialog != null)
                    pDialog.dismiss();

                if (mResult.contains("success")) {
                    Config.getAlert(getActivity(), "Profile Updated Successfully", "Success");

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Config.EMAIL, email);
                    editor.putString(Config.FIRST_NAME, f_name);
                    editor.putString(Config.LAST_NAME, l_name);
                    editor.putString(Config.CELL_NO, m_number);
                    editor.commit();

                    is_changed = false;
                } else {
                    Config.getAlert(getActivity(), "Error while updating profile", "Success");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Config.getAlert(getActivity(), "Error while updating profile", "Success");
            }

        }
    }

    private Bitmap cropAndGivePointedShape(Bitmap originalBitmap) {
        Bitmap bmOverlay = Bitmap.createBitmap(originalBitmap.getWidth(),
                originalBitmap.getHeight(),
                Bitmap.Config.ARGB_8888);

        Paint p = new Paint();
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(originalBitmap, 0, 0, null);
        canvas.drawRect(0, 0, 20, 20, p);

        Point a = new Point(0, 20);
        Point b = new Point(20, 20);
        Point c = new Point(0, 40);

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.lineTo(b.x, b.y);
        path.lineTo(c.x, c.y);
        path.lineTo(a.x, a.y);
        path.close();

        canvas.drawPath(path, p);

        a = new Point(0, 40);
        b = new Point(0, 60);
        c = new Point(20, 60);

        path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.lineTo(b.x, b.y);
        path.lineTo(c.x, c.y);
        path.lineTo(a.x, a.y);
        path.close();

        canvas.drawPath(path, p);

        canvas.drawRect(0, 60, 20, originalBitmap.getHeight(), p);

        return bmOverlay;
    }

    public void ImageCropFunction(Uri uri) {

        // Image Crop Code
        try {

            CropIntent = new Intent("com.android.camera.action.CROP");

            CropIntent.setDataAndType(uri, "image/*");

            CropIntent.putExtra("crop", "true");
            CropIntent.putExtra("outputX", 180);
            CropIntent.putExtra("outputY", 180);
            CropIntent.putExtra("aspectX", 3);
            CropIntent.putExtra("aspectY", 4);
            CropIntent.putExtra("scaleUpIfNeeded", true);
            CropIntent.putExtra("return-data", true);

            startActivityForResult(CropIntent, 2);


        } catch (ActivityNotFoundException e) {

        }
    }

    String imageFilePath;
    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }

}