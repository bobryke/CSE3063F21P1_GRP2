package table;

import model.Course;

import java.util.ArrayList;

public class WeeklyScheduleTable {
    private String hour;
    private String monday;
    private String tuesday;
    private String wednesday;
    private String thursday;
    private String friday;

    public WeeklyScheduleTable(String hour, String monday,
                               String tuesday, String wednesday, String thursday, String friday) {
        this.hour = hour;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
    }

    public WeeklyScheduleTable() {
        this("","","","","","");
    }

    public static ArrayList<WeeklyScheduleTable> convertToTable(Course[][] weeklySchedule){
        ArrayList<WeeklyScheduleTable> weeklyScheduleTable = new ArrayList<WeeklyScheduleTable>();
        for(int i=0;i<8;i++){
            String lectureHour = "";
            if(i==0)
                lectureHour = " 09:30 - 10:20 ";
            else if(i==1)
                lectureHour = " 10:30 - 11:20 ";
            else if(i==2)
                lectureHour = " 11:30 - 12:20 ";
            else if(i==3)
                lectureHour = " 13:00 - 13:50 ";
            else if(i==4)
                lectureHour = " 14:00 - 14:50 ";
            else if(i==5)
                lectureHour = " 15:00 - 15:50 ";
            else if(i==6)
                lectureHour = " 16:00 - 16:50 ";
            else if(i==7)
                lectureHour = " 17:00 - 17:50 ";
            weeklyScheduleTable.add(new WeeklyScheduleTable(lectureHour,
                    ((weeklySchedule[i][0]==null)? "" : weeklySchedule[i][0].getCourseID()),
                    ((weeklySchedule[i][1]==null)? "" : weeklySchedule[i][1].getCourseID()),
                    ((weeklySchedule[i][2]==null)? "" : weeklySchedule[i][2].getCourseID()),
                    ((weeklySchedule[i][3]==null)? "" : weeklySchedule[i][3].getCourseID()),
                    ((weeklySchedule[i][4]==null)? "" : weeklySchedule[i][4].getCourseID())
            ));
        }
        return weeklyScheduleTable;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMonday() {
        return monday;
    }

    public void setMonday(String monday) {
        this.monday = monday;
    }

    public String getTuesday() {
        return tuesday;
    }

    public void setTuesday(String tuesday) {
        this.tuesday = tuesday;
    }

    public String getWednesday() {
        return wednesday;
    }

    public void setWednesday(String wednesday) {
        this.wednesday = wednesday;
    }

    public String getThursday() {
        return thursday;
    }

    public void setThursday(String thursday) {
        this.thursday = thursday;
    }

    public String getFriday() {
        return friday;
    }

    public void setFriday(String friday) {
        this.friday = friday;
    }
}
