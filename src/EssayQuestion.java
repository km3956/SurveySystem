import utils.Output;
import utils.Validation;

import java.util.ArrayList;
import java.util.Map;

public class EssayQuestion extends Question{
    public EssayQuestion(String prompt, int numOfResponses) {
        super(prompt, numOfResponses);
    }

    @Override
    public String display() {
        StringBuilder displayString = new StringBuilder();
        displayString.append(getPrompt()).append("\n");
        displayString.append("This is an essay question\n");
        return displayString.toString();
    }

    @Override
    public void takeQuestion(){
        ArrayList<Response> mainResponse = new ArrayList<>();
        for (int i = 0; i<numOfResponses; i++){
            Output.output("Enter response #" + (i+1));
            String option = Validation.validStringInput();
            Response optionResponse = new Response(option);
            mainResponse.add(optionResponse);
        }
        setResponse(mainResponse);
    }

    @Override
    public boolean isEssayQuestion(){
        return true;
    }

    public String generateTabulation(Map<String, Integer> tally, ArrayList<Response> userResponseQuestion) {
        StringBuilder tabulation = new StringBuilder();

        for (String response : tally.keySet()) {
            tabulation.append(response).append("\n");
        }
        return tabulation.toString();
    }
}
