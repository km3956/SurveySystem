import utils.Validation;

import java.util.ArrayList;
import java.util.Map;

public class TrueFalseQuestion extends MultiChoiceQuestion{

    public TrueFalseQuestion(String prompt) {
        super(prompt, 1, createOptions());
    }

    private static ArrayList<String> createOptions() {
        ArrayList<String> options = new ArrayList<>();
        options.add("T");
        options.add("F");
        return options;
    }

    @Override
    public String display() {
        StringBuilder displayString = new StringBuilder();
        displayString.append(getPrompt()).append("\n");
        displayString.append("T/F\n");
        return displayString.toString();
    }

    @Override
    public void takeQuestion(){
        ArrayList<Response> mainResponse = new ArrayList<>();
        String option = Validation.validSingleCharInputTrueOrFalse();
        option = option.toUpperCase();
        Response optionResponse = new Response(option);
        mainResponse.add(optionResponse);
        setResponse(mainResponse);
    }

    public void  modify(){
        modifyPrompt();
    }

    @Override
    public String displayResponse() {
        StringBuilder displayString = new StringBuilder();
        ArrayList<Response> responses = this.getUserResponse();
        displayString.append("The correct answer/answers: \n");
        for (Response response : responses) {
            displayString.append("    - ").append(response.getResponse()).append("\n");
        }
        return String.valueOf(displayString);
    }

    @Override
    public String generateTabulation(Map<String, Integer> tally, ArrayList<Response> userQuestionResponse) {
        StringBuilder tabulation = new StringBuilder();
        for (String option : options) {
            tabulation.append(option).append(": ").append(tally.getOrDefault(option, 0)).append("\n");
        }
        return tabulation.toString();
    }

}