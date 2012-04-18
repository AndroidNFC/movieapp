package androidnfc.movieapp;

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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.ImageView;
import android.widget.AdapterView.OnItemSelectedListener;
import androidnfc.movieapp.common.Constants;
import androidnfc.movieapp.models.Movie;
import androidnfc.movieapp.models.SearchResultMovie;

public class MovieappActivity extends Activity {

	private CoverFlow coverFlow;
	private TextView movieTitleText;
	private ImageView emptyCover;
	private SearchResultMovie selectedMovie;
	private MainCoverflowTask coverflowTask;

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

		coverFlow = (CoverFlow) findViewById(R.id.main_coverflow);
		movieTitleText = (TextView) findViewById(R.id.main_movietitle);
		emptyCover = (ImageView) findViewById(R.id.main_emptycover);

		movieTitleText.setText("");
		movieTitleText.setGravity(Gravity.CENTER_HORIZONTAL);

		coverflowTask = new MainCoverflowTask(this, new CoverSelectedListener());
		coverflowTask.execute("http://www.finnkino.fi/xml/Schedule/");
	}

	private void setSelectedMovie(SearchResultMovie movie) {
		this.selectedMovie = movie;
		movieTitleText.setText(movie.getTitle());
		
		Intent intent = new Intent(MovieappActivity.this,
				MovieDetailsActivity.class);
		intent.putExtra(Constants.EXTRAS_KEY_IMDB_ID,
				movie.getImdbId());
		intent.putExtra(Constants.EXTRAS_KEY_FINNKINO_ID,
				movie.getFinnkinoId());
		this.startActivity(intent);
	}
	
	
	public final class CoverSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			
			SearchResultMovie movie = coverflowTask.getMovies().get(position);
			setSelectedMovie(movie);
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			// Do nothing?
		}

	}
}
