package androidnfc.movieapp.common;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class ImageLoader {
	public static Bitmap loadImage(String url) {
		Bitmap bitmap = null;

		try {
			Log.d("DEBUG", "Image url: "+url);
			URL imageURL = new URL(url);
			try {
				bitmap = BitmapFactory.decodeStream(imageURL.openConnection()
						.getInputStream());

			} catch (IOException e) {
				Log.e("ImageLoader", "Failed to decode bitmap image.", e);
			}

		} catch (MalformedURLException e) {
			Log.e("ImageLoader", "malformed URL: " + url, e);
		}

		return bitmap;
	}

}
