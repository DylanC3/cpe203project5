public class Node {

    private Point currPt;
    private Node prev;

    private int g;
    private int h;
    private int f;

    public Node(Point currPt, Node prev, int g, int h, int f) {
        this.currPt = currPt;
        this.prev = prev;
        this.g = g;
        this.h = h;
        this.f = f;
    }

    // getters & setters
    public Point getCurrPt() { return currPt; }
    public Node getPrev() { return prev; }
    public int getG() { return g; }
    public int getH() { return h; }
    public int getF() { return f; }

    public void setCurrPt(Point currPt) { this.currPt = currPt;}
    public void setPrev(Node prev) { this.prev = prev; }
    public void setG(int g) { this.g = g; }
    public void setH(int h) { this.h = h; }
    public void setF(int f) { this.f = f; }
}
