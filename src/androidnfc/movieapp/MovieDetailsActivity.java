package androidnfc.movieapp;

import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidnfc.movieapp.common.Constants;
import androidnfc.movieapp.common.ImageLoader;
import androidnfc.movieapp.models.ImdbMovie;
import androidnfc.movieapp.models.Movie;
import androidnfc.movieapp.models.Show;
import androidnfc.movieapp.parsers.FinnkinoParser;
import androidnfc.movieapp.parsers.ImdbJSONParser;
import androidnfc.movieapp.parsers.FinnkinoHandler;

public class MovieDetailsActivity extends Activity {

	private final String XML_PARSER_DEBUG_TAG = "XMLParserActivity";
	private final SimpleDateFormat FINNKINO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.UK);
	private final SimpleDateFormat DATUM_FORMAT = new SimpleDateFormat("EEE yyyy-MM-dd", Locale.UK);
	private final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm", Locale.UK);
	private TextView title;
	private TextView year;
	private TextView director;
	private TextView description;
	private TextView rating;
	private TextView cast;
	private ImageView poster;
	private Bitmap bitmapResult;
	private ImdbMovie currentMovie;
	private ScrollView resultsLayout;
	private ProgressBar spinner;
	private Object currentShows;
	final Handler handler = new Handler();
	final Runnable posterExecutor = new Runnable() {
		public void run() {
			displayMovieDetails();
		}
	};
	private TextView showsText;



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.movie);

		title = (TextView) findViewById(R.id.movie_title);
		year = (TextView) findViewById(R.id.movie_year);
		director = (TextView) findViewById(R.id.movie_director);
		rating = (TextView) findViewById(R.id.movie_rating);
		cast = (TextView) findViewById(R.id.movie_cast);
		description = (TextView) findViewById(R.id.movie_description);
		poster = (ImageView) findViewById(R.id.movie_poster);
		resultsLayout = (ScrollView) findViewById(R.id.movie_results);
		spinner = (ProgressBar) findViewById(R.id.movie_loading);
		showsText = (TextView) findViewById(R.id.movie_shows);
	
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
			Object o1 = extras.get(Constants.EXTRAS_KEY_IMDB_ID);
			Object o2 = extras.get(Constants.EXTRAS_KEY_FINNKINO_ID);
			Object shows = null;
			if (extras.containsKey(Constants.EXTRAS_SHOWS)) {
				shows = extras.get(Constants.EXTRAS_SHOWS);
			}
			// Just some glue here..
			if (o1 == null || o2 == null) {
				return;
			}
			final String imdbId = o1.toString();
			currentShows = shows;
			try {
				// Load stuff async
				spinner.setVisibility(View.VISIBLE);
				resultsLayout.setVisibility(View.GONE);
				Thread t = new Thread() {

					public void run() {
						ImdbMovie movie = ImdbJSONParser.create().fetchMovie(imdbId);
						if (movie == null) {
							throw new UnsupportedOperationException();
						}
						currentMovie = movie;
						
						// TODO: Add image cache
						bitmapResult = ImageLoader.loadImage(currentMovie
								.getPosterUrl());
						handler.post(posterExecutor);
					}
				};
				t.start();

			} catch (Exception e) {

				Log.e(XML_PARSER_DEBUG_TAG, "XML Parser Error", e);
			}
		}

	}

	private void displayMovieDetails() {
		spinner.setVisibility(View.GONE);
		resultsLayout.setVisibility(View.VISIBLE);
		if (currentMovie != null) {
			title.setText(currentMovie.getTitle());
			year.setText(String.valueOf(currentMovie.getProductionYear()));
			director.setText(currentMovie.getDirector());
			cast.setText(currentMovie.getActors());
			rating.setText(currentMovie.getRating());
			description.setText(currentMovie.getPlot());
		}
		if (bitmapResult != null) {
			Log.i("MovieDetailsActivity", "Displayin");
			poster.setImageBitmap(bitmapResult);
		}
		
		if (currentShows != null) {
			if (currentShows instanceof List) {
				List<Show> shows = (List<Show>) currentShows;
				String showsText = "";
				for (Show show : shows) {
					try {
						Date start = FINNKINO_DATE_FORMAT.parse(show.getShowStart());
						String location = show.getTheater().trim()+ ", "+show.getTheaterHall().trim();
						showsText += location + " - "+DATUM_FORMAT.format(start) + " "+TIME_FORMAT.format(start) + " \n";
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				this.showsText.setText(showsText);
			}
			Log.i("MovieDetailsActivity", "HASZ SHOWS! "+currentShows);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.movie_details_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
	    switch (item.getItemId()) {
	        case R.id.trailer:
				intent = new Intent(MovieDetailsActivity.this, WebDisplay.class);
				Log.d(XML_PARSER_DEBUG_TAG, "imdbID: " + currentMovie.getId());
				intent.putExtra(Constants.EXTRAS_KEY_IMDB_ID, currentMovie.getId());
				intent.putExtra(Constants.EXTRAS_KEY_MOVIE_TITLE, currentMovie.getTitle());
				MovieDetailsActivity.this.startActivity(intent);
	            return true;
	        case R.id.imdb:
				intent = new Intent(MovieDetailsActivity.this, WebDisplay.class);
				intent.putExtra(Constants.EXTRAS_KEY_IMDB_ID, currentMovie.getId());
				MovieDetailsActivity.this.startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
}