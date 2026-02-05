package models;

public enum GameStatusEnum {

    NON_STARTED("Not Started"),
    INCOMPLETE("Incomplete"),
    COMPLETE("Completed");

    private String label;

    GameStatusEnum(final String label){
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
