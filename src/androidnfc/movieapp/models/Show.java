package androidnfc.movieapp.models;

public class Show {

	private Movie movie;
	private String showStart;
	private int theaterID;
	private int hallID;
	private String theater;
	private String theaterHall;
	private String presentationMethodAndLanguage;
	
	public Movie getMovie() {
		return movie;
	}
	public void setMovie(Movie movie) {
		this.movie = movie;
	}
	public String getShowStart() {
		return showStart;
	}
	public void setShowStart(String showStart) {
		this.showStart = showStart;
	}
	public int getTheaterID() {
		return theaterID;
	}
	public void setTheaterID(int theaterID) {
		this.theaterID = theaterID;
	}
	public int getTheaterHallID() {
		return hallID;
	}
	public void setTheaterHallID(int hallID) {
		this.hallID = hallID;
	}
	public String getPresentationMethodAndLanguage() {
		return presentationMethodAndLanguage;
	}
	public void setPresentationMethodAndLanguage(String presentationMethodAndLanguage) {
		this.presentationMethodAndLanguage = presentationMethodAndLanguage;
	}
	public String getTheater() {
		return theater;
	}
	public void setTheater(String theater) {
		this.theater = theater;
	}
	public String getTheaterHall() {
		return theaterHall;
	}
	public void setTheaterHall(String hall) {
		this.theaterHall = hall;
	}
	
	public String toString() {
		
		return "Title: " + this.movie.getTitle() + "\n" +
			   "Show start: " + this.getShowStart() + "\n" +
			   "Theater: " + this.getTheater() + "\n" +
			   "Hall: " + this.getTheaterHall();
			   
	}

}
