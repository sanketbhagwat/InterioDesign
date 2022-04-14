package com.example.interiodesign;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.CamcorderProfile;
import android.net.Uri;
import android.os.*;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Material;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.Texture;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class showAR extends AppCompatActivity {
    private ArFragment arFragment;

    private int clickNo = 0;
    private ImageButton videoRecordingButton,closeBottomSheet,instructionButton;
    private FloatingActionButton addButton;
    private Node currentNode = null;
    private Product productObject;
    private VideoRecorder videoRecorder;


    private FirebaseData firebaseData;
    private RecyclerView bottomSheetRecyclerView;
    private Map<Node, Product> nodeProductMap = new HashMap<>();
    private Map<Node, ArrayList<Material>> nodeMaterialMap = new HashMap<>();
    private Map<ViewRenderable, Node> viewRenderableNodeMap = new HashMap<>();
    private Map<Node, ViewRenderable> nodeViewRenderableMap = new HashMap<>();

    private BottomSheetBehavior bottomSheetBehavior;
    private GestureDetector gestureDetector;
    private ViewRenderable viewRenderable;
    private ArrayAdapter<String> textureAdapter;
    private InterioDesignAdapter interioDesignAdapter;



    public showAR(){ }




    /*private void fetchList(){
        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReferencese = firebaseDatabase.getReference("Categories");
        Log.e("manual",this.getClass().getSimpleName()+" ");
        databaseReferencese.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        Product product=new Product(
                                dataSnapshot1.child("img").getValue().toString(),
                                (Long) dataSnapshot1.child("price").getValue(),
                                dataSnapshot1.child("title").getValue().toString(),
                                dataSnapshot1.getKey()
                        );
                        productList.add(product);
                    }
                }
                Log.e("manual",this.getClass().getSimpleName()+" "+productList.toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("test",error.getMessage());
            }
        });
        Log.e("manual",productList.toString());
    }
*/
    private void changeTextureInNodeProductMap(Integer pos,String texture){
        ArrayList<String> textureList = nodeProductMap.get(currentNode).getTexture();
        textureList.set(pos,texture);
        nodeProductMap.get(currentNode).setTexture(textureList);
    }

    private void changeTexture(Integer position, String textureString){
        Log.e("manualll",this. getClass(). getSimpleName()+" "+textureString+" "+Integer.toString(position));
        if(currentNode != null && textureString.equals("default")){
            Log.e("manual",this.getClass().getSimpleName()+" "+"equal" );
            Material defaultMaterial = nodeMaterialMap.get(currentNode).get(position);
            Renderable currentNodeCopy = currentNode.getRenderable().makeCopy();
            currentNodeCopy.setMaterial(position,(Material) defaultMaterial);
            currentNode.setRenderable(currentNodeCopy);
            changeTextureInNodeProductMap(position,textureString);
        }
        else if(currentNode!=null){
            CompletableFuture<Texture> textureCompletableFuture =
                    Texture.builder().setSource(this, getResources().getIdentifier(textureString,"drawable",getPackageName())).build();

            ((CompletableFuture<?>) textureCompletableFuture).thenAccept(texture -> {
                Renderable currentNodeCopy = currentNode.getRenderable().makeCopy();
                currentNodeCopy.getMaterial(position).setTexture("baseColor",(Texture) texture);
                Log.e("manual",this. getClass(). getSimpleName()+" "+currentNodeCopy.getSubmeshCount());
                currentNode.setRenderable(currentNodeCopy);
                changeTextureInNodeProductMap(position,textureString);
            });
        }
        else{
            Toast.makeText(getBaseContext(), "please load a model", Toast.LENGTH_SHORT).show();
        }
    }
    private String getNewName(ArrayList<String> textureList){
        String newName = "";
        for(int i=0;i< textureList.size();i++){
            if(!textureList.get(i).equals("default"))
                newName += Integer.toString(i) + textureList.get(i);
        }
        return newName;
    }

    public void addAllToCart(View view){
        Log.e("manual",Integer.toString(nodeProductMap.size()));
        Log.e("manual","inside addAllToCart "+nodeProductMap.values().toString());

        nodeProductMap.forEach((k,v)->{
            Product product = new Product(v);
            String newName = product.getKey()+getNewName(product.getTexture());
            Log.e("manual","inside addAllToCart "+v.getKey());
            Log.e("manual","inside addAllToCart "+newName);
            product.setKey(newName);
            firebaseData.addToCartList(FirebaseAuth.getInstance().getUid(),product);
        });
        Log.e("manual",nodeProductMap.values().toString());
    }

    private void saveBitmapToDisk(Bitmap bitmap, String filename) throws IOException {

        File out = new File(filename);
        if (!out.getParentFile().exists()) {
            out.getParentFile().mkdirs();
        }
        try (FileOutputStream outputStream = new FileOutputStream(filename);
             ByteArrayOutputStream outputData = new ByteArrayOutputStream()) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputData);
            outputData.writeTo(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException ex) {
            throw new IOException("Failed to save bitmap to disk", ex);
        }
    }

    private String generateFilename() {
        String date =
                new SimpleDateFormat("yyyyMMddHHmmss", java.util.Locale.getDefault()).format(new Date());
        return Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES) +  "/InterioDesign/Images" + date + "_screenshot.jpg";
    }


    public void takePhoto(View view1) {
        final String filename = generateFilename();
        ArSceneView view = arFragment.getArSceneView();

        // Create a bitmap the size of the scene view.
        final Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.ARGB_8888);

        // Create a handler thread to offload the processing of the image.
        final HandlerThread handlerThread = new HandlerThread("PixelCopier");
        handlerThread.start();
        // Make the request to copy.
        PixelCopy.request(view, bitmap, (copyResult) -> {
            if (copyResult == PixelCopy.SUCCESS) {
                try {
                    saveBitmapToDisk(bitmap, filename);
                } catch (IOException e) {
                    Toast toast = Toast.makeText(this, e.toString(),
                            Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                        "Photo saved", Snackbar.LENGTH_LONG);
                snackbar.show();
            } else {
                Toast toast = Toast.makeText(showAR.this,
                        "Failed to copyPixels: " + copyResult, Toast.LENGTH_LONG);
                toast.show();
            }
            handlerThread.quitSafely();
        }, new Handler(handlerThread.getLooper()));
    }


