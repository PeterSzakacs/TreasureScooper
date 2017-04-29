package szakacs.kpi.fei.tuke.misc.configProcessors.SAXprocessor;

import org.junit.Assert;
import org.junit.Test;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.intrfc.player.Player;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.ActorBasic;
import szakacs.kpi.fei.tuke.intrfc.misc.GameConfig;
import szakacs.kpi.fei.tuke.intrfc.misc.GameConfigProcessor;
import szakacs.kpi.fei.tuke.intrfc.misc.GameWorldPrototype;
import szakacs.kpi.fei.tuke.misc.ConfigProcessingException;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyEntrance;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyLevel;

import java.util.Map;
import java.util.Set;

/**
 * Created by developer on 17.2.2017.
 */
public class SAXConfigProcessorTest {

    @Test
    public void quickTest() throws Exception {
        GameConfigProcessor saxProcessor;
        try {
            saxProcessor = new SAXConfigProcessor();
            saxProcessor.processGameConfig();
        } catch (ConfigProcessingException e) {
            throw new Exception("The processor has failed the test (or the resource is not valid)", e);
        }
        printConfig(saxProcessor.getGameConfig());
    }

    private void printConfig(GameConfig config) {
        int levelIdx = 1;
        System.out.print("\n\n");
        for (DummyLevel level : config.getLevels()){
            System.out.println("level number: " + levelIdx);
            System.out.println("gameType: " + level.getGameType());
            GameWorldPrototype prototype = level.getGameWorldPrototype();
            Map<DummyEntrance, Class<? extends Player>> map = level.getEntranceToPlayerMap();
            Assert.assertTrue(map.size() > 0);
            Assert.assertTrue(prototype != null);
            for (DummyEntrance de : map.keySet()){
                System.out.println();
                System.out.println("Entrance with id " + de.getId()
                        + "\nleading to tunnel " + de.getTunnel().getId()
                        + "\nat position X: " + de.getX() + ", Y: " + de.getY()
                        + "\nis assigned to player: " + map.get(de).getSimpleName());
            }
            System.out.print("\n\n\n");
            levelIdx++;
        }
        Map<Class<? extends ActorBasic>, Set<Direction>> map = config.getActorInfoMap();
        for (Class clazz : map.keySet()){
            System.out.println("Actor of type " + clazz.getSimpleName() + " can be oriented in the following directions:");
            for (Direction direction: map.get(clazz))
                System.out.println(direction.name());
        }
    }
}