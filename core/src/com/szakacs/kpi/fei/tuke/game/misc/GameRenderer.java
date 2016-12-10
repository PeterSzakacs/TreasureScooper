package com.szakacs.kpi.fei.tuke.game.misc;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Affine2;
import com.szakacs.kpi.fei.tuke.game.arena.game.TreasureScooper;
import com.szakacs.kpi.fei.tuke.game.arena.pipe.Pipe;
import com.szakacs.kpi.fei.tuke.game.arena.pipe.PipeHead;
import com.szakacs.kpi.fei.tuke.game.arena.pipe.PipeSegment;
import com.szakacs.kpi.fei.tuke.game.arena.tunnels.HorizontalTunnel;
import com.szakacs.kpi.fei.tuke.game.arena.tunnels.TunnelCell;
import com.szakacs.kpi.fei.tuke.game.arena.weapon.Bullet;
import com.szakacs.kpi.fei.tuke.game.arena.weapon.Weapon;
import com.szakacs.kpi.fei.tuke.game.enums.ActorType;
import com.szakacs.kpi.fei.tuke.game.enums.Direction;
import com.szakacs.kpi.fei.tuke.game.enums.PipeSegmentType;
import com.szakacs.kpi.fei.tuke.game.intrfc.Actor;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.ManipulableGameInterface;
import com.szakacs.kpi.fei.tuke.game.misc.AdvancedConfigProcessor;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by developer on 2.12.2016.
 */
public class GameRenderer implements ApplicationListener {
    private float elapsedTime = 0;
    private ManipulableGameInterface world;
    private AdvancedConfigProcessor configProcessor;

    private List<TextureAtlas> textures;
    private Map<Direction, Animation> pipeHeadSprites;
    private Map<PipeSegmentType, Sprite> pipeSegmentSprites;
    private Map<ActorType, Map<Direction, Sprite>> actorSprites;

    private SpriteBatch batch;
    private Sprite bgSprite;
    private Sprite nuggetSprite;
    private Sprite bulletSprite;
    private Sprite queue;
    private BitmapFont score;

    public GameRenderer(){
        this.configProcessor = new AdvancedConfigProcessor();
        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            parser.parse(new File("config.xml"), configProcessor);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        this.world = new TreasureScooper(configProcessor);
    }

