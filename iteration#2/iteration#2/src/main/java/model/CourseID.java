package model;

public class CourseID {
    private String departmentCode;
    private String postfixCode;

    public CourseID(String courseID){
        Integer letterCount = 0;
        for(int i=0;i<courseID.length();i++){
            if(Character.isAlphabetic(courseID.charAt(i))){
                letterCount++;
            }
            else{
                break;
            }
        }
        departmentCode = courseID.substring(0,letterCount);
        postfixCode = courseID.substring(letterCount);
    }

    @Override
    public String toString(){
        return departmentCode+postfixCode;
    }

    @Override
    public boolean equals(Object rhs){
        return this.toString().equals(rhs.toString());
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public String getPostfixCode() {
        return postfixCode;
    }
}
