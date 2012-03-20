package androidnfc.movieapp.models;

public class SearchResultMovie {

	private int imdbId;
	private int finnkinoId;
	private String title;
	private String originalTitle;
	private int productionYear;

	public SearchResultMovie() {
		this.title = "unnamed";
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOriginalTitle() {
		return originalTitle;
	}

	public void setOriginalTitle(String originalTitle) {
		this.originalTitle = originalTitle;
	}

	public int getProductionYear() {
		return productionYear;
	}

	public void setProductionYear(int productionYear) {
		this.productionYear = productionYear;
	}

	public String toString() {

		return "Title: " + this.title + "\n" + "Original Title: "
				+ this.originalTitle + "\n" + "Production Year: "
				+ this.productionYear + "\n";

	}

	public int getImdbId() {
		return imdbId;
	}

	public void setImdbId(int imdbId) {
		this.imdbId = imdbId;
	}

	public int getFinnkinoId() {
		return finnkinoId;
	}

	public void setFinnkinoId(int finnkinoId) {
		this.finnkinoId = finnkinoId;
	}

}
