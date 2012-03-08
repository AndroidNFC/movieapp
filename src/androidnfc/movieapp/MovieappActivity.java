package androidnfc.movieapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MovieappActivity extends Activity {
	
	private Button nfcTagButton;
	private Button xmlParserButton;
	private Button openBrowserButton;
	private Button openMapButton;
	private Button openVideoButton;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        nfcTagButton =		(Button)findViewById(R.id.nfcTagButton);
        xmlParserButton = 	(Button)findViewById(R.id.xmlParserButton);
        openBrowserButton = (Button)findViewById(R.id.openBrowserButton);
        openMapButton = 	(Button)findViewById(R.id.openMapButton);
        openVideoButton = 	(Button)findViewById(R.id.openVideoButton);
        
        xmlParserButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MovieappActivity.this,
										   XMLParserActivity.class);
				MovieappActivity.this.startActivity(intent);
			}
		});
    }
}
