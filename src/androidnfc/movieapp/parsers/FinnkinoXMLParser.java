package androidnfc.movieapp.parsers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import androidnfc.movieapp.models.Movie;

public class FinnkinoXMLParser {

	public List<Movie> parse(int id) {
		URL url;
		try {
			url = new URL("http://www.finnkino.fi/xml/Events/?eventID="+id);

			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser parser = spf.newSAXParser();

			XMLReader reader = parser.getXMLReader();
			MovieEventHandler handler = new MovieEventHandler();
			reader.setContentHandler(handler);

			reader.parse(new InputSource(url.openStream()));
			List<Movie> movieList = handler.getParsedMovies();

			return movieList;
		} catch (Exception e) {
			// TODO HAndle exceptions
			e.printStackTrace();
		}
		return null;
	}
}
