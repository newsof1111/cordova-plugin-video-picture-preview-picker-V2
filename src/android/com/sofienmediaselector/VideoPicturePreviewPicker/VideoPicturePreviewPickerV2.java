/**
 * An Image Picker Plugin for Cordova/PhoneGap.
 */
package com.sofienmediaselector;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import android.content.pm.PackageManager;
import android.os.Build;
import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.util.Log;


import com.sofienmediaselector.ImageOrVideoItem;
import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.VideoView;



public class VideoPicturePreviewPickerV2 extends CordovaPlugin {

	private CallbackContext callbackContext;
	private JSONObject params;

	public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
		 this.callbackContext = callbackContext;
		 this.params = args.getJSONObject(0);

		 Context context = this.cordova.getActivity().getApplicationContext();

		 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int read = context
				.checkCallingOrSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            int write = context
				.checkCallingOrSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (read != PackageManager.PERMISSION_GRANTED && write != PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(cordova.getActivity(), GettingPermissionsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else {
				if (action.equals("openPicker")) {
					Intent intent = new Intent(cordova.getActivity(), VideoPicturePickerActivity.class);
					boolean Is_multiSelect = false;
					int limit_Select = 5;
					if (this.params.has("limit_Select")) {
						limit_Select = this.params.getInt("limit_Select");
					}
					if (this.params.has("Is_multiSelect")) {
						Is_multiSelect= this.params.getBoolean("Is_multiSelect");

					}
					
					intent.putExtra("Is_multiSelect", Is_multiSelect);
					intent.putExtra("limit_Select", limit_Select);

					if (this.cordova != null) {
						this.cordova.startActivityForResult((CordovaPlugin) this, intent, 0);
					}
				}
			}
        } else {
			if (action.equals("openPicker")) {
				Intent intent = new Intent(cordova.getActivity(), VideoPicturePickerActivity.class);
				boolean Is_multiSelect = false;
				int limit_Select = 5;
					if (this.params.has("limit_Select")) {
						limit_Select = this.params.getInt("limit_Select");
					}
					if (this.params.has("Is_multiSelect")) 
						Is_multiSelect= this.params.getBoolean("Is_multiSelect");
				intent.putExtra("Is_multiSelect", Is_multiSelect);
				intent.putExtra("limit_Select", limit_Select);

				if (this.cordova != null) {
					this.cordova.startActivityForResult((CordovaPlugin) this, intent, 0);
				}
			}

			
		}
		
		return true;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	
		if (resultCode == Activity.RESULT_OK && data != null) {
			ArrayList<String> type = data.getStringArrayListExtra("type");
			ArrayList<String> path = data.getStringArrayListExtra("path");
        JSONArray jArray = new JSONArray();// /ItemDetail jsonArray
        JSONObject jResult = new JSONObject();// main object

        for (int i = 0; i < type.size(); i++) {
            JSONObject jGroup = new JSONObject();// /sub Object

           
            try {
                jGroup.put("type", type.get(i));
                jGroup.put("path", path.get(i));
               

                jArray.put(jGroup);

                // /itemDetail Name is JsonArray Name
                jResult.put("selectedMedia", jArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
			this.callbackContext.success(jResult);
		} else {
			this.callbackContext.error("No images selected");
		}
		
					this.callbackContext.success("success");

	}
}
