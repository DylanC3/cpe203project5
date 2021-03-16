import processing.core.PImage;
import java.util.List;
import java.util.Optional;

public class InfectedMiner extends ActiveEntity {

    public static final String INFECTED_MINER_KEY = "infected_miner";
    private int resourceLimit;
    private boolean vaccinated;

    public InfectedMiner(
            String id,
            int resourceLimit,
            Point position,
            int actionPeriod,
            List<PImage> images)

    {
        super(id, position, actionPeriod, images);
        this.resourceLimit = resourceLimit;
    }

    public boolean isVaccinated() { return this.vaccinated; }
    public void setVaccinated(boolean b) {
        this.vaccinated = b;
    }

    public void _executeActivity(WorldModel world,
                                 ImageStore imageStore,
                                 EventScheduler scheduler)
    {
        Optional<Entity> docTarget =
                world.findNearest(this.getPosition(), Doctor.class);
        if(docTarget.get().getPosition().adjacent(this.getPosition())) {
            transformFromInfected(world, scheduler, imageStore);
        }
        else {
            scheduler.scheduleEvent(this,
                    new Activity(this, world, imageStore),
                    this.getActionPeriod());
        }
    }


    // turn infected miner back to notFullMiner
    public void transformFromInfected(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        // was ActiveEntity miner
        MinerNotFull miner = new MinerNotFull(this.getId(), this.resourceLimit,
                this.getPosition(), this.getActionPeriod(),
                100, imageStore.getImageList(Functions.MINER_KEY));

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        miner.scheduleActions(scheduler, world, imageStore);
        miner.setVaccinated(true);
    }
}