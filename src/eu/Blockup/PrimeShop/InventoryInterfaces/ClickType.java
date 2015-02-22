package eu.Blockup.PrimeShop.InventoryInterfaces;

public enum ClickType {

    RIGHT(true), LEFT(false), SHIFT_RIGHT(true, true), SHIFT_LEFT(false, true), UNKNOWN(
            false, false);

    private boolean isRightClick;
    private boolean isShiftClick = false;

    ClickType(boolean isRightClick) {
        this.isRightClick = isRightClick;
    }

    ClickType(boolean isRightClick, boolean isShiftClick) {
        this(isRightClick);
        this.isShiftClick = isShiftClick;
    }

    public boolean isRightClick() {
        return !this.isRightClick;
    }

    public boolean isLeftClick() {
        return this.isRightClick;
    }

    public boolean isShiftClick() {
        return this.isShiftClick;
    }
}
