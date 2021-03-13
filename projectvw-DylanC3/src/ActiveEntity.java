import processing.core.PImage;
import java.util.List;
import java.util.Random;

public abstract class ActiveEntity extends Entity {

    protected static final Random rand = new Random();
    private int actionPeriod;

    public ActiveEntity(
            String id,
            Point position,
            int actionPeriod,
            List<PImage> images)

    {
        super(id, position, images);
        this.actionPeriod = actionPeriod;
    }

    protected int getActionPeriod() { return actionPeriod; }
    abstract void _executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
    protected void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore),
                this.getActionPeriod());
    }

}
