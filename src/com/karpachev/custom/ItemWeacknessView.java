package com.karpachev.custom;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.karpachev.activities.R;

@EViewGroup ( R.layout.list_vitamin ) public class ItemWeacknessView extends RelativeLayout implements android.view.View.OnClickListener {

     @ViewById public TextView                                twDescription;
     @ViewById public TextView                                twName;
     @ViewById public TextView                                twShortVitamin;

     // GO TO ACTIVE RECORD
     public int                                               longDescriptionPosition;
     @StringArrayRes ( R.array.vitamins_descr_long ) String[] vitaminsDescrLong;
     private final Activity                                   context;

     public ItemWeacknessView ( Context context , Activity act ) {
          super(context);
          this.context = act;
          setOnClickListener(this);
     }

     @Override public void onClick(View v) {
          Dialog d = new Dialog(context);
          d.setContentView(R.layout.dialog_microelement);
          ((TextView) d.findViewById(R.id.text)).setText(vitaminsDescrLong[longDescriptionPosition]);

          d.show();
     }
}
