package androidnfc.movieapp;

import java.util.Collection;
import java.util.LinkedList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {

	private Context context;
	private LinkedList<ImageView> images;
	private int width;
	private int height;

	public ImageAdapter(Context c) {
		context = c;
		images = new LinkedList<ImageView>();

		width = (int) c.getResources().getDimension(
				R.dimen.gallery_poster_width);
		height = (int) c.getResources().getDimension(
				R.dimen.gallery_poster_height);

	}

	public void loadImages(Collection<ImageView> collection) {

		for (ImageView img : collection) {
			images.add(img);
		}

	}

	public int getCount() {
		return images.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView result = images.get(position);
		result.setScaleType(ImageView.ScaleType.FIT_CENTER);
		
		result.setLayoutParams( new Gallery.LayoutParams(width, height));

		result.setPadding(10, 10, 10, 10);
		 result.setBackgroundColor(0x000000);
		return result;
		
	}

	public float getScale(boolean focused, int offset) {

		return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));

	}

	public void setItem(int position, ImageView item) {
		images.set(position, item);
	}

}