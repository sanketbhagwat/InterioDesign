package com.example.interiodesign;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class imageGallery {
    @SuppressLint("Range")
    public static ArrayList<String> listOfImages(Context context) {
//        Uri uri;
//        Cursor cursor;
//        int column_index_data, column_index_folder_name;
        ArrayList<String> listOfAllImages = new ArrayList<>();
//        String absolutePathOfImages;
//        uri= MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//
//        String[]projection={MediaStore.MediaColumns.DATA,MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
//        Toast.makeText(context.getApplicationContext(), "herree", Toast.LENGTH_SHORT).show();
//        String orderBy=MediaStore.Video.Media.DATE_TAKEN;
//        cursor = context.getContentResolver().query(uri,projection,null,null,orderBy+" DESC");
//        column_index_data=cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
////        column_index_folder_name=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
//
//        while (cursor.moveToNext()){
//            absolutePathOfImages= cursor.getString(column_index_data);
//            Log.e("imagepath", absolutePathOfImages);
//            lisOfAllImages.add(absolutePathOfImages);
//        }

        String uri = MediaStore.Images.Media.DATA;
        // if GetImageFromThisDirectory is the name of the directory from which image will be retrieved
        String condition = uri + " like '%/interiodesign/%'";
        String[] projection = {uri, MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media.SIZE};
        try {

            Cursor cursor = context.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                    condition, null, null);
            if (cursor != null) {
                boolean isDataPresent = cursor.moveToFirst();
                if (isDataPresent) {
                    do {
                        Toast.makeText(context.getApplicationContext(), "hereee", Toast.LENGTH_SHORT).show();
                        Log.e("imagepath", cursor.getString(cursor.getColumnIndex(uri)));
//                        Picasso.get().load(cursor.getString(cursor.getColumnIndex(uri))).into(img1);
                        listOfAllImages.add(cursor.getString(cursor.getColumnIndex(uri)));

                    } while (cursor.moveToNext());
                }
                if (cursor != null) {
                    cursor.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


      return listOfAllImages;
    }
    @SuppressLint("Range")
    public static ArrayList<String>listOfVideos(Context context){
        String uri = MediaStore.Video.Media.DATA;
        ArrayList<String>listOfAllVideos=new ArrayList<>();

        // if GetImageFromThisDirectory is the name of the directory from which image will be retrieved
        String condition = uri + " like '%/interiodesign/%'";
        String[] projection = new String[]{uri, MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.SIZE};
        try {

            Cursor cursor = context.getContentResolver().query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection,
                    condition, null, null);
            if (cursor != null) {
                boolean isDataPresent = cursor.moveToFirst();
                if (isDataPresent) {
                    do {
                        Toast.makeText(context.getApplicationContext(), "hereee", Toast.LENGTH_SHORT).show();
                        Log.e("imagepath", cursor.getString(cursor.getColumnIndex(uri)));
//                        Picasso.get().load(cursor.getString(cursor.getColumnIndex(uri))).into(img1);
//                        listOfAllImages.add(cursor.getString(cursor.getColumnIndex(uri)));
                        listOfAllVideos.add(cursor.getString(cursor.getColumnIndex(uri)));

                    } while (cursor.moveToNext());
                }
                if (cursor != null) {
                    cursor.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }




        return listOfAllVideos;

    }
}

