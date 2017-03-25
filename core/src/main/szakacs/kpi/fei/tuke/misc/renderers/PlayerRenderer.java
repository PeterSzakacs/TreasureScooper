package szakacs.kpi.fei.tuke.misc.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import szakacs.kpi.fei.tuke.arena.actors.pipe.Pipe;
import szakacs.kpi.fei.tuke.arena.actors.pipe.PipeSegment;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.arena.actors.pipe.PipeHead;
import szakacs.kpi.fei.tuke.enums.PipeSegmentType;
import szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel.GameLevelPrivileged;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Created by developer on 24.1.2017.
 */
public class PlayerRenderer extends AbstractGameRenderer {

    private List<TextureAtlas> textures;
    private Map<Direction, Animation<TextureRegion>> pipeHeadSprites;
    private Map<PipeSegmentType, Sprite> pipeSegmentSprites;
    private float elapsedTime;

    public PlayerRenderer(SpriteBatch batch, GameLevelPrivileged game) {
        super(batch, game);
        this.initializeSprites();
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
        for (Pipe pipe : playerManager.getPipes()) {
            elapsedTime += Gdx.graphics.getDeltaTime();
            PipeHead head = pipe.getHead();
            for (PipeSegment seg : pipe.getSegmentStack()) {
                pipeSegmentSprites.get(seg.getSegmentType()).setPosition(seg.getX(), seg.getY());
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
            batch.draw(keyFrame, head.getX(), head.getY(),
                    keyFrame.getRegionWidth() / 2.0f, keyFrame.getRegionHeight() / 2.0f,
                    keyFrame.getRegionWidth(), keyFrame.getRegionHeight(),
                    1, 1, rotation);
        }
    }

    @Override
    public void dispose() {
        for (TextureAtlas ta : this.textures){
            ta.dispose();
        }
        for (Sprite spr : pipeSegmentSprites.values())
            spr.getTexture().dispose();
    }
}
