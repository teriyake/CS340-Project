public class Return {
    private int spv;
    private boolean conflict;

    public Return(int n, Boolean b) {
        this.spv = n;
        this.conflict = b;
    }

    public int getSPV() {
        return this.spv;
    }

    public void updateSPV(int n) {
        this.spv = n;
    }

    public void updateConflict(Boolean b) {
        this.conflict = b;
    }

    public boolean hasConflict() {
        return this.conflict;
    }
}
