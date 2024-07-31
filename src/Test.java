import utils.FileUtils;
import utils.Output;
import utils.SerializationHelper;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Test extends Survey implements Serializable {
    private static final long serialVersionUID = 1L;

    public Test(String fileName) {
        super(fileName);
    }

    public Test takeTest(){
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Test newTest = new Test(fileName+currentTime);
        int counter = 1;
        for (Question q : listOfQuestions) {
            StringBuilder displayString = new StringBuilder();
            displayString.append(counter).append(". ");
            displayString.append(q.display());
            displayString.toString();
            Output.output(String.valueOf(displayString));
            counter ++;
            q.takeQuestion();
            newTest.addQuestion(q);
        }
        return newTest;
    }

    public static Test deserialize(String basePath){
        String selectedSurvey = FileUtils.listAndPickFileFromDir(basePath);
        return deserialize_(selectedSurvey);
    }

    public static Test deserialize_(String path){
        Test survey = SerializationHelper.deserialize(Test.class, path);
        return survey;
    }


    public void gradeTest(Test correctTest) {
        int score = 0;

        int numEssayQuestion = 0;
        for (int i = 0; i < this.getNumberOfQuestions(); i++) {
            if (correctTest.listOfQuestions.get(i).isEssayQuestion()){
                numEssayQuestion++;
            } else{
                ArrayList<Response> correctAnswer = correctTest.listOfQuestions.get(i).getUserResponse();
                ArrayList<Response> userAnswer = this.listOfQuestions.get(i).getUserResponse();
                if (Response.compareResponses(correctAnswer, userAnswer)){
                    score++;
                }
            }
        }
        float scorePercentage = ((float) score / this.getNumberOfQuestions()) * 100;
        float essayPercentage = ((float) (this.getNumberOfQuestions() - numEssayQuestion) / this.getNumberOfQuestions()) * 100;

        String formattedScorePercentage = String.format("%.2f", scorePercentage);
        String formattedEssayPercentage = String.format("%.2f", essayPercentage);

        Output.output("You received a " + formattedScorePercentage + " on the test. The test was worth 100 points, but only " + formattedEssayPercentage +
                " of those points could be auto graded because there were " + numEssayQuestion + " essay question(s).");    }

    public void displayWithAnswers(){
        int counter = 1;
        for (Question q : listOfQuestions) {
            StringBuilder displayString = new StringBuilder();
            displayString.append(counter).append(") ");
            displayString.append(q.display());
            displayString.toString();
            if (!q.isEssayQuestion()) {
                displayString.append(q.displayResponse());
            }
            Output.output(String.valueOf(displayString));
            counter ++;
        }
    }
}
