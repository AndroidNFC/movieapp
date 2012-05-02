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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.ImageView;
import android.widget.AdapterView.OnItemSelectedListener;
import androidnfc.movieapp.common.Constants;
import androidnfc.movieapp.models.Movie;
import androidnfc.movieapp.models.SearchResultMovie;
import androidnfc.movieapp.parsers.ImdbJSONParser;

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
			back.setImageResource(R.drawable.ic_launcher);

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
		coverFlow.setUnselectedAlpha(0.7f);
		movieTitleText = (TextView) findViewById(R.id.main_movietitle);
		emptyCover = (ImageView) findViewById(R.id.main_emptycover);

		coverFlow.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int i,
					long l) {
				click();

			}
		});
		movieTitleText.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				click();
			}
		});
		movieTitleText.setText("");
		movieTitleText.setGravity(Gravity.CENTER_HORIZONTAL);

		coverflowTask = new MainCoverflowTask(this, new CoverSelectedListener());
		coverflowTask.execute("http://www.finnkino.fi/xml/Schedule/");
	}

	private void setSelectedMovie(SearchResultMovie movie) {
		this.selectedMovie = movie;
		movieTitleText.setText(movie.getTitle());
	}

	public final class CoverSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			coverFlow.setSelection(position);
			SearchResultMovie movie = coverflowTask.getMovies().get(position);
			setSelectedMovie(movie);
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			// Do nothing?
		}

	}

	private void click() {
		Intent intent = new Intent(MovieappActivity.this,
				MovieDetailsActivity.class);
		if (selectedMovie.getImdbId() == null) {
			ImdbJSONParser parser = ImdbJSONParser.create();
			String originalTitle = selectedMovie.getTitle();
			List<SearchResultMovie> searched = parser.search(originalTitle);
			if (searched.size() == 0) {
				Log.e("MOVIEAPP", "No search result for movie " + originalTitle);
				return;
			} else {
				SearchResultMovie movie = searched.get(0);
				selectedMovie.setImdbId(movie.getImdbId());
			}
		}

		intent.putExtra(Constants.EXTRAS_KEY_IMDB_ID, selectedMovie.getImdbId());
		intent.putExtra(Constants.EXTRAS_KEY_FINNKINO_ID,
				selectedMovie.getFinnkinoId());
		intent.putExtra(Constants.EXTRAS_FINNKINO_DATA,
				selectedMovie);
		startActivity(intent);
	}
}
