/**
 * 
 */
package androidnfc.movieapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.util.Log;
import android.view.Window;

/**
 * @author Ken
 *
 */
public class WebDisplay extends Activity {
	private String trailerID = "40472";
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.webdisplay);
		
		//create a web view
		WebView myWebView = (WebView) findViewById(R.id.webview);
		WebSettings webSettings = myWebView.getSettings();
		
		PluginState pluginState = webSettings.getPluginState();
		Log.d("webDisplay", "get plugin state is ---> " + pluginState);
		
		if (pluginState == PluginState.OFF){
			webSettings.setPluginState(PluginState.ON_DEMAND);
		}
		
		Log.d("webDisplay", "get plugin state is ---> " + pluginState);
		
		//get the trailer ID from intent extra
		//The trailerID should be assigned value before this being called.
//		Intent intent = getIntent();
//		trailerID = intent.getStringExtra("trailerID");
		
//		String url = "http://www.traileraddict.com/player.swf?id=" + trailerID + "&e=y";
		String url = "http://www.traileraddict.com/player.swf?id=" + trailerID;
		/**change the following line to the URL where the trailer is e.g.
		 * http://www.traileraddict.com/player.swf?id=31763&e=y
		 * but this require to install Adobe Flash
		 * **/
		Log.d("webDisplay", "FirstAcvity --->onCreate + " + url
				);
//		http://www.traileraddict.com/player.swf?id=31766&e=y
		myWebView.loadUrl(url);
		Log.d("webDisplay", "FirstAcvity --->onCreate2");
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
