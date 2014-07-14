package com.karpachev.activities;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;

import android.app.Activity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.karpachev.custom.ItemVitaminView;
import com.karpachev.custom.ItemVitaminView_;

@EActivity ( R.layout.activity_vitamins ) public class ActivityVitamins extends Activity {

     @ViewById LinearLayout                                    llMicroelementsRoot;
     @StringArrayRes ( R.array.vitamins_names_short ) String[] vitaminsNamesShort;
     @StringArrayRes ( R.array.vitamins_descr_short ) String[] vitaminsDescrShort;
     @StringArrayRes ( R.array.vitamins_names ) String[]       vitaminsNames;

     @AfterViews void afterViews() {
          for ( int i = 0; i < vitaminsNamesShort.length; i++ ) {
               ItemVitaminView view = ItemVitaminView_.build(getApplicationContext(), this);
               // view.image.setBackgroundResource(R.drawable.ic_launcher);
               view.longDescriptionPosition = i;
               view.twDescription.setText(vitaminsDescrShort[i]);
               view.twName.setText(vitaminsNames[i]);
               view.twShortVitamin.setText(vitaminsNamesShort[i]);
               llMicroelementsRoot.addView(view);

               Animation a = AnimationUtils.loadAnimation(this, R.anim.fade_in);
               a.setStartOffset(i * 100);
               view.startAnimation(a);
          }
     }
}