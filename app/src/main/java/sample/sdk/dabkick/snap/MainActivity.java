package sample.sdk.dabkick.snap;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;


import com.dabkick.videosdk.publicsettings.DabKick;
import com.dabkick.videosdk.publicsettings.DabKickMedia;
import com.dabkick.videosdk.publicsettings.DabKickPhoto;
import com.dabkick.videosdk.publicsettings.DabKickVideoButton;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public RelativeLayout mDisplayView, mainActivity, cameraContrlLayt ;
    DabKickVideoButton watchTogether;
    RecyclerView photosList;
    LSCameraPreview mCameraPreview;
    boolean cameraGranted = true;
    ImageView mCameraSnapBtn, mCameraFlipBtn, mCameraFlashBtn;
    ImageView mResultView;
    String localImagePath;
    DabKickMedia photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        cameraContrlLayt.setVisibility(View.GONE);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(
                this, 2, LinearLayoutManager.VERTICAL, false);
        photosList.setLayoutManager(layoutManager);

        ArrayList<DabKickMedia> newList = new ArrayList<>(Util.getAllPhotos());
        CategoriesAdapter adapter = new CategoriesAdapter(this, newList, new CategoriesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DabKickMedia photoInfo) {

                localImagePath = photoInfo.getUrl();
                photo = photoInfo;
            }
        });

        photosList.setAdapter(adapter);

        //cameraSetup();

        watchTogether.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                watchWithFriends();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0: {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CAMERA)) {
                    Util.showUserDeniedDialog("NO CAMERA ACCESS ", "Uh oh! Looks like you have denied " +
                            "the camera permission. we can't use camera to take pictire", MainActivity.this, Manifest.permission.CAMERA);
                } else {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        cameraGranted = true;
                        mDisplayView.addView(mCameraPreview);
                    } else {
                        Util.showSettingsDialog(" NO CAMERA ACCESS ", "Uh oh! Looks like we can't use camera to take picture. " +
                                "This app camera is disabled until you give DabKick permission to use your camera", this);
                    }
                }
                return;
            }
        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    void init() {

        cameraContrlLayt = (RelativeLayout) findViewById(R.id.cameraCtrlLayout);
        mCameraSnapBtn = (ImageView) findViewById(R.id.capture_Img);
        mCameraFlipBtn = (ImageView) findViewById(R.id.flip_camera);
        mCameraFlashBtn = (ImageView) findViewById(R.id.camera_flash);
        mainActivity = (RelativeLayout) findViewById(R.id.main_layout);
        watchTogether = (DabKickVideoButton) findViewById(R.id.watch_together);
        photosList = (RecyclerView) findViewById(R.id.photo_list);

        watchTogether.setTag("back");

    }

    void cameraSetup() {
        //setup the camera

        mDisplayView = new RelativeLayout(this);
        mDisplayView.setBackgroundColor(Color.BLACK);

        int h = (int) (Util.getScreenHeight(this) * 0.61 - Util.convertDpToPixel(this, 70));
        int w = (int) (h / 16.0 * 9);


        RelativeLayout.LayoutParams frameLayoutParams = new RelativeLayout.LayoutParams(w, h);
        frameLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        frameLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        frameLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        frameLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        frameLayoutParams.addRule(Gravity.CENTER);
        mDisplayView.setLayoutParams(frameLayoutParams);

        final ProgressBar progressBar = new ProgressBar(MainActivity.this);
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        param.addRule(RelativeLayout.CENTER_IN_PARENT);
        progressBar.setLayoutParams(param);
        progressBar.setTag(100);
        progressBar.setVisibility(View.VISIBLE);
        mDisplayView.addView(progressBar);

        mResultView = new ImageView(MainActivity.this);
        mResultView.setTag("back");
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w, h);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(Gravity.CENTER);
        mResultView.setLayoutParams(params);
        mResultView.setBackgroundColor(Color.BLACK);
        mDisplayView.addView(mResultView);

        mCameraPreview = new LSCameraPreview(this, mDisplayView);

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            cameraGranted = false;
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    0);
        } else {
            cameraGranted = true;
            mDisplayView.addView(mCameraPreview);
        }


        mainActivity.addView(mDisplayView);
        cameraContrlLayt.setVisibility(View.VISIBLE);
        cameraContrlLayt.bringToFront();

        //camera setup end

        int num = mCameraPreview.getNumOfCamera();
        if (num <= 1)
            mCameraFlipBtn.setVisibility(View.INVISIBLE);

        mCameraFlipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mCameraFlipBtn.setClickable(false);
                try {
                    mCameraPreview.setFlashOff();
                    mCameraFlashBtn.setImageResource(R.drawable.flash_off_v70);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (mCameraPreview.isFrontCamera()) {
                    mCameraPreview.switchToBackCamera();
                    mCameraFlashBtn.setVisibility(View.VISIBLE);
                    mCameraFlipBtn.setImageResource(R.drawable.camera_flip);
                } else {
                    mCameraPreview.switchToFrontCamera();
                    mCameraFlashBtn.setVisibility(View.INVISIBLE);
                    mCameraFlipBtn.setImageResource(R.drawable.camera_flip);
                }
                mCameraFlipBtn.setClickable(true);
            }
        });

        boolean hasFlash = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if (!hasFlash)
            mCameraFlashBtn.setVisibility(View.INVISIBLE);

        mCameraFlashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraFlashBtn.setClickable(false);
                if (mCameraPreview.hasFlash()) {
                    if (mCameraPreview.switchFlashMode()) {
                        mCameraFlashBtn.setImageResource(R.drawable.flash_off_v70);
                    } else {
                        mCameraFlashBtn.setImageResource(R.drawable.flash_on_v70);
                    }
                }
                mCameraFlashBtn.setClickable(true);

            }
        });
        //if the camera is front then dont show the flash symbol.
        if (mCameraPreview.isFrontCamera()) {
            mCameraFlashBtn.setVisibility(View.INVISIBLE);
        }

        mCameraSnapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LSCameraPreview.isSafeToTakePic == true) {
                    mCameraPreview.setOnCapturedListener(new LSCameraPreview.OnCapturedListener() {
                        @Override
                        public void onCaptured(final Bitmap resultBitmap) {


                            final String filename = "DabkickSDK.jpg";
                            File filesDir = MainActivity.this.getCacheDir();
                            final String imageFullPath = filesDir + "/" + filename;

                            localImagePath = imageFullPath;
                            Bitmap capturedImage = resultBitmap;

                            ImageView imageView = new ImageView(MainActivity.this);
                            imageView.setLayoutParams(new ViewGroup.LayoutParams(100, 100));

                            //imageView.setImageBitmap(resultBitmap);

                            Util.adjustToFillParant(resultBitmap, imageView, MainActivity.this);
                            final Bitmap bitmap = Bitmap.createScaledBitmap(resultBitmap, imageView.getLayoutParams().width, imageView.getLayoutParams().height, false);
                            resultBitmap.recycle();

                            mResultView.setImageBitmap(bitmap);
                            mResultView.bringToFront();



                            cameraContrlLayt.setVisibility(View.GONE);
                            watchTogether.setVisibility(View.VISIBLE);
                            watchTogether.setTag("front");
                            watchTogether.bringToFront();
                        }
                    });
                    mCameraPreview.capture();
                    LSCameraPreview.isSafeToTakePic = false;
                }

            }

        });
    }

    void watchWithFriends() {

        //DabKickPhoto photo = new DabKickPhoto(localImagePath);

        DabKick.setMediaProvider(Util.createDabKickMediaProvider(photo));
        DabKick.watchWithFriends(MainActivity.this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {

            watchTogether.setVisibility(View.GONE);
            mDisplayView.bringToFront();
            cameraContrlLayt.bringToFront();
            cameraContrlLayt.setVisibility(View.VISIBLE);

            mCameraPreview.bringToFront();
            mCameraPreview.resumePreview();

            watchTogether.setTag("back");

        }
    }

    @Override
    public void onBackPressed() {
        if (watchTogether.getTag() == "front") {

            watchTogether.setVisibility(View.GONE);
            mDisplayView.bringToFront();
            cameraContrlLayt.bringToFront();
            cameraContrlLayt.setVisibility(View.VISIBLE);

            mCameraPreview.bringToFront();
            mCameraPreview.resumePreview();

            watchTogether.setTag("back");

        } else {

            super.onBackPressed();
        }
    }
}
