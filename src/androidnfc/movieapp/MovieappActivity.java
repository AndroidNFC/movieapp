package androidnfc.movieapp;

import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import android.widget.ImageView;

public class MovieappActivity extends Activity {

	private Button nfcTagButton;
	private Button xmlParserButton;
	private Button openBrowserButton;
	private Button openMapButton;
	private Button openVideoButton;
	private Button inTheatersButton;
	private Button movieDetailsButton;

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

        nfcTagButton = (Button)findViewById(R.id.nfcTagButton);
        
        xmlParserButton = (Button)findViewById(R.id.xmlParserButton);
        xmlParserButton.setOnClickListener(new xmlParserOnClickListener());
        
        openBrowserButton = (Button)findViewById(R.id.openBrowserButton);
        openBrowserButton.setOnClickListener(new openBrowserOnClickListener());
        
        inTheatersButton = (Button)findViewById(R.id.inTheatersButton);
        inTheatersButton.setOnClickListener(new inTheatersOnClickListener());

        movieDetailsButton = (Button)findViewById(R.id.movieDetailsButton);
		movieDetailsButton.setOnClickListener(new movieDetailsOnClickListener());
	}

	private class movieDetailsOnClickListener implements OnClickListener {
		public void onClick(View v) {
			Intent intent = new Intent(MovieappActivity.this,
					MovieDetailsActivity.class);
			MovieappActivity.this.startActivity(intent);
		}
	}

	private class xmlParserOnClickListener implements OnClickListener {
		public void onClick(View v) {
			Intent intent = new Intent(MovieappActivity.this,
					XMLParserActivity.class);
			MovieappActivity.this.startActivity(intent);
		}
	}

	private final class openBrowserOnClickListener implements OnClickListener {
		public void onClick(View v) {
			//move to search activity
//			Toast.makeText(getApplicationContext(), "move to search activity",
//				      Toast.LENGTH_SHORT).show();
			Intent it = new Intent();
			it.setClass(MovieappActivity.this, WebDisplay.class);
//			it.putExtra("trailerID", trailerID);
			startActivity(it);
		}
	}
     
    private final class inTheatersOnClickListener implements OnClickListener {
		public void onClick(View v) {
			Intent intent = new Intent(MovieappActivity.this,
									   MovieScheduleActivity.class);
			MovieappActivity.this.startActivity(intent);
		}
    }

}
