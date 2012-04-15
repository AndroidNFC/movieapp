package androidnfc.movieapp.parsers;

import java.util.LinkedList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;
import androidnfc.movieapp.models.Movie;
import androidnfc.movieapp.models.Show;
import androidnfc.movieapp.models.Trailer;

public class TrailerHandler extends DefaultHandler {

	private List<Trailer> trailerList;
	private Trailer tempTrailer;
	private String tempValue;

	private static final String[] EMPTY_ARRAY = new String[0];

	public TrailerHandler() {

		this.trailerList = new LinkedList<Trailer>();

	}

	/*
	 * Returns a list of parsed trailers.
	 */
	public List<Trailer> getParsedTrailers() {
		return this.trailerList;
	}


	@Override
	public void startDocument() {
		// Do nothing.
	}

	@Override
	public void endDocument() {
		// Do nothing.
	}

	@Override
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes attrs) {
		tempValue = null;
		if (qName.equals("trailers")) {

			this.trailerList = new LinkedList<Trailer>();

		} else if (qName.equals("trailer")) {

			this.tempTrailer = new Trailer();

		}

	}

	@Override
	public void characters(char[] ch, int start, int length) {
		if (tempValue != null) {

			tempValue += new String(ch, start, length);
		} else {
			tempValue = new String(ch, start, length);
		}

	}

	@Override
	public void endElement(String namespaceURI, String localName, String qName) {

		if (qName.equals("trailer")) {

			this.trailerList.add(tempTrailer);

		} else if (qName.equals("title")) {

			this.tempTrailer.setTitle(tempValue);

		} else if (qName.equals("link")) {

			this.tempTrailer.setLink(tempValue);

		} else if (qName.equals("pubDate")) {

			this.tempTrailer.setPublishDate(tempValue);

		} else if (qName.equals("trailer_id")) {

			this.tempTrailer.setTrailerID(Integer.parseInt(tempValue));

		} else if (qName.equals("embed")) {

			this.tempTrailer.setEmbedHTML(tempValue);

		}

	}

}
