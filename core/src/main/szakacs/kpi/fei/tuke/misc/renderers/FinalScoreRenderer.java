package szakacs.kpi.fei.tuke.misc.renderers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import szakacs.kpi.fei.tuke.intrfc.Player;
import szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.misc.GameRenderer;
import szakacs.kpi.fei.tuke.intrfc.misc.GameResults;

import java.util.List;
import java.util.Map;

/**
 * Created by developer on 16.3.2017.
 */
public class FinalScoreRenderer implements GameRenderer {

    private class RenderingVars {
        private final SpriteBatch batch;
        private final BitmapFont totalScoreFont;
        private final BitmapFont levelScoreFont;
        private final GlyphLayout glyphLayout;
        private final int width;
        private final int height;

        private RenderingVars(SpriteBatch batch, int width, int height){
            this.batch = batch;
            this.totalScoreFont = new BitmapFont();
            this.levelScoreFont = new BitmapFont();
            this.glyphLayout = new GlyphLayout();
            totalScoreFont.setColor(Color.WHITE);
            totalScoreFont.getData().setScale(5);
            levelScoreFont.setColor(Color.WHITE);
            levelScoreFont.getData().setScale(4);
            this.width = width;
            this.height = height;
        }
    }

    private final RenderingVars renderingVars;
    private final List<Map<Class<? extends Player>, Integer>> levelScores;
    private final Map<Class<? extends Player>, Integer> totalScores;
    private final StringBuilder scoreStringBuilder;

    public FinalScoreRenderer(SpriteBatch batch, GameResults results, int width, int height){
        this.renderingVars = new RenderingVars(batch, width, height);
        this.levelScores = results.getLevelScores();
        this.totalScores = results.getTotalGameScores();
        this.scoreStringBuilder = new StringBuilder();
    }

    @Override
    public void render(){
        renderLevelScores();
        renderTotalScore();
    }

    @Override
    public void dispose() {
        renderingVars.totalScoreFont.dispose();
    }

    @Override
    public void reset(GameLevelPrivileged game) {
    }

    private void renderTotalScore(){
        scoreStringBuilder.append("Total scores:\n");
        for (Class clazz : totalScores.keySet()){
            scoreStringBuilder.append(clazz.getSimpleName())
                    .append(" has scored ")
                    .append(totalScores.get(clazz).intValue())
                    .append(" points\n");
        }
        renderingVars.glyphLayout.setText(renderingVars.totalScoreFont, scoreStringBuilder.toString());
        renderingVars.totalScoreFont.draw(renderingVars.batch, scoreStringBuilder.toString(),
                renderingVars.width/2 - renderingVars.glyphLayout.width/2,
                renderingVars.height/2 + renderingVars.glyphLayout.height/2
        );

        scoreStringBuilder.setLength(0);
    }

    private void renderLevelScores(){
        scoreStringBuilder.append("Per level scores:\n");
        for (int i = 0; i < levelScores.size(); i++) {
            scoreStringBuilder.append("    Level").append(i+1).append("\n");
            Map<Class<? extends Player>, Integer> levelScore = levelScores.get(i);
            for (Class clazz : levelScore.keySet()) {
                scoreStringBuilder.append("        ").append(clazz.getSimpleName())
                        .append(": ").append(totalScores.get(clazz).intValue())
                        .append(" points\n");
            }
            renderingVars.glyphLayout.setText(renderingVars.levelScoreFont, scoreStringBuilder.toString());
            renderingVars.levelScoreFont.draw(renderingVars.batch, scoreStringBuilder.toString(), 0, renderingVars.height);
        }
        scoreStringBuilder.setLength(0);
    }
}