package szakacs.kpi.fei.tuke.misc.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel.GameLevelPrivileged;

/**
 * Created by developer on 24.1.2017.
 */
public class BackgroundRenderer extends AbstractGameRenderer {

    private Sprite bgSprite;
    private BitmapFont gameState;

    public BackgroundRenderer(SpriteBatch batch, GameLevelPrivileged game) {
        super(batch, game);
        this.bgSprite = new Sprite(new Texture(Gdx.files.internal("images/background.png")));
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/UbuntuMono-B.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.size = 90;
        param.color = Color.GOLD;
        param.borderWidth = 5;
        this.gameState = fontGenerator.generateFont(param);
        fontGenerator.dispose();
    }

    @Override
    public void render() {
        bgSprite.setPosition(0, 0);
        bgSprite.draw(batch);
        String displayMsg = "Remaining nuggets: " + Integer.toString(game.getGameWorld().getNuggetCount());
        switch (game.getState()) {
            case PLAYING:
                break;
            case WON:
                displayMsg += "\nSuccess!";
                break;
            case LOST:
                displayMsg += "\nFailure!";
                break;
            default:
                throw new IllegalStateException("Illegal game state: " + game.getState());
        }
        gameState.draw(batch, displayMsg, 128, 2000);
    }

    @Override
    public void dispose() {
        bgSprite.getTexture().dispose();
    }
}
