package androidnfc.movieapp;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.ImageView;
import android.widget.AdapterView.OnItemSelectedListener;
import androidnfc.movieapp.common.Constants;
import androidnfc.movieapp.models.Movie;
import androidnfc.movieapp.models.SearchResultMovie;
import androidnfc.movieapp.parsers.FinnkinoParser;
import androidnfc.movieapp.parsers.ImdbJSONParser;

public class MovieappActivity extends Activity {

	private CoverFlow coverFlow;
	private TextView movieTitleText;
	private SearchResultMovie selectedMovie;
	private MainCoverflowTask coverflowTask;
	String extraData1, extraData2, extraData3, extraData4, extraData5;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Bundle extras = getIntent().getExtras();
		Log.i("DEBUU", "DaEEa: "+extras);
        if (extras != null && savedInstanceState == null)
        {
        	// Just where do we need all these..
        	extraData1 = extras.getString("serviceidmovie1");
	        extraData2 = extras.getString("serviceidmovie2");
	        extraData3 = extras.getString("serviceidmovie3");
	        extraData4 = extras.getString("serviceidmovie4");
	        extraData5 = extras.getString("serviceidmovie5");
	        
	       // extraData4 = "298988"; 
	        
	        //extraData5 = "tt1034314";

	       	        
	        Thread t = new Thread() {
				public void run() {
					FinnkinoParser parser = new FinnkinoParser();
					List<Movie> movies = parser.getUpcomingEvents(new Date(), 7, Integer.parseInt(extraData4));
					if (movies == null || movies.size() != 1) {
						return;
					}
					List<SearchResultMovie> searchResults = MainCoverflowTask.constructSearchResults(movies);
					SearchResultMovie m = searchResults.get(0);
					m.setImdbId(extraData5);
					openMovieDetails(m);
				}
			};
			t.start();			
        }
        else 
        {
    		{
    			ImageView back = (ImageView) findViewById(R.id.topbar_back);
    			back.setImageResource(R.drawable.ic_launcher);

    			ImageView search = (ImageView) findViewById(R.id.topbarSearch);
    			search.setOnClickListener(new View.OnClickListener() {

    				public void onClick(View v) {
    					Intent intent = new Intent(MovieappActivity.this, SearchActivity.class);
    					MovieappActivity.this.startActivity(intent);
    				}
    			});
    		}

    		coverFlow = (CoverFlow) findViewById(R.id.main_coverflow);
    		coverFlow.setUnselectedAlpha(0.5f);
    		movieTitleText = (TextView) findViewById(R.id.main_movietitle);

    		coverFlow.setOnItemClickListener(new OnItemClickListener() {

    			public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
    				click(selectedMovie);

    			}
    		});

    		movieTitleText.setOnClickListener(new OnClickListener() {

    			public void onClick(View v) {
    				click(selectedMovie);

    			}
    		});
    		movieTitleText.setText("");
    		movieTitleText.setGravity(Gravity.CENTER_HORIZONTAL);

    		coverflowTask = new MainCoverflowTask(this, new CoverSelectedListener(), new FlowUpdatedListener() {

    			@Override
    			void onFlowUpdate() {
    				MovieappActivity.this.runOnUiThread(new Runnable() {

    					public void run() {
    						((ImageAdapter) coverFlow.getAdapter()).notifyDataSetChanged();
    					}
    				});

    			}
    		});
    		coverflowTask.execute("http://www.finnkino.fi/xml/Schedule/");
    	}
		// TODO Glue for Top Panel. This should be integrated in some
		// TopPanelView-widget or so

	}

	private void setSelectedMovie(SearchResultMovie movie) {
		this.selectedMovie = movie;
		movieTitleText.setText(movie.getTitle());
	}

	private void click(final SearchResultMovie movie) {
		
		if (movie != null && movie.getImdbId() == null) {
			Thread t = new Thread() {
				public void run() {
					
					openMovieDetails(movie);
				}
			};
			t.start();
		}

		
	}
	private void openMovieDetails(SearchResultMovie movie) {
		final Intent intent = new Intent(MovieappActivity.this, MovieDetailsActivity.class);
		if (movie.getImdbId() == null || movie.getImdbId().isEmpty()) {
			ImdbJSONParser parser = ImdbJSONParser.create();
			String originalTitle = movie.getTitle();
			List<SearchResultMovie> searched = parser.search(originalTitle);
			if (searched.size() == 0) {
				Log.e("MOVIEAPP", "No search result for movie " + originalTitle);
				return;
			} else {
				movie = searched.get(0);
				movie.setImdbId(movie.getImdbId());
			}	
		}
		final SearchResultMovie m = movie;
		runOnUiThread(new Runnable() {
			
			public void run() {
				intent.putExtra(Constants.EXTRAS_KEY_IMDB_ID, m.getImdbId());
				intent.putExtra(Constants.EXTRAS_KEY_FINNKINO_ID, m.getFinnkinoId());
				intent.putExtra(Constants.EXTRAS_FINNKINO_DATA, m);
				startActivity(intent);
				
			}
		});
	}
	public final class CoverSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			coverFlow.setSelection(position);
			SearchResultMovie movie = coverflowTask.getMovies().get(position);
			setSelectedMovie(movie);
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			// Do nothing?
		}
	}

	public abstract class FlowUpdatedListener {

		abstract void onFlowUpdate();
	}
}
