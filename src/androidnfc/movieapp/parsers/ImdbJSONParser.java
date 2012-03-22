package androidnfc.movieapp.parsers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.util.Log;
import androidnfc.movieapp.models.ImdbMovie;
import androidnfc.movieapp.models.Movie;
import androidnfc.movieapp.models.SearchResultMovie;

public class ImdbJSONParser {

	private final String DEBUG_TAG = "ImdbJSONParser";
	private final String IMDBAPI_URL = "http://www.imdbapi.com/";
	private final String SEARCH_API_URL = "http://www.imdb.com/xml/find?json=1&nr=1&tt=on&q=";
	
	public List<SearchResultMovie> search(String text){
		String json = getHttpJsonResponse(SEARCH_API_URL+text);
		List<SearchResultMovie> result = new ArrayList<SearchResultMovie>();
		try {
			if (json != null && json.length() > 2) {
				JSONObject a = new JSONObject(json);
				JSONArray popular = a.getJSONArray("title_popular");
				if (popular != null) {
					for (int i=0; i < popular.length(); i++) {
						JSONObject o = popular.getJSONObject(i);
						SearchResultMovie r = new SearchResultMovie();
						r.setImdbId(o.getString("id"));
						r.setDescription(o.getString("title_description"));
						r.setTitle(o.getString("title"));
					}
				}
			}
		} catch (Exception e) {
			Log.e(DEBUG_TAG, "Error searching "+text);
		}
		
		return result;
	}
	
	public ImdbMovie fetchMovie(String id) {

		try {		
			String json = getHttpJsonResponse(IMDBAPI_URL+"?plot=full&i="+id);
			if (json != null && json.length() > 2 && !json.contains("Incorrect IMDb ID") && !json.contains("Parse Error") ) {
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
				return m;
			} else {
				Log.e(DEBUG_TAG, "Error in parsing "+id);
			}
			return null;
		} catch (Exception e) {
			
			Log.e(DEBUG_TAG, "Failed to parse "+id, e);
			
		}
		return null;
	}
	
	private String getHttpJsonResponse(String url) {
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(entity.getContent()));
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
