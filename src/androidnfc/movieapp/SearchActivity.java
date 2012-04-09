package androidnfc.movieapp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebSettings.PluginState;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidnfc.movieapp.common.Constants;
import androidnfc.movieapp.common.ImageLoader;
import androidnfc.movieapp.models.ImdbMovie;
import androidnfc.movieapp.models.SearchResultMovie;
import androidnfc.movieapp.parsers.ImdbJSONParser;

public class SearchActivity extends Activity {

	private Button nfcTagButton;
	private Button xmlParserButton;
	private Button openBrowserButton;
	private Button openMapButton;
	private Button openVideoButton;
	private LinearLayout resultLayout;
	private ProgressBar spinner;
	private ListView resultList;
	public static String trailerID = ""; 
	private List<SearchResultMovie> results;

	final Handler handler = new Handler();
	final Runnable executor = new Runnable() {
		public void run() {
			setSearchResults();
		}
	};


	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		resultLayout = (LinearLayout) findViewById(R.id.resultLayout);
		resultList = (ListView) findViewById(R.id.resultLayout_list);
		spinner = (ProgressBar) findViewById(R.id.searchProgressBar);
		spinner.setVisibility(View.GONE);
		resultLayout.setVisibility(View.GONE);
		// TODO Glue for back-button. This should be integrated in some
		// TopPanelView-widget or so
		{
			ImageView back = (ImageView) findViewById(R.id.topbar_back);
			back.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					finish();
				}
			});
		}

		final EditText searchField = (EditText) findViewById(R.id.searchfield);
		ImageView image = (ImageView) findViewById(R.id.searchButton);
		image.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String text = searchField.getText().toString();
				search(text);
			}
		});
		searchField.setOnKeyListener(new OnKeyListener() {
			
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (KeyEvent.KEYCODE_ENTER == keyCode) {
					String text = searchField.getText().toString();
					search(text);
					return true;
				}
			
				return false;
			}
		});
	}

	private void search(final String text) {
		
		Log.d("search", "Searching for " + text);
		if (text.isEmpty()) {
			return;
		}
		spinner.setVisibility(View.VISIBLE);
		resultLayout.setVisibility(View.GONE);
		Thread t = new Thread() {
			public void run() {
				List<SearchResultMovie> t = ImdbJSONParser.create().search(text);
				results = t;
				
				handler.post(executor);
			}
		};
		t.start();

	}

	private void setSearchResults() {
		spinner.setVisibility(View.GONE);
		resultLayout.setVisibility(View.VISIBLE);
		if (results == null || results.size() == 0)  {
			resultList.setAdapter(null);
			return;
		}
		

		final SearchResultMovie[] res = new SearchResultMovie[results.size()];
		ResultArrayAdapter adapter = new ResultArrayAdapter(
				getApplicationContext(), results.toArray(res));
		resultList.setAdapter(adapter);

		resultList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				SearchResultMovie model = res[position];

				Intent intent = new Intent(SearchActivity.this,
						MovieDetailsActivity.class);
				intent.putExtra(Constants.EXTRAS_KEY_IMDB_ID,
						model.getImdbId());
				intent.putExtra(Constants.EXTRAS_KEY_FINNKINO_ID,
						model.getFinnkinoId());
				SearchActivity.this.startActivity(intent);
			}
		});
		
	}

	public class ResultArrayAdapter extends ArrayAdapter<SearchResultMovie> {
		private final Context context;
		private final SearchResultMovie[] values;

		public ResultArrayAdapter(Context context, SearchResultMovie[] values) {
			super(context, R.layout.searchresult, values);
			this.context = context;
			this.values = values;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.searchresult, parent,
					false);
			TextView name = (TextView) rowView.findViewById(R.id.resultName);
			TextView desc = (TextView) rowView
					.findViewById(R.id.resultDescription);
			name.setText(values[position].getTitle());
			desc.setText(String.valueOf(values[position].getDescription()));
			return rowView;
		}
	}

}
