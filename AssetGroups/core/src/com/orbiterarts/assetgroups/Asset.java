package com.orbiterarts.assetgroups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

/**
 * Created by Joseph on 2/24/2016.
 */
public class Asset implements Json.Serializable{

    public Class<?> type;
    public String path;
    public AssetLoaderParameters parameters;

    private static final String TAG = "ASSET CLASS";

    @Override
    public void write(Json json) {

        json.writeValue("assetType",type.getName());
        json.writeValue("path",path);
        json.writeValue("parameters",parameters);

    }

    @Override
    public void read(Json json, JsonValue jsonData) {

        try{
            // get the asset class type as reported in the assets/data/chapterassets.json file
            type = Class.forName(jsonData.get("type").asString());

        }catch(Exception e){
            type = null;
        }

        path = jsonData.get("path").asString();

        JsonValue parametersValue = jsonData.get("parameters");
        parameters = parametersValue != null ? json.fromJson(AssetLoaderParameters.class,
                parametersValue.toString()):null;

    }
}
