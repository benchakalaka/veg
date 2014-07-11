package com.karpachev.custom;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.karpachev.activities.R;

@EViewGroup ( R.layout.list_microelement ) public class ItemMicroelementView extends RelativeLayout implements android.view.View.OnClickListener {

     @ViewById public ImageView                                    image;
     @ViewById public TextView                                     twDescription;
     @ViewById public TextView                                     twName;
     @ViewById public TextView                                     twShortMicroelement;

     public int                                                    longDescriptionPosition;

     @StringArrayRes ( R.array.microelements_descr_long ) String[] microelementsNamesLongDescr;
     private final Activity                                        context;

     public ItemMicroelementView ( Context context , Activity act ) {
          super(context);
          this.context = act;
          setOnClickListener(this);
     }

     @Override public void onClick(View v) {
          Dialog d = new Dialog(context);
          d.setContentView(R.layout.dialog_microelement);
          ((TextView) d.findViewById(R.id.text)).setText(microelementsNamesLongDescr[longDescriptionPosition]);

          d.show();
     }
}
