package sample.sdk.dabkick.snap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.dabkick.videosdk.publicsettings.DabKickMedia;
import com.dabkick.videosdk.publicsettings.DabKickMediaProvider;
import com.dabkick.videosdk.publicsettings.DabKickMediaType;
import com.dabkick.videosdk.publicsettings.DabKickPhoto;
import com.dabkick.videosdk.publicsettings.DabKickVideo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by iFocus on 02-04-2018.
 */

public class Util {

    public static String[] getVideoCategories(){

        String[] categories = new String[] {
                "Entertainment"
        };

        return categories;
    }

    public static String[] getPhotoCategories(){

        String[] categories = new String[] {
                "Cartoon"
        };

        return categories;
    }

    public static List<DabKickMedia> getAllPhotos(){

        List<DabKickMedia> media = new ArrayList<>();

        DabKickPhoto p1 = new DabKickPhoto("https://pmcvariety.files.wordpress.com/2014/05/new-the-flintstones-movie-warner-bros.jpg?w=1000&h=563&crop=1");
        DabKickPhoto p2 = new DabKickPhoto("https://www.wbkidsgo.com/Portals/4/Content/Hero/Hero_TheFlintstones/Source/Default/showArt_FL.png");
        DabKickPhoto p3 = new DabKickPhoto("http://www.boomerangtv.co.uk/dynamic/show/00000000/3/ebed31d3fca1244ca2524b261ac0b8b2.jpg");
        DabKickPhoto p4 = new DabKickPhoto("https://www.thestar.com/content/dam/thestar/news/queenspark/2017/05/10/ont-woman-cant-sell-suv-because-of-flintstones-era-computer-glitch/flintstonescar.jpg.size.custom.crop.1086x613.jpg");
        DabKickPhoto p5 = new DabKickPhoto("http://img.timeinc.net/time/photoessays/2008/10_cars/flintstones.jpg");
        DabKickPhoto p6 = new DabKickPhoto("https://i.ytimg.com/vi/UL7beNWNLEQ/maxresdefault.jpg");
        DabKickPhoto p7 = new DabKickPhoto("https://vignette.wikia.nocookie.net/flinstones/images/a/ad/Fred_Flintstone.png/revision/latest?cb=20110919071811");
        DabKickPhoto p8 = new DabKickPhoto("data:image/jpeg;base64,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");
        DabKickPhoto p9 = new DabKickPhoto("https://vignette.wikia.nocookie.net/edp/images/3/39/The-flintstones-characters.jpg/revision/latest/scale-to-width-down/367?cb=20150822191631");
        DabKickPhoto p10 = new DabKickPhoto("http://c8.alamy.com/comp/K3J3KY/the-flintstones-barney-rubble-fred-flintstone-wilma-flintstone-K3J3KY.jpg");


        media.add(p1);
        media.add(p2);
        media.add(p3);
        media.add(p4);
        media.add(p5);
        media.add(p6);
        media.add(p7);
        media.add(p8);
        media.add(p9);
        media.add(p10);

        return media;

    }

    public static List<DabKickMedia> getAllVideos(){

        List<DabKickMedia> media = new ArrayList<>();

        DabKickVideo mask = new DabKickVideo("Erica Fernandes", "sulphur cooling mask", "20500","https://img.youtube.com/vi/ldOWJcG5eyE/0.jpg",
                //"https://www.youtube.com/watch?v=ldOWJcG5eyE");
                "http://qvc0.content.video.llnw.net/smedia/4826bb17d50c481b98a9e427e1c29c79/Y-/AwwQn_RUR5K0hivvTyecrE_Av5AHFfD0YaJ-RbrYU/a301149-18021616.mp4");
        DabKickVideo shopping = new DabKickVideo("Erica Fernandes", "Shopping Haul", "40300","https://img.youtube.com/vi/N9itVne50Nk/0.jpg",
                "https://www.youtube.com/watch?v=N9itVne50Nk");
        DabKickVideo nykaaReview = new DabKickVideo("Erica Fernandes", "Nykaa Cheeky Blush DUO", "20200","https://img.youtube.com/vi/J7CfUJiM1uw/0.jpg",
                "https://www.youtube.com/watch?v=J7CfUJiM1uw");
        DabKickVideo blackHeads = new DabKickVideo("Erica Fernandes", "BlackHeads", "102500","https://img.youtube.com/vi/lpb8DkbrgvQ/0.jpg",
                "https://www.youtube.com/watch?v=lpb8DkbrgvQ");
        DabKickVideo hairFall = new DabKickVideo("Erica Fernandes", "HairFall Rescue", "74400","https://img.youtube.com/vi/or1PrGxhgfM/0.jpg",
                "https://www.youtube.com/watch?v=or1PrGxhgfM");
        DabKickVideo faceWash = new DabKickVideo("Erica Fernandes", "Face Wash", "61000","https://img.youtube.com/vi/U-slXLrp8-U/0.jpg",
                "https://www.youtube.com/watch?v=U-slXLrp8-U");
        DabKickVideo hairSerum = new DabKickVideo("Erica Fernandes", "Hair Serum", "44900","https://img.youtube.com/vi/4mVsgwfuaPg/0.jpg",
                "https://www.youtube.com/watch?v=4mVsgwfuaPg");
        DabKickVideo eyeCream = new DabKickVideo("Erica Fernandes", "Eye Cream", "43200","https://img.youtube.com/vi/yUKFHaT4DTU/0.jpg",
                "https://www.youtube.com/watch?v=yUKFHaT4DTU");
        DabKickVideo tutorial = new DabKickVideo("Erica Fernandes", "Makeup tutorial", "214600","https://img.youtube.com/vi/dt0oVfkGQNI/0.jpg",
                "https://www.youtube.com/watch?v=dt0oVfkGQNI");
        DabKickVideo smokyEyehack = new DabKickVideo("Erica Fernandes", "Smoky Eye Hack", "84200","https://img.youtube.com/vi/oIhNDpsG_xA/0.jpg",
                "https://www.youtube.com/watch?v=oIhNDpsG_xA");

        media.add(mask);
        media.add(shopping);
        media.add(nykaaReview);
        media.add(blackHeads);
        media.add(hairFall);
        media.add(faceWash);
        media.add(hairSerum);
        media.add(eyeCream);
        media.add(tutorial);
        media.add(smokyEyehack);

        return media;

    }

