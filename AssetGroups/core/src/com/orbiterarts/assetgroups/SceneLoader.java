package com.orbiterarts.assetgroups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

/**
 * Created by Joseph on 2/24/2016.
 */
public class SceneLoader extends AsynchronousAssetLoader<SceneData,SceneLoader.SceneParameter>{
    static public class SceneParameter extends AssetLoaderParameters<SceneData> {}

    public static final String TAG = "SceneLoader";
    private SceneData sceneData = null;

    public SceneLoader(FileHandleResolver resolver){
        super(resolver);
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, SceneParameter parameter) {

        Array<AssetDescriptor> dependencies = new Array<AssetDescriptor>();

        dependencies.add(new AssetDescriptor<Texture>(stripExtension(fileName)+".jpg",Texture.class));

        return dependencies;

    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, SceneParameter parameter) {

        Gdx.app.log(TAG, "loading" + fileName);

        sceneData = new SceneData();
        sceneData.backgroundTexture = manager.get(stripExtension(fileName)+".jpg",Texture.class);

        try{
            JsonReader reader = new JsonReader();
            JsonValue root = reader.parse(file);

            sceneData.numHotspots = root.getInt("hotspots");
            sceneData.numProps = root.getInt("props");

            JsonValue hotspotList = root.get("hotspotList");
            JsonValue.JsonIterator hotspotIter = hotspotList.iterator();

            // iterate thru the hotspot list in the json file, attaching the hotspot vertices
            // to the hotspot name in the ObjectMap defined in the SceneData class
            while(hotspotIter.hasNext()){
                JsonValue hotspotVal = hotspotIter.next();
                String name = hotspotVal.getString("name");
                String vertices = hotspotVal.getString("vertices");
                Polygon polygon = new Polygon(getVertices(vertices));
                sceneData.hotSpots.put(name,polygon);

                Gdx.app.log(TAG,"loaded "+name+" polygon vertices");
            }
            Gdx.app.log(TAG,"Hotspots: "+((Integer)sceneData.numHotspots).toString());
            Gdx.app.log(TAG,"Props:"+((Integer)sceneData.numProps).toString());


        }catch(Exception e){
            Gdx.app.log(TAG,"error reading file "+fileName+" "+e.getMessage());
        }

    }


    private float[] getVertices(String vertices) {

        String[] strArray = vertices.replaceAll(" ","").split(",");
        float[] vertArray = new float[strArray.length];

        for(int i=0;i<strArray.length;i++){
            try{
                vertArray[i] = Float.parseFloat(strArray[i]);
            }catch (NumberFormatException nfe){
                Gdx.app.log(TAG,nfe.toString());
            }
        }
        return vertArray;
    }

    private String stripExtension(String fileName) {

        if(fileName==null)
            return null;

        int pos = fileName.lastIndexOf(".");

        if(pos == -1)
            return fileName;

        return fileName.substring(0,pos);

    }

    @Override
    public SceneData loadSync(AssetManager manager, String fileName, FileHandle file, SceneParameter parameter) {

        return sceneData;
    }



}
