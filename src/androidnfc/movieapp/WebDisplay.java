package androidnfc.movieapp;

import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.widget.Toast;
import androidnfc.movieapp.common.Constants;
import androidnfc.movieapp.models.Trailer;
import androidnfc.movieapp.parsers.TrailerParser;

public class WebDisplay extends Activity {
	public static String trailerID = "";
	private WebView myWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.webdisplay);

		Intent intent = getIntent();

		String movieTitle = intent.getStringExtra(Constants.EXTRAS_KEY_MOVIE_TITLE);

		final String imdbID = intent.getStringExtra(Constants.EXTRAS_KEY_IMDB_ID).substring(2);
		if (myWebView == null) {
			myWebView = (WebView) findViewById(R.id.webview);
			WebSettings webSettings = myWebView.getSettings();
			PluginState pluginState = webSettings.getPluginState();
			if (pluginState == PluginState.OFF) {
				webSettings.setPluginState(PluginState.ON_DEMAND);
			}

			Display display = getWindowManager().getDefaultDisplay();
			final int displayWidth = display.getWidth();

			Thread t = new Thread() {

				@Override
				public void run() {
					TrailerParser parser = new TrailerParser(displayWidth);
					// List<Trailer> trailers = parser.getTrailersByID(imdbID);
					final List<Trailer> trailers = parser.getTrailersByID(imdbID);

					WebDisplay.this.runOnUiThread(new Runnable() {

						public void run() {
							if (trailers.size() > 0) {

								trailerID = "" + trailers.get(0).getTrailerID();
								showTrailer();

							} else {
								CharSequence text = "Trailers not found!";
								int duration = Toast.LENGTH_SHORT;
								Toast toast = Toast.makeText(WebDisplay.this, text, duration);
								toast.show();
							}

						}
					});

				}
			};
			t.start();
		}

	}

	private void showTrailer() {
		// trailerID = "31833"; //for testing the trailer
		if (trailerID.compareTo("") != 0) {
			// TEST if adobe flash has been installed
			PackageManager pm = getPackageManager();
			List<ApplicationInfo> appList = pm.getInstalledApplications(PackageManager.GET_META_DATA);
			if (appList.isEmpty()) {
				Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
				return;
			} else {
				ApplicationInfo app = null;
				Boolean installedFlag = false;
				for (Iterator<ApplicationInfo> i = appList.iterator(); i.hasNext();) {
					app = i.next();
					if (app.dataDir.contains("adobe") && app.dataDir.contains("flash")) {
						installedFlag = true;
						break;
					}
				}
				if (installedFlag == false)
					Toast.makeText(getApplicationContext(), "Please install Adobe Flash Player", Toast.LENGTH_LONG).show();
			}
			// start web view
			String url = "http://www.traileraddict.com/player.swf?id=" + trailerID + "&e=y";
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url));
			startActivity(i);
		} else {
			Toast.makeText(getApplicationContext(), "XML parser did not pass the trailer ID", Toast.LENGTH_SHORT).show();
			return;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.d("webDisplay", "FirstAcvity --->onPause");
		super.onPause();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Log.d("webDisplay", "FirstAcvity --->onStop");
		super.onStop();
	}

}
