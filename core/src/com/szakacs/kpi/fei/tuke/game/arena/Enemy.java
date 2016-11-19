package com.szakacs.kpi.fei.tuke.game.arena;

import com.szakacs.kpi.fei.tuke.game.enums.Direction;
import com.szakacs.kpi.fei.tuke.game.intrfc.Actor;

/**
 * Created by developer on 5.11.2016.
 */
public class Enemy implements Actor {

    // The Y coordinates are already in the HorizontalTunnel object.

    private int x;
    private int y;
    private Direction direction;
    private TreasureScooper world;
    private AbstractPlayer player;
    private int movementDelta;
    private boolean moving;

    public Enemy(Direction leftToRightDirection, int y, TreasureScooper world){
        this.direction = leftToRightDirection;
        if (leftToRightDirection == Direction.RIGHT)
            this.x = -world.getOffsetX();
        else
            this.x = world.getWidth();
        this.movementDelta = world.getOffsetX()/4;
        this.y = y;
        this.world = world;
        this.player = world.getPlayer();
        this.moving = true;
    }

    public Direction getDirection(){
        return this.direction;
    }

    @Override
    public int getX(){
        return this.x;
    }

    @Override
    public int getY(){
        return this.y;
    }

    void act(){
        if (moving) {
            if (direction == Direction.RIGHT) {
                this.x += this.movementDelta;
            } else {
                this.x -= this.movementDelta;
            }
            if (this.player.getHeadY() < this.y) {
                for (PipeSegment seg : this.player.getSegmentStack()) {
                    if (seg.getY() == this.y) {
                        if (Math.abs(seg.getX() - this.x) < world.getOffsetX())
                            this.moving = false;
                    } else if (seg.getY() < this.y)
                        break;
                }
            }
        } else
            this.player.damagePipe();
    }
}
