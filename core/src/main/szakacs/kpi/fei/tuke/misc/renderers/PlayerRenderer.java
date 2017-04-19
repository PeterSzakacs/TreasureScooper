package szakacs.kpi.fei.tuke.misc.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import szakacs.kpi.fei.tuke.arena.actors.pipe.PipeHead;
import szakacs.kpi.fei.tuke.arena.actors.pipe.PipeSegment;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.enums.PipeSegmentType;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.pipe.PipeBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.misc.Rectangle;
import szakacs.kpi.fei.tuke.misc.renderers.helpers.PlayerSoundsManager;
import szakacs.kpi.fei.tuke.misc.renderers.helpers.WeaponRenderer;

import java.util.*;

/**
 * Created by developer on 24.1.2017.
 */
public class PlayerRenderer extends AbstractGameRenderer {

    private WeaponRenderer weaponRenderer;
    private PlayerSoundsManager soundsManager;

    // TODO: create better quality sounds for push and pop.

    private List<TextureAtlas> textures;
    private Map<Direction, Animation<TextureRegion>> pipeHeadSprites;
    private Map<PipeSegmentType, Sprite> pipeSegmentSprites;
    private float elapsedTime;

    public PlayerRenderer(SpriteBatch batch, GameLevelPrivileged game) {
        super(batch, game);
        this.initializeSprites();
        this.weaponRenderer = new WeaponRenderer(batch, game);
        this.soundsManager = new PlayerSoundsManager(game);
    }

    private void initializeSprites(){
        this.pipeHeadSprites = new EnumMap<>(Direction.class);
        this.textures = new ArrayList<>(Direction.values().length);
        for (Direction dir : Direction.values()){
            TextureAtlas ta = new TextureAtlas(Gdx.files.internal(
                    "images/Pipe/PipeHead/" + dir.name() + ".atlas"));
            this.pipeHeadSprites.put(dir, new Animation<TextureRegion>(1/10f,
                    ta.getRegions()));
            this.textures.add(ta);
        }

        this.pipeSegmentSprites = new EnumMap<>(PipeSegmentType.class);
        for(PipeSegmentType pst : PipeSegmentType.values()){
            this.pipeSegmentSprites.put(pst, new Sprite(new Texture(
                    Gdx.files.internal("images/Pipe/PipeSegment/" + pst.name() + ".png")
            )));
        }
    }

    @Override
    public void render() {
        for (PipeBasic pipe : playerManager.getPipes()) {
            elapsedTime += Gdx.graphics.getDeltaTime();
            PipeHead head = pipe.getHead();
            for (PipeSegment seg : pipe.getSegmentStack()) {
                Rectangle segmentRectangle = seg.getActorRectangle();
                pipeSegmentSprites.get(seg.getSegmentType()).setPosition(
                        segmentRectangle.getRectangleX(),
                        segmentRectangle.getRectangleY()
                );
                pipeSegmentSprites.get(seg.getSegmentType()).draw(batch);
            }
            Animation<TextureRegion> anim = this.pipeHeadSprites.get(Direction.RIGHT);
            int rotation = 0;
            switch (head.getDirection()) {
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
            Rectangle headRectangle = head.getActorRectangle();
            batch.draw(keyFrame, headRectangle.getRectangleX(), headRectangle.getRectangleY(),
                    keyFrame.getRegionWidth() / 2.0f, keyFrame.getRegionHeight() / 2.0f,
                    keyFrame.getRegionWidth(), keyFrame.getRegionHeight(),
                    1, 1, rotation);
            weaponRenderer.renderWeapon(pipe);
            soundsManager.playSounds(pipe);
        }
    }

    @Override
    public void dispose() {
        for (TextureAtlas ta : this.textures){
            ta.dispose();
        }
        for (Sprite spr : pipeSegmentSprites.values())
            spr.getTexture().dispose();
        weaponRenderer.dispose();
        soundsManager.dispose();
    }

    @Override
    public void reset(GameLevelPrivileged game) {
        super.reset(game);
        weaponRenderer.reset(game);
        soundsManager.reset(game);
    }
}
