package szakacs.kpi.fei.tuke.misc.renderers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.ActorBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.misc.ActorRectangle;
import szakacs.kpi.fei.tuke.intrfc.misc.GameConfig;
import szakacs.kpi.fei.tuke.intrfc.misc.Rectangle;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by developer on 24.1.2017.
 */
public class ActorRenderer extends AbstractGameRenderer {

    private Map<Class<? extends ActorBasic>, Map<Direction, Sprite>> actorSprites;

    public ActorRenderer(SpriteBatch batch, GameLevelPrivileged game, GameConfig config) {
        super(batch, game);
        this.initializeActorSprites(config.getActorToDirectionsMap());
    }

    private void initializeActorSprites(Map<Class<? extends ActorBasic>, Set<Direction>> mappings){
        this.actorSprites = new HashMap<>(mappings.size());
        for (Class<? extends ActorBasic> clazz : mappings.keySet()){
            Map<Direction, Sprite> directionsToSpritesMap = new EnumMap<>(Direction.class);
            for (Direction dir : mappings.get(clazz))
                directionsToSpritesMap.put(dir,
                        new Sprite(
                                new Texture("images/Actors/"
                                        + clazz.getSimpleName() + "_" + dir.name() + ".png")
                        )
                );
            this.actorSprites.put(clazz, directionsToSpritesMap);
        }
    }

    @Override
    public void render() {
        Rectangle actorRectangle;
        for (ActorBasic actor : actorManager.getActors()){
            actorRectangle = actor.getActorRectangle();
            Sprite actorSprite = actorSprites.get(actor.getClass()).get(actor.getDirection());
            actorSprite.setPosition(actorRectangle.getRectangleX(), actorRectangle.getRectangleY());
            actorSprite.draw(batch);
        }

        // Every actor that has been removed shall slowly fade into the background after removal
        Map<ActorBasic, Integer> unregisteredActors = actorManager.getUnregisteredActors();
        for (ActorBasic actor : unregisteredActors.keySet()){
            actorRectangle = actor.getActorRectangle();
            Sprite actorSprite = actorSprites.get(actor.getClass()).get(actor.getDirection());
            actorSprite.setPosition(actorRectangle.getRectangleX(), actorRectangle.getRectangleY());
            actorSprite.setAlpha((float)1/(float)unregisteredActors.get(actor));
            actorSprite.draw(batch);
            actorSprite.setAlpha(1f);
        }
    }

    @Override
    public void dispose() {
        for (Class clazz : actorSprites.keySet()) {
            for (Sprite spr : actorSprites.get(clazz).values()) {
                spr.getTexture().dispose();
            }
        }
    }
}
