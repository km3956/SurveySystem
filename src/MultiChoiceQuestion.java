import utils.Output;
import utils.Validation;

import java.util.ArrayList;
import java.util.Map;

public class MultiChoiceQuestion extends Question{
    protected ArrayList<String> options;
    protected ArrayList<Response> selectedResponse;

    public MultiChoiceQuestion(String prompt, int numOfResponses, ArrayList<String> options) {
        super(prompt, numOfResponses);
        this.options = options;
    }

    @Override
    public void takeQuestion(){
        ArrayList<Response> mainResponse = new ArrayList<>();
        for (int i = 0; i<numOfResponses; i++){
            Output.output("Enter response #" + (i+1));
            String option = Validation.validSingleCharInputMCQ(options);
            option = option.toUpperCase();
            Response optionResponse = new Response(option);
            mainResponse.add(optionResponse);
        }
        setResponse(mainResponse);
    }

    @Override
    public String display() {
        StringBuilder displayString = new StringBuilder();
        displayString.append(getPrompt()).append("\n");
        for (int i = 0; i < options.size(); i++) {
            char optionChar = (char) ('A' + i);
            displayString.append(optionChar).append(") ").append(options.get(i)).append(" ");
        }
        displayString.append("\n");
        return displayString.toString();
    }

    @Override
    public void modify() {
        modifyPrompt();
        Output.output("Do you wish to modify the choices? (1 for Yes)");
        int option = Validation.validIntegerInput();
        if (option == 1){
            modifyChoice(options);
        }
        modifyNumResponses();
        Output.output("Do you want to add a choice? (1 for Yes)");
        option = Validation.validIntegerInput();
        if (option == 1){
            addChoice();
        }
        Output.output("Do you want to delete a choice? (1 for Yes)");
        option = Validation.validIntegerInput();
        if (option == 1){
            deleteChoice(options);
        }
    }

    public void addChoice(){
        Output.output("Value of choice:");
        String new_value = Validation.validStringInput();
        options.add(new_value);
    }

    public void modifyChoice(ArrayList<String> choices) {
        StringBuilder displayString = new StringBuilder();
        Output.output("Which choice do you want to modify?");
        for (int i = 0; i < choices.size(); i++) {
            char optionChar = (char) ('A' + i);
            displayString.append(optionChar).append(") ").append(choices.get(i)).append(" ");
        }
        Output.output(displayString.toString());
        String choice = Validation.validSingleCharInputMCQ(options);
        String new_value = Validation.validStringInput();
        int index = Character.toUpperCase(choice.charAt(0)) - 'A';
        options.set(index, new_value);
    }

    public void deleteChoice(ArrayList<String> choices){
        StringBuilder displayString = new StringBuilder();
        Output.output("Which choice do you want to delete?");
        for (int i = 0; i < choices.size(); i++) {
            char optionChar = (char) ('A' + i);
            displayString.append(optionChar).append(") ").append(choices.get(i)).append(" ");
        }
        Output.output(displayString.toString());
        String choice = Validation.validSingleCharInputMCQ(choices);
        int index = Character.toUpperCase(choice.charAt(0)) - 'A';
        options.remove(index);
    }

    public void modifyNumResponses(){
        Output.output("The number of responses the question can take right now: " + numOfResponses);
        Output.output("Do you wish to modify the number of responses allowed? (1 for Yes)");
        int option = Validation.validIntegerInput();
        if (option == 1) {
            Output.output("Enter a new number of responses allowed:");
            int newNumResponses = Validation.validIntegerInput();
            boolean flag = true;
            do {
                Output.output("Enter the number of responses for your multiple-choice question:");
                newNumResponses = Validation.validIntegerInput();
                if (newNumResponses <= options.size()){
                    flag = false;
                } else {
                    Output.output("Number of responses should be less than number of choices for your multiple-choice question:");
                }
            } while (flag);
            updateNumResponses(newNumResponses);
        }
    }

    @Override
    public String displayResponse() {
        StringBuilder displayString = new StringBuilder();
        ArrayList<Response> responses = this.getUserResponse();
        displayString.append("The correct choice/choices:\n");
        for (Response response : responses) {
            char optionChar = response.getResponse().charAt(0);
            int index = optionChar - 'A';
            if (index >= 0 && index < options.size()) {
                displayString.append(optionChar).append(") ").append(options.get(index)).append("\n");
            }
        }
        return displayString.toString();
    }

    @Override
    public String generateTabulation(Map<String, Integer> tally, ArrayList<Response> userQuestionResponse) {
        StringBuilder tabulation = new StringBuilder();
        for (int i = 0; i < options.size(); i++) {
            char index = (char) ('A' + i);
            String indexStr = String.valueOf(index);
            int count = tally.getOrDefault(indexStr, 0);
            tabulation.append(indexStr).append(": ").append(count).append("\n");
        }
        return tabulation.toString();
    }
}