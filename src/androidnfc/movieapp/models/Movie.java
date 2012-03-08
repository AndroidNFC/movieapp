package androidnfc.movieapp.models;

public class Movie {

	private String title;
	
	public Movie() {
		this.title = "unnamed";
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String toString() {
		return this.title;
	}

}
