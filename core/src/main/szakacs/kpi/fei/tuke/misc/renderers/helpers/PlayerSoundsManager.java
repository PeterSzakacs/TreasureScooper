package szakacs.kpi.fei.tuke.misc.renderers.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import szakacs.kpi.fei.tuke.arena.actors.pipe.Pipe;
import szakacs.kpi.fei.tuke.arena.actors.pipe.Weapon;
import szakacs.kpi.fei.tuke.intrfc.player.PlayerToken;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.pipe.PipeBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel.GameLevelPrivileged;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by developer on 19.4.2017.
 */
public class PlayerSoundsManager {

    private class PipeInfo {
        private PlayerToken token;

        private int frontIndex;
        private int rearIndex;

        private int numSegments = 0;
        private long pushSoundId = -1;
        private long popSoundId = -1;
        private boolean isPushPlaying = false;
        private boolean isPopPlaying = false;

        private PipeInfo(int frontIndex, int rearIndex, PlayerToken token){
            this.frontIndex = frontIndex; this.rearIndex = rearIndex;
            this.token = token;
        }
    }
    private Map<PipeBasic, PipeInfo> infos;

    private Sound pushSound;
    private Sound popSound;
    private Sound shootSound;
    private Sound loadSound;

    public PlayerSoundsManager(GameLevelPrivileged game){
        this.infos = new HashMap<>(3);
        this.pushSound = Gdx.audio.newSound(Gdx.files.internal("music/push.mp3"));
        this.popSound = Gdx.audio.newSound(Gdx.files.internal("music/pop.mp3"));
        this.shootSound = Gdx.audio.newSound(Gdx.files.internal("music/shoot.mp3"));
        this.loadSound = Gdx.audio.newSound(Gdx.files.internal("music/load.mp3"));
        this.resetPipesInfo(game);
    }


    public void playSounds(PipeBasic pipe){
        PipeInfo info = infos.get(pipe);
        playPipeSound(info, pipe.getSegmentStack(info.token).getNumElements());
        playWeaponSound(info, pipe.getHead().getWeapon());
    }

    private void playPipeSound(PipeInfo info, int numSegments){
        if (numSegments > info.numSegments) {
            popSound.stop(info.popSoundId);
            info.isPopPlaying = false;
            if (!info.isPushPlaying) {
                info.pushSoundId = pushSound.loop();
                info.isPushPlaying = true;
            }
            info.numSegments = numSegments;
        } else if (numSegments < info.numSegments) {
            pushSound.stop(info.pushSoundId);
            info.isPushPlaying = false;
            if (!info.isPopPlaying){
                info.popSoundId = popSound.loop();
                info.isPopPlaying = true;
            }
            info.numSegments = numSegments;
        } else {
            pushSound.stop(info.pushSoundId);
            info.isPushPlaying = false;
            popSound.stop(info.popSoundId);
            info.isPopPlaying = false;
            // do not update numSegments, if the pipe has not moved (saves a little overhead)
        }
    }

    private void playWeaponSound(PipeInfo info, Weapon weapon){
        if (weapon.getFrontIndex() != info.frontIndex){
            shootSound.play();
            info.frontIndex = weapon.getFrontIndex();
        }
        if (weapon.getRearIndex() != info.rearIndex){
            loadSound.play();
            info.rearIndex = weapon.getRearIndex();
        }
    }

    public void reset(GameLevelPrivileged game){
        pushSound.stop();
        popSound.stop();
        resetPipesInfo(game);
    }

    private void resetPipesInfo(GameLevelPrivileged game){
        infos.clear();
        Map<PlayerToken, Pipe> pipesMap = game.getPlayerManager().getPipesUpdatable();
        for (PlayerToken token : pipesMap.keySet()){
            Pipe pipe = pipesMap.get(token);
            Weapon weapon = pipe.getHead().getWeapon();
            infos.put(pipe, new PipeInfo(
                    weapon.getFrontIndex(),
                    weapon.getRearIndex(),
                    token)
            );
        }
    }

    public void dispose(){
        pushSound.dispose();
        popSound.dispose();
        shootSound.dispose();
        loadSound.dispose();
    }
}
