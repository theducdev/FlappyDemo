package com.theduc.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.theduc.game.FlappyDemo;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.Preferences;
import sprites.Bird;
import sprites.Tube;

public class PlayState extends State{
    private static final int TUBE_SPACING = 125;
    private static final int TUBE_COUNT = 4;
    private  static final int GROUND_Y_OFFSET = -30;
    private Bird bird;
    private Texture bg;
    private Texture ground;
    private Vector2 groundPos1, groundPos2;

    private Array <Tube> tubes;

    private int score;
    private BitmapFont font;
    private GlyphLayout layout;
    private boolean gameOver;





    protected PlayState(GameStateManager gsm) {
        super(gsm);
        gameOver = false;
        layout = new GlyphLayout();
        score = 0;
        font = new BitmapFont();
        bird = new Bird(50, 300);
        cam.setToOrtho(false, FlappyDemo.WIDTH / 2, FlappyDemo.HEIGHT / 2);
        bg = new Texture("bg.png");
        ground = new Texture("ground.png");
        groundPos1 = new Vector2(cam.position.x - cam.viewportWidth / 2, GROUND_Y_OFFSET);
        groundPos2 = new Vector2(cam.position.x - cam.viewportWidth / 2 + ground.getWidth(), GROUND_Y_OFFSET);
        tubes = new Array<Tube>();
        for (int i = 1; i <= TUBE_COUNT; i++) {
            tubes.add(new Tube(i * (TUBE_SPACING + Tube.TUBE_WIDTH)));

        }
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched()) {
            bird.jump();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        updateGround();
        bird.update(dt);
        cam.position.x = bird.getPosition().x + 60;

        float currentSpeed = Bird.MOVEMENT + score * 0.000005f;
        bird.setMovementSpeed(currentSpeed);

        for (int i = 0; i < tubes.size; i++) {
            Tube tube = tubes.get(i);
            score++;

            if(cam.position.x - (cam.viewportWidth / 2) > tube.getPosBotTube().x + tube.getTopTube().getWidth()) {
                tube.repositsion(tube.getPosTopTube().x + (Tube.TUBE_WIDTH + TUBE_SPACING)*TUBE_COUNT);
            }

            if (tube.collides(bird.getBounds()) || bird.getPosition().y <= ground.getHeight() + GROUND_Y_OFFSET) {
                Preferences preferences = ((FlappyDemo) Gdx.app.getApplicationListener()).getPreferences();
                int highscore = preferences.getInteger("highscore", 0);
                if (score > highscore) {
                    preferences.putInteger("highscore", score);
                    preferences.flush();
                    highscore = score;
                }
                gsm.set(new GameOverState(gsm, score, highscore));
                bird.resetSpeed();
            }
        }
        cam.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(bg, cam.position.x - (cam.viewportWidth / 2), 0);
        sb.draw(bird.getBird(), bird.getPosition().x, bird.getPosition().y);
        for(Tube tube : tubes){
            sb.draw(tube.getBottomTube(), tube.getPosBotTube().x, tube.getPosBotTube().y);
            sb.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y);
        }
        sb.draw(ground, groundPos1.x, groundPos1.y);
        sb.draw(ground, groundPos2.x, groundPos2.y);
        layout.setText(font, String.valueOf(score));
        font.getData().setScale(1.2f);
        font.draw(sb, layout, cam.position.x - layout.width / 2, cam.position.y + cam.viewportHeight / 2 - layout.height - 20);
        sb.end();
    }

    @Override
    public void dispose() {
        bg.dispose();
        bird.dispose();
        ground.dispose();
        font.dispose();
        for (Tube tube : tubes) {
            tube.dispose();
            System.out.println("Play State Disposed");

        }
    }

    private void updateGround() {
        if(cam.position.x - (cam.viewportWidth / 2) > groundPos1.x + ground.getWidth())
            groundPos1.add(ground.getWidth() * 2, 0);
        if(cam.position.x - (cam.viewportWidth / 2) > groundPos2.x + ground.getWidth())
            groundPos2.add(ground.getWidth() * 2, 0);
    }
}
