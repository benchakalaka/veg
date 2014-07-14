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

@EActivity ( R.layout.activity_your_weacknesses ) public class ActivityYourWeacknesses extends Activity {

     @ViewById LinearLayout                                llMicroelementsRoot;
     @StringArrayRes ( R.array.ne_want ) String[]          notEnough;
     @StringArrayRes ( R.array.ne_what_ne ) String[]       notEnoughWhatExactly;
     @StringArrayRes ( R.array.ne_what_where_is ) String[] notEnoughWhereICanFindIt;

     @AfterViews void afterViews() {
          for ( int i = 0; i < notEnough.length; i++ ) {
               ItemVitaminView view = ItemVitaminView_.build(getApplicationContext(), this);
               // view.image.setBackgroundResource(R.drawable.ic_launcher);
               view.longDescriptionPosition = i;
               view.twDescription.setText(notEnough[i]);
               view.twName.setText(notEnoughWhereICanFindIt[i]);
               view.twShortVitamin.setText(notEnoughWhatExactly[i]);
               llMicroelementsRoot.addView(view);

               Animation a = AnimationUtils.loadAnimation(this, R.anim.fade_in);
               a.setStartOffset(i * 100);
               view.startAnimation(a);
          }
     }
}