package sample.sdk.dabkick.snap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.util.List;

/**
 * Created by iFocus on 02-04-2018.
 */

public class LSCameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    SurfaceHolder mHolder;
    boolean mIsFrontCamera = true;
    boolean mIsFromRestart = false;
    RelativeLayout mDisplayView;
    public static boolean mIsFlashOn = false;
    public static boolean isSafeToTakePic = true;
    int mRotateAngle;
    float mDiff;
    float mStartY;

    @SuppressWarnings("deprecation")
    static Camera mCamera = null;
    Activity mActivity;

    @SuppressWarnings("deprecation")
    public LSCameraPreview(Activity activity, RelativeLayout displayView) {
        super(activity);
        mHolder = getHolder();
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mHolder.addCallback(this);
        mDisplayView = displayView;
        mActivity = activity;

    }

    @Override
    @SuppressWarnings("deprecation")
    public void surfaceCreated(SurfaceHolder holder) {

        int cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;

        try {
            mCamera = Camera.open(cameraId);
            mIsFrontCamera = true;
        } catch (Exception e) {
            e.printStackTrace();

            cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
            try {
                mCamera = Camera.open(cameraId);
                mIsFrontCamera = false;
            } catch (Exception ex) {
                ex.printStackTrace();

                mIsFromRestart = false;

            }
        }

        setFlashAuto();

        try {
            setCameraDisplayOrientation(mActivity, cameraId, mCamera);
            mCamera.setPreviewDisplay(mHolder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    public void restartCamera() {
        int cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;

        mIsFromRestart = true;

        try {
            mCamera = Camera.open(cameraId);
        } catch (Exception e) {
            cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;

            try {
                mCamera = Camera.open(cameraId);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        try {
            mCamera.setPreviewDisplay(mHolder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        if (mCamera != null) {
            //mCamera.setDisplayOrientation(90);

            Camera.Parameters parameters = mCamera.getParameters();
            Camera.Size s = getBestPreviewSize(parameters.getSupportedPreviewSizes(), width, height);
            // s = parameters.getSupportedPreviewSizes().get(0);
            parameters.setPreviewSize(s.width, s.height);
            mCamera.setParameters(parameters);

//            if (mDisplayView.getLayoutParams().width < s.width){
//                mDisplayView.getLayoutParams().height = (int)((float)mDisplayView.getLayoutParams().width/s.width*s.height);
//            }
//            else
//            {
            int w = mDisplayView.getLayoutParams().width;
            int h = mDisplayView.getLayoutParams().height;

            mDisplayView.getLayoutParams().width = (int) (h * (float) s.height / s.width);
            mDisplayView.requestLayout();
//            mDisplayView.getLayoutParams().width = s.height;
//            mDisplayView.getLayoutParams().height = s.width;
            //mDisplayView.requestLayout();
//            if (Math.abs((float)h/w - (float)s.width/s.height) > 0.05)
//            {
//                mDisplayView.getLayoutParams().width = (int)(h * (float)s.height/s.width);
//                mDisplayView.requestLayout();
//            }


            Log.d("yuan_camera", "optimize:" + s.height + " " + s.width + " " + (float) s.width / s.height);
            Log.d("yuan_camera", "original:" + w + " " + h + " " + (float) h / w);
            Log.d("yuan_camera", "preview:" + mDisplayView.getLayoutParams().width + " " + mDisplayView.getLayoutParams().height + " " + (float) mDisplayView.getLayoutParams().height / mDisplayView.getLayoutParams().width);
            //Log.d("yuan_camera","optimize choices:"+parameters.getSupportedPreviewSizes().toString());

            try {
                mCamera.startPreview();
                isSafeToTakePic = true;
            } catch (Exception e) {
                mCamera.release();
                mCamera = null;
                e.printStackTrace();
            }
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }

    }

    public void releaseCamera(){
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @SuppressWarnings("deprecation")
    public void capture() {

        if (mCamera == null)
            return;

        try {
            mCamera.takePicture(null, null, save_picture_in_gallery);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    Camera.PictureCallback save_picture_in_gallery = new Camera.PictureCallback() {

        public void onPictureTaken(byte[] arg0, Camera arg1) {

            if (mCamera != null) {
                mCamera.startPreview();
                LSCameraPreview.isSafeToTakePic = true;
            }

            Bitmap bmp = BitmapFactory.decodeByteArray(arg0, 0, arg0.length);

            //Bitmap bmp = Bitmap.createBitmap(bitmapPicture, 0, 0, bitmapPicture.getWidth(), bitmapPicture.getHeight(), null, false);
            //bitmapPicture.recycle();

            if (mIsFrontCamera) {
                bmp = rotate(bmp, mRotateAngle, true);
            } else
                bmp = rotate(bmp, mRotateAngle, false);

            if (mCapturedListener != null) {
                mCapturedListener.onCaptured(bmp);
                mCapturedListener = null;
            }

            //Shwetha End
        }
    };


    public void resumePreview() {
        if (mCamera == null)
            return;
        //mCamera.setDisplayOrientation(90);
        mCamera.startPreview();
    }

    @SuppressWarnings("deprecation")
    void setFlashAuto() {

        if (!hasFlash() || mCamera == null)
            return;
        Camera.Parameters params = mCamera.getParameters();
        params.setFlashMode(params.FLASH_MODE_AUTO);
        mCamera.setParameters(params);
    }

    public boolean hasFlash() {
        if (mCamera == null) {
            return false;
        }

        Camera.Parameters parameters = mCamera.getParameters();

        if (parameters.getFlashMode() == null) {
            return false;
        }

        List<String> supportedFlashModes = parameters.getSupportedFlashModes();
        if (supportedFlashModes == null || supportedFlashModes.isEmpty() || supportedFlashModes.size() == 1 && supportedFlashModes.get(0).equals(Camera.Parameters.FLASH_MODE_OFF)) {
            return false;
        }

        return true;
    }

    @SuppressWarnings("deprecation")
    public boolean switchFlashMode() {

        if (mIsFlashOn) {
            setFlashOff();
            return true;
        } else {
            setFlashOn();
            return false;
        }
    }

    public int getNumOfCamera() {
        return Camera.getNumberOfCameras();
    }

    @SuppressWarnings("deprecation")
    public void setFlashOn() {
        mIsFlashOn = true;
        if (mCamera != null) {
            Camera.Parameters params = mCamera.getParameters();
            params.setFlashMode(params.FLASH_MODE_ON);
            mCamera.setParameters(params);
        }
    }

    @SuppressWarnings("deprecation")
    public void setFlashOff() {
        mIsFlashOn = false;
        if (mCamera != null) {
            Camera.Parameters params = mCamera.getParameters();
            params.setFlashMode(params.FLASH_MODE_OFF);
            mCamera.setParameters(params);
        }
    }

    @SuppressWarnings("deprecation")
    public void switchToFrontCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
        }

        int cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        mCamera = Camera.open(cameraId);
        try {
            //this step is critical or preview on new camera will no know where to render to
            mCamera.setPreviewDisplay(mHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mIsFrontCamera = true;

        //mCamera.setDisplayOrientation(90);
        setCameraDisplayOrientation(mActivity, cameraId, mCamera);
        mCamera.startPreview();
    }

    @SuppressWarnings("deprecation")
    public void switchToBackCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
        }

        int cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;

        try {
            mCamera = Camera.open(cameraId);
            //this step is critical or preview on new camera will no know where to render to
            mCamera.setPreviewDisplay(mHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mIsFrontCamera = false;

        //  mCamera.setDisplayOrientation(90);
        setCameraDisplayOrientation(mActivity, cameraId, mCamera);
        mCamera.startPreview();
    }


//    //vallabh added
//    //when camera came back from the onResume() in live chat it suppose to crash if we use the swithToBackCamera()
//    //because we are releasing it before we initialize the camera in Live Sesion.
//    public void switchToBackCameraWhenInOnResume() {
//        int cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
//        mCamera = Camera.open(cameraId);
//        try {
//            //this step is critical or preview on new camera will no know where to render to
//            mCamera.setPreviewDisplay(mHolder);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        mIsFrontCamera = false;
//
//        mCamera.setDisplayOrientation(90);
//        mCamera.startPreview();
//    }

    public boolean isFrontCamera() {
        return mIsFrontCamera;
    }

    @SuppressWarnings("deprecation")
    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.05;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    private Camera.Size getBestPreviewSize(List<Camera.Size> sizes, int width, int height) {
        Camera.Size result = null;
        for (Camera.Size size : sizes) {
//            if (size.width <= height) {
            if (result == null) {
                result = size;
            } else {
                int resultArea = result.width * result.height;
                int newArea = size.width * size.height;

                if (resultArea < newArea) {
                    result = size;
                }
            }
//            }
        }
        return result;

    }


    public Bitmap rotate(Bitmap in, int angle, boolean mirror) {
        Matrix mat = new Matrix();

        if (mirror)
            mat.preScale(-1, 1);

        mat.postRotate(angle);
        return Bitmap.createBitmap(in, 0, 0, in.getWidth(), in.getHeight(), mat, true);
    }

    OnCapturedListener mCapturedListener = null;

    //Listener
    public void setOnCapturedListener(OnCapturedListener listener) {
        mCapturedListener = listener;
    }

    public interface OnCapturedListener {
        void onCaptured(Bitmap resultBitMap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Get the pointer ID

        if (mCamera == null) {
            return false;
        }
        Camera.Parameters params = null;
        try {
            params = mCamera.getParameters();
        }catch (Exception e){
            return false;
        }

        int action = event.getAction();


        if (event.getPointerCount() > 1) {
            // handle multi-touch events
            if (action == MotionEvent.ACTION_POINTER_DOWN) {
                mDist = getFingerSpacing(event);
            } else if (action == MotionEvent.ACTION_MOVE && params.isZoomSupported()) {
                mCamera.cancelAutoFocus();
                handleZoom(event, params);
            }
        } else {
            // handle single touch events
            if (action == MotionEvent.ACTION_UP) {
                try {
                    handleFocus(event, params);
                } catch (Exception e) {
                    //if autofocous is not there ..
                }
            }
        }
        return true;
    }

    private float mDist;

    private void handleZoom(MotionEvent event, Camera.Parameters params) {
        int maxZoom = params.getMaxZoom();
        int zoom = params.getZoom();
        float newDist = getFingerSpacing(event);
        if (newDist > mDist) {
            //zoom in
            if (zoom < maxZoom)
                zoom++;
        } else if (newDist < mDist) {
            //zoom out
            if (zoom > 0)
                zoom--;
        }
        mDist = newDist;
        params.setZoom(zoom);
        mCamera.setParameters(params);
    }

    public void handleFocus(MotionEvent event, Camera.Parameters params) {
        int pointerId = event.getPointerId(0);
        int pointerIndex = event.findPointerIndex(pointerId);
        // Get the pointer's current position
        float x = event.getX(pointerIndex);
        float y = event.getY(pointerIndex);
        try {
            List<String> supportedFocusModes = params.getSupportedFocusModes();
            if (supportedFocusModes != null) {
                if (supportedFocusModes != null && supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                    mCamera.autoFocus(new Camera.AutoFocusCallback() {
                        @Override
                        public void onAutoFocus(boolean b, Camera camera) {
                            // currently set to auto-focus on single touch
                        }
                    });
                }
            }
        } catch (Exception e) {
        }
    }

    /**
     * Determine the space between the first two fingers
     */
    private float getFingerSpacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    public void setCameraDisplayOrientation(Activity activity,
                                            int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
            // compensate the mirror
        } else {
            // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        mRotateAngle = result;
        camera.setDisplayOrientation(result);
    }
}
