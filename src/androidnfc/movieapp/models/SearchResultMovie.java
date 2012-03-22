package androidnfc.movieapp.models;

public class SearchResultMovie {

	private String imdbId;
	private int finnkinoId;
	private String title;
	private String description;

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

}
