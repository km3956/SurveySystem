import utils.FSConfig;
import utils.Output;
import utils.Validation;

import java.io.File;
import java.util.ArrayList;

public class TestMenu {
    private static String basePath_test = FSConfig.serialDir + "Test" + File.separator;
    private static String basePath_response = FSConfig.serialDir + "ResponseTest" + File.separator;

    public TestMenu() {
    }

    private void displayTestMenu() {
        Output.output("1) Create a new Test\n" +
                "2) Display an existing Test without correct answers\n" +
                "3) Display an existing Test with correct answers\n" +
                "4) Load an existing Test\n" +
                "5) Save the current Test\n" +
                "6) Take the current Test\n" +
                "7) Modify the current Test\n" +
                "8) Tabulate a Test\n" +
                "9) Grade a Test\n" +
                "10) Return to the previous menu");
    }

    private void displayQuestionTest() {
        Output.output("1) Add a new T/F question\n" +
                "2) Add a new multiple-choice question\n" +
                "3) Add a new short answer question\n" +
                "4) Add a new essay question\n" +
                "5) Add a new date question\n" +
                "6) Add a new matching question\n" +
                "7) Return to previous menu");
    }

    public void testMenu() {
        Test loadedTest = null;
        int option = 0;
        do {
            displayTestMenu();
            option = Validation.validIntegerInput();
            switch (option) {
                case 1:
                    Output.output("Enter Test Name: ");
                    String testName = Validation.validStringInput();
                    loadedTest = createTest(testName);
                    break;
                case 2:
                    if (loadedTest != null) {
                        loadedTest.displayQuestions();
                    } else {
                        Output.output("You must have a test loaded in order to display it.");
                    }
                    break;
                case 3:
                    if (loadedTest != null) {
                        loadedTest.displayWithAnswers();
                    } else {
                        Output.output("You must have a test loaded in order to display it.");
                    }
                    break;
                case 4:
                    loadedTest = loadTest();
                    break;
                case 5:
                    if (loadedTest != null) {
                        saveTest(loadedTest);
                        Output.output("Save was successful!");
                    } else {
                        Output.output("You must have a test loaded in order to save it.");
                    }
                    break;
                case 6:
                    if (loadedTest != null) {
                        saveResponse(takeTest(loadedTest));
                    } else {
                        Output.output("You must have a test loaded in order to take it.");
                    }
                    break;
                case 7:
                    if (loadedTest != null) {
                        modifyTest(loadedTest);
                        SurveyMenu.deleteResponses(loadedTest.getFileName());
                    } else {
                        Output.output("You must have a survey loaded in order to modify it.");
                    }
                    break;
                case 8:
                    if (loadedTest != null) {
                        tabulateTest(loadedTest);
                    } else {
                        Output.output("You must have a survey loaded in order to tabulate it.");
                    }
                    break;
                case 9:
                    if (loadedTest != null) {
                        String test = loadedTest.getFileName();
                        Test loadedResponse = chooseResponse(test);
                        loadedResponse.gradeTest(loadedTest);
                    } else {
                        Output.output("You must have a test loaded in order to grade it.");
                    }
                case 10:
                    break;
                default:
                    Output.output("Invalid option. Please try again.");
                }
        } while (option != 10);
    }

    private void tabulateTest(Test loadedTest) {
        loadedTest.tabulate(basePath_response);
    }

    private void modifyTest(Test test) {
        int questionNumber = 0;
        boolean flag = true;
        do {
            Output.output("What question do you wish to modify?");
            questionNumber = Validation.validIntegerInput();
            if (questionNumber <= test.getNumberOfQuestions()){
                flag = false;
            } else {
                Output.output("Survey does not have this Question Number");
            }
        } while (flag);
        Question updateQuestion = test.getQuestion(questionNumber-1);
        updateQuestion.modify();
        if (!updateQuestion.isEssayQuestion()){
            updateQuestion.modifyResponse();
        }
        Test.serialize(test, basePath_test);
    }

    private Test loadTest() {
        File dir = new File(basePath_test);
        File[] files = dir.listFiles();
        if(files == null || files.length == 0) {
            Output.output("No Test to load.");
            return null;
        } else{
            return Test.deserialize(basePath_test);
        }
    }

    private Test createTest(String testName){
        Test test = new Test(testName);
        int option = 0;
        do {
            displayQuestionTest();
            option = Validation.validIntegerInput();
            String prompt;
            int numOfResponses;
            switch (option) {
                case 1:
                    Output.output("Enter the prompt for your True/False question:");
                    prompt = Validation.validStringInput();
                    TrueFalseQuestion trueFalseQuestion = new TrueFalseQuestion(prompt);
                    Output.output("Enter the correct answer:");
                    trueFalseQuestion.takeQuestion();
                    test.addQuestion(trueFalseQuestion);
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
                    Output.output("Enter the correct answer:");
                    multiChoiceQuestion.takeQuestion();
                    test.addQuestion(multiChoiceQuestion);
                    break;
                case 3:
                    Output.output("Enter the prompt for your short-answer question:");
                    prompt = Validation.validStringInput();
                    Output.output("Enter the number of responses for your short-answer question:");
                    numOfResponses = Validation.validIntegerInput();
                    Output.output("Enter the word-limit for your short-answer question:");
                    int wordLimit = Validation.validIntegerInput();
                    ShortAnswerQuestion shortAnswerQuestion = new ShortAnswerQuestion(prompt, numOfResponses, wordLimit);
                    Output.output("Enter the correct answer:");
                    shortAnswerQuestion.takeQuestion();
                    test.addQuestion(shortAnswerQuestion);
                    break;
                case 4:
                    Output.output("Enter the prompt for your essay question:");
                    prompt = Validation.validStringInput();
                    Output.output("Enter the number of responses for your essay question:");
                    numOfResponses = Validation.validIntegerInput();
                    EssayQuestion essayQuestion = new EssayQuestion(prompt, numOfResponses);
                    test.addQuestion(essayQuestion);
                    break;
                case 5:
                    Output.output("Enter the prompt for your date question:");
                    prompt = Validation.validStringInput();
                    Output.output("Enter the number of responses for your date question:");
                    numOfResponses = Validation.validIntegerInput();
                    DateQuestion dateQuestion = new DateQuestion(prompt, numOfResponses);
                    Output.output("Enter the correct answer:");
                    dateQuestion.takeQuestion();
                    test.addQuestion(dateQuestion);
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
                    Output.output("Enter the correct answer:");
                    matchingQuestion.takeQuestion();
                    test.addQuestion(matchingQuestion);
                    break;
                case 7:
                    Output.output("Returning to previous menu");
                    saveTest(test);
                    break;
                default:
                    Output.output("Invalid option. Please try again.");
            }
        } while (option != 7);
        return test;
    }

    private String saveTest(Test test) {
        return Test.serialize(test, basePath_test);
    }

    private String saveResponse(Test test){
        return Test.serialize(test, basePath_response);
    }

    private Test takeTest(Test loadedTest) {
        return loadedTest.takeTest();
    }

    private Test chooseResponse(String testName){
        return Response.deserialize(testName, basePath_response);
    }

}