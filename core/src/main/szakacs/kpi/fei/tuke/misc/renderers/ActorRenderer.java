package szakacs.kpi.fei.tuke.misc.renderers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.enums.ActorType;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.Actor;
import szakacs.kpi.fei.tuke.intrfc.arena.game.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.misc.GameConfig;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by developer on 24.1.2017.
 */
public class ActorRenderer extends AbstractGameRenderer {

    private Map<Class<? extends Actor>, Map<Direction, Sprite>> actorSprites;

    public ActorRenderer(SpriteBatch batch, GameLevelPrivileged game, GameConfig config) {
        super(batch, game);
        this.initializeActorSprites(config.getActorToDirectionsMap());
    }

    private void initializeActorSprites(Map<Class<? extends Actor>, Set<Direction>> mappings){
        this.actorSprites = new HashMap<>(mappings.size());
        for (Class<? extends Actor> clazz : mappings.keySet()){
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
        for (Actor actor : actorManager.getActors()){
            Sprite actorSprite = actorSprites.get(actor.getClass()).get(actor.getDirection());
            actorSprite.setPosition(actor.getX(), actor.getY());
            actorSprite.draw(batch);
        }

        // Every actor that has been removed shall slowly fade into the background after removal
        Map<Actor, Integer> unregisteredActors = actorManager.getUnregisteredActors();
        for (Actor actor : unregisteredActors.keySet()){
            Sprite actorSprite = actorSprites.get(actor.getClass()).get(actor.getDirection());
            actorSprite.setPosition(actor.getX(), actor.getY());
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
