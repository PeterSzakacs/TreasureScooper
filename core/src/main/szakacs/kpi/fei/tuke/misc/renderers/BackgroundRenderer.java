package szakacs.kpi.fei.tuke.misc.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import szakacs.kpi.fei.tuke.enums.GameState;
import szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel.GameLevelPrivileged;

/**
 * Renders the background of the game and displays any global feedback,
 * such as number of remaining nuggets or the state of the current level
 * upon finishing it.
 */
public class BackgroundRenderer extends AbstractGameRenderer {

    private Sprite bgSprite;
    private BitmapFont remainingNuggets;
    private BitmapFont gameState;

    public BackgroundRenderer(SpriteBatch batch, GameLevelPrivileged game) {
        super(batch, game);
        this.bgSprite = new Sprite(new Texture(Gdx.files.internal("images/background.png")));
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/UbuntuMono-B.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.size = 90;
        param.color = Color.GOLD;
        param.borderWidth = 5;
        this.remainingNuggets = fontGenerator.generateFont(param);
        this.gameState = fontGenerator.generateFont(param);
        fontGenerator.dispose();
    }

    @Override
    public void render() {
        bgSprite.setPosition(0, 0);
        bgSprite.draw(batch);
        String displayMsg = "Remaining nuggets: " + Integer.toString(game.getGameWorld().getNuggetCount());
        remainingNuggets.draw(batch, displayMsg, 128, 2000);

        if (game.getState() != GameState.PLAYING){
            if (game.getState() == GameState.WON){
                gameState.draw(batch, "Success!", 3600, 2000);
            } else {
                gameState.draw(batch, "Failure!", 3600, 2000);
            }
        }
    }

    @Override
    public void dispose() {
        bgSprite.getTexture().dispose();
        remainingNuggets.dispose();
        gameState.dispose();
    }
}
