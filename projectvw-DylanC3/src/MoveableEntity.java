import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public abstract class MoveableEntity extends AnimatedEntity {

//    private PathingStrategy strategy = new SingleStepPathingStrategy();
    private PathingStrategy strategy = new AStarPathingStrategy();

    public PathingStrategy getStrategy() { return strategy; }

    public MoveableEntity(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images)

    {
        super(id, position, actionPeriod, animationPeriod, images);
    }

    private Point nextPosition(WorldModel world, Point destPos) {
        List<Point> points;
        points = getStrategy().computePath(this.getPosition(), destPos,
                p -> (world.withinBounds(p) && !world.isOccupied(p)),
                (p1, p2) -> p1.adjacent(p2),
                PathingStrategy.CARDINAL_NEIGHBORS);

        if(points.size() == 0) { // no path found
            return this.getPosition();
        }
        return points.get(0);
    }

    abstract boolean _moveTo(WorldModel world, Entity target, EventScheduler scheduler);

    protected void _moveToHelper(WorldModel world, Entity target, EventScheduler scheduler) {
        Point nextPos = nextPosition(world, target.getPosition());

        if (!this.getPosition().equals(nextPos)) {
            Optional<Entity> occupant = world.getOccupant(nextPos);
            if (occupant.isPresent()) {
                scheduler.unscheduleAllEvents(occupant.get());
            }

            world.moveEntity(this, nextPos);
        }
    }

    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore),
                this.getActionPeriod());
        scheduler.scheduleEvent(this,
                new Animation(this,0),
                this.getAnimationPeriod());
    }
}
