package cards.model;

public class StoryCardYCoordinate implements Comparable<StoryCardYCoordinate> {

    private int yCoordinate = 0;

    private int index;

    private int priority;

    public int getPriority() {
        return this.priority;
    }

    public void setPriority(int pri) {
        this.priority = pri;
    }

    public int getYCoordinate() {
        return this.yCoordinate;
    }

    public int getIndex() {
        return this.index;
    }

    public StoryCardYCoordinate(int coordinate, int idx) {
        this.yCoordinate = coordinate;
        this.index = idx;
    }

    public int compareTo(StoryCardYCoordinate arg0) {
        if (this.yCoordinate == arg0.getYCoordinate()) {
            return 0;
        }
        else
            if (this.yCoordinate < arg0.getYCoordinate()) {
                return -1;
            }
            else {
                return 1;
            }
    }

}
