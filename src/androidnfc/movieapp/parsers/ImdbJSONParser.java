package androidnfc.movieapp.parsers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import androidnfc.movieapp.common.HardcodedImdbCache;
import androidnfc.movieapp.models.ImdbMovie;
import androidnfc.movieapp.models.SearchResultMovie;

public class ImdbJSONParser {

	private final String DEBUG_TAG = "ImdbJSONParser";
	private final String IMDBAPI_URL = "http://www.imdbapi.com/";
	private final String SEARCH_API_URL = "http://www.imdb.com/xml/find?json=1&nr=1&tt=on&q=";

	public static ImdbJSONParser create() {
		return new ImdbJSONParser();
	}

	public List<SearchResultMovie> search(String text) {
		try {
			text = URLEncoder.encode(text, "utf-8");
		} catch (Exception e) {
			Log.e(DEBUG_TAG, "Error in url-encoding " + text);
		}

		String json = getHttpJsonResponse(SEARCH_API_URL + text);
		Log.i("MOVIEAPP", json);
		List<SearchResultMovie> result = new ArrayList<SearchResultMovie>();
		try {
			if (json != null && json.length() > 2) {
				JSONObject a = new JSONObject(json);
				if (a.has("title_popular")) {
					JSONArray popular = a.getJSONArray("title_popular");
					for (int i = 0; i < popular.length(); i++) {
						JSONObject o = popular.getJSONObject(i);
						result.add(jsonObjectToMovie(o));
					}
				}
				if (a.has("title_exact")) {
					JSONArray popular = a.getJSONArray("title_exact");
					for (int i = 0; i < popular.length(); i++) {
						JSONObject o = popular.getJSONObject(i);
						result.add(jsonObjectToMovie(o));
					}

				}
				if (a.has("title_approx")) {
					JSONArray approx = a.getJSONArray("title_approx");
					for (int i = 0; (i < approx.length() && i < 3); i++) {
						JSONObject o = approx.getJSONObject(i);
						result.add(jsonObjectToMovie(o));
					}
				}
				if (result.size() == 0) {
					throw new UnsupportedOperationException("No movie found");
				}
			}
		} catch (Exception e) {
			Log.e(DEBUG_TAG, "Error searching " + text);
			e.printStackTrace();
		}

		return result;
	}

	private SearchResultMovie jsonObjectToMovie(JSONObject o) {
		try {
			SearchResultMovie r = new SearchResultMovie();

			r.setImdbId(o.getString("id"));
			String desc = o.getString("title_description");
			desc = TextUtils.join("", TextUtils.split(desc, "<[^>]*>"));
			r.setDescription(desc);
			String titleStr = o.getString("title");

			r.setTitle(Html.fromHtml(titleStr).toString());
			return r;
		} catch (Exception e) {
			Log.e(DEBUG_TAG, "Error parsing object " + o);
			return null;
		}
	}

	public ImdbMovie fetchMovie(String id) {
		String json = "N/A";
		try {
			if (HardcodedImdbCache.STATIC_CACHE.containsKey(id)) {
				json = HardcodedImdbCache.STATIC_CACHE.get(id);
			} else {
				json = getHttpJsonResponse(IMDBAPI_URL + "?plot=full&i=" + id);
			}

			if (json != null && json.length() > 2 && !json.contains("Incorrect IMDb ID") && !json.contains("Parse Error")) {

				JSONObject o = new JSONObject(json);
				ImdbMovie m = new ImdbMovie();
				m.setActors(o.getString("Actors"));
				m.setDirector(o.getString("Director"));
				String genre = o.getString("Genre");
				if (genre != null && genre.length() > 0) {
					String[] genres = genre.split(", ");
					m.setGenres(genres);
				}
				m.setId(id);
				m.setRuntime(o.getString("Runtime"));
				m.setPlot(o.getString("Plot"));
				m.setPosterUrl(o.getString("Poster"));
				m.setProductionYear(o.getInt("Year"));
				m.setRated(o.getString("Rated"));
				m.setRating(o.getString("Rating"));
				m.setReleased(o.getString("Released"));
				if (o.getString("Votes") != null) {
					try {
						m.setVotes(Long.parseLong(o.getString("Votes")));
					} catch (NumberFormatException e) {
						// TODO: handle exception
					}

				}
				m.setWriter(o.getString("Writer"));
				m.setTitle(o.getString("Title"));
				return m;
			} else {
				Log.e(DEBUG_TAG, "Error in parsing " + json);
			}
		} catch (Exception e) {
			Log.i(DEBUG_TAG, "Error in parsing " + json);

		}
		return null;
	}

	private String getHttpJsonResponse(String url) {
		StringBuilder builder = new StringBuilder();
		try {
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, 25000);
			HttpConnectionParams.setSoTimeout(httpParameters, 25000);

			HttpClient client = new DefaultHttpClient(httpParameters);
			HttpGet httpGet = new HttpGet(url);
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
			} else {
				Log.e(DEBUG_TAG, "No data!");
				return null;
			}
		} catch (Exception e) {
			Log.e(DEBUG_TAG, "Err!", e);
			return null;
		}
		return builder.toString();
	}
}
