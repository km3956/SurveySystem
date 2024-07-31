import utils.Output;
import utils.Validation;

import java.util.ArrayList;
import java.util.Map;

public class ShortAnswerQuestion extends EssayQuestion{
    int wordLimit;
    public ShortAnswerQuestion(String prompt, int numOfResponses, int wordLimit) {
        super(prompt, numOfResponses);
        this.wordLimit = wordLimit;
    }

    @Override
    public String display() {
        StringBuilder displayString = new StringBuilder();
        displayString.append(getPrompt()).append("\n");
        displayString.append("This is a short answer question\n");
        return displayString.toString();
    }

    public void  modify(){
        modifyPrompt();
        modifyNumResponses();
        Output.output("Do you wish to modify the word limit? (1 for Yes)");
        int option = Validation.validIntegerInput();
        if (option == 1) {
            Output.output("Enter new word limit:");
            int newWordLimit = Validation.validIntegerInput();
            this.wordLimit = newWordLimit;
        }
    }


    @Override
    public void takeQuestion(){
        boolean flag_ = true;
        ArrayList<Response> mainResponse = new ArrayList<>();
        do {
            mainResponse = new ArrayList<>();
            int wordCount = 0;
            for (int i = 0; i<numOfResponses; i++) {
                Output.output("Enter response #" + (i+1));
                String option = Validation.validStringInput();
                Response optionResponse = new Response(option);
                mainResponse.add(optionResponse);
                String[] words = option.trim().split("\\s+");
                wordCount = wordCount + words.length;
            }
                if (wordCount <= wordLimit){
                    flag_ = false;
                } else {
                    Output.output("The response exceeds the word limit");
                }
            } while (flag_);
        setResponse(mainResponse);
    }

    @Override
    public boolean isEssayQuestion(){
        return false;
    }

    @Override
    public String generateTabulation(Map<String, Integer> tally, ArrayList<Response> userQuestionResponse) {
        StringBuilder tabulation = new StringBuilder();
        for (Map.Entry<String, Integer> entry : tally.entrySet()) {
            tabulation.append(entry.getKey()).append(" ").append(entry.getValue()).append("\n");
        }
        return tabulation.toString();
    }

}
