import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class MinerFull extends MoveableEntity {

    private int resourceLimit;
    private boolean vaccinated;

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

    public boolean isVaccinated() { return this.vaccinated; }
    public void setVaccinated(boolean b) {
        this.vaccinated = b;
    }

    public void _executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> fullTarget =
                world.findNearest(this.getPosition(), Blacksmith.class);

        if(this.transformInfectedMiner(world, imageStore, scheduler))
        {
            return;
        }

        if (fullTarget.isPresent() && _moveTo(world,
                fullTarget.get(), scheduler))
        {
             transformFull(world, scheduler, imageStore);
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
        // was ActiveEntity
        MinerNotFull miner = new MinerNotFull(this.getId(), this.resourceLimit,
                this.getPosition(), this.getActionPeriod(),
                this.getAnimationPeriod(),
                this.getImages());

        if(this.isVaccinated())
            miner.setVaccinated(true);

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        miner.scheduleActions(scheduler, world, imageStore);
    }

    public boolean transformInfectedMiner(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        if(!vaccinated) {
            if (world.getBackgroundCell(this.getPosition()).getId().equals("background_covid")) {
                Point newPos = this.getPosition();
                world.removeEntity(this);
                scheduler.unscheduleAllEvents(this);

                InfectedMiner infectedMiner = new InfectedMiner(InfectedMiner.INFECTED_MINER_KEY,
                        this.resourceLimit, newPos, this.getActionPeriod(), this.getAnimationPeriod(),
                        imageStore.getImageList(InfectedMiner.INFECTED_MINER_KEY));
                infectedMiner.setVaccinated(false);
                world.addEntity(infectedMiner);
                infectedMiner.scheduleActions(scheduler, world, imageStore);
                return true;
            }
            return false;
        }
        return false;
    }
}
