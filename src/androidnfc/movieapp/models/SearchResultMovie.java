package androidnfc.movieapp.models;

import java.util.List;

public class SearchResultMovie {

	private String imdbId;
	private int finnkinoId;
	private String title;
	private String description;
	private String imageUrl;
	private List<Show> shows;

	public SearchResultMovie() {
		this.title = "unnamed";
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImdbId() {
		return imdbId;
	}

	public void setImdbId(String imdbId) {
		this.imdbId = imdbId;
	}

	public int getFinnkinoId() {
		return finnkinoId;
	}

	public void setFinnkinoId(int finnkinoId) {
		this.finnkinoId = finnkinoId;
	}

	public String getImageURL() {
		return imageUrl;
	}

	public void setImageURL(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public List<Show> getShows() {
		return shows;
	}

	public void setShows(List<Show> shows) {
		this.shows = shows;
	}

}
