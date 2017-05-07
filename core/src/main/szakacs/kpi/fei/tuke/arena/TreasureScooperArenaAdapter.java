package szakacs.kpi.fei.tuke.arena;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import kpi.openlab.arena.gdx.ArenaLwjglApplication;
import kpi.openlab.arena.impl.BotImpl;
import kpi.openlab.arena.impl.BotResultImpl;
import kpi.openlab.arena.interfaces.Bot;
import kpi.openlab.arena.interfaces.BotResult;
import szakacs.kpi.fei.tuke.intrfc.player.Player;
import szakacs.kpi.fei.tuke.intrfc.misc.GameConfig;
import szakacs.kpi.fei.tuke.intrfc.misc.GameConfigProcessor;
import szakacs.kpi.fei.tuke.intrfc.misc.GameResults;
import szakacs.kpi.fei.tuke.misc.ConfigProcessingException;
import szakacs.kpi.fei.tuke.misc.CoreGameRenderer;
import szakacs.kpi.fei.tuke.misc.GameLevelInitializationException;
import szakacs.kpi.fei.tuke.misc.configProcessors.SAXprocessor.SAXConfigProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by developer on 21.3.2017.
 */
public class TreasureScooperArenaAdapter extends ArenaLwjglApplication<CoreGameRenderer, Player> {

    private CoreGameRenderer renderer;
    private List<Bot<Player>> bots;

    public TreasureScooperArenaAdapter() throws ConfigProcessingException, GameLevelInitializationException {
        GameConfigProcessor configProcessor = new SAXConfigProcessor();
        configProcessor.processGameConfig();
        GameConfig config = configProcessor.getGameConfig();
        this.renderer = new CoreGameRenderer(new GameManager(config), config);

        Set<Class<? extends Player>> playerClasses = config.getPlayerClasses();
        this.bots = new ArrayList<>(playerClasses.size());
        for (Class<? extends Player> playerCls : playerClasses){
            try {
                bots.add(new BotImpl<>(playerCls.hashCode(), playerCls.getSimpleName(), playerCls.newInstance()));
            } catch (InstantiationException | IllegalAccessException e) {
                throw new ConfigProcessingException("Could not initialize bots");
            }
        }
    }

    @Override
    protected CoreGameRenderer arenaStarting(List<Bot<Player>> bots) {
        return renderer;
    }

    @Override
    protected List<BotResult> arenaFinishing(CoreGameRenderer app) {
        GameResults results = app.getManager().getResults();
        List<BotResult> botResults = new ArrayList<>(bots.size());
        for (Bot<Player> bot : bots){
            botResults.add(new BotResultImpl(
                    bot.getId(),
                    results.getTotalGameScores().get(bot.getBotInstance().getClass()).getScore()
            ));
        }
        return botResults;
    }

    @Override
    public int getNumberOfRequiredBots() {
        return bots.size();
    }

    @Override
    public boolean isHigherScoreBetter() {
        return true;
    }

    @Override
    public Class<Player> getBotInterfaceClass() {
        return Player.class;
    }

    @Override
    protected LwjglApplicationConfiguration getLwjglConfiguration() {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = renderer.getWorld().getWidth();
        config.height = renderer.getWorld().getHeight();
        config.title = "Treasure Scooper";
        config.foregroundFPS = 12;
        return config;
    }

    public List<Bot<Player>> getBotList(){
        return bots;
    }
}
