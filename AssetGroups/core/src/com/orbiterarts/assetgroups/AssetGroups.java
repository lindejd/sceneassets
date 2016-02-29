package com.orbiterarts.assetgroups;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entries;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class AssetGroups extends ApplicationAdapter implements InputProcessor{

    private static final String TAG = "AssetGroups";

	private SpriteBatch batch;
    private OrthographicCamera camera;
    private Texture background,progressBarImg,progressBarBaseImg;
    private Viewport viewport;

    private SceneData sceneData;
    private Vector2 progrssBarPos;
    private boolean loaded = false;
    private Assets manager;

    private static final float WORLD_TO_SCREEN = 1.0f/100.0f;
    private static final float SCENE_WIDTH = 19.20f;
    private static final float SCENE_HEIGHT = 10.80f;

    private Polygon polygon;
    private Vector3 touch;
    private ShapeRenderer shapeRenderer;

	@Override
	public void create () {
		batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false);

        viewport = new StretchViewport(SCENE_WIDTH,SCENE_HEIGHT,camera);
        viewport.apply();
        camera.position.set(SCENE_WIDTH / 2, SCENE_HEIGHT / 2, 0);

        manager = new Assets("data/assets.json");
        //load assets for the loading screen
        manager.loadGroup("loading_screen");
        manager.finishLoading();

        //get assets
        background = manager.get("loading_screen/background.png");
        progressBarImg = manager.get("loading_screen/progress_bar.png");
        progressBarBaseImg = manager.get("loading_screen/progress_bar_base.png");

        //progress bar position
        progrssBarPos = new Vector2();
        progrssBarPos.set((Gdx.graphics.getWidth() - progressBarImg.getWidth()) >> 1, Gdx.graphics.getHeight() >> 1);

        manager.loadGroup("chapter1");

        touch = new Vector3();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

        Gdx.input.setInputProcessor(this);

	}

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(SCENE_WIDTH/2,SCENE_HEIGHT/2,0);
    }

    @Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(background,
                0.0f, 0.0f,
                0.0f, 0.0f,
                background.getWidth(), background.getHeight(),
                WORLD_TO_SCREEN, WORLD_TO_SCREEN,
                0.0f,
                0, 0,
                background.getWidth(), background.getHeight(),
                false, false);
        batch.end();

        if(!loaded){
            batch.begin();
            batch.draw(progressBarBaseImg,progrssBarPos.x,progrssBarPos.y);
            batch.draw(progressBarImg, progrssBarPos.x, progrssBarPos.y, progressBarBaseImg.getWidth() * manager.getProgress(),
                    progressBarBaseImg.getHeight());
            batch.end();

            if(manager.update()){
                loaded=true;

                sceneData = manager.get("data/chapter1/scene1.json");
                background = sceneData.getSceneBackgroundTexture();

                Gdx.app.log(TAG,"Assets loaded, on to chapter 1");

            }
        }else{

            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin();


            for(ObjectMap.Entry item : sceneData.getHotSpots()){

                shapeRenderer.polygon(sceneData.getHotspot(item.key.toString()).getVertices());

            }

            shapeRenderer.end();
        }

	}

    @Override
    public void dispose() {
        manager.dispose();
        batch.dispose();
        shapeRenderer.dispose();
        background.dispose();

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {


        touch.set((float) screenX, (float) screenY, 0.0f);

        viewport.unproject(touch);

        for(ObjectMap.Entry item : sceneData.getHotSpots()){

            polygon = new Polygon(sceneData.getHotspot(item.key.toString()).getVertices());
            if(polygon.contains(touch.x,touch.y)){
                Gdx.app.log(TAG,"TOUCH!");
            }

        }

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
