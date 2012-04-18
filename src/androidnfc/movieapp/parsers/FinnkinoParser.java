package androidnfc.movieapp.parsers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.util.Log;
import androidnfc.movieapp.models.Movie;
import androidnfc.movieapp.models.Show;

public class FinnkinoParser {

	private final String FINNKINO_PARSER_DEBUG_TAG = "MovieEventHandler";
	
	public List<Movie> getEventsByID(int eventID) {
		URL url;
		try {
			url = new URL("http://www.finnkino.fi/xml/Events/?eventID=" + eventID);

			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser parser = spf.newSAXParser();

			XMLReader reader = parser.getXMLReader();
			FinnkinoHandler handler = new FinnkinoHandler();
			reader.setContentHandler(handler);

			reader.parse(new InputSource(url.openStream()));
			List<Movie> movieList = handler.getParsedMovies();
			
			
			return movieList;
			
		} catch (Exception e) {
			
			Log.e(FINNKINO_PARSER_DEBUG_TAG, "Failed to parse event.", e);
			
		}
		return null;
	}
	
	public List<Movie> getUpcomingEvents() {
		
		List<Movie> movieList = new ArrayList<Movie>();
        try {
        	
        	URL url = new URL ("http://www.finnkino.fi/xml/Schedule/");
		
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser parser = spf.newSAXParser();
			
			XMLReader reader = parser.getXMLReader();
			FinnkinoHandler handler = new FinnkinoHandler();
			reader.setContentHandler(handler);
			
			reader.parse(new InputSource(url.openStream()));
			movieList = handler.getParsedMovies();
		} catch (Exception e) {
			Log.e(FINNKINO_PARSER_DEBUG_TAG, "XML Parser Error", e);
		}
        
        return movieList;
		
	}
	
}
