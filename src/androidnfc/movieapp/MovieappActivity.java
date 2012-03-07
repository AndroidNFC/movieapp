package androidnfc.movieapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MovieappActivity extends Activity {
    /** Called when the activity is first created. */
	private Button nfcTagButton;
	private Button xmlParserButton;
	private Button openBrowser;
	private Button openMap;
	private Button openVideo;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        nfcTagButton = (Button)findViewById(R.id.nfcTagButton);
    }
}