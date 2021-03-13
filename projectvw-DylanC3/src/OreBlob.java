import processing.core.PImage;
import java.util.List;
import java.util.Optional;

public class OreBlob extends MoveableEntity {

    public static final String BLOB_KEY = "blob";
    public static final String BLOB_ID_SUFFIX = " -- blob";
    public static final int BLOB_PERIOD_SCALE = 4;
    public static final int BLOB_ANIMATION_MIN = 50;
    public static final int BLOB_ANIMATION_MAX = 150;


    public OreBlob(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images)
    {
        super(id, position, actionPeriod, animationPeriod, images);
    }

    public boolean _moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (this.getPosition().adjacent(target.getPosition())) {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else {
            _moveToHelper(world, target, scheduler);
            return false;
        }
    }

    public void _executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> blobTarget =
                world.findNearest(this.getPosition(), Vein.class);
        long nextPeriod = this.getActionPeriod();

        if (blobTarget.isPresent()) {
            Point tgtPos = blobTarget.get().getPosition();

            if (_moveTo(world, blobTarget.get(), scheduler)) {
                ActiveEntity quake = new Quake(Quake.QUAKE_ID, tgtPos, Quake.QUAKE_ACTION_PERIOD, Quake.QUAKE_ANIMATION_PERIOD,
                        imageStore.getImageList(Quake.QUAKE_KEY));

                world.addEntity(quake);
                nextPeriod += this.getActionPeriod();
                quake.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore),
                nextPeriod);
    }


}
