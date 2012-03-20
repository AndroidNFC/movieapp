package androidnfc.movieapp;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidnfc.movieapp.models.Movie;
import androidnfc.movieapp.parsers.FinnkinoXMLParser;
import androidnfc.movieapp.parsers.MovieHandler;

public class MovieDetailsActivity extends Activity {

	private final String XML_PARSER_DEBUG_TAG = "XMLParserActivity";
	public static final String EXTRAS_KEY_IMDB_ID = "IMDB_ID";
	public static final String EXTRAS_KEY_FINNKINO_ID = "FINNKINO_ID";
	private TextView tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// The activity is being created.

		setContentView(R.layout.movie);
		tv = (TextView) findViewById(R.id.movieText);
		tv.setText("");

		// TODO Glue for Top panel. This should be integrated in some
		// TopPanelView-widget or so
		{
			ImageView back = (ImageView) findViewById(R.id.topbar_back);
			back.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					finish();
				}
			});
			ImageView search = (ImageView) findViewById(R.id.topbarSearch);
			search.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					Intent intent = new Intent(MovieDetailsActivity.this,
							SearchActivity.class);
					MovieDetailsActivity.this.startActivity(intent);
				}
			});
		}
	}

	@Override
	protected void onStart() {

		super.onStart();
		Intent i = getIntent();
		Bundle extras = i.getExtras();
		if (extras != null) {
			Object o1 = extras.get(EXTRAS_KEY_IMDB_ID);
			Object o2 = extras.get(EXTRAS_KEY_FINNKINO_ID);
			// Just some glue here..
			if (o1 == null || o2 == null) {
				return;
			}
			int imdbId = (Integer) o1;
			int finnkinoId = (Integer) o2;
			try {
				tv.setText("");
				List<Movie> movies = new FinnkinoXMLParser().parse(finnkinoId);
				for (Movie movie : movies) {
					tv.append(movie.toString());
				}
			} catch (Exception e) {
				tv.setText("Error: " + e.getMessage());
				Log.e(XML_PARSER_DEBUG_TAG, "XML Parser Error", e);
			}
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		// The activity has become visible (it is now "resumed").
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Another activity is taking focus (this activity is about to be
		// "paused").
	}

	@Override
	protected void onStop() {
		super.onStop();
		// The activity is no longer visible (it is now "stopped")
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// The activity is about to be destroyed.
	}

}