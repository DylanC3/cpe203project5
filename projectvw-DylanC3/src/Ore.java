import processing.core.PImage;
import java.util.List;
import java.util.Random;

public class Ore extends ActiveEntity {

    public static final String ORE_ID_PREFIX = "ore -- ";
    public static final int ORE_CORRUPT_MIN = 20000;
    public static final int ORE_CORRUPT_MAX = 30000;

    public Ore(
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
        Point pos = getPosition();

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        ActiveEntity blob = new OreBlob(getId() + OreBlob.BLOB_ID_SUFFIX, pos,
                getActionPeriod() / OreBlob.BLOB_PERIOD_SCALE,
                OreBlob.BLOB_ANIMATION_MIN + rand.nextInt(
                        OreBlob.BLOB_ANIMATION_MAX
                                - OreBlob.BLOB_ANIMATION_MIN),
                imageStore.getImageList(OreBlob.BLOB_KEY));

        world.addEntity(blob);
        blob.scheduleActions(scheduler, world, imageStore);
    }

}
