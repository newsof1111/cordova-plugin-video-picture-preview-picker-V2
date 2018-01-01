package com.sofienvppp2;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.text.format.DateUtils;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.content.Intent;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import android.os.Build;
import android.media.MediaPlayer;


public class VideoPicturePickerActivity extends Activity {
    private int MIME_TYPE;
    private GridView DeviceImageVideoGridview;
    private static DeviceImageVideoGridviewAdapter DeviceImageVideoGridview_Adapter = null;
    private TextView doneText, description, source, countSelection;
    private FrameLayout done_button, cancel_button, counterbadges;
    private ImageView imagePreview, fullscreen;
    private VideoView videoPreview;
    private int selectedElement = -1, limit_Select = 1;
    private ArrayList<ImageOrVideoItem> SelectedElement_List = new ArrayList<ImageOrVideoItem>();
    private ArrayList<ImageOrVideoItem> data;
    private boolean Is_multiSelect, picture_selector, video_selector, display_video_time, display_preview;
    public int myLastVisiblePos;
    private LinearLayout imageVideoContainer;
    public float i = 1;
    public int selectionCounter = 0;
    private boolean showed = false;
    public int screenwidth, screenheight;
    public boolean block_scroll_reaction = false;
    private RelativeLayout imageVideoContainerParent;
    public int DeviceImageVideoGridview_destination_height;
    private FakeR fakeR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fakeR = new FakeR(this);
        setContentView(fakeR.getId("layout", "video_picture_picker2_activity"));
        data = new ArrayList<ImageOrVideoItem>();

        // hide default headerBar
        getActionBar().hide();

        Is_multiSelect = true;
        //limit_Select = 5;
        limit_Select = getIntent().getIntExtra("limit_Select", 5);
        Is_multiSelect = getIntent().getBooleanExtra("Is_multiSelect", false);
        picture_selector = getIntent().getBooleanExtra("picture_selector", false);
        video_selector = getIntent().getBooleanExtra("video_selector", false);
        display_video_time = getIntent().getBooleanExtra("display_video_time", false);
        display_preview = getIntent().getBooleanExtra("display_preview", false);


        //linking View to Controller
        DeviceImageVideoGridview = (GridView) findViewById(fakeR.getId("id", "gridView"));
        doneText = ((TextView) findViewById(fakeR.getId("id", "actionbar_done_textview")));
        source = ((TextView) findViewById(fakeR.getId("id", "source")));
        countSelection = ((TextView) findViewById(fakeR.getId("id", "countSelection")));
        description = ((TextView) findViewById(fakeR.getId("id", "description")));
        cancel_button = (FrameLayout) findViewById(fakeR.getId("id", "actionbar_discard"));
        done_button = (FrameLayout) findViewById(fakeR.getId("id", "actionbar_done"));
        counterbadges = (FrameLayout) findViewById(fakeR.getId("id", "actionbar_add"));
        imagePreview = (ImageView) findViewById(fakeR.getId("id", "imageView1"));
        videoPreview = (VideoView) findViewById(fakeR.getId("id", "videoView1"));
        fullscreen = (ImageView) findViewById(fakeR.getId("id", "imageButton"));
        imageVideoContainerParent = (RelativeLayout) findViewById(fakeR.getId("id", "imageVideoContainerParent"));
        imageVideoContainer = (LinearLayout) findViewById(fakeR.getId("id", "imageVideoContainer"));


