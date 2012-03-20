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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.webkit.URLUtil;

import androidnfc.movieapp.models.Movie;
import androidnfc.movieapp.parsers.MovieHandler;

public class MovieScheduleActivity extends Activity {
    
	private final String MOVIE_SCHEDULE_DEBUG_TAG = "MovieScheduleActivity";
	private final String COVER_FLOW_DEBUG_TAG = "CoverFlow";
	
	private CoverFlow coverFlow;
	private TextView movieTitleText;
	private ImageView emptyCover;
	
    List<Movie> movies;
    List<ImageView> coverImages;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule);
        
        coverFlow = (CoverFlow) findViewById(R.id.schedule_coverflow);
        movieTitleText = (TextView) findViewById(R.id.schedule_movietitle);
        emptyCover = (ImageView) findViewById(R.id.schedule_emptycover);
        
        movieTitleText.setText("Title");
        movieTitleText.setGravity(Gravity.CENTER_HORIZONTAL);
        
        movies = new ArrayList<Movie>();
        coverImages = new LinkedList<ImageView>();
        
        try {
        	
			URL url = new URL ("http://www.finnkino.fi/xml/Schedule/");
		
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser parser = spf.newSAXParser();
			
			XMLReader reader = parser.getXMLReader();
			MovieHandler handler = new MovieHandler();
			reader.setContentHandler(handler);
			
			reader.parse(new InputSource(url.openStream()));
			movies = handler.getParsedMovies();
			
        } catch (Exception e) {
			Log.e(MOVIE_SCHEDULE_DEBUG_TAG, "XML Parser Error", e);
		}
        	
        Bitmap placeHolder;
        
		for (Movie movie : movies) {
			
			String imageURLString = movie.getImageURL();
			if (URLUtil.isHttpUrl(imageURLString)) {
			
				try {
					
					URL imageURL = new URL(movie.getImageURL());
					
					try {
						
						Bitmap bitmap;
						bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
						ImageView imageView = new ImageView(this);
						imageView.setImageBitmap(bitmap);
						coverImages.add(imageView);
						
					} catch (IOException e) {
						Log.e(MOVIE_SCHEDULE_DEBUG_TAG, "Failed to decode bitmap image.", e);
					}
					
				} catch (MalformedURLException e) {
					Log.e(MOVIE_SCHEDULE_DEBUG_TAG, "XML contained a malformed URL: " + movie.getImageURL(), e);
				}
			
			}
			
		}
		
		int coverCount = coverImages.size();
		int initialCoverPos = coverCount / 2;
		
		Log.d(MOVIE_SCHEDULE_DEBUG_TAG, "Number of cover images: " + coverCount);
		Log.d(MOVIE_SCHEDULE_DEBUG_TAG, "ID of initially selected cover image: " + initialCoverPos);
        
        ImageAdapter coverImageAdapter = new ImageAdapter(this);
        coverImageAdapter.loadImages(coverImages);
        coverFlow.setAdapter(coverImageAdapter);
        
		// Some configuration options.
        coverFlow.setEmptyView(emptyCover);
        coverFlow.setSpacing(0);
        coverFlow.setSelection(initialCoverPos, false);
        coverFlow.setAnimationDuration(500);
        coverFlow.setOnItemSelectedListener(new OnItemSelectedListener() {
        	
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Log.d(COVER_FLOW_DEBUG_TAG, "position: " + position + ", id: " + id);
				movieTitleText.setText(MovieScheduleActivity.this.movies.get(position).getTitle());
			}
			public void onNothingSelected(AdapterView<?> arg0) {
				// Do nothing?
			}
        	
		});
        
        movieTitleText.setText(MovieScheduleActivity.this.movies.get(initialCoverPos).getTitle());
        
    }
	
    @Override
    protected void onStart() {
        super.onStart();
        // The activity is about to become visible.
    }
    @Override
    protected void onResume() {
        super.onResume();
        // The activity has become visible (it is now "resumed").
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Another activity is taking focus (this activity is about to be "paused").
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


