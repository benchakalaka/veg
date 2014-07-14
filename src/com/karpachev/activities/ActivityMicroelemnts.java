package com.karpachev.activities;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;

import android.app.Activity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout; 

import com.karpachev.custom.ItemMicroelementView;
import com.karpachev.custom.ItemMicroelementView_;
 
@EActivity ( R.layout.activity_microelements ) public class ActivityMicroelemnts extends Activity {

     @ViewById LinearLayout                                         llMicroelementsRoot;
     @StringArrayRes ( R.array.microelements_names ) String[]       microelementsNames;
     @StringArrayRes ( R.array.microelements_names_small ) String[] microelementsNamesSmall;
     @StringArrayRes ( R.array.microelements_descr_short ) String[] microelementsNamesShortDescr;

     @AfterViews void afterViews() {
          for ( int i = 0; i < microelementsNames.length; i++ ) {
               ItemMicroelementView view = ItemMicroelementView_.build(getApplicationContext(), this);
               // view.image.setBackgroundResource(R.drawable.ic_launcher);
               view.longDescriptionPosition = i;
               view.twDescription.setText(microelementsNamesShortDescr[i]);
               view.twName.setText(microelementsNames[i]);
               view.twShortMicroelement.setText(microelementsNamesSmall[i]);
               llMicroelementsRoot.addView(view);

               Animation a = AnimationUtils.loadAnimation(this, R.anim.fade_in);
               a.setStartOffset(i * 100);
               view.startAnimation(a);

          }
     }
}