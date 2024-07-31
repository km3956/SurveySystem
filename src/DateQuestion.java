import utils.Output;
import utils.Validation;

import java.util.ArrayList;
import java.util.Map;

public class DateQuestion extends Question{
    public DateQuestion(String prompt, int numOfResponses) {
        super(prompt, numOfResponses);
    }

    @Override
    public String display() {
        StringBuilder displayString = new StringBuilder();
        displayString.append(getPrompt()).append("\n");
        displayString.append("A date should be entered in the following format: YYYY-MM-DD\n");
        return displayString.toString();
    }

    @Override
    public void takeQuestion() {
        ArrayList<Response> mainResponse = new ArrayList<>();
        for (int i = 0; i<numOfResponses; i++) {
            Output.output("Enter response #" + (i+1));
            String option = Validation.validDateInput();
            Response optionResponse = new Response(option);
            mainResponse.add(optionResponse);
        }
        setResponse(mainResponse);
    }

    @Override
    public String generateTabulation(Map<String, Integer> tally, ArrayList<Response> userQuestionResponse) {
        StringBuilder tabulation = new StringBuilder();
        for (Map.Entry<String, Integer> entry : tally.entrySet()) {
            tabulation.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return tabulation.toString();
    }
}