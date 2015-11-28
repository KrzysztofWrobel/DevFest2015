package io.gripit.presentation.models;

/**
 * Created by krzysztofwrobel on 28/11/15.
 */
public class SurveyAnswer {
    String id;
    String interest;
    String name;
    String startup;
    String student;
    String time;
    String technology;
    String mail;

    public void setId(String id) {
        this.id = id;
    }

    public String getInterest() {
        return interest;
    }

    public String getName() {
        return name;
    }

    public int getModId() {
        return Integer.valueOf(id) % 2;
    }

    public String getStudentBlock() {
        return isStudent() ? "is" : "isn't";
    }

    public String getStartupBlock() {
        String startupText = "didn't answered about";

        switch (startup) {
            case "Tak":
                startupText = "has";
                break;
            case "Nie":
                startupText = "doesn't have";
                break;
            case "Zamierzam":
                startupText = "is going to have";
                break;

        }
        return startupText;
    }

    public boolean isStudent() {
        switch (student) {
            case "Tak":
                return true;
            case "Nie":
                return false;
        }
        return false;
    }

    public String getTime() {
        return time;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public String getTechnology() {
        return technology;
    }

    public String getStudent() {
        return student;
    }

    public String getStartup() {
        return startup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SurveyAnswer that = (SurveyAnswer) o;

        return !(id != null ? !id.equals(that.id) : that.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
