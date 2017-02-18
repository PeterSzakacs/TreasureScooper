package szakacs.kpi.fei.tuke.misc.renderers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.enums.ActorType;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.Actor;
import szakacs.kpi.fei.tuke.intrfc.arena.game.GameLevelPrivileged;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by developer on 24.1.2017.
 */
public class ActorRenderer extends AbstractGameRenderer {

    private Map<ActorType, Map<Direction, Sprite>> actorSprites;

    public ActorRenderer(SpriteBatch batch, GameLevelPrivileged game, Map<ActorType, Set<Direction>> mappings) {
        super(batch, game);
        this.initializeActorSprites(mappings);
    }

    private void initializeActorSprites(Map<ActorType, Set<Direction>> mappings){
        this.actorSprites = new EnumMap<>(ActorType.class);
        for (ActorType at : mappings.keySet()){
            Map<Direction, Sprite> directionsToSpritesMap = new EnumMap<>(Direction.class);
            for (Direction dir : mappings.get(at))
                directionsToSpritesMap.put(dir,
                        new Sprite(
                                new Texture("images/Actors/"
                                        + at.name() + "_" + dir.name() + ".png")
                        )
                );
            this.actorSprites.put(at, directionsToSpritesMap);
        }
    }

    @Override
    public void render() {
        for (Actor actor : actorManager.getActors()){
            Sprite actorSprite = actorSprites.get(actor.getType()).get(actor.getDirection());
            actorSprite.setPosition(actor.getX(), actor.getY());
            actorSprite.draw(batch);
        }

        // Every actor that has been removed shall slowly fade into the background after removal
        Map<Actor, Integer> unregisteredActors = actorManager.getUnregisteredActors();
        for (Actor actor : unregisteredActors.keySet()){
            Sprite actorSprite = actorSprites.get(actor.getType()).get(actor.getDirection());
            actorSprite.setPosition(actor.getX(), actor.getY());
            actorSprite.setAlpha((float)1/(float)unregisteredActors.get(actor));
            actorSprite.draw(batch);
            actorSprite.setAlpha(1f);
        }
    }

    @Override
    public void dispose() {
        for (ActorType at : actorSprites.keySet()) {
            for (Sprite spr : actorSprites.get(at).values()) {
                spr.getTexture().dispose();
            }
        }
    }
}