//    public void changeColor(View view){
//        Log.e("manual",this. getClass(). getSimpleName()+" "+(String)view.getTag());
//
//        if(currentNode != null && view.getTag().equals("defaultColor")){
//            Material defaultMaterial = nodeMaterialMap.get(currentNode).get(0);
//            Renderable currentNodeCopy = currentNode.getRenderable().makeCopy();
//            currentNodeCopy.setMaterial((Material) defaultMaterial);
//            currentNode.setRenderable(currentNodeCopy);
//
//        }
//        else if(currentNode != null){
//            int color = Integer.decode((String)view.getTag());
//            CompletableFuture<Material> materialCompletableFuture =
//                    MaterialFactory.makeOpaqueWithColor(this, new Color((Integer) color));
//
//            ((CompletableFuture<?>) materialCompletableFuture).thenAccept(material -> {
//                Renderable currentNodeCopy = currentNode.getRenderable().makeCopy();
//                currentNodeCopy.setMaterial((Material) material);
//                currentNode.setRenderable(currentNodeCopy);
//            });
//        }
//        else{
//            Toast.makeText(getBaseContext(), "please load a model", Toast.LENGTH_SHORT).show();
//        }
//    }

    private void removeModel(){
        if(currentNode != null){
            arFragment.getArSceneView().getScene().removeChild(currentNode);
            nodeProductMap.remove(currentNode);
            nodeMaterialMap.remove(currentNode);
            nodeViewRenderableMap.remove(currentNode);
            currentNode.setParent(null);
            currentNode = null;
        }
        else{
            Toast.makeText(getBaseContext(), "please load a model", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ar);

        if(Build.VERSION.SDK_INT>=21){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            Window window=getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        }

        firebaseData = new FirebaseData();
        firebaseData.getAllProducts();

        Intent i = getIntent();
        Product productObjectReference = (Product) i.getParcelableExtra("productObject");


        addButton = findViewById(R.id.addButton);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arCameraArea);
        videoRecordingButton = findViewById(R.id.video_recording_button);
        closeBottomSheet=findViewById(R.id.closeBottomSheet);
        instructionButton=findViewById(R.id.instructionButton);
        bottomSheetRecyclerView = findViewById(R.id.bottomSheetRecyclerView);
        LinearLayout bottomSheetLayout = findViewById(R.id.bottom_sheet);

        bottomSheetRecyclerView.setHasFixedSize(true);
        bottomSheetRecyclerView.setLayoutManager(new GridLayoutManager(getBaseContext(),2));
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
        bottomSheetBehavior.setPeekHeight(0);
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);




        firebaseData.getProductMutableLiveList().observe(arFragment.getViewLifecycleOwner(), new Observer<ArrayList<Product>>() {
            @Override
            public void onChanged(ArrayList<Product> products) {
                Log.e("manual",products.get(0).getPrice().toString());
                interioDesignAdapter = new InterioDesignAdapter(R.layout.collection_row,products,true,bottomSheetLayout);
                bottomSheetRecyclerView.setAdapter(interioDesignAdapter);
            }
        });
        firebaseData.getErrorMutableLive().observe(arFragment.getViewLifecycleOwner(), new Observer<DatabaseError>() {
            @Override
            public void onChanged(DatabaseError databaseError) {
                Toast.makeText(getBaseContext(), databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });


        gestureDetector = new GestureDetector(getBaseContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent motionEvent) {
                Log.e("manual","double tapped inside gesture detector");
                return true;
            }
        });

        String[] textures = getResources().getStringArray(R.array.textures);

        textureAdapter = new ArrayAdapter<>(this,R.layout.dropdown_items,textures);






        bottomSheetLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("manualllllllllllllllllllllll", this.getClass().getSimpleName()+" "+ interioDesignAdapter.getProductObject());
                if(interioDesignAdapter.getProductObject() != null){
                    productObject = new Product(interioDesignAdapter.getProductObject());
                    Log.e("manual","when fetched from bottomsheet"+productObject.toString());
                    clickNo = 0;
                    procedure( productObject.getKey());
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
            }
        });

        findViewById(R.id.closeButton).setOnClickListener(e -> {
            finish();
        });


        videoRecordingButton.setOnClickListener(e->{
            if(videoRecorder==null) {
                videoRecorder = new VideoRecorder();
                videoRecorder.setSceneView(arFragment.getArSceneView());
                int orientation = getResources().getConfiguration().orientation;
                videoRecorder.setVideoQuality(CamcorderProfile.QUALITY_HIGH, orientation);
            }
            Log.e("manual","entered here");
            boolean isRecording = videoRecorder.onToggleRecord();
            if(isRecording) {
                Toast.makeText(this, "Started Recording", Toast.LENGTH_SHORT).show();
                videoRecordingButton.setImageResource(R.drawable.outline_videocam_24);
            }
            else{
                Toast.makeText(this, "Recording stopped", Toast.LENGTH_SHORT).show();
                videoRecordingButton.setImageResource(R.drawable.outline_videocam_off_24);
            }
        });

        closeBottomSheet.setOnClickListener(e->{
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        });

        instructionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                instructionPopup iPopup = new instructionPopup();
                iPopup.showPopupWindow(view);

            }
        });



        if(productObjectReference != null){
            productObject = new Product(productObjectReference);
            String modelName = productObject.getKey();
            Log.e("manual",this. getClass(). getSimpleName()+" "+"not null");
            clickNo=0;
            procedure(modelName);
        }
    }







    private boolean handleTouch(HitTestResult hitTestResult, MotionEvent motionEvent){
        if (hitTestResult.getNode() != null) {
            currentNode = hitTestResult.getNode();
            if(gestureDetector.onTouchEvent(motionEvent)){
                Log.e("manual","tapped double");
                nodeViewRenderableMap.get(currentNode).getView().setVisibility(View.VISIBLE);
            }
        }
        return true;
    }




    private void setDefault(){
        productObject.setQuantity(1L);
        ArrayList<String> textureList = new ArrayList<>();
        for(int i=0;i<currentNode.getRenderable().getSubmeshCount();i++)
            textureList.add("default");
        productObject.setTexture(textureList);
    }

    private ArrayList<Material> getMaterialList(Node currentNode){
        ArrayList<Material> materialArrayList = new ArrayList<>();
        for(int i=0;i<currentNode.getRenderable().getSubmeshCount();i++){
            materialArrayList.add(currentNode.getRenderable().getMaterial(i));
        }
        return materialArrayList;
    }

    private void addToRecent(){
        Long time = Calendar.getInstance().getTimeInMillis();
        if(productObject.getTexture()!=null)
            Log.e("manual","date "+productObject.getTexture().toString());
        else
            Log.e("manual","date "+"null");

        Product product = new Product(productObject);
        product.setTime(time);
        firebaseData.addToRecentlyViewedList(FirebaseAuth.getInstance().getUid(), product);
    }


    private void addModel(Anchor anchor, ModelRenderable modelRenderable) {
        Log.e("manual",this. getClass(). getSimpleName()+" "+"inside admodel");

        AnchorNode anchorNode = new AnchorNode(anchor);
        anchorNode.setParent(arFragment.getArSceneView().getScene());


        TransformableNode model = new TransformableNode(arFragment.getTransformationSystem());
        model.setParent(anchorNode);
        model.setRenderable(modelRenderable);
        model.select();
        model.setOnTouchListener(this::handleTouch);
        currentNode = model;
        nodeViewRenderableMap.put(currentNode,viewRenderable);
        viewRenderableNodeMap.put(viewRenderable,currentNode);

        AutoCompleteTextView autoCompleteTextViewMaterial = viewRenderable.getView().findViewById(R.id.autoCompleteTextViewMaterial);
        ArrayList<String> materialCountList = getMaterialCountList(currentNode.getRenderable().getSubmeshCount());
        ArrayAdapter<String> materialAdapter = new ArrayAdapter<>(this,R.layout.dropdown_items,materialCountList);
        autoCompleteTextViewMaterial.setAdapter(materialAdapter);

        Node forViewRenderable = new Node();
        forViewRenderable.setParent(currentNode);
        forViewRenderable.setRenderable(viewRenderable);
        forViewRenderable.setLocalPosition(new Vector3(0.0f,currentNode.getLocalPosition().y+1.5f, 0.0f));

        viewRenderable.getView().setVisibility(View.INVISIBLE);
        addToRecent();
        nodeMaterialMap.put(currentNode,getMaterialList(currentNode));
        setDefault();
        nodeProductMap.put(currentNode,productObject);
    }

    private ArrayList<String> getMaterialCountList(int count){
        ArrayList<String> materialCountList = new ArrayList<>();
        for(int i=1;i<=count;i++){
            materialCountList.add(Integer.toString(i));
        }
        return materialCountList;
    }


    private void buildViewRenderable(ViewRenderable renderable){
        TextView title = renderable.getView().findViewById(R.id.title);
        title.setText(productObject.getTitle());

        TextView price = renderable.getView().findViewById(R.id.price);
        price.setText(productObject.getPrice().toString()+" Rs");

        Button doneButton = renderable.getView().findViewById(R.id.doneButton);
        doneButton.setOnClickListener(view -> {
              renderable.getView().setVisibility(view.INVISIBLE);
        });

        Button deleteModelButton = renderable.getView().findViewById(R.id.deleteModelButton);
        deleteModelButton.setOnClickListener(view ->{
            currentNode = viewRenderableNodeMap.get(renderable);
            viewRenderableNodeMap.remove(renderable);
            this.removeModel();
        });

        AutoCompleteTextView autoCompleteTextViewMaterial = renderable.getView().findViewById(R.id.autoCompleteTextViewMaterial);
        AutoCompleteTextView autoCompleteTextViewTexture = renderable.getView().findViewById(R.id.autoCompleteTextViewTexturte);
        autoCompleteTextViewTexture.setAdapter(textureAdapter);
        autoCompleteTextViewMaterial.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currentNode = viewRenderableNodeMap.get(renderable);
                if(!autoCompleteTextViewTexture.getText().toString().equals(""))
                    changeTexture(Integer.parseInt(autoCompleteTextViewMaterial.getText().toString())-1,autoCompleteTextViewTexture.getText().toString());
            }
        });


        autoCompleteTextViewTexture.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currentNode = viewRenderableNodeMap.get(renderable);
                if(!autoCompleteTextViewMaterial.getText().toString().equals(""))
                    changeTexture(Integer.parseInt(autoCompleteTextViewMaterial.getText().toString())-1,autoCompleteTextViewTexture.getText().toString());
            }
        });
    }


    private void buildModel(HitResult hitResult,File file){
       ViewRenderable.builder()
                .setView(this,R.layout.view_renderable)
                .build()
                .thenAccept(renderable->{
                    viewRenderable = renderable;
                    buildViewRenderable(renderable);

                    renderable.setShadowCaster(false);
                    renderable.setShadowReceiver(false);
                    //renderable.setSizer(new FixedHeightViewSizer(0.5f));
                    //renderable.setSizer(new FixedWidthViewSizer(0.5f));
                })
               .exceptionally(
               (throwable) -> {
                   throw new AssertionError("Could not load plane card view.", throwable);
               });

        RenderableSource renderableSource = RenderableSource
                .builder()
                .setSource(this, Uri.parse(file.getPath()), RenderableSource.SourceType.GLB)
                .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                .build();

        Anchor anchor = hitResult.createAnchor();
        ModelRenderable.builder()
                .setSource(this, renderableSource)
                .build()
                .thenAccept(modelRenderable -> addModel( anchor, modelRenderable))
                .exceptionally(throwable -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Something is not right" + throwable.getMessage()).show();
                    return null;
                });
    }

    private File getFile(String modelName){
        File file = new File(this.getCacheDir(),modelName+".glb");

        if(file.exists()){
            Log.e("manual",this. getClass(). getSimpleName()+" "+"exists");
        }
        else {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference model = storage.getReference().child(modelName+".glb");
            Log.e("manual",this. getClass(). getSimpleName()+" "+model.toString());

            Log.e("manual",this. getClass(). getSimpleName()+" "+"not exists");
            Log.e("manual",this. getClass(). getSimpleName()+" "+this.getCacheDir().toString());
            Toast.makeText(getBaseContext(), "inside", Toast.LENGTH_SHORT).show();

            model.getFile(file)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getBaseContext(),"now it is downloaded",Toast.LENGTH_SHORT);
                            Log.e("manual",this.getClass().getSimpleName()+" "+"now the file is downloaded and stored from storage");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getBaseContext(),e.toString(),Toast.LENGTH_SHORT);
                            Log.e("manual",this.getClass().getSimpleName()+" download failed "+e.toString());
                        }
                    });
            Log.e("manual",this. getClass(). getSimpleName()+" "+file.getName());
        }


        /*try {
            file = File.createTempFile(modelName,".glb",this.getCacheDir());
            Log.e("manual",this. getClass(). getSimpleName()+" "+this.getCacheDir().toString());
            Toast.makeText(getBaseContext(), "inside", Toast.LENGTH_SHORT).show();
            model.getFile(file);
            Log.e("manual",this. getClass(). getSimpleName()+" "+file.getName());
        }  catch (IOException e) {
            Toast.makeText(getBaseContext(), e.getStackTrace().toString(), Toast.LENGTH_SHORT).show();
            this.finish();
        }*/
        return file;
    }


    private void procedure(String modelName){
        File file = getFile(modelName);
        Log.e("manual",this. getClass(). getSimpleName()+" "+"zxzxzx");

        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
            clickNo++;
            if (clickNo == 1) {
                Log.e("manual",this. getClass(). getSimpleName()+" "+"yes");
                buildModel(hitResult,file);
            }
        });
    }


}

