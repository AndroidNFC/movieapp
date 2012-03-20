package androidnfc.movieapp;

import java.util.Collection;
import java.util.LinkedList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	
	private Context context;
	private LinkedList<ImageView> images;
	
	public ImageAdapter(Context c) {
		context = c;
		images = new LinkedList<ImageView>();
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
		
		return images.get(position);
		
	}
	
	public float getScale(boolean focused, int offset) {
		
		return Math.max(0, 1.0f / (float)Math.pow(2, Math.abs(offset)));
		
	}
	
}