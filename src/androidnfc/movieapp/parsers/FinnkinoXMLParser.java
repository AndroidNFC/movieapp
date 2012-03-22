package androidnfc.movieapp.parsers;

import java.net.URL;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.util.Log;
import androidnfc.movieapp.models.Movie;

public class FinnkinoXMLParser {

	private final String FINNKINO_XML_PARSER_DEBUG_TAG = "MovieEventHandler";
	
	public List<Movie> parse(int eventID) {
		URL url;
		try {
			url = new URL("http://www.finnkino.fi/xml/Events/?eventID=" + eventID);

			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser parser = spf.newSAXParser();

			XMLReader reader = parser.getXMLReader();
			MovieHandler handler = new MovieHandler();
			reader.setContentHandler(handler);

			reader.parse(new InputSource(url.openStream()));
			List<Movie> movieList = handler.getParsedMovies();

			return movieList;
			
		} catch (Exception e) {
			
			Log.e(FINNKINO_XML_PARSER_DEBUG_TAG, "Failed to parse event.", e);
			
		}
		return null;
	}
}
