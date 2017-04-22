package com.sofienmediaselector;

import java.util.ArrayList;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;






import java.io.File;
import java.util.ArrayList;

import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.net.Uri;
import java.io.File;



public class DeviceImageVideoGridviewAdapter extends BaseAdapter {

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private Context context;
	private int layoutResourceId;
	private ArrayList<ImageOrVideoItem> data = new ArrayList<ImageOrVideoItem>();
	private boolean isMultiSelectionActiv;
	private  RequestManager glide;
	private VideoPicturePickerActivity main;

	public DeviceImageVideoGridviewAdapter(Context context, int layoutResourceId, ArrayList<ImageOrVideoItem> data,RequestManager glide,VideoPicturePickerActivity main) {
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
		this.setMultiSelectionActiv(false);
		this.glide = glide;
		this.main = main;


	}
	public boolean isMultiSelectionActiv() {
		return isMultiSelectionActiv;
	}

	public void setMultiSelectionActiv(boolean multiSelectionActiv) {
		isMultiSelectionActiv = multiSelectionActiv;
	}

    private Uri getUriFromMediaStore(int position) {
       
        Uri mediaUri = Uri.parse("file://" + data.get(position).getUrl());
        return mediaUri;
    }
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder holder;
		ImageOrVideoItem item;
		item =data.get(position);

			if (row == null )
		{
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new ViewHolder();

			holder.image = (ImageView) row.findViewById(FakeR.getId(context, "id","imageS"));
			holder.selectionbox = (ImageView) row.findViewById(FakeR.getId(context, "id","selectionbox"));
			holder.videoDuration = (TextView) row.findViewById(FakeR.getId(context, "id","videoDuration"));

			holder.selectionbox.setVisibility(View.GONE);


			row.setTag(holder);


		}

		
				holder = (ViewHolder) row.getTag();
				holder.videoDuration.setText("");
				holder.selectionbox.setVisibility(View.GONE);

				if(isMultiSelectionActiv())
				{
					holder.selectionbox.setVisibility(View.VISIBLE);
					if(item.isMultiSelectionState())
					{
						try {
							glide.load(FakeR.getId(context, "drawable","selected"))
							.placeholder(FakeR.getId(context, "color","WhiteSmoke"))
							.override(100, 100)
							.centerCrop()
							.skipMemoryCache( true )
							.diskCacheStrategy( DiskCacheStrategy.NONE )
							.into(holder.selectionbox);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					else
					{
						try {
							glide.load(FakeR.getId(context, "drawable","selected_not"))
							.placeholder(FakeR.getId(context, "color","WhiteSmoke"))
							.override(100, 100)
							.centerCrop()
							.skipMemoryCache( true )
							.diskCacheStrategy( DiskCacheStrategy.NONE )
							.into(holder.selectionbox);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
						//holder.selectionbox.setImageResource(FakeR.getId(context, "drawable","selected_not"));


				}
				//Picasso.with(this.context).load(new File(data.get(position).getUrl())).resize(100, 100)
					//	.centerCrop().skipMemoryCache().into(holder.image);
						//Picasso.with(this.context).invalidate(new File(data.get(position).getUrl()));
				if(!item.isVideo())
                {
					
					try {
						//glide.load(new File(item.getUrl()))
						glide.load(new File(item.getUrl()))
							.placeholder(FakeR.getId(context, "color","WhiteSmoke"))
							.override(150, 150)
							.centerCrop()
							//.skipMemoryCache( true )
							.diskCacheStrategy( DiskCacheStrategy.RESULT )
							.into(holder.image);
						} 
					catch (Exception e) 
						{e.printStackTrace();}
				}

                else
                {
                    holder.videoDuration.setText(item.getDuration());
				
								
								glide
								.load( Uri.fromFile( new File( item.getUrl() ) ) )
								.placeholder(FakeR.getId(context, "color","WhiteSmoke"))
								.crossFade()
								.override(100, 100)
								.diskCacheStrategy( DiskCacheStrategy.RESULT )
								.into(holder.image);
							


                }



				holder.image.setAlpha(1f);

				if(item.isSelected())
				{
					holder.image.setAlpha(0.5f);
				}



			


		return row;
	}

	
//******** Define ViewHolder Class *************
static class ViewHolder 
	{
		ImageView image,selectionbox;
		TextView videoDuration;

	}

}
