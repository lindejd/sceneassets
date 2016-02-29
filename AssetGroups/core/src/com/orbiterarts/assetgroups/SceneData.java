package com.orbiterarts.assetgroups;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Created by Joseph on 2/24/2016.
 */
public class SceneData {

    Texture backgroundTexture = null;
    int numHotspots = 0;
    int numProps = 0;

    ObjectMap<String,Polygon> hotSpots = new ObjectMap<String,Polygon>();
    ObjectMap<String,String> action=new ObjectMap<String, String>();
    ObjectMap<String,String> links = new ObjectMap<String, String>();

    public Texture getSceneBackgroundTexture(){
        return backgroundTexture;
    }

    public Polygon getHotspot(String hotspotName){
        return hotSpots.get(hotspotName);
    }

    public ObjectMap<String,Polygon> getHotSpots(){
        return hotSpots;
    }

}
