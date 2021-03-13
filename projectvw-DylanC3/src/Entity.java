import processing.core.PImage;
import java.util.List;

public abstract class Entity {

    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;

    protected String getId() { return id; }
    protected Point getPosition() { return position; }
    protected void setPosition(Point position) { this.position = position; }
    protected List<PImage> getImages() { return images; }
    protected int getImageIndex() { return imageIndex; }
    protected void setImageIndex(int imageIndex) { this.imageIndex = imageIndex; }
    protected PImage getCurrentImage() { return this.images.get(this.imageIndex);}

    public Entity(
            String id,
            Point position,
            List<PImage> images) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
    }


}
