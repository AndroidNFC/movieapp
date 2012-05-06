package androidnfc.movieapp.common;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HardcodedImdbCache {

	public static final Map<String, String> STATIC_CACHE;
	static {
		Map<String, String> map = new HashMap<String, String>();
		map.put("tt1034314",
				"{\"Title\":\"Iron Sky\",\"Year\":\"2012\",\"Rated\":\"N/A\",\"Released\":\"4 Apr 2012\",\"Genre\":\"Action, Comedy, Sci-Fi\",\"Director\":\"Timo Vuorensola\",\"Writer\":\"Johanna Sinisalo, Jarmo Puskala\",\"Actors\":\"Julia Dietze, Christopher Kirby, Götz Otto, Tilo Prückner\",\"Plot\":\"The Nazis set up a secret base on the moon in 1945 where they hide out and plan to return to power in 2018.\",\"Poster\":\"http://ia.media-imdb.com/images/M/MV5BMTk5OTczNzQ2NV5BMl5BanBnXkFtZTcwMjU3MTk1NA@@._V1_SX320.jpg\",\"Runtime\":\"N/A\",\"Rating\":\"N/A\",\"Votes\":\"N/A\",\"ID\":\"tt1034314\",\"Response\":\"True\"}");
		map.put("tt1403865",
				"{\"Title\":\"True Grit\",\"Year\":\"2010\",\"Rated\":\"G\",\"Released\":\"11 Jun 2010\",\"Genre\":\"Adventure, Western, Drama\",\"Director\":\"Ethan Coen, Joel Coen\",\"Writer\":\"Ethan Coen, Joel Coen\",\"Actors\":\"Jeff Bridges, Matt Damon, Hailee Steinfeld\",\"Plot\":\"A tough U.S. Marshal helps a stubborn young woman track down her father's murderer.\",\"Poster\":\"http://ia.media-imdb.com/images/M/MV5BMjIxNjAzODQ0N15BMl5BanBnXkFtZTcwODY2MjMyNA@@._V1._SY317_.jpg\",\"Runtime\":\"1 hrs 50 mins\",\"Rating\":\"7.8\",\"Votes\":\"120766\",\"ID\":\"tt1403865\",\"Response\":\"True\"}");
		map.put("tt0065126",
				"{\"Title\":\"True Grit\",\"Year\":\"1969\",\"Rated\":\"G\",\"Released\":\"11 Jun 1969\",\"Genre\":\"Adventure, Western, Drama\",\"Director\":\"Henry Hathaway\",\"Writer\":\"Charles Portis, Marguerite Roberts\",\"Actors\":\"John Wayne, Kim Darby, Glen Campbell, Jeremy Slate\",\"Plot\":\"A drunken, hard-nosed U.S. Marshal and a Texas Ranger help a stubborn young woman track down her father's murderer in Indian territory.\",\"Poster\":\"http://ia.media-imdb.com/images/M/MV5BMTYwNTE3NDYzOV5BMl5BanBnXkFtZTcwNTU5MzY0MQ@@._V1_SX320.jpg\",\"Runtime\":\"2 hrs 8 mins\",\"Rating\":\"7.3\",\"Votes\":\"16092\",\"ID\":\"tt0065126\",\"Response\":\"True\"}");

		map.put("tt0848228",
				"{\"Title\":\"The Avengers\",\"Year\":\"2012\",\"Rated\":\"G\",\"Released\":\"11 Jun 2012\",\"Genre\":\"Adventure, Western, Drama\",\"Director\":\"Joss Whedon\",\"Writer\":\"Zak Penn, Joss Whedon\",\"Actors\":\"Robert Downey Jr., Chris Evans, Scarlett Johansson\",\"Plot\":\"Nick Fury of S.H.I.E.L.D. brings together a team of super humans to form The Avengers to help save the Earth from Loki and his army.\",\"Poster\":\"http://ia.media-imdb.com/images/M/MV5BMTk2NTI1MTU4N15BMl5BanBnXkFtZTcwODg0OTY0Nw@@._V1._SY317_CR0,0,214,317_.jpg\",\"Runtime\":\"2 hrs 30 mins\",\"Rating\":\"8.8\",\"Votes\":\"65676\",\"ID\":\"tt0848228\",\"Response\":\"True\"}");

		map.put("tt0092099",
				"{\"Title\":\"Top Gun\",\"Year\":\"1986\",\"Rated\":\"PG\",\"Released\":\"16 May 1986\",\"Genre\":\"Action, Drama, Romance\",\"Director\":\"Tony Scott\",\"Writer\":\"Jim Cash, Jack Epps Jr.\",\"Actors\":\"Tom Cruise, Kelly McGillis, Val Kilmer, Anthony Edwards\",\"Plot\":\"The macho students of an elite US Flying school for advanced fighter pilots compete to be best in the class, and one romances the teacher.\",\"Poster\":\"http://ia.media-imdb.com/images/M/MV5BMTY3ODg4OTU3Nl5BMl5BanBnXkFtZTYwMjI1Nzg4._V1_SX320.jpg\",\"Runtime\":\"1 hr 50 mins\",\"Rating\":\"6.6\",\"Votes\":\"101709\",\"ID\":\"tt0092099\",\"Response\":\"True\"}");
		STATIC_CACHE = Collections.unmodifiableMap(map);
	}

}
