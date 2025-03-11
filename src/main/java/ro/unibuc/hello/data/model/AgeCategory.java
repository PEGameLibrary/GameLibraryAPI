package ro.unibuc.hello.data.model;

public enum AgeCategory {
    EVERYONE("Everyone", 0), 
    TEEN("Teen (13+)", 13), 
    MATURE("Mature (18+)", 18);

    private final String description;
    private final int requiredAge;

    AgeCategory(String description, int requiredAge) {
        this.description = description;
        this.requiredAge = requiredAge;
    }

    public String getDescription() {
        return description;
    }

    public int getRequiredAge() {
        return requiredAge;
    }
}
