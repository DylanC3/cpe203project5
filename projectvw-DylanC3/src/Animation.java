public class Animation implements Action {

    private AnimatedEntity entity;
    private int repeatCount;

    public Animation(
            AnimatedEntity entity,
            int repeatCount)
    {
        this.entity = entity;
        this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler)
    {
        entity.nextImage();

        if (this.repeatCount != 1) {
            scheduler.scheduleEvent(this.entity,
                    new Animation(this.entity, Math.max(this.repeatCount - 1, 0)),
                    entity.getAnimationPeriod());
        }
    }

}
