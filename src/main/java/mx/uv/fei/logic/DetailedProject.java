package mx.uv.fei.logic;

import java.util.Objects;

public class DetailedProject {
    private int projectID;
    private String academicBodyName;
    private String academicBodyID;
    private String investigationProjectName;
    private String lgacName;
    private String investigationLine;
    private String approxDuration;
    private String receptionWorkModality;
    private String receptionWorkName;
    private String requisites;
    private String director;
    private String coDirector;
    private int numberStudents;
    private String investigationDescription;
    private String receptionWorkDescription;
    private String expectedResults;
    private String bibliography;
    private String projectState;

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public String getAcademicBodyName() {
        return academicBodyName;
    }

    public void setAcademicBodyName(String academicBodyName) {
        this.academicBodyName = academicBodyName;
    }
    
    public String getAcademicBodyID() {
        return academicBodyID;
    }
    
    public void setAcademicBodyID(String academicBodyID) {
        this.academicBodyID = academicBodyID;
    }
    
    public String getInvestigationProjectName() {
        return investigationProjectName;
    }

    public void setInvestigationProjectName(String investigationProjectName) {
        this.investigationProjectName = investigationProjectName;
    }

    public String getLgacName() {
        return lgacName;
    }

    public void setLgacName(String lgacName) {
        this.lgacName = lgacName;
    }

    public String getInvestigationLine() {
        return investigationLine;
    }

    public void setInvestigationLine(String investigationLine) {
        this.investigationLine = investigationLine;
    }

    public String getApproxDuration() {
        return approxDuration;
    }

    public void setApproxDuration(String approxDuration) {
        this.approxDuration = approxDuration;
    }

    public String getReceptionWorkModality() {
        return receptionWorkModality;
    }

    public void setReceptionWorkModality(String receptionWorkModality) {
        this.receptionWorkModality = receptionWorkModality;
    }
    
    public String getReceptionWorkName() {
        return receptionWorkName;
    }
    
    public void setReceptionWorkName(String receptionWorkName) {
        this.receptionWorkName = receptionWorkName;
    }
    
    public String getRequisites() {
        return requisites;
    }

    public void setRequisites(String requisites) {
        this.requisites = requisites;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getCoDirector() {
        return coDirector;
    }

    public void setCoDirector(String coDirector) {
        this.coDirector = coDirector;
    }

    public int getNumberStudents() {
        return numberStudents;
    }

    public void setNumberStudents(int numberStudents) {
        this.numberStudents = numberStudents;
    }

    public String getInvestigationDescription() {
        return investigationDescription;
    }

    public void setInvestigationDescription(String investigationDescription) {
        this.investigationDescription = investigationDescription;
    }

    public String getReceptionWorkDescription() {
        return receptionWorkDescription;
    }

    public void setReceptionWorkDescription(String receptionWorkDescription) {
        this.receptionWorkDescription = receptionWorkDescription;
    }

    public String getExpectedResults() {
        return expectedResults;
    }

    public void setExpectedResults(String expectedResults) {
        this.expectedResults = expectedResults;
    }

    public String getBibliography() {
        return bibliography;
    }

    public void setBibliography(String bibliography) {
        this.bibliography = bibliography;
    }
    
    public String getProjectState() {
        return projectState;
    }
    
    public void setProjectState(String projectState) {
        this.projectState = projectState;
    }
    
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        DetailedProject other = (DetailedProject) object;
        return projectID == other.projectID
                && Objects.equals(academicBodyName, other.academicBodyName)
                && Objects.equals(investigationProjectName, other.investigationProjectName)
                && Objects.equals(lgacName, other.lgacName)
                && Objects.equals(investigationLine, other.investigationLine)
                && Objects.equals(approxDuration, other.approxDuration)
                && Objects.equals(receptionWorkModality, other.receptionWorkModality)
                && Objects.equals(receptionWorkName, other.receptionWorkName)
                && Objects.equals(requisites, other.requisites)
                && Objects.equals(director, other.director)
                && Objects.equals(coDirector, other.coDirector)
                && numberStudents == other.numberStudents
                && Objects.equals(investigationDescription, other.investigationDescription)
                && Objects.equals(receptionWorkDescription, other.receptionWorkDescription)
                && Objects.equals(expectedResults, other.expectedResults)
                && Objects.equals(bibliography, other.bibliography)
                && Objects.equals(projectState, other.projectState);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(projectID, academicBodyName, investigationProjectName, lgacName, investigationLine,
                approxDuration, receptionWorkModality, receptionWorkName, requisites, director, coDirector,
                numberStudents, investigationDescription, receptionWorkDescription, expectedResults, bibliography,
                projectState);
    }
    
    @Override
    public String toString() {
        return "DetailedProject{" +
                "projectID=" + projectID +
                ", academicBodyName='" + academicBodyName + '\'' +
                ", investigationProjectName='" + investigationProjectName + '\'' +
                ", lgacName='" + lgacName + '\'' +
                ", investigationLine='" + investigationLine + '\'' +
                ", approxDuration='" + approxDuration + '\'' +
                ", receptionWorkModality='" + receptionWorkModality + '\'' +
                ", receptionWorkName='" + receptionWorkName + '\'' +
                ", requisites='" + requisites + '\'' +
                ", director='" + director + '\'' +
                ", coDirector='" + coDirector + '\'' +
                ", numberStudents=" + numberStudents +
                ", investigationDescription='" + investigationDescription + '\'' +
                ", receptionWorkDescription='" + receptionWorkDescription + '\'' +
                ", expectedResults='" + expectedResults + '\'' +
                ", bibliography='" + bibliography + '\'' +
                ", projectState='" + projectState + '\'' +
                '}';
    }
}
