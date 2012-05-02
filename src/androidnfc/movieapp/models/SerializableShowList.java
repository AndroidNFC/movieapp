package androidnfc.movieapp.models;

import java.io.Serializable;
import java.util.ArrayList;
public class SerializableShowList extends ArrayList<Show> implements Serializable {
	public static final SerializableShowList newInstance(){
		return new SerializableShowList();
	}
	private static final long serialVersionUID = 1L;
	
}
