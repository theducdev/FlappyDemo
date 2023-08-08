package com.theduc.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class GameOverState extends State {
    private static final int WIDTH = 480;
    private static final int HEIGHT = 800;
    private Texture gameoverTexture;
    private BitmapFont font;
    private int finalScore;
    private int highscore;
    private  static final int GROUND_Y_OFFSET = -30;
    private Texture bg;
    private Texture ground;
    private Vector2 groundPos1, groundPos2;

    private Texture playagainbtn;


    public GameOverState(GameStateManager gsm, int score, int highscore) {
        super(gsm);
        finalScore = score;
        this.highscore = highscore;
        cam.setToOrtho(false, WIDTH / 2, HEIGHT / 2);
        gameoverTexture = new Texture("gameover.png");
        font = new BitmapFont();
        bg = new Texture("bg.png");
        ground = new Texture("ground.png");
        playagainbtn = new Texture("playagain.png");
        groundPos1 = new Vector2(cam.position.x - cam.viewportWidth / 2, GROUND_Y_OFFSET);
        groundPos2 = new Vector2(cam.position.x - cam.viewportWidth / 2 + ground.getWidth(), GROUND_Y_OFFSET);
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            cam.unproject(touchPos);

            float playAgainButtonLeft = cam.position.x - 35;
            float playAgainButtonRight = cam.position.x - 35 + 70;
            float playAgainButtonTop = cam.position.y - 95 + 70;
            float playAgainButtonBottom = cam.position.y - 95;

            if (touchPos.x >= playAgainButtonLeft && touchPos.x <= playAgainButtonRight &&
                    touchPos.y >= playAgainButtonBottom && touchPos.y <= playAgainButtonTop) {
                gsm.set(new PlayState(gsm));
            }
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(bg, cam.position.x - (cam.viewportWidth / 2), 0);
        sb.draw(ground, groundPos1.x, groundPos1.y);
        sb.draw(ground, groundPos2.x, groundPos2.y);
        sb.draw(gameoverTexture, cam.position.x - gameoverTexture.getWidth() / 2, cam.position.y - gameoverTexture.getHeight() / 2);
        font.draw(sb, "Score: " + finalScore, cam.position.x - 40, cam.position.y + 80);
        font.draw(sb, "Highscore: " + highscore, cam.position.x - 50, cam.position.y + 50);
        sb.draw(playagainbtn,  cam.position.x - 35, cam.position.y - 95, 70, 70);
        sb.end();
    }


    @Override
    public void dispose() {
        gameoverTexture.dispose();
        font.dispose();
    }
}
