package szakacs.kpi.fei.tuke.misc.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.enums.TunnelCellType;
import szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.HorizontalTunnelBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.TunnelCellBasic;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by developer on 24.1.2017.
 */
public class TunnelsRenderer extends AbstractGameRenderer {

    private Sprite nuggetSprite;
    private Map<TunnelCellType, Sprite> tunnelCellSprites;
    private Set<TunnelCellBasic> interconnections;

    public TunnelsRenderer(SpriteBatch batch, GameLevelPrivileged game) {
        super(batch, game);
        this.nuggetSprite = new Sprite(new Texture(Gdx.files.internal("images/Tunnels/NUGGET.png")));
        this.tunnelCellSprites = new EnumMap<>(TunnelCellType.class);
        for (TunnelCellType tcType : TunnelCellType.values()) {
            this.tunnelCellSprites.put(
                    tcType,
                    new Sprite(
                            new Texture(Gdx.files.internal("images/Tunnels/Cells/" + tcType.name() + ".png"))
                    )
            );
        }
        this.interconnections = new HashSet<>();
        this.initializeInterconnections();
    }

    @Override
    public void render() {
        for (TunnelCellBasic cell : interconnections) {
            Sprite cellSprite = tunnelCellSprites.get(cell.getCellType());
            cellSprite.setPosition(cell.getX(), cell.getY());
            cellSprite.draw(batch);
        }
        for(HorizontalTunnelBasic tunnel : world.getTunnels()) {
            for (TunnelCellBasic cell : tunnel.getCells()){
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
    public void reset(GameLevelPrivileged game){
        super.reset(game);
        this.initializeInterconnections();
    }

    private void initializeInterconnections(){
        interconnections.clear();
        for (TunnelCellBasic cell : world.getEntrances().values()) {
            for ( ; cell.getCellAtDirection(Direction.DOWN) != null;
                 cell = cell.getCellAtDirection(Direction.DOWN)) {
                interconnections.add(cell);
            }
        }
        for (HorizontalTunnelBasic ht : world.getTunnels()){
            Set<TunnelCellBasic> exits = ht.getCellsBySearchCriteria(cell ->
                    cell.getCellAtDirection(Direction.DOWN) != null
            );
            for (TunnelCellBasic exitCell : exits){
                for (TunnelCellBasic cell = exitCell.getCellAtDirection(Direction.DOWN);
                     cell.getCellAtDirection(Direction.DOWN) != null;
                     cell = cell.getCellAtDirection(Direction.DOWN)) {
                    interconnections.add(cell);
                }
            }

        }
    }
}
