package at.htl.model;

import java.io.Serializable;
import java.util.Map;

public class Survey implements Serializable {

    private String question;

    private Map<String, Integer> result;

    public Survey() {
    }

    public Survey(String question, Map<String, Integer> result) {
        this.question = question;
        this.result = result;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Map<String, Integer> getResult() {
        return result;
    }

    public void setResult(Map<String, Integer> result) {
        this.result = result;
    }

}
