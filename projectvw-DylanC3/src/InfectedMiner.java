import processing.core.PImage;
import java.util.List;
import java.util.Optional;

public class InfectedMiner extends Entity {

    public static final String INFECTED_MINER_KEY = "infected_miner";
    private int actionP;


    public InfectedMiner(
            String id,
            Point position,
            int actionPeriod,
            List<PImage> images)

    {
        super(id, position, images);
        this.actionP = actionPeriod;

    }

    public int getActionP() {
        return actionP;
    }

    // turn infected miner back to notFullMiner
    public void transformFull(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        ActiveEntity miner = new MinerNotFull(this.getId(), 0,
                this.getPosition(), this.getActionP(),
                100,
                this.getImages());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        miner.scheduleActions(scheduler, world, imageStore);
    }
}