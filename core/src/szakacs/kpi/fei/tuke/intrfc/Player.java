package szakacs.kpi.fei.tuke.intrfc;

import szakacs.kpi.fei.tuke.intrfc.misc.proxies.PlayerGameInterface;

/**
 * Created by developer on 2.12.2016.
 */
public interface Player {
    void act();

    void initialize(PlayerGameInterface world);

    void deallocate();
}
