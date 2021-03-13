import processing.core.PImage;
import java.util.List;

public class Quake extends AnimatedEntity {

    public static final String QUAKE_KEY = "quake";
    public static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;

    public static final String QUAKE_ID = "quake";
    public static final int QUAKE_ACTION_PERIOD = 1100;
    public static final int QUAKE_ANIMATION_PERIOD = 100;

    public Quake(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images)
    {
        super(id, position, actionPeriod, animationPeriod, images);
    }

    // overridden since it uses variable that applies only to Quake
    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore),
                this.getActionPeriod());
        scheduler.scheduleEvent(this, new Animation(this,
                QUAKE_ANIMATION_REPEAT_COUNT),
                this.getAnimationPeriod());
    }

    public void _executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }
}
