/**
 * An Image Picker Plugin for Cordova/PhoneGap.
 */
package com.sofienvppp2;

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


public class VideoPicturePreviewPickerV2 extends CordovaPlugin {

    private CallbackContext callbackContext;
    private JSONObject params;
    boolean Is_multiSelect = false;
    boolean picture_selector = false;
    boolean video_selector = false;
    boolean display_video_time = false;
    boolean display_preview = false;
    int limit_Select = 5;


    public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        this.callbackContext = callbackContext;
        this.params = args.getJSONObject(0);
        if (this.params.has("limit_Select"))
            limit_Select = this.params.getInt("limit_Select");

        if (this.params.has("Is_multiSelect"))
            Is_multiSelect = this.params.getBoolean("Is_multiSelect");

        if (this.params.has("picture_selector"))
            picture_selector = this.params.getBoolean("picture_selector");

        if (this.params.has("video_selector"))
            video_selector = this.params.getBoolean("video_selector");

        if (this.params.has("display_video_time"))
            display_video_time = this.params.getBoolean("display_video_time");

        if (this.params.has("display_preview"))
            display_preview = this.params.getBoolean("display_preview");

        Context context = this.cordova.getActivity().getApplicationContext();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int read = context
                    .checkCallingOrSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            int write = context
                    .checkCallingOrSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (read != PackageManager.PERMISSION_GRANTED && write != PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(cordova.getActivity(), GettingPermissionsActivity.class);
                    if (this.cordova != null)
                        this.cordova.startActivityForResult((CordovaPlugin) this, intent, 0);


                    context.startActivity(intent);
            } else {
                if (action.equals("openPicker")) {
                    Intent intent = new Intent(cordova.getActivity(), VideoPicturePickerActivity.class);



                    intent.putExtra("Is_multiSelect", Is_multiSelect);
                    intent.putExtra("limit_Select", limit_Select);
                    intent.putExtra("picture_selector", picture_selector);
                    intent.putExtra("video_selector", video_selector);
                    intent.putExtra("display_video_time", display_video_time);
                    intent.putExtra("display_preview", display_preview);

                    if (this.cordova != null) {
                        this.cordova.startActivityForResult((CordovaPlugin) this, intent, 0);
                    }
                }
            }
        } else {
            if (action.equals("openPicker")) {
                Intent intent = new Intent(cordova.getActivity(), VideoPicturePickerActivity.class);


                intent.putExtra("Is_multiSelect", Is_multiSelect);
                intent.putExtra("limit_Select", limit_Select);
                intent.putExtra("picture_selector", picture_selector);
                intent.putExtra("video_selector", video_selector);
                intent.putExtra("display_video_time", display_video_time);
                intent.putExtra("display_preview", display_preview);

                if (this.cordova != null) {
                    this.cordova.startActivityForResult((CordovaPlugin) this, intent, 0);
                }
            }


        }

        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK && data != null) {
            String action ="";
            if(data.hasExtra("PermissionAction"))
             action = data.getStringExtra("PermissionAction");


            if(action.equals("PermissionOK") || action.equals("PermissionNOTOK") )
            {
                if(action.equals("PermissionOK") )
                {
                    Intent intent = new Intent(cordova.getActivity(), VideoPicturePickerActivity.class);
                    intent.putExtra("Is_multiSelect", Is_multiSelect);
                    intent.putExtra("limit_Select", limit_Select);
                    intent.putExtra("picture_selector", picture_selector);
                    intent.putExtra("video_selector", video_selector);
                    intent.putExtra("display_video_time", display_video_time);
                    intent.putExtra("display_preview", display_preview);

                    if (this.cordova != null)
                    {
                        this.cordova.startActivityForResult((CordovaPlugin) this, intent, 0);

                    }

                }
                if(action.equals("PermissionNOTOK") )
                {
                    this.callbackContext.error("no permission is given !");
                }
            }
else
            {


            ArrayList<String> type = data.getStringArrayListExtra("type");
            ArrayList<String> path = data.getStringArrayListExtra("path");
            JSONArray jArray = new JSONArray();
            JSONObject jResult = new JSONObject();

            for (int i = 0; i < type.size(); i++) {
                JSONObject jGroup = new JSONObject();

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
        }
        }
        else {
            this.callbackContext.error("No images selected");
        }


    }
}
