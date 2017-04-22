package com.sofienvppp2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.widget.LinearLayout;


//to avoid blocking the activity until loading every picture ,I used a thread to work in background instead
public class loadbackgroundPictureTask extends AsyncTask<Void, Integer, BitmapDrawable>
{
	private String URL;
	private Context main;
	private LinearLayout imageVideoContainer;
	private boolean fortest;
	public loadbackgroundPictureTask(LinearLayout imageVideoContainer, String url,Context main,boolean fortest)
		{
			this.main=main;
			this.fortest=fortest;
			this.imageVideoContainer=imageVideoContainer;
			this.URL=url;

		}
	
    protected BitmapDrawable doInBackground(Void... params)
	    {
			Bitmap myBitmap = blur(decodeSampledBitmapFromResource(URL, 100, 100));
/*
			try {
				ExifInterface exif = new ExifInterface(URL);
				int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
				Matrix matrix = new Matrix();
				if (orientation == 6) {
					matrix.postRotate(90);
				}
				else if (orientation == 3) {
					matrix.postRotate(180);
				}
				else if (orientation == 8) {
					matrix.postRotate(270);
				}
				myBitmap = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), matrix, true); // rotating bitmap
			}
			catch (Exception e) {
				return null;
			}
*/
			
			BitmapDrawable background = new BitmapDrawable(myBitmap);
			
			return background;
	    }

    protected void onProgressUpdate(Integer... progress) 
	    {
	    }

    protected void onPostExecute(BitmapDrawable result)
	    {

			if(fortest)
			imageVideoContainer.setBackgroundDrawable(result);

	    }

	public static Bitmap decodeSampledBitmapFromResource(String absolutePath, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// BitmapFactory.decodeResource(res, resId, options);
		BitmapFactory.decodeFile(absolutePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(absolutePath, options);

	}
	public Bitmap blur(Bitmap image) {
		if (null == image) return null;

		Bitmap outputBitmap = Bitmap.createBitmap(image);
		final RenderScript renderScript = RenderScript.create(main);
		Allocation tmpIn = Allocation.createFromBitmap(renderScript, image);
		Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);
		if (android.os.Build.VERSION.SDK_INT >= 17)
		{
			//Intrinsic Gausian blur filter
			ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
			theIntrinsic.setRadius(25f);
			theIntrinsic.setInput(tmpIn);
			theIntrinsic.forEach(tmpOut);
			tmpOut.copyTo(outputBitmap);

			return outputBitmap;
		}
		else{
			return null;
		}

	}
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}
    
}
