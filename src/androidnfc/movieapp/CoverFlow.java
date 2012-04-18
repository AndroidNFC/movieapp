package androidnfc.movieapp;


import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Transformation;
import android.widget.Gallery;
import android.widget.ImageView;

public class CoverFlow extends Gallery {

    
 public CoverFlow(Context context) {
  super(context);
  this.setStaticTransformationsEnabled(true);
 }

 public CoverFlow(Context context, AttributeSet attrs) {
  super(context, attrs);
        this.setStaticTransformationsEnabled(true);
 }
 
}