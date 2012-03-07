package androidnfc.movieapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
        openBrowser = (Button)findViewById(R.id.openBrowser);
        openBrowser.setOnClickListener(new openBrowserOnClickListener());
    }
    
    private final class openBrowserOnClickListener implements OnClickListener {
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Toast toast = Toast.makeText(getApplicationContext(), "open browser", Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
//			Intent it = new Intent();
//			it.putExtra("CLocation", getReceive());
//			it.setClass(Selecao.this, N3SeleText.class);
//			startActivity(it);
		}
}
}