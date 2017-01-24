package com.szakacs.kpi.fei.tuke.game.misc.renderers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.szakacs.kpi.fei.tuke.game.enums.ActorType;
import com.szakacs.kpi.fei.tuke.game.enums.Direction;
import com.szakacs.kpi.fei.tuke.game.intrfc.Actor;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.world.ManipulableGameInterface;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Created by developer on 24.1.2017.
 */
public class ActorRenderer extends AbstractGameRenderer {

    private Map<ActorType, Map<Direction, Sprite>> actorSprites;

    public ActorRenderer(SpriteBatch batch, ManipulableGameInterface world, Map<ActorType, List<Direction>> mappings) {
        super(batch, world);
        this.initializeActorSprites(mappings);
    }

    private void initializeActorSprites(Map<ActorType, List<Direction>> mappings){
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
    }

    @Override
    public void render() {
        for (Actor actor : world.getActors()){
            Sprite actorSprite = actorSprites.get(actor.getType()).get(actor.getDirection());
            actorSprite.setPosition(actor.getX(), actor.getY());
            actorSprite.draw(batch);
        }
    }

    @Override
    public void dispose() {
        for (ActorType at : actorSprites.keySet()) {
            for (Sprite spr : actorSprites.get(at).values())
                spr.getTexture().dispose();
        }
    }
}