    @Override
    public void create() {
        this.batch = new SpriteBatch();
        Map<ActorType, List<Direction>> mappings = configProcessor.getActorToDirectionsMap();
        this.actorSprites = new EnumMap<>(ActorType.class);
        for (ActorType at : mappings.keySet()){
            Map<Direction, Sprite> directionsToSpritesMap = new EnumMap<>(Direction.class);
            for (Direction dir : mappings.get(at))
                directionsToSpritesMap.put(dir,
                        new Sprite(
                                new Texture("images/128/Actors/"
                                        + at.name() + "_" + dir.name() + ".png")
                        )
                );
            this.actorSprites.put(at, directionsToSpritesMap);
        }
        this.bgSprite = new Sprite(new Texture(Gdx.files.internal("images/128/background.png")));
        this.nuggetSprite = new Sprite(new Texture(Gdx.files.internal("images/128/Objects/nugget.png")));

        this.pipeHeadSprites = new EnumMap<>(Direction.class);
        this.textures = new ArrayList<>(Direction.values().length);
        for (Direction dir : Direction.values()){
            TextureAtlas ta = new TextureAtlas(Gdx.files.internal(
                    "images/128/PipeHead/sprites/" + dir.name() + ".atlas"));
            this.pipeHeadSprites.put(dir, new Animation(1/10f,
                    ta.getRegions()));
            this.textures.add(ta);
        }

        this.pipeSegmentSprites = new EnumMap<>(PipeSegmentType.class);
        for(PipeSegmentType pst : PipeSegmentType.values()){
            this.pipeSegmentSprites.put(pst, new Sprite(new Texture(
                    Gdx.files.internal("images/128/PipeSegment/" + pst.name() + ".png")
            )));
        }

        this.bulletSprite = new Sprite(new Texture(Gdx.files.internal("images/128/Objects/bullet.png")));
        this.queue = new Sprite(new Texture(
                Gdx.files.internal("images/128/Objects/queue.png")
        ));
        this.queue.setPosition(3712, 1792);

        this.score = new BitmapFont();
        this.score.setColor(Color.YELLOW);
        this.score.getData().setScale(5);
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.update();

        elapsedTime += Gdx.graphics.getDeltaTime();
        batch.begin();
        bgSprite.setPosition(0, 0);
        bgSprite.draw(batch);
        renderQueue();
        renderTunnels();
        renderActors();
        renderPlayer();
        String displayMsg = Integer.toString(world.getPipe().getScore());
        switch (world.getState()) {
            case PLAYING:
                break;
            case WON:
                displayMsg += "\nYou won the game!";
                break;
            case LOST:
                displayMsg += "\nGame over!";
                break;
            default:
                throw new IllegalStateException("Undefined game state: " + world.getState());
        }
        score.draw(batch, displayMsg, 128, 2000);
        batch.end();
        try {
            Thread.sleep(90);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        batch.dispose();
        bgSprite.getTexture().dispose();
        nuggetSprite.getTexture().dispose();
        bulletSprite.getTexture().dispose();
        queue.getTexture().dispose();
        for (TextureAtlas ta : this.textures){
            ta.dispose();
        }
        for (Sprite spr : pipeSegmentSprites.values())
            spr.getTexture().dispose();
        for (ActorType at : actorSprites.keySet()) {
            for (Sprite spr : actorSprites.get(at).values())
                spr.getTexture().dispose();
        }
    }

    public ManipulableGameInterface getWorld() {
        return world;
    }

    /*
     * helper methods
     */

    private void renderQueue(){
        queue.draw(batch);
        Weapon weapon = world.getPipe().getWeapon(this.world);
        List<Bullet> bullets = weapon.getBullets();
        if (!weapon.isEmpty()) {
            for (int i = 0; i < weapon.getCapacity(); i++) {
                if (bullets.get(i) != null) {
                    bulletSprite.setPosition(3744 + i * 32, 1824);
                    bulletSprite.draw(batch);
                }
            }
            Color original = bulletSprite.getColor();
            bulletSprite.setColor(Color.CORAL);
            bulletSprite.setPosition(3744 + weapon.getFront() * 32, 1824);
            bulletSprite.draw(batch);
            bulletSprite.setColor(Color.GREEN);
            bulletSprite.setPosition(3744 + weapon.getRear() * 32, 1824);
            bulletSprite.draw(batch);
            bulletSprite.setColor(original);
        }
    }

    private void renderTunnels(){
        for(HorizontalTunnel tunnel : world.getTunnels()) {
            List<TunnelCell> cells = tunnel.getCells();
            for (TunnelCell cell : cells){
                if (cell.hasNugget()){
                    nuggetSprite.setPosition(cell.getX(), cell.getY());
                    nuggetSprite.draw(batch);
                }
            }
        }
    }

    private void renderActors(){
        for (Actor actor : world.getActors()){
            Sprite actorSprite = actorSprites.get(actor.getType()).get(actor.getDirection());
            actorSprite.setPosition(actor.getX(), actor.getY());
            actorSprite.draw(batch);
        }
    }

    private void renderPlayer(){
        Pipe pipe = world.getPipe();
        PipeHead head = pipe.getHead();
        for (PipeSegment seg : pipe.getSegmentStack()){
            pipeSegmentSprites.get(seg.getSegmentType()).setPosition(seg.getX(), seg.getY());
            pipeSegmentSprites.get(seg.getSegmentType()).draw(batch);
        }
        Animation anim = this.pipeHeadSprites.get(Direction.RIGHT);
        int rotation = 0;
        switch (head.getDirection()){
            case UP:
                rotation = 90;
                break;
            case LEFT:
                rotation = 180;
                break;
            case DOWN:
                rotation = -90;
                break;
        }
        TextureRegion keyFrame = anim.getKeyFrame(elapsedTime, true);
        batch.draw(keyFrame, head.getX(), head.getY(),
                keyFrame.getRegionWidth()/2.0f, keyFrame.getRegionHeight()/2.0f,
                keyFrame.getRegionWidth(), keyFrame.getRegionHeight(),
                1,1, rotation);
    }
}
