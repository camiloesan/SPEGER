package mx.uv.fei.logic;

public class Advancement {

    private int advancementID;
    private String advancementName;
    private String advancementDescription;
    private String advancementStartDate;
    private String advancementDeadline;
    private int projectId;

    public String getAdvancementName() {
        return advancementName;
    }

    public void setAdvancementName(String advancementName) {
        this.advancementName = advancementName;
    }

    public String getAdvancementDescription() {
        return advancementDescription;
    }

    public void setAdvancementDescription(String advancementDescription) {
        this.advancementDescription = advancementDescription;
    }

    public String getAdvancementStartDate() {
        return advancementStartDate;
    }

    public void setAdvancementStartDate(String advancementStartDate) {
        this.advancementStartDate = advancementStartDate;
    }

    public String getAdvancementDeadline() {
        return advancementDeadline;
    }

    public void setAdvancementDeadline(String advancementDeadline) {
        this.advancementDeadline = advancementDeadline;
    }
    
    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getAdvancementID() {
        return advancementID;
    }

    public void setAdvancementID(int advancementID) {
        this.advancementID = advancementID;
    }
}
