package androidnfc.movieapp;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import androidnfc.movieapp.models.Movie;
import androidnfc.movieapp.parsers.FinnkinoParser;
import androidnfc.movieapp.parsers.FinnkinoHandler;

public class MainCoverflowTask extends AsyncTask<String, Void, List<Movie>> {

	private final String FETCH_XML_TASK_DEBUG_TAG = "FetchFinnkinoXMLTask";
	
	Activity activity;
    List<Movie> movies;
    List<ImageView> coverImages;
    ImageAdapter coverImageAdapter;

	private CoverFlow coverFlow;
	private TextView movieTitleText;
	private ImageView emptyCover;

	private ProgressBar progressBar;
	
	public MainCoverflowTask(Activity activity) {
		this.activity = activity;
		movies = new ArrayList<Movie>();
		coverImages = new LinkedList<ImageView>();
	}
	
	@Override
	protected void onPreExecute() {
		// Show progress dialog.
		progressBar = (ProgressBar) activity.findViewById(R.id.main_progressBar);
	}
	
	@Override
	protected List<Movie> doInBackground(String... urls) {
		
        coverFlow = (CoverFlow) activity.findViewById(R.id.main_coverflow);
        movieTitleText = (TextView) activity.findViewById(R.id.main_movietitle);
        emptyCover = (ImageView) activity.findViewById(R.id.main_emptycover);
		
        FinnkinoParser parser = new FinnkinoParser();
        this.movies = parser.getUpcomingEvents();
        return this.movies;
	}
	
	@Override
	protected void onPostExecute(List<Movie> movies) {
		
        // Create cover images.
		for (Movie movie : movies) {
			
			String imageURLString = movie.getImageURL();
			if (URLUtil.isHttpUrl(imageURLString)) {
			
				try {
					
					URL imageURL = new URL(movie.getImageURL());
					
					try {
						
						Bitmap bitmap;
						bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
						ImageView imageView = new ImageView(activity);
						imageView.setImageBitmap(bitmap);
						coverImages.add(imageView);
						
					} catch (IOException e) {
						Log.e(FETCH_XML_TASK_DEBUG_TAG, "Failed to decode bitmap image.", e);
					}
					
				} catch (MalformedURLException e) {
					Log.e(FETCH_XML_TASK_DEBUG_TAG, "XML contained a malformed URL: " + movie.getImageURL(), e);
				}
			
			}
			
		}
		
		int coverCount = coverImages.size();
		int initialCoverPos = coverCount / 2;
		
		Log.d(FETCH_XML_TASK_DEBUG_TAG, "Number of cover images: " + coverCount);
		Log.d(FETCH_XML_TASK_DEBUG_TAG, "ID of initially selected cover image: " + initialCoverPos);
        
        coverImageAdapter = new ImageAdapter(activity);
        coverImageAdapter.loadImages(coverImages);
        coverFlow.setAdapter(coverImageAdapter);
        
		// Some configuration options.
        coverFlow.setEmptyView(emptyCover);
        coverFlow.setSpacing(0);
        coverFlow.setSelection(initialCoverPos, false);
        coverFlow.setAnimationDuration(500);
        coverFlow.setOnItemSelectedListener(new coverSelectedListener());
        
        movieTitleText.setText(movies.get(initialCoverPos).getTitle());
		
        // Remove progress bar.
        progressBar.setVisibility(View.GONE);
	}
	
    private final class coverSelectedListener implements OnItemSelectedListener {
    	
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			Log.d(FETCH_XML_TASK_DEBUG_TAG, "position: " + position + ", id: " + id);
			movieTitleText.setText(MainCoverflowTask.this.movies.get(position).getTitle());
		}
		public void onNothingSelected(AdapterView<?> arg0) {
			// Do nothing?
		}
		
    }
	
}