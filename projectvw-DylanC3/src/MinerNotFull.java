import processing.core.PImage;
import java.util.List;
import java.util.Optional;

public class MinerNotFull extends MoveableEntity {

    private int resourceLimit;
    private int resourceCount;

    public MinerNotFull(
            String id,
            int resourceLimit,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images)
    {
        super(id, position, actionPeriod, animationPeriod, images);
        this.resourceLimit = resourceLimit;
    }

    public void _executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> notFullTarget =
                world.findNearest(this.getPosition(), Ore.class);

        if (!notFullTarget.isPresent() || !_moveTo(world,
                notFullTarget.get(),
                scheduler)
                || !transformNotFull(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    new Activity(this, world, imageStore),
                    this.getActionPeriod());
        }
    }

    public boolean _moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (this.getPosition().adjacent(target.getPosition())) {
            this.resourceCount += 1;
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);

            return true;
        }
        else {
            _moveToHelper(world, target, scheduler);
            return false;
        }
    }

    public boolean transformNotFull(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (this.resourceCount >= this.resourceLimit) {
            ActiveEntity miner = new MinerFull(this.getId(), this.resourceLimit,
                    this.getPosition(), this.getActionPeriod(),
                    this.getAnimationPeriod(),
                    this.getImages());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            miner.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }
}