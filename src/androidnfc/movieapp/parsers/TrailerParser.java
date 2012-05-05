package androidnfc.movieapp.parsers;

import java.net.URL;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.util.Log;
import androidnfc.movieapp.models.Movie;
import androidnfc.movieapp.models.Trailer;

public class TrailerParser {

	private final String TRAILERADDICT_XML_PARSER_DEBUG_TAG = "TrailerAddictXMLParser";
	
	private int displayWidth;
	
	public TrailerParser(int displayWidth) {
		this.displayWidth = displayWidth;
	}
	
	public List<Trailer> getTrailersByID(String imdbID) {
		URL url;
		try {
			url = new URL("http://api.traileraddict.com/?count=100&imdb=" + imdbID + "&width=" + displayWidth);

			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser parser = spf.newSAXParser();

			XMLReader reader = parser.getXMLReader();
			TrailerHandler handler = new TrailerHandler();
			reader.setContentHandler(handler);

			reader.parse(new InputSource(url.openStream()));
			List<Trailer> trailerList = handler.getParsedTrailers();

			return trailerList;
			
		} catch (Exception e) {
			
			Log.e(TRAILERADDICT_XML_PARSER_DEBUG_TAG, "Failed to parse trailer information.", e);
			
		}
		return null;
	}
	
}
