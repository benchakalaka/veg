package com.karpachev.activities;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.AnimationRes;

import android.app.Activity;
import android.content.Intent;
import android.view.animation.Animation;
import android.widget.Button;

@EActivity ( R.layout.activity_start ) public class ActivityStart extends Activity {

     @ViewById Button                         btnProductsList;
     @ViewById Button                         btnDesireDescritpion;
     @ViewById Button                         btnMicroelements;

     @AnimationRes ( R.anim.blink ) Animation blink1;
     @AnimationRes ( R.anim.blink ) Animation blink2;
     @AnimationRes ( R.anim.blink ) Animation blink3;

     @AfterViews void afterViews() {
          blink1.setStartOffset(200);
          blink2.setStartOffset(400);
          blink3.setStartOffset(600);

          btnProductsList.startAnimation(blink1);
          btnDesireDescritpion.startAnimation(blink2);
          btnMicroelements.startAnimation(blink3);
     }

     @Click void btnMicroelements() {
          startActivity(new Intent(ActivityStart.this, ActivityMicroelemnts_.class));
     }
}
