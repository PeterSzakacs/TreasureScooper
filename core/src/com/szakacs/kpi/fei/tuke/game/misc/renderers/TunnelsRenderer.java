package com.szakacs.kpi.fei.tuke.game.misc.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.szakacs.kpi.fei.tuke.game.arena.world.HorizontalTunnel;
import com.szakacs.kpi.fei.tuke.game.arena.world.TunnelCell;
import com.szakacs.kpi.fei.tuke.game.enums.Direction;
import com.szakacs.kpi.fei.tuke.game.enums.TunnelCellType;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.GamePrivileged;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Created by developer on 24.1.2017.
 */
public class TunnelsRenderer extends AbstractGameRenderer {

    private Sprite nuggetSprite;
    private Map<TunnelCellType, Sprite> tunnelCellSprites;
    private List<TunnelCell> interconnections;

    public TunnelsRenderer(SpriteBatch batch, GamePrivileged game) {
        super(batch, game);
        this.nuggetSprite = new Sprite(new Texture(Gdx.files.internal("images/128/Objects/nugget.png")));
        this.tunnelCellSprites = new EnumMap<>(TunnelCellType.class);
        for (TunnelCellType tcType : TunnelCellType.values()) {
            this.tunnelCellSprites.put(
                    tcType,
                    new Sprite(
                            new Texture(Gdx.files.internal("images/128/Tunnels/" + tcType.name() + ".png"))
                    )
            );
        }
        this.initializeInterconnections();
    }

    @Override
    public void render() {
        for (TunnelCell cell : interconnections) {
            Sprite cellSprite = tunnelCellSprites.get(cell.getCellType());
            cellSprite.setPosition(cell.getX(), cell.getY());
            cellSprite.draw(batch);
        }
        for(HorizontalTunnel tunnel : world.getTunnels()) {
            for (TunnelCell cell : tunnel.getCells()){
                Sprite cellSprite = tunnelCellSprites.get(cell.getCellType());
                cellSprite.setPosition(cell.getX(), cell.getY());
                cellSprite.draw(batch);
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

    @Override
    public void reset(GamePrivileged game){
        super.reset(game);
        this.initializeInterconnections();
    }

    private void initializeInterconnections(){
        this.interconnections = new ArrayList<>();
        for (TunnelCell cell = world.getRootCell();
             cell.getCellType() != TunnelCellType.ENTRANCE;
             cell = cell.getCellAtDirection(Direction.DOWN)) {
            interconnections.add(cell);
        }
        for (HorizontalTunnel ht : world.getTunnels()){
            List<TunnelCell> exits = ht.getCellsBySearchCriteria(cell ->
                    cell.getCellType() == TunnelCellType.EXIT
            );
            for (TunnelCell exitCell : exits){
                for (TunnelCell cell = exitCell.getCellAtDirection(Direction.DOWN);
                     cell.getCellType() != TunnelCellType.ENTRANCE;
                     cell = cell.getCellAtDirection(Direction.DOWN)) {
                    interconnections.add(cell);
                }
            }

        }
    }
}
