import utils.Output;
import utils.Validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchingQuestion extends Question{
    ArrayList<String> leftColumn;
    ArrayList<String> rightColumn;

    public MatchingQuestion(String prompt, int numOfResponses, ArrayList<String> leftColumn, ArrayList<String> rightColumn) {
        super(prompt, numOfResponses);
        this.leftColumn = leftColumn;
        this.rightColumn = rightColumn;
    }

    @Override
    public String display() {
        StringBuilder displayString = new StringBuilder();
        displayString.append(getPrompt()).append("\n");

        int maxColumn = Math.max(leftColumn.size(), rightColumn.size());
        for (int i = 0; i < maxColumn; i++) {
            char optionChar = (char) ('A' + i);
            String leftChoice;
            if (i < leftColumn.size()) {
                leftChoice = leftColumn.get(i);
            } else {
                leftChoice = "";
            }
            String rightChoice;
            if (i < rightColumn.size()) {
                rightChoice = rightColumn.get(i);
            } else {
                rightChoice = "";
            }
            int leftColumnWidth = 20;
            String formattedLeftChoice = String.format("%-" + leftColumnWidth + "s", leftChoice);

            displayString.append(optionChar).append(") ").append(formattedLeftChoice)
                    .append("\t\t").append(i + 1).append(") ").append(rightChoice).append("\n");
        }
        return displayString.toString();
    }


    @Override
    public void modify() {
        modifyPrompt();
        Output.output("Do you wish to modify the left column choices? (1 for Yes)");
        int option = Validation.validIntegerInput();
        if (option == 1){
            modifyChoice(leftColumn, "L");
        }
        Output.output("Do you wish to modify the right column choices? (1 for Yes)");
        option = Validation.validIntegerInput();
        if (option == 1){
            modifyChoice(rightColumn, "R");
        }
        Output.output("Do you want to add a left choice? (1 for Yes)");
        option = Validation.validIntegerInput();
        if (option == 1){
            addChoice("L");
        }
        Output.output("Do you want to add a right choice? (1 for Yes)");
        option = Validation.validIntegerInput();
        if (option == 1){
            addChoice("R");
        }
        Output.output("Do you want to delete a left choice? (1 for Yes)");
        option = Validation.validIntegerInput();
        if (option == 1){
            deleteChoice(leftColumn, "L");
        }
        Output.output("Do you want to delete a right choice? (1 for Yes)");
        option = Validation.validIntegerInput();
        if (option == 1){
            deleteChoice(rightColumn, "R");
        }
        modifyNumResponses();
    }

    @Override
    public void takeQuestion() {
        ArrayList<Response> mainResponse = new ArrayList<>();
        for (int i = 0; i<numOfResponses; i++){
            Output.output("Enter response #" + (i+1));
            String option = Validation.validFormattedInput(leftColumn, rightColumn);
            option = option.toUpperCase();
            Response optionResponse = new Response(option);
            mainResponse.add(optionResponse);
        }
        setResponse(mainResponse);
    }

    public void deleteChoice(ArrayList<String> choices, String column){
        StringBuilder displayString = new StringBuilder();
        if (column.equalsIgnoreCase("L")){
            Output.output("Which left choice do you want to delete?");
        } else {
            Output.output("Which right choice do you want to delete?");
        }
        for (int i = 0; i < choices.size(); i++) {
            char optionChar = (char) ('A' + i);
            displayString.append(optionChar).append(") ").append(choices.get(i)).append(" ");
        }
        Output.output(displayString.toString());
        String choice = Validation.validSingleCharInputMCQ(choices);
        int index = Character.toUpperCase(choice.charAt(0)) - 'A';
        if (column.equalsIgnoreCase("L")){
            leftColumn.remove(index);
        } else {
            rightColumn.remove(index);
        }
    }

    public void addChoice(String column){
        if (column.equalsIgnoreCase("L")){
            Output.output("Value of left choice:");
        } else {
            Output.output("Value of right choice:");
        }
        String new_value = Validation.validStringInput();
        if (column.equalsIgnoreCase("L")){
            leftColumn.add(new_value);
        } else {
            rightColumn.add(new_value);
        }
    }

    public void modifyChoice(ArrayList<String> choices, String column){
        StringBuilder displayString = new StringBuilder();
        Output.output("Which choice do you want to modify?");
        for (int i = 0; i < choices.size(); i++) {
            char optionChar = (char) ('A' + i);
            displayString.append(optionChar).append(") ").append(choices.get(i)).append(" ");
        }
        Output.output(displayString.toString());
        String choice = Validation.validSingleCharInputMCQ(choices);
        String new_value = Validation.validStringInput();
        int index = Character.toUpperCase(choice.charAt(0)) - 'A';
        if (column.equalsIgnoreCase("L")){
            leftColumn.set(index, new_value);
        }
        else{
            rightColumn.set(index, new_value);
        }

    }

    @Override
    public String generateTabulation(Map<String, Integer> tally, ArrayList<Response> userQuestionResponse) {
        int numResponses = getNumOfResponses();
        List<List<Response>> groupedResponses = new ArrayList<>();
        for (int i = 0; i < userQuestionResponse.size(); i += numResponses) {
            List<Response> group = new ArrayList<>();
            for (int j = 0; j < numResponses && (i + j) < userQuestionResponse.size(); j++) {
                group.add(userQuestionResponse.get(i + j));
            }
            groupedResponses.add(group);
        }
        Map<String, Integer> detailedTally = new HashMap<>();
        for (List<Response> group : groupedResponses) {
            StringBuilder keyBuilder = new StringBuilder();
            for (Response response : group) {
                keyBuilder.append(response.getResponse()).append(" ");
            }
            String key = keyBuilder.toString().trim();
            detailedTally.put(key, detailedTally.getOrDefault(key, 0) + 1);
        }
        List<String> keys = new ArrayList<>(detailedTally.keySet());
        for (int i = 0; i < keys.size() - 1; i++) {
            for (int j = i + 1; j < keys.size(); j++) {
                if (keys.get(i).compareTo(keys.get(j)) > 0) {
                    String temp = keys.get(i);
                    keys.set(i, keys.get(j));
                    keys.set(j, temp);
                }
            }
        }
        StringBuilder tabulation = new StringBuilder();
        for (String key : keys) {
            tabulation.append(key).append("\n").append(detailedTally.getOrDefault(key, 0)).append("\n");
        }
        return tabulation.toString();
    }
}
