import utils.FSConfig;
import utils.Output;
import utils.Validation;

import java.io.File;
import java.util.ArrayList;

public class SurveyMenu {
    private static String basePath_survey = FSConfig.serialDir + "Survey" + File.separator;
    private static String basePath_response = FSConfig.serialDir + "ResponseSurvey" + File.separator;

    public SurveyMenu() {
    }

    private void displaySurveyMenu() {
        Output.output("1) Create a new Survey\n" +
                "2) Display an existing Survey\n" +
                "3) Load an existing Survey\n" +
                "4) Save the current Survey\n" +
                "5) Take the current Survey\n" +
                "6) Modify the current Survey\n" +
                "7) Tabulate a survey\n" +
                "8) Return to previous menu");
    }

    private void displayQuestionSurvey() {
        Output.output("1) Add a new T/F question\n" +
                "2) Add a new multiple-choice question\n" +
                "3) Add a new short answer question\n" +
                "4) Add a new essay question\n" +
                "5) Add a new date question\n" +
                "6) Add a new matching question\n" +
                "7) Return to previous menu");
    }


    public void surveyMenu(){
        Survey loadedSurvey = null;
        int option = 0;
        do {
            displaySurveyMenu();
            option = Validation.validIntegerInput();
            switch (option) {
                case 1:
                    Output.output("Enter Survey Name: ");
                    String surveyName = Validation.validStringInput();
                    loadedSurvey = createSurvey(surveyName);
                    break;
                case 2:
                    if (loadedSurvey != null) {
                        loadedSurvey.displayQuestions();
                    } else {
                        Output.output("You must have a survey loaded in order to display it.");
                    }
                    break;
                case 3:
                    loadedSurvey = loadSurvey();
                    break;
                case 4:
                    if (loadedSurvey != null) {
                        saveSurvey(loadedSurvey);
                        Output.output("Save was successful!");
                    } else {
                        Output.output("You must have a survey loaded in order to save it.");
                    }
                    break;
                case 5:
                    if (loadedSurvey != null) {
                        saveResponse(takeSurvey(loadedSurvey));
                    } else {
                        Output.output("You must have a survey loaded in order to take it.");
                    }
                    break;
                case 6:
                    if (loadedSurvey != null) {
                        modifySurvey(loadedSurvey);
                        deleteResponses(loadedSurvey.getFileName());
                    } else {
                        Output.output("You must have a survey loaded in order to modify it.");
                    }
                    break;
                case 7:
                    if (loadedSurvey != null) {
                        tabulateSurvey(loadedSurvey);
                    } else {
                        Output.output("You must have a survey loaded in order to tabulate it.");
                    }
                    break;
                case 8:
                    break;
                default:
                    Output.output("Invalid option. Please try again.");
            }
        } while (option != 8);

    }

    private void tabulateSurvey(Survey loadedSurvey) {
        loadedSurvey.tabulate(basePath_response);
    }

    private Survey takeSurvey(Survey loadedSurvey) {
        return loadedSurvey.takeSurvey();
    }

