package androidnfc.movieapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidnfc.movieapp.common.Constants;
import androidnfc.movieapp.common.ImageLoader;
import androidnfc.movieapp.models.ImdbMovie;
import androidnfc.movieapp.models.SearchResultMovie;
import androidnfc.movieapp.models.Show;
import androidnfc.movieapp.parsers.ImdbJSONParser;

public class MovieDetailsActivity extends Activity {

	private final String XML_PARSER_DEBUG_TAG = "XMLParserActivity";
	private final String TICKETS_URL_PREFIX = "https://m.finnkino.fi/Websales/Movie/";
	private final String IMDB_URL_PREFIX = "http://www.imdb.com/title/";
	private final String TICKETS_URL_POSTFIX = "/?dt=";
	private final SimpleDateFormat WEBSALES_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy", Locale.UK);
	private final SimpleDateFormat FINNKINO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.UK);
	private final SimpleDateFormat SPINNER_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
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
	private Spinner scheduleDate;
	private TextView ticketText;
	private ImageView ticketImage;

	private String currentFinnkinoId;
	private ArrayAdapter<CharSequence> dataAdapter;

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
		scheduleDate = (Spinner) findViewById(R.id.movie_theater_date);
		ticketText = (TextView) findViewById(R.id.tickets_text);
		ticketImage = (ImageView) findViewById(R.id.tickets_image);

		dataAdapter = new ArrayAdapter<CharSequence>(getApplicationContext(), android.R.layout.simple_spinner_item);

		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		scheduleDate.setAdapter(dataAdapter);

		OnClickListener l = new View.OnClickListener() {

			public void onClick(View v) {
				if (currentFinnkinoId != null) {
					String postfix = "";
					try {
						Date d = SPINNER_DATE_FORMAT.parse(scheduleDate.getSelectedItem().toString());
						postfix = TICKETS_URL_POSTFIX + WEBSALES_DATE_FORMAT.format(d);
					} catch (Exception e) {
						// TODO: handle exception
					}

					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(TICKETS_URL_PREFIX + currentFinnkinoId + postfix));
					startActivity(browserIntent);
				}

			}
		};

		ticketText.setOnClickListener(l);
		ticketImage.setOnClickListener(l);

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
					Intent intent = new Intent(MovieDetailsActivity.this, SearchActivity.class);
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
			final Object o1 = extras.get(Constants.EXTRAS_KEY_IMDB_ID);
			final Object o2 = extras.get(Constants.EXTRAS_KEY_FINNKINO_ID);

			// Just some glue here..
			if (o1 == null || o2 == null) {
				return;
			}
			final String imdbId = o1.toString();
			final Object finnkinoData = extras.get(Constants.EXTRAS_FINNKINO_DATA);

			final SearchResultMovie movieData = (SearchResultMovie) finnkinoData;

			if (movieData != null) {
				// Log.i("DEBUG", "Moviedata: " + movieData.getShows().size());
				currentShows = movieData.getShows();
			}

			try {
				// Load stuff async
				spinner.setVisibility(View.VISIBLE);
				resultsLayout.setVisibility(View.GONE);
				Thread t = new Thread() {

					@Override
					public void run() {
						ImdbMovie movie = ImdbJSONParser.create().fetchMovie(imdbId);
						if (movie == null) {
							MovieDetailsActivity.this.runOnUiThread(new Runnable() {

								public void run() {
									spinner.setVisibility(View.GONE);
									Toast.makeText(getApplicationContext(), "Failed to retrieve IMDB data!", Toast.LENGTH_LONG).show();
								}
							});
							movie = new ImdbMovie();
							movie.setId(imdbId);
							if (movieData != null) {
								movie.setTitle(movieData.getTitle());
								movie.setPosterUrl(movieData.getImageURL());
							}
						}
						currentMovie = movie;

						if (o2.toString() != null) {
							currentFinnkinoId = o2.toString();
						} else {
							currentFinnkinoId = null;
						}

						// TODO: Add image cache
						bitmapResult = ImageLoader.loadImage(currentMovie.getPosterUrl());
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
		scheduleRoot.setVisibility(View.VISIBLE);
		if (currentShows != null) {
			scheduleData.removeAllViews();
			if (currentShows instanceof List) {

				List<Show> shows = (List<Show>) currentShows;
				final Map<String, Map<String, List<Date>>> map = new HashMap<String, Map<String, List<Date>>>();
				final Context c = this.getApplicationContext();
				for (Show show : shows) {
					try {
						String d = show.getShowStart().split("T")[0];

						Date start = FINNKINO_DATE_FORMAT.parse(show.getShowStart());
						String location = show.getTheater().trim() + " " + show.getTheaterHall().trim();
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

				dataAdapter.clear();

				scheduleDate.setOnItemSelectedListener(new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
						String key = parent.getItemAtPosition(pos).toString();
						Map<String, List<Date>> dmap = map.get(key);
						scheduleData.removeAllViews();
						for (String location : dmap.keySet()) {

							List<Date> l = dmap.get(location);
							TextView description = new TextView(c);
							description.setText(location + ":");
							description.setTextColor(Color.parseColor("#a4a4a4"));
							description.setPadding(0, 5, 5, 2);
							scheduleData.addView(description);

							int i = 0;
							String dateString = "";
							for (Date date : l) {
								dateString += TIME_FORMAT.format(date);
								if (i < l.size() - 1) {
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

					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}

				});
				Set<String> keys = map.keySet();
				String[] sorted = new String[keys.size()];
				Arrays.sort(keys.toArray(sorted));
				for (String key : sorted) {
					dataAdapter.add(key);
				}
				scheduleDate.setSelection(0);
			}
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
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse(IMDB_URL_PREFIX + currentMovie.getId()));
			MovieDetailsActivity.this.startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}