package com.szakacs.kpi.fei.tuke.game.misc.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.szakacs.kpi.fei.tuke.game.arena.tunnels.HorizontalTunnel;
import com.szakacs.kpi.fei.tuke.game.arena.tunnels.TunnelCell;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.world.ManipulableGameInterface;

import java.util.List;

/**
 * Created by developer on 24.1.2017.
 */
public class TunnelsRenderer extends AbstractGameRenderer {

    private Sprite nuggetSprite;

    public TunnelsRenderer(SpriteBatch batch, ManipulableGameInterface world) {
        super(batch, world);
        this.nuggetSprite = new Sprite(new Texture(Gdx.files.internal("images/128/Objects/nugget.png")));
    }

    @Override
    public void render() {
        for(HorizontalTunnel tunnel : world.getTunnels()) {
            List<TunnelCell> cells = tunnel.getCells();
            for (TunnelCell cell : cells){
                if (cell.hasNugget()){
                    nuggetSprite.setPosition(cell.getX(), cell.getY());
                    nuggetSprite.draw(batch);
                }
            }
        }
    }

    @Override
    public void dispose() {
        nuggetSprite.getTexture().dispose();
    }
}
