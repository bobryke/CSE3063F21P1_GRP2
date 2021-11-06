public class ElectiveCourse extends Course{
    int quota;
    public ElectiveCourse(String courseName, String courseID, String courseType, int courseCredit) {
        super(courseName, courseID, courseType, courseCredit);

    }

    public boolean isQuotaFull(){
        return true;
    }

}
