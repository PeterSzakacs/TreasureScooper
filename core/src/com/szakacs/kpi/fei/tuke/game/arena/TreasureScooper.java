package com.szakacs.kpi.fei.tuke.game.arena;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.szakacs.kpi.fei.tuke.game.enums.Direction;
import com.szakacs.kpi.fei.tuke.game.enums.GameState;
import com.szakacs.kpi.fei.tuke.game.enums.PipeSegmentType;
import com.szakacs.kpi.fei.tuke.game.intrfc.Actor;
import com.szakacs.kpi.fei.tuke.game.misc.ConfigurationProcessor;
import com.szakacs.kpi.fei.tuke.game.player.PipeA;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreasureScooper implements ApplicationListener {
	private int width = 4096;
    private int height = 2048;
    private int offsetX  = 128;
    private int offsetY = 128;
    private int initX = 1920;
    private int initY = 1664;

    private SpriteBatch batch;
    private Sprite bgSprite;
    private BitmapFont score;
    private Sprite nuggetSprite;
    private Map<Direction, Sprite> moleSprites;
    private Map<Direction, Sprite> pipeHeadSprites;
    private Map<PipeSegmentType, Sprite> pipeSegmentSprites;
    private GameState state;

    private int remainingNuggetsCount;
    private AbstractPlayer player;
    private List<HorizontalTunnel> tunnels;
    private List<List<Enemy>> enemies;

    public TreasureScooper() {
        List<List<Integer>> entrances = new ArrayList<List<Integer>>();
        List<Integer> yCoordinatesOfLevels = new ArrayList<Integer>();
        ConfigurationProcessor configProc = new ConfigurationProcessor();
        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            parser.parse(new File("images/128/config.xml"), configProc);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } finally {
            entrances = configProc.getLevelEntrances();
            yCoordinatesOfLevels = configProc.getyCoordinatesOfLevels();
            this.tunnels = new ArrayList<HorizontalTunnel>(6);
            for (int idx = 0; idx < entrances.size(); idx++){
                HorizontalTunnel ht = new HorizontalTunnel(yCoordinatesOfLevels.get(idx), entrances.get(idx), this);
                this.tunnels.add(ht);
                if (idx > 0){
                    HorizontalTunnel previous = tunnels.get(idx - 1);
                    ht.setPreviousTunnel(previous);
                    previous.setNextTunnel(ht);
                }
                this.remainingNuggetsCount += ht.getNuggetCount();
            }
            this.enemies = new ArrayList<List<Enemy>>(6);
        }
/*        for (int x = 0; x < 4096; x += 128) {
            this.tunnels.get(0).collectNugget(x);
        }*/
    }

	@Override
	public void create () {
        this.batch = new SpriteBatch();
        this.bgSprite = new Sprite(new Texture(Gdx.files.internal("images/128/background.png")));
        this.nuggetSprite = new Sprite(new Texture(Gdx.files.internal("images/128/Objects/nugget.png")));
        this.moleSprites = new HashMap<Direction, Sprite>(2);
        this.moleSprites.put(Direction.LEFT, new Sprite(new Texture("images/128/Enemies/mole_left.png")));
        this.moleSprites.put(Direction.RIGHT, new Sprite(new Texture("images/128/Enemies/mole_right.png")));
        this.pipeHeadSprites = new HashMap<Direction, Sprite>(4);
        for (Direction dir : Direction.values()){
            pipeHeadSprites.put(dir, new Sprite(new Texture(
                    Gdx.files.internal("images/128/PipeHead/" + dir.name() + ".png")
            )));
        }
        this.pipeSegmentSprites = new HashMap<PipeSegmentType, Sprite>(6);
        for(PipeSegmentType pst : PipeSegmentType.values()){
            this.pipeSegmentSprites.put(pst, new Sprite(new Texture(
                    Gdx.files.internal("images/128/PipeSegment/" + pst.name() + ".png")
            )));
        }
        this.player = new PipeA(this);
        this.score = new BitmapFont();
        this.score.setColor(Color.YELLOW);
        this.score.getData().setScale(5);
        this.state = GameState.PLAYING;
    }

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        String displayMsg = Integer.toString(player.getScore());
        switch (state){
            case PLAYING:
                update();
                break;
            case WON:
                displayMsg += "\nYou won the game!";
                break;
            case LOST:
                displayMsg += "\nGame over!";
                break;
            default:
                throw new IllegalStateException("Undefined game state: " + state);
        }

		batch.begin();
        bgSprite.setPosition(0, 0);
        bgSprite.draw(batch);
        GoldNugget nugget;
        for(HorizontalTunnel tunnel : tunnels) {
            GoldNugget[] nuggets = tunnel.getNuggets();
            for (int i = 0; i < nuggets.length; i++) {
                nugget = nuggets[i];
                if (nugget != null) {
                    nuggetSprite.setPosition(i * this.offsetX, tunnel.getY());
                    nuggetSprite.draw(batch);
                }
            }
            for (Enemy enemy : tunnel.getEnemies()){
                this.moleSprites.get(enemy.getDirection()).setPosition(enemy.getX(), enemy.getY());
                this.moleSprites.get(enemy.getDirection()).draw(batch);
            }
        }
        PipeHead head = player.getHead();
        for (PipeSegment seg : player.getSegmentStack()){
            pipeSegmentSprites.get(seg.getSegmentType()).setPosition(seg.getX(), seg.getY());
            pipeSegmentSprites.get(seg.getSegmentType()).draw(batch);
        }
        pipeHeadSprites.get(head.getDirection()).setPosition(head.getX(), head.getY());
        pipeHeadSprites.get(head.getDirection()).draw(batch);
        score.draw(batch, displayMsg, 128, 2000);

        batch.end();
	}

    private void update() {
        this.player.act();
        this.player.setOperationApplied(false);
        for (HorizontalTunnel ht : tunnels)
            ht.act();
        try {
            Thread.sleep(80);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose () {
		batch.dispose();
        bgSprite.getTexture().dispose();
        nuggetSprite.getTexture().dispose();
        for (Sprite spr : pipeHeadSprites.values())
            spr.getTexture().dispose();
        for (Sprite spr : pipeSegmentSprites.values())
            spr.getTexture().dispose();
        for (Sprite spr : moleSprites.values())
            spr.getTexture().dispose();
	}


    public HorizontalTunnel getTunnel(int offsetFromTopmost){
        if (offsetFromTopmost < 0 || offsetFromTopmost > tunnels.size())
            return null;
        return tunnels.get(offsetFromTopmost);
    }

    public HorizontalTunnel getTunnelByYCoordinate(int y){
        HorizontalTunnel result = null;
        for (HorizontalTunnel tunnel : tunnels){
            if (tunnel.getY()/128 == y/128) {
                result = tunnel;
                break;
            }
        }
        return result;
    }

    void setGameState(GameState state){
        this.state = state;
    }

    public boolean intersects(Actor ActorA, Actor ActorB){
        if (ActorA.getX()/offsetX == ActorB.getX()/offsetX)
            if (ActorA.getY()/offsetY == ActorB.getY()/offsetY)
                return true;
        return false;
    }

    void onNuggetCollected(){
        this.remainingNuggetsCount--;
    }

    /*
     * begin getters & setters
     */

    public AbstractPlayer getPlayer(){
        return this.player;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public int getInitX() {
        return initX;
    }

    public int getInitY() {
        return initY;
    }

    public int getRemainingNuggetsCount(){
        return this.remainingNuggetsCount;
    }

    public List<List<Enemy>> getEnemies(){
        this.enemies.clear();
        for (HorizontalTunnel ht : this.tunnels){
            this.enemies.add(ht.getEnemies());
        }
        return this.enemies;
    }

    List<HorizontalTunnel> getTunnels() {
        return tunnels;
    }

    /*
     * end getters & setters
     */
}