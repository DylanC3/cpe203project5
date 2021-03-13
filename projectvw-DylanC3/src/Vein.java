import processing.core.PImage;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Vein extends ActiveEntity {

    public Vein(
            String id,
            Point position,
            int actionPeriod,
            List<PImage> images)
    {
        super(id, position, actionPeriod, images);
    }

    public void _executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround(getPosition());

        if (openPt.isPresent()) {
            ActiveEntity ore = new Ore(Ore.ORE_ID_PREFIX + getId(), openPt.get(),
                    Ore.ORE_CORRUPT_MIN + rand.nextInt(
                            Ore.ORE_CORRUPT_MAX - Ore.ORE_CORRUPT_MIN),
                    imageStore.getImageList(Functions.ORE_KEY));
            world.addEntity(ore);
            ore.scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore),
                this.getActionPeriod());
    }
}
