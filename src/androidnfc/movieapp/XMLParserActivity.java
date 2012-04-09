package androidnfc.movieapp;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidnfc.movieapp.models.Movie;
import androidnfc.movieapp.parsers.FinnkinoHandler;

public class XMLParserActivity extends Activity {
	
	private final String XML_PARSER_DEBUG_TAG = "XMLParserActivity";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        TextView tv = new TextView(this);
        tv.setText("");
        setContentView(tv);
        
        try {
        	
			URL url = new URL ("http://www.finnkino.fi/xml/Events/?eventID=298876");
		
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser parser = spf.newSAXParser();
			
			XMLReader reader = parser.getXMLReader();
			FinnkinoHandler handler = new FinnkinoHandler();
			reader.setContentHandler(handler);
			
			reader.parse(new InputSource(url.openStream()));
			List<Movie> movieList = handler.getParsedMovies();
			
			for (Movie movie : movieList) {
				tv.append(movie.toString());
			}
			
        } catch (Exception e) {
			tv.setText("Error: " + e.getMessage());
			Log.e(XML_PARSER_DEBUG_TAG, "XML Parser Error", e);
		}
        
        
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