    public static void showUserDeniedDialog(String title, String message, final Activity activity, final String permission) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(true);

        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        ActivityCompat.requestPermissions((Activity) activity,
                                new String[]{permission},
                                0);
//                        dialog.cancel();

                    }
                });
        AlertDialog alert11 = builder.create();
        //crashes sometimes if in case the activity is stopped while performing a show
        if (!activity.isFinishing())
            alert11.show();
    }

    public static void showSettingsDialog(String title, String message, final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(true);

        builder.setPositiveButton(
                "APP SETTINGS",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        try {
                            //Open the specific App Info page:
                            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.parse("package:" + activity.getApplicationContext().getPackageName()));
                            activity.startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            //Open the generic Apps page:
                            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                            activity.startActivity(intent);
                        }

                    }
                });

        builder.setNegativeButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(!activity.isFinishing()) //here make sure your activity is running
                            dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder.create();
        if(!activity.isFinishing())//here make sure your activity is running
            alert11.show();
    }

    static public float getScreenHeight(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }

    static public float convertDpToPixel(Context c, float dp) {
        float density = c.getResources().getDisplayMetrics().density;
        float pixel = dp * density;

        return pixel;
    }

    static public float convertPixelToDp(Context c, float pixel) {
        float density = c.getResources().getDisplayMetrics().density;
        float dp = pixel / density;

        return dp;
    }

    static public int getScreenWidth(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    static public void adjustToFillParant(Bitmap bitmap, ImageView imageView, Activity mCurrentActivity) {

        if(bitmap == null || imageView == null)
            return;

        int parantWidth = (int) getScreenWidth(mCurrentActivity);
        int parantHeight = (int) getScreenHeight(mCurrentActivity) - (int) convertDpToPixel(mCurrentActivity, 110);

        int imageWidth = bitmap.getWidth();
        int imageHeight = bitmap.getHeight();

        double ratio = (double) parantWidth / imageWidth;
        double resultHeight = imageHeight * ratio;
        if (resultHeight > parantHeight) {
            ratio = (double) parantHeight / imageHeight;
            imageView.getLayoutParams().width = (int) (ratio * imageWidth);
            imageView.getLayoutParams().height = (int) (parantHeight);
        } else {
            imageView.getLayoutParams().width = (int) (parantWidth);
            imageView.getLayoutParams().height = (int) (resultHeight);
        }
    }

    static public DabKickMediaProvider createDabKickMediaProvider(final DabKickMedia selectedVideo) {

        final ArrayList<String> videoCategories = new ArrayList<>(Arrays.asList(getVideoCategories()));
        final ArrayList<String> photoCategories = new ArrayList<>(Arrays.asList(getPhotoCategories()));

        return new DabKickMediaProvider() {
            @Override
            public List<DabKickMedia> provideMedia(DabKickMediaType mediaType, String category, int offset) {
                if (mediaType == DabKickMediaType.VIDEO) {
                    List<DabKickMedia> list = new ArrayList<>();
                    list.addAll(getAllVideos());
                    return list;
                } else {
                    List<DabKickMedia> list = new ArrayList<>();
                    list.addAll(getAllPhotos());
                    return list;
                }
            }

            @Override
            public List<String> provideCategories(DabKickMediaType mediaType, int offset) {
                if (mediaType == DabKickMediaType.VIDEO) {
                    if (offset == videoCategories.size()) {
                        // cannot provide any more video categories
                        return new ArrayList<>();
                    }
                    int endIndex = Math.min(videoCategories.size(), offset + 5);
                    return new ArrayList<>(videoCategories.subList(offset, endIndex));
                } else {
                    if (offset == photoCategories.size()) {
                        // cannot provide any more video categories
                        return new ArrayList<>();
                    }
                    int endIndex = Math.min(photoCategories.size(), offset + 5);
                    return new ArrayList<>(photoCategories.subList(offset, endIndex));
                }
            }


            @Override
            public List<DabKickMedia> startDabKickWithMedia() {
                List<DabKickMedia> smallerList = new ArrayList<>();
                if (selectedVideo != null) {  // user selected video, if any
                    smallerList.add(selectedVideo);
                }
                return smallerList;
            }

        };
    }

}
