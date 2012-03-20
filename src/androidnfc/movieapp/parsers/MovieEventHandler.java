package androidnfc.movieapp.parsers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;
import androidnfc.movieapp.models.Movie;

public class MovieEventHandler extends DefaultHandler {
	
	private List<Movie> movieList;
	private Movie tempMovie;
	private String tempValue;
	
	private final String MOVIE_EVENT_DEBUG_TAG = "MovieEventHandler";
	
	private static final String[] EMPTY_ARRAY = new String[0];
	
	public List<Movie> getParsedMovies() {
		return this.movieList;
	}

	@Override
	public void startDocument() {
		// Do nothing.
	}
	
	@Override
	public void endDocument() {
		
		Log.d(MOVIE_EVENT_DEBUG_TAG, String.format("In endDocument(). %d movies read.", movieList.size()));
		
		// Do nothing.
	}
	
	@Override
	public void startElement(String namespaceURI, String localName,
							 String qName, Attributes attrs) {
		
		if (qName.equals("Events")) {
			
			this.movieList = new LinkedList<Movie>();
			
		} else if (qName.equals("Event")) {
			
			this.tempMovie = new Movie();
			
		}
		
	}
	
	@Override
	public void characters(char[] ch, int start, int length) {
		tempValue = new String(ch, start, length);
	}
	
	@Override
	public void endElement(String namespaceURI, String localName, String qName) {
		
		if (qName.equals("Event")) {
			
			this.movieList.add(tempMovie);
			
		} else if (qName.equals("Title")) {
			
			this.tempMovie.setTitle(tempValue);
			
		} else if (qName.equals("OriginalTitle")) {
			
			this.tempMovie.setOriginalTitle(tempValue);
			
		} else if (qName.equals("ProductionYear")) {
			
			this.tempMovie.setProductionYear(Integer.parseInt(tempValue));
			
		} else if (qName.equals("LengthInMinutes")) {
			
			this.tempMovie.setLengthInMinutes(Integer.parseInt(tempValue));
			
		} else if (qName.equals("dtLocalRelease")) {
			
			this.tempMovie.setDtLocalRelease(tempValue);
			
		} else if (qName.equals("Rating")) {
			
			this.tempMovie.setRating(Integer.parseInt(tempValue));
			
		} else if (qName.equals("RatingLabel")) {
			
			this.tempMovie.setRatingLabel(tempValue);
			
		} else if (qName.equals("RatingImageUrl")) {
			
			this.tempMovie.setRatingImageURL(tempValue);
			
		} else if (qName.equals("LocalDistributorName")) {
			
			this.tempMovie.setLocalDistributorName(tempValue);
			
		} else if (qName.equals("GlobalDistributorName")) {
			
			this.tempMovie.setGlobalDistributorName(tempValue);
			
		} else if (qName.equals("ProductionCompanies")) {
			
			if (!tempValue.equals("-")) {
				String[] companies = tempValue.split(",");
				for (String str : companies) {
					str = str.trim();
				}
				this.tempMovie.setProductionCompanies(companies);
			} else {
				this.tempMovie.setProductionCompanies(EMPTY_ARRAY);
			}
			
		} else if (qName.equals("Genres")) {
			
			if (!tempValue.equals("-")) {
				String[] genres = tempValue.split(",");
				for (String str : genres) {
					str = str.trim();
				}
				this.tempMovie.setGenres(genres);
			} else {
				this.tempMovie.setGenres(EMPTY_ARRAY);
			}
			
		} else if (qName.equals("Synopsis")) {
			
			this.tempMovie.setSynopsis(tempValue);
			
		} else if (qName.equals("EventURL")) {
			
			this.tempMovie.setEventURL(tempValue);
			
		}
		
	}
	
}
