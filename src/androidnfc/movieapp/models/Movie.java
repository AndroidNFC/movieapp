package androidnfc.movieapp.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Movie {

	private int movieID;
	private String title;
	private String originalTitle;
	private int productionYear;
	private int lengthInMinutes;
	private String dtLocalRelease;
	private String rating; // age limit
	private String ratingLabel;
	private String ratingImageURL;
	private String localDistributorName;
	private String globalDistributorName;
	private String[] productionCompanies;
	private String[] genres;
	private String synopsis;
	private String eventURL;
	private String imageURL;
	private String videoURL;
	private final List<Show> shows = new SerializableShowList();
	
	public class SerializableShowList extends ArrayList<Show> implements Serializable {

		private static final long serialVersionUID = 1L;
		
	}

	
	public Movie() {
		this.title = "unnamed";
	}

	public int getEventID() {
		return this.movieID;
	}

	public void setEventID(int id) {
		this.movieID = id;
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

	public int getLengthInMinutes() {
		return lengthInMinutes;
	}

	public void setLengthInMinutes(int lengthInMinutes) {
		this.lengthInMinutes = lengthInMinutes;
	}

	public String getDtLocalRelease() {
		return dtLocalRelease;
	}

	public void setDtLocalRelease(String dtLocalRelease) {
		this.dtLocalRelease = dtLocalRelease;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getRatingLabel() {
		return ratingLabel;
	}

	public void setRatingLabel(String ratingLabel) {
		this.ratingLabel = ratingLabel;
	}

	public String getRatingImageURL() {
		return ratingImageURL;
	}

	public void setRatingImageURL(String ratingImageURL) {
		this.ratingImageURL = ratingImageURL;
	}

	public String getLocalDistributorName() {
		return localDistributorName;
	}

	public void setLocalDistributorName(String localDistributorName) {
		this.localDistributorName = localDistributorName;
	}

	public String getGlobalDistributorName() {
		return globalDistributorName;
	}

	public void setGlobalDistributorName(String globalDistributorName) {
		this.globalDistributorName = globalDistributorName;
	}

	public String[] getProductionCompanies() {
		return productionCompanies;
	}

	public void setProductionCompanies(String[] productionCompanies) {
		this.productionCompanies = productionCompanies;
	}

	public String[] getGenres() {
		return genres;
	}

	public void setGenres(String[] genres) {
		this.genres = genres;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	public String getEventURL() {
		return eventURL;
	}

	public void setEventURL(String eventURL) {
		this.eventURL = eventURL;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getVideoURL() {
		return this.videoURL;
	}
	
	public void setVideoURL(String videoURL) {
		this.videoURL = videoURL;
	}
	
	private String arrayToString(String[] array) {
		String genreString = "-";
		if (array.length > 0) {
			genreString = "";
			for (int i = 0; i < array.length; i++) {
				genreString += array[i];
				if (i < array.length - 1) {
					genreString += ", ";
				}
			}
		}
		return genreString;
	}
	
	public String toString() {
		
		return "Title: " + this.title + "\n" +
			   "Original Title: " + this.originalTitle + "\n" +
			   "Production Year: " + this.productionYear + "\n" +
			   "Length: " + this.lengthInMinutes + "\n" +
			   "Date: " + this.dtLocalRelease + "\n" +
			   "Rating: " + this.rating + "\n" +
			   "Rating Label: " + this.ratingLabel + "\n" +
			   "Local Distributor: " + this.localDistributorName + "\n" +
			   "Global Distributor: " + this.globalDistributorName + "\n" +
			   "Production Companies: " + arrayToString(this.productionCompanies) + "\n" +
			   "Genres: " + arrayToString(this.genres) + "\n" +
			   "Synopsis: " + this.synopsis + "\n" +
			   "Image URL: " + this.imageURL;
	}

	public List<Show> getShows() {
		return this.shows ;
	}

}
