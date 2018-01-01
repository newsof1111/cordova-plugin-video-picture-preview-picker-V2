package com.sofienvppp2;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;

public class GettingPermissionsActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int read = checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            int write = checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (read != PackageManager.PERMISSION_GRANTED && write != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{
                                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                        },
                        0
                );
            } else
                GettingPermissionsActivity.this.finish();
        } else
            GettingPermissionsActivity.this.finish();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Intent returnIntent = new Intent();
        Bundle res = new Bundle();
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            res.putString("PermissionAction", "PermissionOK");
        else
            res.putString("PermissionAction", "PermissionNOTOK");

        returnIntent.putExtras(res);
        setResult(RESULT_OK, returnIntent);
        GettingPermissionsActivity.this.finish();


    }
}
