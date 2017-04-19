package szakacs.kpi.fei.tuke.misc.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
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
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
                    Gdx.files.internal("fonts/UbuntuMono-B.ttf")
            );
            FreeTypeFontGenerator.FreeTypeFontParameter param =
                    new FreeTypeFontGenerator.FreeTypeFontParameter();
            param.color = Color.GOLD;
            param.shadowColor = Color.CORAL;
            param.shadowOffsetX = 5;
            param.shadowOffsetY = 5;
            param.size = 90;
            this.totalScoreFont = generator.generateFont(param);
            param.size = 64;
            this.levelScoreFont = generator.generateFont(param);
            generator.dispose();
            this.batch = batch;
            this.glyphLayout = new GlyphLayout();
            this.width = width;
            this.height = height;
        }

        private void dispose(){
            totalScoreFont.dispose();
            levelScoreFont.dispose();
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
        renderingVars.dispose();
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
            scoreStringBuilder.append("    Level ").append(i+1).append("\n");
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