        getData();
        fullscreen.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hidePreview();
                imageVideoContainer.setBackgroundDrawable(null);
                imagePreview.setImageBitmap(null);
            }
        });
        doneText.setEnabled(false);
        done_button.setEnabled(false);
        done_button.setAlpha(0.5f);
        imagePreview.setEnabled(false);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenwidth = size.x;
        screenheight = size.y;


        ViewGroup.LayoutParams imageVideoContainerParent_layoutParams = imageVideoContainerParent.getLayoutParams();
        imageVideoContainerParent_layoutParams.height = 0;
        imageVideoContainerParent.setLayoutParams(imageVideoContainerParent_layoutParams);
        ViewGroup.LayoutParams imageVideoContainer_layoutParams = imageVideoContainer.getLayoutParams();
        imageVideoContainer_layoutParams.height = (screenheight - (140 + 100)) / 2;
        imageVideoContainer.setLayoutParams(imageVideoContainer_layoutParams);

        videoPreview.setVisibility(View.GONE);


        myLastVisiblePos = DeviceImageVideoGridview.getFirstVisiblePosition();

        DeviceImageVideoGridview.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int currentFirstVisPos = view.getFirstVisiblePosition();
                if (showed && currentFirstVisPos > myLastVisiblePos && currentFirstVisPos > 40 && !block_scroll_reaction)
                    hidePreview();

                myLastVisiblePos = currentFirstVisPos;
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub

            }
        });


        //display or hide in order of Plugin's options
        counterbadges.setVisibility(View.GONE);


        DeviceImageVideoGridview_Adapter = new DeviceImageVideoGridviewAdapter(this, fakeR.getId("layout", "simple_list_item_1"), data, Glide.with(this), this, display_video_time);
        DeviceImageVideoGridview.setAdapter(DeviceImageVideoGridview_Adapter);


        // cancel_button's clickEvent
        cancel_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < data.size(); i++) {
                    data.get(i).setMultiSelectionState(false);
                }
                hidePreview();
                imageVideoContainer.setBackgroundDrawable(null);
                imagePreview.setImageBitmap(null);
                counterbadges.setVisibility(View.GONE);
                DeviceImageVideoGridview_Adapter.setMultiSelectionActiv(false);
                SelectedElement_List.clear();
                selectionCounter = 0;
                countSelection.setText(selectionCounter + "");
                finish();
            }
        });

        // done_button's clickEvent
        done_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                Bundle res = new Bundle();
                ArrayList<String> resultList = new ArrayList<String>();
                if (!DeviceImageVideoGridview_Adapter.isMultiSelectionActiv()) {
                    SelectedElement_List.add(data.get(selectedElement));
                }
                res.putStringArrayList("type", convertListOfObjectToListOfStrings(SelectedElement_List).get(0));
                res.putStringArrayList("path", convertListOfObjectToListOfStrings(SelectedElement_List).get(1));
                returnIntent.putExtras(res);


                counterbadges.setVisibility(View.GONE);
                DeviceImageVideoGridview_Adapter.setMultiSelectionActiv(false);
                SelectedElement_List.clear();
                selectionCounter = 0;
                countSelection.setText(selectionCounter + "");
                hidePreview();
                imageVideoContainer.setBackgroundDrawable(null);
                imagePreview.setImageBitmap(null);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            videoPreview.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    if (what == 3) {
                        int videoHeight = videoPreview.getHeight();
                        int videoWidth = videoPreview.getWidth();
                        if ((videoHeight != ((screenheight - (140 + 100)) / 2)) && (videoWidth != screenwidth)) {
                            ViewGroup.LayoutParams layoutParams = videoPreview.getLayoutParams();
                            layoutParams.height = ((screenheight - (140 + 100)) / 2);
                            layoutParams.width = screenwidth;
                            videoPreview.setLayoutParams(layoutParams);
                        }

                        return true;
                    }


                    return false;
                }
            });
        }

        // DeviceImageVideoGridview element's clickEvent
        DeviceImageVideoGridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (selectedElement > -1)
                    data.get(selectedElement).setSelected(false);
                selectPictureVideo(position);

                selectedElement = position;

                ImageOrVideoItem item = data.get(position);
                item.setSelected(true);


                if (item.isVideo()) {

                    imageVideoContainer.setBackgroundResource(fakeR.getId("color", "Black"));
                    fullscreen.setVisibility(View.VISIBLE);
                    imagePreview.setVisibility(View.GONE);
                    videoPreview.setVisibility(View.GONE);
                    videoPreview.setVideoPath(item.getUrl());
                    videoPreview.start();
                    videoPreview.setVisibility(View.VISIBLE);

                    source.setText(getResources().getString(fakeR.getId("string", "vpp_from")) + " " + item.getTitle());

                    description.setText(item.getDuration());

                } else {
                    imagePreview.setVisibility(View.VISIBLE);
                    fullscreen.setVisibility(View.VISIBLE);
                    imagePreview.setEnabled(true);
                    videoPreview.setVisibility(View.GONE);
                    videoPreview.stopPlayback();

                    try

                    {
                        Glide.with(getApplicationContext()).load(new File(item.getUrl()))
                                .placeholder(fakeR.getId("color", "White"))
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)

                                .into(imagePreview);
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    description.setText(item.getDuration());
                    source.setText(getResources().getString(fakeR.getId("string", "vpp_from")) + " " + item.getTitle());


                }
                if (!showed && display_preview) {

                    ObjectAnimator anim = ObjectAnimator.ofInt(imageVideoContainerParent, "height", imageVideoContainerParent.getMeasuredHeight(), ((screenheight - (140 + 100)) / 2));
                    anim.setDuration(100);
                    anim.addUpdateListener(new AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(final ValueAnimator animation) {
                            int val = (Integer) animation.getAnimatedValue();
                            ViewGroup.LayoutParams layoutParams = imageVideoContainerParent.getLayoutParams();
                            layoutParams.height = val;
                            imageVideoContainerParent.setLayoutParams(layoutParams);

                        }
                    });


                    anim.start();


                    DeviceImageVideoGridview_destination_height = ((screenheight - (140 + 100)) / 2);

                    ObjectAnimator anim2 = ObjectAnimator.ofInt(DeviceImageVideoGridview, "height", DeviceImageVideoGridview.getHeight(), DeviceImageVideoGridview_destination_height);
                    anim2.setDuration(80);
                    anim2.addUpdateListener(new AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(final ValueAnimator animation) {
                            int val = (Integer) animation.getAnimatedValue();
                            ViewGroup.LayoutParams layoutParams = DeviceImageVideoGridview.getLayoutParams();
                            layoutParams.height = val;
                            DeviceImageVideoGridview.setLayoutParams(layoutParams);

                        }
                    });
                    anim2.addListener(new Animator.AnimatorListener() {
                        public void onAnimationRepeat(final Animator animation) {
                        }

                        public void onAnimationStart(final Animator animation) {
                        }

                        public void onAnimationCancel(final Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(final Animator animation) {
                            showed = true;

                            block_scroll_reaction = true;
                            DeviceImageVideoGridview.invalidateViews();
                            DeviceImageVideoGridview.post(new Runnable() {
                                @Override
                                public void run() {
                                    DeviceImageVideoGridview.smoothScrollToPosition(selectedElement);

                                }
                            });
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    block_scroll_reaction = false;


                                }
                            }, 1000);


                        }
                    });
                    anim2.start();
                }


            }
        });


        DeviceImageVideoGridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {


                if (Is_multiSelect && !DeviceImageVideoGridview_Adapter.isMultiSelectionActiv()) {
                    for (int i = 0; i < data.size(); i++) {
                        data.get(i).setMultiSelectionState(false);
                    }
                    SelectedElement_List.clear();
                    countSelection.setText("0");
                    counterbadges.setVisibility(View.VISIBLE);
                    DeviceImageVideoGridview_Adapter.setMultiSelectionActiv(true);
                    DeviceImageVideoGridview_Adapter.notifyDataSetChanged();
                    Vibrator v = (Vibrator) getBaseContext().getSystemService(getBaseContext().VIBRATOR_SERVICE);
                    v.vibrate(50);
                    doneText.setEnabled(false);
                    done_button.setEnabled(false);
                    done_button.setAlpha(0.5f);
                }

                return false;
            }
        });


    }

    private void hidePreview() {
        imageVideoContainer.setBackgroundDrawable(null);
        imagePreview.setImageBitmap(null);
        imagePreview.setVisibility(View.GONE);
        final ObjectAnimator anim3 = ObjectAnimator.ofInt(DeviceImageVideoGridview, "height", DeviceImageVideoGridview.getMeasuredHeight(), screenheight - (140 + 100));
        anim3.setDuration(50);
        anim3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(final ValueAnimator animation) {
                int val = (Integer) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = DeviceImageVideoGridview.getLayoutParams();
                layoutParams.height = val;
                DeviceImageVideoGridview.setLayoutParams(layoutParams);

            }
        });
        anim3.addListener(new Animator.AnimatorListener() {
            public void onAnimationCancel(final Animator animation) {
            }

            public void onAnimationRepeat(final Animator animation) {
            }

            public void onAnimationStart(final Animator animation) {
            }

            @Override
            public void onAnimationEnd(final Animator animation) {
            }
        });

        ObjectAnimator anim = ObjectAnimator.ofInt(imageVideoContainerParent, "height", imageVideoContainerParent.getMeasuredHeight(), 0);
        anim.setDuration(50);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(final ValueAnimator animation) {
                int val = (Integer) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = imageVideoContainerParent.getLayoutParams();
                layoutParams.height = val;
                imageVideoContainerParent.setLayoutParams(layoutParams);

            }
        });
        anim.addListener(new Animator.AnimatorListener() {
            public void onAnimationCancel(final Animator animation) {
            }

            public void onAnimationRepeat(final Animator animation) {
            }

            public void onAnimationStart(final Animator animation) {
                anim3.start();
            }

            public void onAnimationEnd(final Animator animation) {
                showed = false;
                //  DeviceImageVideoGridview
                if (selectedElement > -1)
                    data.get(selectedElement).setSelected(false);
                selectedElement = -1;
                if (!DeviceImageVideoGridview_Adapter.isMultiSelectionActiv()) {
                    doneText.setEnabled(false);
                    done_button.setEnabled(false);
                    done_button.setAlpha(0.5f);
                }
                fullscreen.setVisibility(View.GONE);
                imagePreview.setVisibility(View.VISIBLE);
                videoPreview.setVisibility(View.GONE);
                imagePreview.setEnabled(false);
                videoPreview.stopPlayback();
                source.setText("");
                description.setText("");
            }
        });
        anim.start();


    }




    /*
    ***********************************************************************************************************************************************
    ***********************************************************************************************************************************************
                                                        HARDWARE BACK BUTTON
    ***********************************************************************************************************************************************
    ***********************************************************************************************************************************************
     */


    @Override
    public void onBackPressed() {
        if (showed) {
            hidePreview();
            return;
        }
        if (!showed && DeviceImageVideoGridview_Adapter.isMultiSelectionActiv()) {
            for (int i = 0; i < data.size(); i++) {
                data.get(i).setMultiSelectionState(false);
            }
            SelectedElement_List.clear();
            countSelection.setText("0");
            counterbadges.setVisibility(View.GONE);
            DeviceImageVideoGridview_Adapter.setMultiSelectionActiv(false);
            DeviceImageVideoGridview_Adapter.notifyDataSetChanged();
            return;
        }
        Glide.get(this).clearMemory();
        imageVideoContainer.setBackgroundDrawable(null);
        imagePreview.setImageBitmap(null);
        imagePreview.setVisibility(View.GONE);

        super.onBackPressed();
    }

    @Override
    public void onStop() {
        if (showed)
            hidePreview();

        imageVideoContainer.setBackgroundDrawable(null);
        imagePreview.setImageBitmap(null);
        imagePreview.setVisibility(View.GONE);

        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (showed)
            hidePreview();
        imageVideoContainer.setBackgroundDrawable(null);
        imagePreview.setImageBitmap(null);
        imagePreview.setVisibility(View.GONE);
        Glide.get(this).clearMemory();
        super.onDestroy();
    }


    /*
    ***********************************************************************************************************************************************
    ***********************************************************************************************************************************************
                                                   fetching pictures and videos from device
    ***********************************************************************************************************************************************
    ***********************************************************************************************************************************************
     */

    private void getData() {

        data = new ArrayList<ImageOrVideoItem>();
        Uri imagesUri, videoUri;
        Cursor imageCursor, videoCursor;
        String imageAbsolutePath;
        int image_column_index_data, image_title,

                column_index_data_FOR_VIDEO, image_date_insert, video_date_insert, video_title, video_description;

        imagesUri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] imageProjection =
                {
                        MediaColumns.DATA,
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                        MediaStore.Images.Media.DATE_ADDED,
                        MediaStore.Images.Media.MIME_TYPE
                };


        imageCursor = this.getContentResolver().query(imagesUri, imageProjection, null, null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");
        MIME_TYPE = imageCursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE);
        image_column_index_data = imageCursor.getColumnIndexOrThrow(MediaColumns.DATA);
        image_title = imageCursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        image_date_insert = imageCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED);


        String[] videoProjection =
                {
                        MediaStore.Video.VideoColumns.DATA,
                        MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME,
                        MediaStore.Video.VideoColumns.DURATION,
                        MediaStore.Video.VideoColumns.DATE_ADDED
                };

        videoUri = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        videoCursor = this.getContentResolver().query(videoUri, videoProjection, null, null, null);
        column_index_data_FOR_VIDEO = videoCursor.getColumnIndexOrThrow(MediaColumns.DATA);
        video_date_insert = videoCursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DATE_ADDED);
        video_title = videoCursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME);
        video_description = videoCursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DURATION);
        if (videoCursor.getColumnCount() > 0 && video_selector) {
            while (videoCursor.moveToNext()) {
                String videoPath = videoCursor.getString(column_index_data_FOR_VIDEO);

                ImageOrVideoItem video = new ImageOrVideoItem
                        (null,
                                videoCursor.getString(video_title),
                                videoCursor.getString(video_description),
                                videoPath,
                                true,
                                Long.parseLong(videoCursor.getString(video_date_insert))
                        );

                data.add(video);
            }

            videoCursor.close();
        }

        if (imageCursor.getColumnCount() > 0 && picture_selector) {

            while (imageCursor.moveToNext()) {
                //gif pictures will cause problem in preview
                if (imageCursor.getString(MIME_TYPE).toLowerCase().contains("image/gif"))
                    continue;

                //**********************************************************************************
                // let's calculate from how long the picture is token
                //**********************************************************************************
                long time = Long.parseLong(imageCursor.getString(image_date_insert));
                long now = System.currentTimeMillis();
                CharSequence relativeTimeStr = DateUtils.getRelativeTimeSpanString(time * 1000, now,
                        DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE);
                //**********************************************************************************

                imageAbsolutePath = imageCursor.getString(image_column_index_data);
                data.add(new ImageOrVideoItem(
                        null,
                        imageCursor.getString(image_title),
                        (String) relativeTimeStr,
                        imageAbsolutePath,
                        false,
                        time)
                );


            }

            imageCursor.close();
        }

        Collections.sort(data, ImageOrVideoItem.getCompByName());


    }



    /*
     ***********************************************************************************************************************************************
     ***********************************************************************************************************************************************
                                          Select/UnSelect Element for preview  and effect directly to our listview
     ***********************************************************************************************************************************************
     ***********************************************************************************************************************************************
      */
    private void selectPictureVideo(int index) {
        View v = DeviceImageVideoGridview.getChildAt(index -
                DeviceImageVideoGridview.getFirstVisiblePosition());
        View vOld = DeviceImageVideoGridview.getChildAt(selectedElement -
                DeviceImageVideoGridview.getFirstVisiblePosition());


        if (v == null)
            return;
        ImageView selectionbox = (ImageView) v.findViewById(fakeR.getId("id", "selectionbox"));
        if (DeviceImageVideoGridview_Adapter.isMultiSelectionActiv()) {
            if (data.get(index).isMultiSelectionState()) {
                data.get(index).setMultiSelectionState(false);
                Glide.with(this).load(fakeR.getId("drawable", "selected_not"))
                        .override(100, 100)
                        .centerCrop()
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(selectionbox);
                SelectedElement_List.remove(data.get(index));


            } else {
                if (!(SelectedElement_List.size() == limit_Select)) {
                    data.get(index).setMultiSelectionState(true);
                    Glide.with(this).load(fakeR.getId("drawable", "selected"))
                            .override(100, 100)
                            .centerCrop()
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(selectionbox);
                    SelectedElement_List.add(data.get(index));
                } else
                    Toast.makeText(getBaseContext(), getResources().getString(fakeR.getId("string", "vpp_limitMultiselect")),
                            Toast.LENGTH_SHORT).show();


            }
            countSelection.setText(SelectedElement_List.size() + "");
            if (SelectedElement_List.size() == 0) {
                doneText.setEnabled(false);
                done_button.setEnabled(false);
                done_button.setAlpha(0.5f);

            } else {
                doneText.setEnabled(true);
                done_button.setEnabled(true);
                done_button.setAlpha(1f);
            }
        } else {
            doneText.setEnabled(true);
            done_button.setEnabled(true);
            done_button.setAlpha(1f);
        }

        if (selectedElement != index) {
            ImageView image = (ImageView) v.findViewById(fakeR.getId("id", "imageS"));

            if (vOld != null) {
                ImageView theOld = (ImageView) vOld.findViewById(fakeR.getId("id", "imageS"));

                theOld.setAlpha(1f);
            }
            image.setAlpha(0.5f);
        }


    }


    public ArrayList<ArrayList<String>> convertListOfObjectToListOfStrings(ArrayList<ImageOrVideoItem> ImageOrVideoItemList) {

        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
        ArrayList<String> type = new ArrayList<String>();
        ArrayList<String> path = new ArrayList<String>();


        for (int i = 0; i < ImageOrVideoItemList.size(); i++) {
            if (ImageOrVideoItemList.get(i).isVideo())
                type.add("video");
            else
                type.add("picture");

            path.add(ImageOrVideoItemList.get(i).getUrl());
            result.add(type);
            result.add(path);


        }
        return result;
    }


}
