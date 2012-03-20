package androidnfc.movieapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import android.widget.ImageView;

public class MovieappActivity extends Activity {

	private Button nfcTagButton;
	private Button xmlParserButton;
	private Button openBrowserButton;
	private Button openMapButton;
	private Button openVideoButton;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// TODO Glue for Top Panel. This should be integrated in some
		// TopPanelView-widget or so
		{
			ImageView back = (ImageView) findViewById(R.id.topbar_back);
			back.setAlpha(0);

			ImageView search = (ImageView) findViewById(R.id.topbarSearch);
			search.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					Intent intent = new Intent(MovieappActivity.this,
							SearchActivity.class);
					MovieappActivity.this.startActivity(intent);
				}
			});
		}

		nfcTagButton = (Button) findViewById(R.id.nfcTagButton);
		xmlParserButton = (Button) findViewById(R.id.xmlParserButton);
		openBrowserButton = (Button) findViewById(R.id.openBrowserButton);
		openMapButton = (Button) findViewById(R.id.openMapButton);
		openVideoButton = (Button) findViewById(R.id.openVideoButton);

		xmlParserButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MovieappActivity.this,
						MovieDetailsActivity.class);
				MovieappActivity.this.startActivity(intent);
			}
		});

		nfcTagButton.setOnClickListener(new nfcTagOnClickListener());

		openBrowserButton = (Button) findViewById(R.id.openBrowserButton);
		openBrowserButton.setOnClickListener(new openBrowserOnClickListener());
	}

	private class nfcTagOnClickListener implements OnClickListener {
		public void onClick(View v) {
			Intent intent = new Intent(MovieappActivity.this,
					XMLParserActivity.class);
			MovieappActivity.this.startActivity(intent);
		}
	}

	// call web view
	private final class openBrowserOnClickListener implements OnClickListener {
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent it = new Intent();
			it.setClass(MovieappActivity.this, WebDisplay.class);
			startActivity(it);
		}
	}

}
