package com.example.interiodesign;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.SceneView;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.ux.FootprintSelectionVisualizer;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.ar.sceneform.ux.TransformationSystem;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class show3D extends AppCompatActivity {

    private SceneView sceneView;
    private Scene scene;
    private TransformationSystem transformationSystem;
    private Renderable renderable;
    private ImageButton closeButton,instructionPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show3_d);

        closeButton=findViewById(R.id.closeButton);
        instructionPopup=findViewById(R.id.instructionButton);

        FirebaseApp.initializeApp(this);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        Intent i = getIntent();
        String modelName = i.getStringExtra("modelName");
        Log.e("manual",this. getClass(). getSimpleName()+" "+"3d"+modelName);

        sceneView = findViewById(R.id.scene_View);
//        scene = sceneView.getScene();

//        TransformationSystem transformationSystem=
        transformationSystem=new TransformationSystem(getResources().getDisplayMetrics(),new FootprintSelectionVisualizer());
        sceneView.getScene().addOnUpdateListener(this::onFrameUpdate);
        sceneView.getRenderer().setClearColor(new com.google.ar.sceneform.rendering.Color(Color.LTGRAY));
        sceneView.getScene().addOnPeekTouchListener(new Scene.OnPeekTouchListener() {
            @Override
            public void onPeekTouch(HitTestResult hitTestResult, MotionEvent motionEvent) {
                transformationSystem.onTouch(hitTestResult,motionEvent);
            }
        });
        sceneView.getScene().getCamera().setLocalPosition(new Vector3(0,0.2f,0));


        procedure(modelName);

        closeButton.setOnClickListener(view -> {
            finish();
        });
        instructionPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                instructionPopup iPopup = new instructionPopup();
                iPopup.showPopupWindow(view);

            }
        });



    }

    private void onFrameUpdate(FrameTime frameTime){
        if(!modelplaced){
            addModel(renderable);
        }
    }

    private void buildModel(File file) {
//        scene= new Scene(sceneView);
//        sceneView.getScene();
        Toast.makeText(this,"innn",Toast.LENGTH_SHORT).show();
        RenderableSource renderableSource = RenderableSource
                .builder()
                .setSource(this, Uri.parse(file.getPath()), RenderableSource.SourceType.GLB)
                .setScale(0.3f)
                .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                .build();


        ModelRenderable.builder()
                .setSource(this,renderableSource)
//                .setRegistryId(file.getPath())
                .build()
                .thenAccept(renderable -> addModel(renderable))
                .exceptionally(throwable -> {
                    Toast.makeText(this,"hereee",Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Something is not right" + throwable.getMessage()).show();
                    return null;
                });
        renderable=renderable;


    }


    private boolean modelplaced=false;

    private void addModel(Renderable renderable) {
        TransformableNode transformableNode=new TransformableNode(transformationSystem);
        transformableNode.getScaleController().setEnabled(true);
        transformableNode.getTranslationController().setEnabled(false);
        transformableNode.getRotationController().setEnabled(true);
//        transformableNode.setLocalRotation(Quaternion.axisAngle(new Vector3(0f, 0f, 1f), 90f));
        transformableNode.setRenderable(renderable);
        sceneView.getScene().addChild(transformableNode);
        transformableNode.setLocalPosition(new Vector3(0f,-0.05f,-0.6f));

        transformableNode.select();
        modelplaced=true;
    }
    private File getFile(String modelName){
        File file = new File(this.getCacheDir(),modelName+".glb");

        if(file.exists()){
            Log.e("manual",this. getClass(). getSimpleName()+" "+"exists");
        }
        else {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference model = storage.getReference().child(modelName+".glb");
            Log.e("manual1",this. getClass(). getSimpleName()+" "+model.toString());

            Log.e("manual2",this. getClass(). getSimpleName()+" "+"not exists");
            Log.e("manual3",this. getClass(). getSimpleName()+" "+this.getCacheDir().toString());
            Toast.makeText(getBaseContext(), "inside", Toast.LENGTH_SHORT).show();

            model.getFile(file)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getBaseContext(),"now it is downloaded",Toast.LENGTH_SHORT);
                            Log.e("manual4",this.getClass().getSimpleName()+" "+"now the file is downloaded and stored from storage");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getBaseContext(),e.toString(),Toast.LENGTH_SHORT);
                            Log.e("manual5",this.getClass().getSimpleName()+" download failed "+e.toString());
                        }
                    });
            Log.e("manual6",this. getClass(). getSimpleName()+" "+file.getName());
        }

        return file;
    }

    private void procedure(String modelName){
        File file = getFile(modelName);
        Log.e("manual7",file.getPath());
        buildModel(file);

    }
    @Override
    protected void onResume() {
        super.onResume();
        try {
            sceneView.resume();
        }
        catch (CameraNotAvailableException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sceneView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sceneView.destroy();
    }


}




