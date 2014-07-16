package com.karpachev.fragments;

import java.io.ObjectInputStream;
import java.util.Random;

import rajawali.Object3D;
import rajawali.SerializedObject3D;
import rajawali.lights.DirectionalLight;
import rajawali.materials.Material;
import rajawali.materials.methods.DiffuseMethod;
import rajawali.materials.textures.Texture;
import rajawali.math.vector.Vector3;
import rajawali.parser.LoaderOBJ;
import rajawali.util.ObjectColorPicker;
import rajawali.util.OnObjectPickedListener;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.karpachev.activities.R;

public class FogFragment extends AExampleFragment {

     @Override protected AExampleRenderer createRenderer() {
          return new FogRenderer(getActivity());
     }

     private final class FogRenderer extends AExampleRenderer implements OnTouchListener , OnObjectPickedListener {
          private DirectionalLight  mLight;
          private Object3D          mRoad;
          private Object3D          mSelectedObject;
          private ObjectColorPicker mPicker;
          private double            x;
          private double            y;

          public FogRenderer ( Context context ) {
               super(context);
          }

          public void getObjectAt(float x, float y) {
               mPicker.getObjectAt(x, y);
          }

          @Override protected void initScene() {
               // /////////////////////////////////////////////////////////////////
               FogFragment.this.mSurfaceView.setOnTouchListener(this);

               mPicker = new ObjectColorPicker(this);
               mPicker.setOnObjectPickedListener(this);
               // ////////////////////////////////////////////////////////
               mLight = new DirectionalLight(0, -1, -1);
               mLight.setPower(.4f);

               getCurrentScene().addLight(mLight);

               int fogColor = 0x999999;

               getCurrentScene().setBackgroundColor(fogColor);
               // getCurrentScene().setFog(new FogParams(FogType.LINEAR, fogColor, 1, 15));

               LoaderOBJ objParser = new LoaderOBJ(mContext.getResources(), mTextureManager, R.raw.road);
               try {

                    ObjectInputStream ois = new ObjectInputStream(mContext.getResources().openRawResource(R.raw.monkey_ser));
                    SerializedObject3D serializedMonkey = (SerializedObject3D) ois.readObject();
                    ois.close();

                    Material material = new Material();
                    material.enableLighting(true);
                    material.setDiffuseMethod(new DiffuseMethod.Lambert());
                    Object3D mMonkey1 = new Object3D(serializedMonkey);
                    mMonkey1.setName("monkey");
                    mMonkey1.setScale(.3f);
                    mMonkey1.setPosition(1, 1, -7);
                    mMonkey1.setRotY(0);
                    mMonkey1.setMaterial(material);
                    mMonkey1.setColor(0x0000ff);
                    getCurrentScene().addChild(mMonkey1);

                    objParser.parse();
                    mRoad = objParser.getParsedObject();
                    mRoad.setZ(-2);
                    mRoad.setRotY(180);
                    getCurrentScene().addChild(mRoad);

                    Material roadMaterial = new Material();
                    roadMaterial.enableLighting(true);
                    roadMaterial.setDiffuseMethod(new DiffuseMethod.Lambert());
                    roadMaterial.addTexture(new Texture("roadTex", R.drawable.road));
                    roadMaterial.setColorInfluence(0);
                    mRoad.getChildByName("Road").setMaterial(roadMaterial);

                    Material signMaterial = new Material();
                    signMaterial.enableLighting(true);
                    signMaterial.setDiffuseMethod(new DiffuseMethod.Lambert());
                    signMaterial.addTexture(new Texture("rajawaliSign", R.drawable.sign));
                    signMaterial.setColorInfluence(0);
                    mRoad.getChildByName("WarningSign").setMaterial(signMaterial);
                    Material warningMaterial = new Material();
                    warningMaterial.enableLighting(true);
                    warningMaterial.setDiffuseMethod(new DiffuseMethod.Lambert());
                    warningMaterial.addTexture(new Texture("warning", R.drawable.warning));
                    warningMaterial.setColorInfluence(0);
                    mRoad.getChildByName("Warning").setMaterial(warningMaterial);

                    getCurrentScene().getCamera().setPosition(0, 1, -1);

                    // Object3D obj = mRoad.getChildByName("WarningSign");

                    mPicker.registerObject(mMonkey1);

                    // obj = mRoad.getChildByName("Warning");
                    // mPicker.registerObject(obj);

                    // mPicker.registerObject(mRoad.getChildByName("rajawaliSign"));

               } catch (Exception e) {
                    e.printStackTrace();
               }

               /*
                * TranslateAnimation3D camAnim = new TranslateAnimation3D(new Vector3(0, 1, 0), new Vector3(0, 1, -23));
                * camAnim.setDurationMilliseconds(8000);
                * camAnim.setInterpolator(new AccelerateDecelerateInterpolator());
                * camAnim.setRepeatMode(RepeatMode.REVERSE_INFINITE);
                * camAnim.setTransformable3D(getCurrentCamera());
                * getCurrentScene().registerAnimation(camAnim);
                * camAnim.play();
                */
          }

          @Override public boolean onTouch(View v, MotionEvent event) {
               x = event.getX();
               y = event.getY();
               switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                         ((FogRenderer) mRenderer).getObjectAt(event.getX(), event.getY());
                         break;
                    case MotionEvent.ACTION_MOVE:
                         ((FogRenderer) mRenderer).moveSelectedObject(event.getX(), event.getY());
                         break;
                    case MotionEvent.ACTION_UP:
                         // ((TouchAndDragRenderer) mRenderer).stopMovingSelectedObject();
                         break;
               }
               return getActivity().onTouchEvent(event);
          }

          private void moveSelectedObject(float x, float y) {
               // / mSelectedObject.setPosition(x, y, mSelectedObject.getZ());
               // Vector3 v = new Vector3(x, y, -7);
               // v.normalize();
               mSelectedObject.setX(x);
               mSelectedObject.setY(y);
               mSelectedObject.setZ(new Random(10).nextInt());

          }

          @Override public void onObjectPicked(Object3D object) {
               mSelectedObject = object;
               // object.rotateAround(new Vector3(25), 25);
               // Vector3 v = new Vector3(x, y, -7);
               // v.normalize();
               // object.setPosition(v);
               object.setZ(object.getZ() + 1f);
               Log.d("app!!!!!!!!!!!!!!!!!!!!", object.getName());
          }

          public void Move(float x, float y) {
               if ( mSelectedObject != null ) {
                    int[] viewport = new int[] { 0, 0, mViewportWidth, mViewportHeight };
                    float[] nearPos = new float[4];
                    float[] farPos = new float[4];
                    Vector3 nearVec = new Vector3(nearPos[0] / nearPos[3], nearPos[1] / nearPos[3], nearPos[2] / nearPos[3]);
                    Vector3 farVec = new Vector3(farPos[0] / farPos[3], farPos[1] / farPos[3], farPos[2] / farPos[3]);

                    float factor = (float) ((Math.abs(mSelectedObject.getZ()) + nearVec.z) / (getCurrentCamera().getFarPlane() - getCurrentCamera().getNearPlane()));

                    Vector3 diff = Vector3.subtractAndCreate(farVec, nearVec);
                    diff.multiply(factor);
                    nearVec.add(diff);

                    mSelectedObject.setX(nearVec.x);
                    mSelectedObject.setY(nearVec.y);
               }
          }
     }
}
