import utils.Output;
import utils.Validation;


public class MainMenu {

    private void displayMainMenu() {
        Output.output("1) Survey\n" +
                "2) Test\n" +
                "3) Quit");
    }

    public void mainMenu(){
        int option = 0;
        do {
            displayMainMenu();
            option = Validation.validIntegerInput();
            switch (option) {
                case 1:
                    SurveyMenu surveyMenu = new SurveyMenu();
                    surveyMenu.surveyMenu();
                    break;
                case 2:
                    TestMenu testMenu = new TestMenu();
                    testMenu.testMenu();
                    break;
                case 3:
                    Output.output("The program has quit");
                    break;
                default:
                    Output.output("Invalid option. Please try again.");
            }
        } while (option != 3);

    }

}