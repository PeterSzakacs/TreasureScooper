package com.szakacs.kpi.fei.tuke.game.misc.renderers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.szakacs.kpi.fei.tuke.game.enums.ActorType;
import com.szakacs.kpi.fei.tuke.game.enums.Direction;
import com.szakacs.kpi.fei.tuke.game.intrfc.actors.Actor;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.Game;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.GamePrivileged;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Created by developer on 24.1.2017.
 */
public class ActorRenderer extends AbstractGameRenderer {

    private Map<ActorType, Map<Direction, Sprite>> actorSprites;

    public ActorRenderer(SpriteBatch batch, GamePrivileged game, Map<ActorType, List<Direction>> mappings) {
        super(batch, game);
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
        for (Actor actor : actorManager.getActors()){
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
