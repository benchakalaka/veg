package com.karpachev.activities;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.threed.jpct.Camera;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Light;
import com.threed.jpct.Loader;
import com.threed.jpct.Logger;
import com.threed.jpct.Object3D;
import com.threed.jpct.RGBColor;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;
import com.threed.jpct.util.MemoryHelper;

/**
 * A simple demo. This shows more how to use jPCT-AE than it shows how to write
 * a proper application for Android. It includes basic activity management to
 * handle pause and resume...
 * 
 * @author EgonOlsen
 */
public class test extends FragmentActivity {

     // Used to handle pause and resume...
     private static test    master      = null;

     private GLSurfaceView  mGLView;
     private MyRenderer     renderer    = null;
     private FrameBuffer    fb          = null;
     private World          world       = null;
     private final RGBColor back        = new RGBColor(100, 100, 100);

     private float          touchTurn   = 0;
     private float          touchTurnUp = 0;

     private float          xpos        = -1;
     private final float    ypos        = -1;

     Object3D[]             obj         = null;
     private int            fps         = 0;

     private Light          sun         = null;

     Camera                 cam;

     @Override protected void onCreate(Bundle savedInstanceState) {

          Logger.log("onCreate");

          if ( master != null ) {
               copy(master);
          }

          super.onCreate(savedInstanceState);
          mGLView = new GLSurfaceView(getApplication());

          mGLView.setEGLConfigChooser(new GLSurfaceView.EGLConfigChooser() {
               @Override public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
                    // Ensure that we get a 16bit framebuffer. Otherwise, we'll fall
                    // back to Pixelflinger on some device (read: Samsung I7500)
                    int[] attributes = new int[] { EGL10.EGL_DEPTH_SIZE, 16, EGL10.EGL_NONE };
                    EGLConfig[] configs = new EGLConfig[1];
                    int[] result = new int[1];
                    egl.eglChooseConfig(display, attributes, configs, 1, result);
                    return configs[0];
               }
          });

          renderer = new MyRenderer();
          mGLView.setRenderer(renderer);

