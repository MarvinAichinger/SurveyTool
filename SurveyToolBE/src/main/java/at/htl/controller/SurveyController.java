package at.htl.controller;

import at.htl.model.Survey;

import javax.enterprise.context.ApplicationScoped;
import java.util.Map;

@ApplicationScoped
public class SurveyController {

    private Survey survey;

    public boolean vote(String option) {
        Map<String, Integer> res = survey.getResult();
        if (res.containsKey(option)) {
            Integer value = res.get(option);
            value++;
            res.put(option, value);
            survey.setResult(res);
            return true;
        }else {
            return false;
        }
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }
}
