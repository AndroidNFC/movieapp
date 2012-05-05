package androidnfc.movieapp;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import androidnfc.movieapp.MovieappActivity.CoverSelectedListener;
import androidnfc.movieapp.MovieappActivity.FlowUpdatedListener;
import androidnfc.movieapp.common.Constants;
import androidnfc.movieapp.common.ImageLoader;
import androidnfc.movieapp.models.ImdbMovie;
import androidnfc.movieapp.models.Movie;
import androidnfc.movieapp.models.SearchResultMovie;
import androidnfc.movieapp.parsers.FinnkinoParser;
import androidnfc.movieapp.parsers.FinnkinoHandler;
import androidnfc.movieapp.parsers.ImdbJSONParser;

public class MainCoverflowTask extends
		AsyncTask<String, Void, List<SearchResultMovie>> {

	private final String FETCH_XML_TASK_DEBUG_TAG = "FetchFinnkinoXMLTask";

	Activity activity;
	private List<SearchResultMovie> movies;
	ImageAdapter coverImageAdapter;

	private CoverFlow coverFlow;
	private TextView movieTitleText;
	private ImageView emptyCover;

	private ProgressBar progressBar;

	private Movie selectedMovie;

	private CoverSelectedListener listener;

	private FlowUpdatedListener updateListener;
	
	public MainCoverflowTask(Activity activity,
			CoverSelectedListener coverSelectedListener, FlowUpdatedListener updateListener) {
		this.activity = activity;
		movies = new ArrayList<SearchResultMovie>();
		coverImageAdapter = new ImageAdapter(activity);
		listener = coverSelectedListener;
		this.updateListener = updateListener;
	}

	@Override
	protected void onPreExecute() {
		// Show progress dialog.
		progressBar = (ProgressBar) activity
				.findViewById(R.id.main_progressBar);
	}

	@Override
	protected List<SearchResultMovie> doInBackground(String... urls) {

		coverFlow = (CoverFlow) activity.findViewById(R.id.main_coverflow);
		movieTitleText = (TextView) activity.findViewById(R.id.main_movietitle);
		emptyCover = (ImageView) activity.findViewById(R.id.main_emptycover);
		
		FinnkinoParser parser = new FinnkinoParser();
		List<Movie> movies = parser.getUpcomingEvents(new Date(), 7);

		this.movies = constructSearchResults(movies);
		List<ImageView> coverImages = new LinkedList<ImageView>();
		// Create temporary cover images.
		for (SearchResultMovie movie : this.movies) {
			ImageView temp = new ImageView(activity);

			temp.setImageResource(R.drawable.emptyposter);
			temp.setBackgroundColor(Color.parseColor("#6f6f6f"));
			coverImages.add(temp);
		}
		
		coverImageAdapter.loadImages(coverImages);
		//fetchMoviePoster(this.movies.get(1));
	

		return this.movies;
	}

	private ImageView fetchMoviePoster(SearchResultMovie movie) {
		String imageURLString = movie.getImageURL();

		if (URLUtil.isHttpUrl(imageURLString)) {

			try {

				URL imageURL = new URL(movie.getImageURL());

				try {

					Bitmap bitmap;
					bitmap = BitmapFactory.decodeStream(imageURL
							.openConnection().getInputStream());
					ImageView imageView = new ImageView(activity);
					imageView.setImageBitmap(bitmap);
					coverImageAdapter.setItem(this.movies.indexOf(movie), imageView);
					return imageView;

				} catch (IOException e) {
					Log.e(FETCH_XML_TASK_DEBUG_TAG,
							"Failed to decode bitmap image.", e);
				}

			} catch (MalformedURLException e) {
				Log.e(FETCH_XML_TASK_DEBUG_TAG,
						"XML contained a malformed URL: " + movie.getImageURL(),
						e);
			}

		}return null;

	}

	public static List<SearchResultMovie> constructSearchResults(List<Movie> movies) {
		List<SearchResultMovie> result = new ArrayList<SearchResultMovie>();
		for (Movie m : movies) {
			m.getOriginalTitle();
			SearchResultMovie movie = new SearchResultMovie();
			movie.setTitle(m.getOriginalTitle());
			movie.setFinnkinoId(m.getEventID());
			movie.setImageURL(m.getImageURL());
			movie.setShows(m.getShows());
			result.add(movie);

		}

		return result;
	}

	@Override
	protected void onPostExecute(List<SearchResultMovie> movies) {
		if (movies.size() == 0) {
			Log.e("DEBUG", "0 movies!");
			return;
		}
		coverFlow.setAdapter(coverImageAdapter);

		int coverCount = coverImageAdapter.getCount();
		final int initialCoverPos = coverCount / 2;

		Log.d(FETCH_XML_TASK_DEBUG_TAG, "Number of cover images: " + coverCount);
		Log.d(FETCH_XML_TASK_DEBUG_TAG,
				"ID of initially selected cover image: " + initialCoverPos);

		// Some configuration options.
		coverFlow.setEmptyView(emptyCover);
		coverFlow.setSpacing(0);
		coverFlow.setSelection(initialCoverPos, false);
		coverFlow.setAnimationDuration(500);
		coverFlow.setOnItemSelectedListener(listener);
		movieTitleText.setText(movies.get(initialCoverPos).getTitle());

		try {
		
			// Load the rest of the posters in here...
			Thread t = new Thread() {
				public void run() {
					
					List<SearchResultMovie> movies = MainCoverflowTask.this.movies;
					movies.get(initialCoverPos);
					int size = movies.size();
					int pos = initialCoverPos;
					for (int i = 0; i < size; i++) {
						pos = (0 == i % 2) ? pos - i: pos + i;
						if (pos > 0 && pos < size-1) {
							fetchMoviePoster(movies.get(pos));
						}
						if (pos % 3 == 0) {
							updateListener.onFlowUpdate();
						}
					}
					fetchMoviePoster(movies.get(0));
					fetchMoviePoster(movies.get(size-1));
					updateListener.onFlowUpdate();
				}
			};
			t.start();
		} catch (Exception e) {
			Log.e("DEBUG", "Error", e);
		}
		
		// Remove progress bar.
		progressBar.setVisibility(View.GONE);
	}

	public List<SearchResultMovie> getMovies() {
		return movies;
	}

}