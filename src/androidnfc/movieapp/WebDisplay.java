package androidnfc.movieapp;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import androidnfc.movieapp.common.Constants;
import androidnfc.movieapp.models.Trailer;
import androidnfc.movieapp.parsers.FinnkinoParser;
import androidnfc.movieapp.parsers.TrailerParser;

public class WebDisplay extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.webdisplay);
		
		Intent intent = getIntent();
		
		String movieTitle = intent.getStringExtra(Constants.EXTRAS_KEY_MOVIE_TITLE);
		
		String imdbID = intent.getStringExtra(Constants.EXTRAS_KEY_IMDB_ID);
		if (imdbID.substring(0, 2).equals("tt")) {
			imdbID = imdbID.substring(2);
		}
		
		// Create a web view
		WebView myWebView = (WebView) findViewById(R.id.webview);
		WebSettings webSettings = myWebView.getSettings();
		PluginState pluginState = webSettings.getPluginState();
		if (pluginState == PluginState.OFF){
			webSettings.setPluginState(PluginState.ON_DEMAND);
		}
		
		Display display = getWindowManager().getDefaultDisplay();
		int displayWidth = display.getWidth();
		
        TrailerParser parser = new TrailerParser(displayWidth);
        //List<Trailer> trailers = parser.getTrailersByID(imdbID);
        List<Trailer> trailers = parser.getTrailersByMovieTitle(movieTitle);
        
        if (trailers.size() > 0) {
        	
            //String embedHTML = trailers.get(0).getEmbedHTML();
    		//String trailerHTML = "<html><body>" + embedHTML + "</body></html>";
            //myWebView.loadData(trailerHTML, "text/html", null);
            
    		String url = "http://www.traileraddict.com/player.swf?id=" + trailers.get(0).getTrailerID();
            
        } else {
        	CharSequence text = "Trailers not found!";
        	int duration = Toast.LENGTH_SHORT;
        	Toast toast = Toast.makeText(this, text, duration);
        	toast.show();
        }
        

	}
	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.d("webDisplay", "FirstAcvity --->onPause");
		super.onPause();
	}
	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Log.d("webDisplay", "FirstAcvity --->onStop");
		super.onStop();
	}
	
}
