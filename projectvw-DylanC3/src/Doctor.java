import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Doctor extends MoveableEntity {

    public Doctor(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images)
    {
        super(id, position, actionPeriod, animationPeriod, images);
    }

    public void _executeActivity(WorldModel world,
                                 ImageStore imageStore,
                                 EventScheduler scheduler)
    {
        Optional<Entity> doctorTarget =
                world.findNearest(this.getPosition(), InfectedMiner.class);
        // want to change this to find the nearest Infected Miner however
        // we decide to to that
        if(doctorTarget.isPresent() && _moveTo(world,
                doctorTarget.get(), scheduler))
        {
            return;
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
        if(this.getPosition().adjacent(target.getPosition())) {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else {
            _moveToHelper(world, target, scheduler);
            return false;
        }
    }

//    public void transformInfected(
//            WorldModel world,
//            EventScheduler scheduler,
//            ImageStore imageStore)
//    {
//        // transforms from Infected back to NotFull
//        ActiveEntity miner = new MinerNotFull(this.getId(), 2,
//                this.getPosition(), this.getActionPeriod(),
//                this.getAnimationPeriod(),
//                this.getImages());
//        world.removeEntity(this);
//        scheduler.unscheduleAllEvents(this);
//
//        world.addEntity(miner);
//        miner.scheduleActions(scheduler, world, imageStore);
//    }



}
