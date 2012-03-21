package androidnfc.movieapp;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.webkit.URLUtil;

import androidnfc.movieapp.models.Movie;
import androidnfc.movieapp.parsers.MovieHandler;

public class MovieScheduleActivity extends Activity {
    
	private final String MOVIE_SCHEDULE_DEBUG_TAG = "MovieScheduleActivity";
	private final String COVER_FLOW_DEBUG_TAG = "CoverFlow";
	
	private CoverFlow coverFlow;
	private TextView movieTitleText;
	private ImageView emptyCover;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule);
        
        coverFlow = (CoverFlow) findViewById(R.id.schedule_coverflow);
        movieTitleText = (TextView) findViewById(R.id.schedule_movietitle);
        emptyCover = (ImageView) findViewById(R.id.schedule_emptycover);
        
        movieTitleText.setText("");
        movieTitleText.setGravity(Gravity.CENTER_HORIZONTAL);
        
        FetchFinnkinoXMLTask myTask = new FetchFinnkinoXMLTask(this);
        myTask.execute("http://www.finnkino.fi/xml/Schedule/");
        
    }
	
    @Override
    protected void onStart() {
        super.onStart();
        // The activity is about to become visible.
    }
    @Override
    protected void onResume() {
        super.onResume();
        // The activity has become visible (it is now "resumed").
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Another activity is taking focus (this activity is about to be "paused").
    }
    @Override
    protected void onStop() {
        super.onStop();
        // The activity is no longer visible (it is now "stopped")
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // The activity is about to be destroyed.
    }
	
}


