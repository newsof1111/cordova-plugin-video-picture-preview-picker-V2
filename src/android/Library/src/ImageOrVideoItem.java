package com.sofienvppp2;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import android.graphics.Bitmap;

public class ImageOrVideoItem {
	private Bitmap image;
	private String title;
	private String url;
	private boolean video;
	private boolean multiSelectionState;
	private String Duration;
	private boolean selected;
	private Long date_insert;


	public ImageOrVideoItem(Bitmap image, String title, String Duration, String url,boolean is_video,Long date_insert) 
	{
		super();
		
		this.setImage(image);
		this.setDate_insert(date_insert);
		this.setTitle(title);
		this.setUrl(url);
		this.setVideo(is_video);
		this.setSelected(false);
		this.setMultiSelectionState(false);

		if (this.isVideo()) 
		{
			Date date = new Date(Integer.parseInt(Duration));
			DateFormat formatter = new SimpleDateFormat("mm:ss");
			this.setDuration(formatter.format(date));
		} else
			this.setDuration(Duration);

		
	}
	public Bitmap getImage() {
		return image;
	}
	public void setImage(Bitmap image) {
		this.image = image;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDuration() {
		return Duration;
	}
	public void setDuration(String duration) {
		Duration = duration;
	}
	public void setDate_insert(Long date_insert) {
		this.date_insert = date_insert;
	}
	public boolean isVideo() {
		return video;
	}
	public void setVideo(boolean video) {
		this.video = video;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	//this function is used to sort the list of ImageOrVideoItems using insert_date
	public static Comparator<ImageOrVideoItem> getCompByName() {
		Comparator<ImageOrVideoItem> comp = new Comparator<ImageOrVideoItem>() {
			@Override
			public int compare(ImageOrVideoItem s2, ImageOrVideoItem s1) {
				return s1.date_insert.compareTo(s2.date_insert);
			}
		};
		return comp;
	}
	public boolean isMultiSelectionState() {
		return multiSelectionState;
	}
	public void setMultiSelectionState(boolean multiSelectionState) {
		this.multiSelectionState = multiSelectionState;
	}
}
