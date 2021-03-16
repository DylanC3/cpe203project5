import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class MinerFull extends MoveableEntity {

    private int resourceLimit;

    public MinerFull(
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
        Optional<Entity> fullTarget =
                world.findNearest(this.getPosition(), Blacksmith.class);

        if (fullTarget.isPresent() && _moveTo(world,
                fullTarget.get(), scheduler))
        {
            if (transformInfectedMiner(world, imageStore, scheduler))
            {
                return;
            }
            else if (fullTarget.isPresent())
            {
                this.transformFull(world, scheduler, imageStore);
            }
        }
        else {
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
            return true;
        }
        else {
            _moveToHelper(world, target, scheduler);
            return false;
        }
    }

    public void transformFull(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        ActiveEntity miner = new MinerNotFull(this.getId(), this.resourceLimit,
                this.getPosition(), this.getActionPeriod(),
                this.getAnimationPeriod(),
                this.getImages());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        miner.scheduleActions(scheduler, world, imageStore);
    }

    public boolean transformInfectedMiner(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        if (world.getBackgroundCell(this.getPosition()).getId().equals("background_covid")) {
            Point newPos = this.getPosition();
            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            InfectedMiner infectedMiner = new InfectedMiner(InfectedMiner.INFECTED_MINER_KEY, newPos,
                        500, imageStore.getImageList(InfectedMiner.INFECTED_MINER_KEY));

            world.addEntity(infectedMiner);
            return true;
        }
        return false;

    }
}
