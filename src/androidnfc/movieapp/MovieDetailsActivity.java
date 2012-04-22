package androidnfc.movieapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
	private LinearLayout scheduleRoot;
	private LinearLayout scheduleData;
	private TextView scheduleDate;



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
		scheduleRoot = (LinearLayout) findViewById(R.id.movie_schedule_layout);
		scheduleData = (LinearLayout) findViewById(R.id.movie_schedule_data_layout);
		scheduleDate = (TextView) findViewById(R.id.movie_theater_date);
	
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
			scheduleData.removeAllViews();
			if (currentShows instanceof List) {
				
				scheduleRoot.setVisibility(View.VISIBLE);
				
				List<Show> shows = (List<Show>) currentShows;
				Map<String, Map<String, List<Date>>> map = new HashMap<String, Map<String,List<Date>>>();
				Context c = this.getApplicationContext();
				for (Show show : shows) {
					try {
						String d = show.getShowStart().split("T")[0];
						
						Date start = FINNKINO_DATE_FORMAT.parse(show.getShowStart());
						String location = show.getTheater().trim()+ " "+show.getTheaterHall().trim();
						if (!map.containsKey(d)) {
							Map<String, List<Date>> dmap = new HashMap<String, List<Date>>();
							map.put(d, dmap);
						}
						
						Map<String, List<Date>> dmap = map.get(d);
						List<Date> l;
						if (dmap.containsKey(location)) {
							l = dmap.get(location);
						} else {
							l = new ArrayList<Date>(3);
							dmap.put(location, l);
						}
						l.add(start);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				
				
				
				for (String key : map.keySet()) {
					Map<String, List<Date>> dmap = map.get(key);
					for (String location : dmap.keySet()) {
						
						List<Date> l = dmap.get(location);
						// TEMPORARY GLUE
						{
						scheduleDate.setText(DATUM_FORMAT.format(l.get(0)));
						}
						TextView description = new TextView(c);
						description.setText(location+":");
						description.setTextColor(Color.parseColor("#a4a4a4"));
						description.setPadding(0, 5, 5, 2);
						scheduleData.addView(description);
						
						int i = 0;
						String dateString = "";
						for (Date date : l) {
							dateString += TIME_FORMAT.format(date);
							if (i < l.size()-1) {
								dateString += ", ";
							}
							i++;
						}
						TextView data = new TextView(c);
						data.setText(dateString);
						data.setTextColor(Color.parseColor("#ffffff"));
						scheduleData.addView(data);
					}
				}
				//this.showsText.setText(showsText);
			}
			Log.i("MovieDetailsActivity", "HASZ SHOWS! "+currentShows);
		} else {
			scheduleRoot.setVisibility(View.GONE);
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