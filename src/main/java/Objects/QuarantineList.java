package Objects;


public class QuarantineList {

    private QuarantineItem[] quarantineItems;

    public QuarantineList(QuarantineItem[] quarantineItems) {
        this.quarantineItems = quarantineItems;
    }

    public QuarantineList() {
    }

    public QuarantineItem[] getQuarantineItems() {
        return quarantineItems;
    }

    public void setQuarantineItems(QuarantineItem[] quarantineItems) {
        this.quarantineItems = quarantineItems;
    }
}
