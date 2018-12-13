package jp.co.mo.simplecamera;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ACTION_CAMERA = 100;

    private static final String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT > 23) {
            if (checkPermission(PERMISSIONS)) {
                requestPermissions(PERMISSIONS, REQUEST_ACTION_CAMERA);
                return;
            }
        }
        takePic();
    }

    private boolean checkPermission(String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean isSuccess = true;
        switch (requestCode) {
            case REQUEST_ACTION_CAMERA:
                for (int i : grantResults) {
                    if (i != PackageManager.PERMISSION_GRANTED) {
                        isSuccess = false;
                    }
                }
                if(isSuccess) {
                    takePic();
                }
            default:
                break;
        }
    }

    public void takePic(View view) {
        checkPermission();
    }

    private void takePic() {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_ACTION_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if(data == null) {
            return;
        }
        switch (requestCode) {
            case REQUEST_ACTION_CAMERA:
                Bundle b = data.getExtras();
                Bitmap bitmap = (Bitmap) b.get("data");
                ImageView imageView = this.findViewById(R.id.imgView);
                imageView.setImageBitmap(bitmap);

                break;
            default:
                break;
        }

    }
}
