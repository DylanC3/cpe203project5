import processing.core.PImage;

import java.util.List;

public abstract class AnimatedEntity extends ActiveEntity {

    private int animationPeriod;

    public AnimatedEntity( String id,
                           Point position,
                           int actionPeriod,
                           int animationPeriod,
                           List<PImage> images)
    {
        super(id, position, actionPeriod, images);
        this.animationPeriod = animationPeriod;
    }

    protected int getAnimationPeriod() { return animationPeriod; }
    protected void nextImage() { setImageIndex((getImageIndex() + 1) % getImages().size()); }

}