          setContentView(R.layout.activity_start);
          LinearLayout ll = (LinearLayout) this.findViewById(R.id.container);
          ll.addView(mGLView);
     }

     @Override protected void onPause() {
          super.onPause();
          mGLView.onPause();
     }

     @Override protected void onResume() {
          super.onResume();
          mGLView.onResume();
     }

     @Override protected void onStop() {
          super.onStop();
     }

     private void copy(Object src) {
          try {
               Logger.log("Copying data from master Activity!");
               Field[] fs = src.getClass().getDeclaredFields();
               for ( Field f : fs ) {
                    f.setAccessible(true);
                    f.set(this, f.get(src));
               }
          } catch (Exception e) {
               throw new RuntimeException(e);
          }
     }

     @Override public boolean onTouchEvent(MotionEvent me) {

          if ( me.getAction() == MotionEvent.ACTION_DOWN ) {
               xpos = me.getX();

               // ypos = me.getY();
               return true;
          }

          if ( me.getAction() == MotionEvent.ACTION_UP ) {
               xpos = -1;
               // ypos = -1;
               touchTurn = 0;
               touchTurnUp = 0;

               // SimpleVector dir = Interact2D.reproject2D3D(cam, fb, (int) me.getX(), (int) me.getY());
               // SimpleVector svTrysu = new SimpleVector();
               // obj[1].getXAxis(svTrysu);
               // obj[1].getYAxis(svTrysu);
               // obj[1].getZAxis(svTrysu);

               // Logger.log(String.valueOf("AAAAAAAAAAAAAAAAAAAAAAAAAAAAA : " + svTrysu.distance(dir)));

               // Object3D picked = world.getObject(0);

               /*
                * SimpleVector[] arr = new SimpleVector[2];
                * SimpleVector sv = new SimpleVector();
                * sv.x = -10;
                * sv.y = -10;
                * sv.z = 0;
                * SimpleVector sv2 = new SimpleVector();
                * sv2.x = me.getX();
                * sv2.y = me.getY();
                * sv2.z = 0;
                * arr[0] = sv;
                * arr[1] = sv2;
                * Polyline line = new Polyline(arr, new RGBColor(200, 200, 250));
                * world.addPolyline(line);
                */
               return true;
          }

          if ( me.getAction() == MotionEvent.ACTION_MOVE ) {
               float xd = me.getX() - xpos;
               // float yd = me.getY() - ypos;

               xpos = me.getX();
               // ypos = me.getY();

               touchTurn = xd / -100f;
               // touchTurnUp = yd / -100f;

               return true;
          }

          try {
               Thread.sleep(15);
          } catch (Exception e) {
               // No need for this...
          }

          return super.onTouchEvent(me);
     }

     protected boolean isFullscreenOpaque() {
          return true;
     }

     class MyRenderer implements GLSurfaceView.Renderer {

          private long time = System.currentTimeMillis();

          public MyRenderer () {
          }

          @Override public void onSurfaceChanged(GL10 gl, int w, int h) {
               if ( fb != null ) {
                    fb.dispose();
               }
               fb = new FrameBuffer(gl, w, h);

               if ( master == null ) {

                    world = new World();
                    world.setAmbientLight(20, 20, 20);

                    sun = new Light(world);
                    sun.setIntensity(250, 250, 250);

                    // Create a texture out of the icon...:-)
                    // Texture texture = new Texture(BitmapHelper.rescale(BitmapHelper.convert(getResources().getDrawable(R.drawable.t)), 64, 64));
                    // TextureManager.getInstance().addTexture("texture", texture);

                    // cube = Primitives.getCube(10);
                    // cube.calcTextureWrapSpherical();
                    // cube.setTexture("texture");
                    // cube.strip();
                    // cube.build();

                    InputStream stream;

                    try {
                         stream = getApplicationContext().getAssets().open("jessy2.3ds");
                         obj = Loader.load3DS(stream, 10f);

                         for ( Object3D element : obj ) {
                              element.calcTextureWrapSpherical();
                              element.strip();
                              Logger.log("NAME:::::::::::: " + element.getName());

                              element.rotateX(250f);
                         }
                         world.addObjects(obj);
                         world.buildAllObjects();
                    } catch (IOException e) {
                         e.printStackTrace();
                    }

                    // world.addObject(cube);

                    cam = world.getCamera();
                    cam.moveCamera(Camera.CAMERA_MOVEOUT, 50);
                    cam.lookAt(obj[0].getTransformedCenter()/* cube.getTransformedCenter() */);

                    SimpleVector sv = new SimpleVector();
                    sv.set(obj[0].getTransformedCenter());
                    sv.y -= 50;
                    sv.z -= 50;
                    sun.setPosition(sv);
                    MemoryHelper.compact();

                    if ( master == null ) {
                         Logger.log("Saving master Activity!");
                         master = test.this;
                    }
               }
          }

          @Override public void onSurfaceCreated(GL10 gl, EGLConfig config) {
          }

          @Override public void onDrawFrame(GL10 gl) {
               if ( touchTurn != 0 ) {
                    for ( Object3D element : obj ) {
                         element.rotateY(touchTurn);
                    }
                    touchTurn = 0;
               }

               if ( touchTurnUp != 0 ) {
                    for ( Object3D element : obj ) {
                         element.rotateX(touchTurnUp);
                    }
                    touchTurnUp = 0;
               }

               fb.clear(back);
               world.renderScene(fb);
               world.draw(fb);
               fb.display();

               if ( System.currentTimeMillis() - time >= 1000 ) {
                    Logger.log(fps + "fps");
                    fps = 0;
                    time = System.currentTimeMillis();
               }
               fps++;
          }
     }
}