package szakacs.kpi.fei.tuke.misc.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import szakacs.kpi.fei.tuke.arena.actors.pipe.PipeHead;
import szakacs.kpi.fei.tuke.arena.actors.pipe.PipeSegment;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.enums.GameState;
import szakacs.kpi.fei.tuke.enums.PipeSegmentType;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.TunnelCellBasic;
import szakacs.kpi.fei.tuke.intrfc.player.PlayerToken;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.pipe.PipeBasic;
import szakacs.kpi.fei.tuke.intrfc.player.PlayerInfo;
import szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.misc.*;
import szakacs.kpi.fei.tuke.intrfc.misc.Stack;
import szakacs.kpi.fei.tuke.misc.renderers.helpers.PlayerSoundsManager;
import szakacs.kpi.fei.tuke.misc.renderers.helpers.WeaponRenderer;

import java.util.*;

/**
 * Renders the pipe and current score of each player.
 * Also manages sounds made by these pipes and displays
 * a the state of his weapon through helper classes
 * {@link PlayerSoundsManager} and {@link WeaponRenderer}.
 */
public class PlayerRenderer extends AbstractGameRenderer {

    private class ScoreRenderingVars {
        private int x;
        private int y;

        private ScoreRenderingVars(int x, int y) {
            this.x = x; this.y = y;
        }
    }

    private float elapsedTime;
    private List<TextureAtlas> textures;
    private Map<Direction, Animation<TextureRegion>> pipeHeadSprites;
    private Map<PipeSegmentType, Sprite> pipeSegmentSprites;
    private Map<PlayerInfo, ScoreRenderingVars> renderingVars;
    private BitmapFont playerScoresFont;
    private final WeaponRenderer weaponRenderer;
    private final PlayerSoundsManager soundsManager;


    public PlayerRenderer(SpriteBatch batch, GameLevelPrivileged game) {
        super(batch, game);
        this.weaponRenderer = new WeaponRenderer(batch, game);
        this.soundsManager = new PlayerSoundsManager(game);
        initializeSprites();
        initializeRenderingVars();
    }

    @Override
    public void render() {
        Map<PlayerToken, PlayerInfo> playerInfoMap = playerManager.getPlayerTokenMap();
        for (PlayerToken token : playerInfoMap.keySet()) {
            PlayerInfo info = playerInfoMap.get(token);
            PipeBasic pipe = info.getPipe();
            elapsedTime += Gdx.graphics.getDeltaTime();
            PipeHead head = pipe.getHead();
            Stack<PipeSegment> segmentStack = pipe.getSegmentStack(token);
            for (PipeSegment seg : segmentStack) {
                Rectangle segmentRectangle = seg.getActorRectangle();
                pipeSegmentSprites.get(seg.getSegmentType()).setPosition(
                        segmentRectangle.getRectangleX(),
                        segmentRectangle.getRectangleY()
                );
                pipeSegmentSprites.get(seg.getSegmentType()).draw(batch);
            }
            Animation<TextureRegion> anim = this.pipeHeadSprites.get(head.getDirection());
            TextureRegion keyFrame = anim.getKeyFrame(elapsedTime, true);
            Rectangle headRectangle = head.getActorRectangle();
            batch.draw(keyFrame, headRectangle.getRectangleX(), headRectangle.getRectangleY());
            renderPlayerScore(info);
            weaponRenderer.renderWeapon(pipe);
            soundsManager.playSounds(pipe);
        }
        if (game.getState() != GameState.PLAYING){
            soundsManager.onEndOfLevel();
        }
    }

    @Override
    public void dispose() {
        for (TextureAtlas ta : textures) {
            ta.dispose();
        }
        for (Sprite spr : pipeSegmentSprites.values()) {
            spr.getTexture().dispose();
        }
        playerScoresFont.dispose();
        weaponRenderer.dispose();
        soundsManager.dispose();
    }

    @Override
    public void reset(GameLevelPrivileged game) {
        super.reset(game);
        weaponRenderer.reset(game);
        soundsManager.reset(game);
        initializeRenderingVars();
    }



    // helper methods



    private void initializeSprites() {
        this.pipeHeadSprites = new EnumMap<>(Direction.class);
        this.textures = new ArrayList<>(Direction.values().length);
        for (Direction dir : Direction.values()) {
            TextureAtlas ta = new TextureAtlas(Gdx.files.internal(
                    "images/Pipe/PipeHead/" + dir.name() + ".atlas"));
            this.pipeHeadSprites.put(dir, new Animation<TextureRegion>(1/10f,
                    ta.getRegions()));
            this.textures.add(ta);
        }

        this.pipeSegmentSprites = new EnumMap<>(PipeSegmentType.class);
        for(PipeSegmentType pst : PipeSegmentType.values()) {
            this.pipeSegmentSprites.put(pst, new Sprite(new Texture(
                    Gdx.files.internal("images/Pipe/PipeSegment/" + pst.name() + ".png")
            )));
        }
    }

    private void initializeRenderingVars() {
        Set<PlayerInfo> infos = game.getPlayerManager().getPlayerInfo();
        this.renderingVars = new HashMap<>(infos.size());
        for (PlayerInfo info : infos) {
            TunnelCellBasic playerEntrance = info.getPipe()
                    .getHead().getCurrentPosition();
            renderingVars.put(
                    info,
                    new ScoreRenderingVars(
                            playerEntrance.getX() + world.getOffsetX()/2,
                            playerEntrance.getY() + world.getOffsetY() + world.getOffsetX()/2
                    )
            );
        }
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
                Gdx.files.internal("fonts/UbuntuMono-B.ttf")
        );
        FreeTypeFontGenerator.FreeTypeFontParameter param =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.color = Color.ORANGE;
        param.size = 80;
        this.playerScoresFont = generator.generateFont(param);
        generator.dispose();
    }

    private void renderPlayerScore(PlayerInfo info) {
        ScoreRenderingVars position = renderingVars.get(info);
        String displayMsg = info.getPlayer().getClass().getSimpleName() + ": " + info.getScore();
        playerScoresFont.draw(batch, displayMsg, position.x, position.y);
    }
}