    private Survey createSurvey(String surveyName){
        Survey survey = new Survey(surveyName);
        int option = 0;
        do {
            displayQuestionSurvey();
            option = Validation.validIntegerInput();
            String prompt;
            int numOfResponses;
            switch (option) {
                case 1:
                    Output.output("Enter the prompt for your True/False question:");
                    prompt = Validation.validStringInput();
                    TrueFalseQuestion trueFalseQuestion = new TrueFalseQuestion(prompt);
                    survey.addQuestion(trueFalseQuestion);
                    break;
                case 2:
                    Output.output("Enter the prompt for your multiple-choice question:");
                    prompt = Validation.validStringInput();
                    Output.output("Enter the number of choices for your multiple-choice question:");
                    int numOfChoice = Validation.validIntegerInput();
                    boolean flag = true;
                    do {
                        Output.output("Enter the number of responses for your multiple-choice question:");
                        numOfResponses = Validation.validIntegerInput();
                        if (numOfResponses <= numOfChoice){
                            flag = false;
                        } else {
                            Output.output("Number of responses should be less than number of choices for your multiple-choice question:");
                        }
                    } while (flag);
                    ArrayList<String> choices = new ArrayList<>();
                    for (int i = 0; i < numOfChoice; i++) {
                        Output.output("Enter choice #" + (i + 1) + ":");
                        String choice = Validation.validStringInput();
                        choices.add(choice);
                    }
                    MultiChoiceQuestion multiChoiceQuestion = new MultiChoiceQuestion(prompt, numOfResponses, choices);
                    survey.addQuestion(multiChoiceQuestion);
                    break;
                case 3:
                    Output.output("Enter the prompt for your short-answer question:");
                    prompt = Validation.validStringInput();
                    Output.output("Enter the number of responses for your short-answer question:");
                    numOfResponses = Validation.validIntegerInput();
                    Output.output("Enter the word-limit for your short-answer question:");
                    int wordLimit = Validation.validIntegerInput();
                    ShortAnswerQuestion shortAnswerQuestion = new ShortAnswerQuestion(prompt, numOfResponses, wordLimit);
                    survey.addQuestion(shortAnswerQuestion);
                    break;
                case 4:
                    Output.output("Enter the prompt for your essay question:");
                    prompt = Validation.validStringInput();
                    Output.output("Enter the number of responses for your essay question:");
                    numOfResponses = Validation.validIntegerInput();
                    EssayQuestion essayQuestion = new EssayQuestion(prompt, numOfResponses);
                    survey.addQuestion(essayQuestion);
                    break;
                case 5:
                    Output.output("Enter the prompt for your date question:");
                    prompt = Validation.validStringInput();
                    Output.output("Enter the number of responses for your date question:");
                    numOfResponses = Validation.validIntegerInput();
                    DateQuestion dateQuestion = new DateQuestion(prompt, numOfResponses);
                    survey.addQuestion(dateQuestion);
                    break;
                case 6:
                    Output.output("Enter the prompt for your matching question:");
                    prompt = Validation.validStringInput();
                    Output.output("Enter the number of choices for your left column:");
                    int numLeft = Validation.validIntegerInput();
                    Output.output("Enter the number of choices for your right column:");
                    int numRight = Validation.validIntegerInput();
                    ArrayList<String> leftChoices = new ArrayList<>();
                    for (int i = 0; i < numLeft; i++) {
                        Output.output("Enter choice left #" + (i + 1) + ":");
                        String choice = Validation.validStringInput();
                        leftChoices.add(choice);
                    }
                    ArrayList<String> rightChoices = new ArrayList<>();
                    for (int i = 0; i < numRight; i++) {
                        Output.output("Enter choice right #" + (i + 1) + ":");
                        String choice = Validation.validStringInput();
                        rightChoices.add(choice);
                    }
                    boolean flag_ = true;
                    do {
                        Output.output("Enter the number of responses for your matching question:");
                        numOfResponses = Validation.validIntegerInput();
                        if (numOfResponses <= numLeft){
                            flag_ = false;
                        } else {
                            Output.output("Number of responses should be less than number of choices (left column) for your matching question:");
                        }
                    } while (flag_);
                    MatchingQuestion matchingQuestion = new MatchingQuestion(prompt, numOfResponses, leftChoices, rightChoices);
                    survey.addQuestion(matchingQuestion);
                    break;
                case 7:
                    Output.output("Returning to previous menu");
                    saveSurvey(survey);
                    break;
                default:
                    Output.output("Invalid option. Please try again.");
            }
        } while (option != 7);
        return survey;
    }

    private String saveSurvey(Survey survey){
        return Survey.serialize(survey, basePath_survey);
    }

    private String saveResponse(Survey survey){
        return Survey.serialize(survey, basePath_response);
    }

    private Survey loadSurvey(){
        File dir = new File(basePath_survey);
        File[] files = dir.listFiles();
        if(files == null || files.length == 0) {
            Output.output("No Survey to load.");
            return null;
        } else{
            return Survey.deserialize(basePath_survey);
        }
    }

    private void modifySurvey(Survey survey) {
        int questionNumber = 0;
        boolean flag = true;
        do {
            Output.output("What question do you wish to modify?");
            questionNumber = Validation.validIntegerInput();
            if (questionNumber <= survey.getNumberOfQuestions()){
                flag = false;
            } else {
                Output.output("Survey does not have this Question Number");
            }
        } while (flag);
        Question updateQuestion = survey.getQuestion(questionNumber-1);
        updateQuestion.modify();
        Survey.serialize(survey, basePath_survey);
    }

    public static void deleteResponses(String surveyName) {
        File responseDir = new File(basePath_response);
        File[] responseFiles = responseDir.listFiles((dir, name) -> name.startsWith(surveyName));

        if (responseFiles != null) {
            for (File file : responseFiles) {
                file.delete();
            }
        }
    }

}