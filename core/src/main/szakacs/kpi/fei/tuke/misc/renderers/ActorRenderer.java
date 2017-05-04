package szakacs.kpi.fei.tuke.misc.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.ActorBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.misc.ActorClassInfo;
import szakacs.kpi.fei.tuke.intrfc.misc.GameConfig;
import szakacs.kpi.fei.tuke.intrfc.misc.Rectangle;
import szakacs.kpi.fei.tuke.intrfc.misc.UnregisteredActorInfo;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by developer on 24.1.2017.
 */
public class ActorRenderer extends AbstractGameRenderer {

    private class ActorInfoCustom{
        private Map<Direction, Sprite> directionsToSpritesMap;
        private Animation<TextureRegion> unregisterAnimation;
        private TextureAtlas atlas;

        private void initialize(ActorClassInfo info, Class<? extends ActorBasic> clazz){
            directionsToSpritesMap = new EnumMap<>(Direction.class);
            for (Direction dir : info.getActorDirections())
                directionsToSpritesMap.put(dir,
                        new Sprite(
                                new Texture("images/Actors/" + clazz.getSimpleName()
                                        + "_" + dir.name() + ".png"
                                )
                        )
                );
            Map<String, Object> properties = info.getProperties();
            String onUnregister = (String) properties.get("onUnregister");
            if (onUnregister.startsWith("animation:")){
                String animName = onUnregister.substring(10);
                atlas = new TextureAtlas(Gdx.files.internal(
                        "animations/" + animName + ".atlas")
                );
                unregisterAnimation = new Animation<>(1,
                        atlas.getRegions());
            }
        }

        private void dispose(){
            for (Sprite spr : directionsToSpritesMap.values()) {
                spr.getTexture().dispose();
            }
            if (atlas != null)
                atlas.dispose();
        }
    }

    private Map<Class<? extends ActorBasic>, ActorInfoCustom> actorSprites;

    public ActorRenderer(SpriteBatch batch, GameLevelPrivileged game, GameConfig config) {
        super(batch, game);
        this.initializeActorSprites(config.getActorInfoMap());
    }

    private void initializeActorSprites(Map<Class<? extends ActorBasic>, ActorClassInfo> mappings){
        this.actorSprites = new HashMap<>(mappings.size());
        for (Class<? extends ActorBasic> clazz : mappings.keySet()){
            ActorInfoCustom info = new ActorInfoCustom();
            info.initialize(mappings.get(clazz), clazz);
            actorSprites.put(clazz, info);
        }
    }

    @Override
    public void render() {
        Rectangle actorRectangle;
        for (ActorBasic actor : actorManager.getActors()){
            actorRectangle = actor.getActorRectangle();
            Sprite actorSprite = actorSprites.get(actor.getClass())
                    .directionsToSpritesMap.get(actor.getDirection());
            actorSprite.setPosition(actorRectangle.getRectangleX(), actorRectangle.getRectangleY());
            actorSprite.draw(batch);
        }

        // Every actor that has been removed shall slowly fade into the background after removal
        Set<UnregisteredActorInfo> unregisteredActors = actorManager.getUnregisteredActors();
        for (UnregisteredActorInfo info : unregisteredActors){
            ActorBasic actor = info.getActor();
            actorRectangle = actor.getActorRectangle();
            ActorInfoCustom classInfo = actorSprites.get(actor.getClass());
            if (classInfo.unregisterAnimation == null) {
                Sprite actorSprite = actorSprites.get(actor.getClass())
                        .directionsToSpritesMap.get(actor.getDirection());
                actorSprite.setPosition(
                        actorRectangle.getRectangleX(), actorRectangle.getRectangleY());
                actorSprite.setAlpha((float) 1 / (float) info.getTurnCountSinceUnregister());
                actorSprite.draw(batch);
                actorSprite.setAlpha(1f);
            } else {
                int timeIdx = info.getTurnCountSinceUnregister() - 1;
                batch.draw(classInfo.unregisterAnimation.getKeyFrame(timeIdx),
                        actorRectangle.getRectangleX(), actorRectangle.getRectangleY());
            }
        }
    }

    @Override
    public void dispose() {
        for (ActorInfoCustom info : actorSprites.values()) {
            info.dispose();
        }
    }
}
