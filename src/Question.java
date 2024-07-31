import utils.Output;
import utils.Validation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public abstract class Question implements Serializable{
    protected String prompt;
    private static final long serialVersionUID = 1L;
    protected int numOfResponses;
    ArrayList<Response> userResponse;

    public Question(String prompt, int numOfResponses){
        this.prompt = prompt;
        this.numOfResponses = numOfResponses;
    }

    public int getNumOfResponses() {
        return numOfResponses;
    }

    public ArrayList<Response> getUserResponse(){
        return this.userResponse;
    }

    public void setResponse(ArrayList<Response> response){
        this.userResponse = response;
    }

    public abstract String display();

    public void updatePrompt(String prompt){
        this.prompt = prompt;
    }

    public String getPrompt(){
        return this.prompt;
    }

    public void updateNumResponses(int numOfResponses){
        this.numOfResponses = numOfResponses;
    }

    public int getNumResponses(){
        return this.numOfResponses;
    }

    public void modify(){
        modifyPrompt();
        modifyNumResponses();
    }

    public void modifyResponse(){
        Output.output("Do you wish to change the correct answer? (1 for Yes)");
        int option = Validation.validIntegerInput();
        if (option == 1) {
            takeQuestion();
        }
    }

    protected void modifyPrompt(){
        Output.output(getPrompt());
        Output.output("Do you wish to modify the prompt? (1 for Yes)");
        int option = Validation.validIntegerInput();
        if (option == 1) {
            Output.output(getPrompt());
            Output.output("Enter a new prompt:");
            String new_prompt = Validation.validStringInput();
            updatePrompt(new_prompt);
        }
    }

    public void modifyNumResponses(){
        Output.output("The number of responses the question can take right now: " + numOfResponses);
        Output.output("Do you wish to modify the number of responses allowed? (1 for Yes)");
        int option = Validation.validIntegerInput();
        if (option == 1) {
            Output.output("Enter a new number of responses allowed:");
            int newNumResponses = Validation.validIntegerInput();
            updateNumResponses(newNumResponses);
        }
    }

    public String displayResponse() {
        StringBuilder displayString = new StringBuilder();
        ArrayList<Response> responses = this.getUserResponse();
        displayString.append("The correct answer/answers: \n");
        for (Response response : responses) {
            displayString.append("    - ").append(response.getResponse()).append("\n");
        }
        return String.valueOf(displayString);
    }

    public abstract void takeQuestion();

    public boolean isEssayQuestion(){
        return false;
    }

    public abstract String generateTabulation(Map<String, Integer> tally, ArrayList<Response> userQuestionResponse);
}
