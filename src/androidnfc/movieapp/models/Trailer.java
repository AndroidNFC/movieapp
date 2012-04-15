package androidnfc.movieapp.models;

public class Trailer {

	private String title;
	private String link;
	private String publishDate;
	private int trailerID;
	private String embedHTML;
	
	public String getTitle() {
		return title;
	}

	public String getLink() {
		return link;
	}

	public String getPublishDate() {
		return publishDate;
	}

	public int getTrailerID() {
		return trailerID;
	}

	public String getEmbedHTML() {
		return embedHTML;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}

	public void setTrailerID(int trailerID) {
		this.trailerID = trailerID;
	}

	public void setEmbedHTML(String embedHTML) {
		this.embedHTML = embedHTML;
	}

}
