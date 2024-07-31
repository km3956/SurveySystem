import utils.FileUtils;
import utils.Output;
import utils.SerializationHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Survey implements Serializable{
    private static final long serialVersionUID = 1L;
    protected String fileName;
    protected ArrayList<Question> listOfQuestions;

    public Survey(String fileName){
        this.listOfQuestions = new ArrayList<>();
        this.fileName = fileName;
    }

    public void addQuestion(Question question) {
        listOfQuestions.add(question);
    }

    public String getFileName(){
        return this.fileName;
    }

    public Question getQuestion(int questionNumber){
        return listOfQuestions.get(questionNumber);
    }

    public int getNumberOfQuestions(){
        return listOfQuestions.size();
    }

    public void displayQuestions() {
        int counter = 1;
        for (Question q : listOfQuestions) {
            StringBuilder displayString = new StringBuilder();
            displayString.append(counter).append(") ");
            displayString.append(q.display());
            displayString.toString();
            Output.output(String.valueOf(displayString));
            counter ++;
        }
    }

    public static String serialize(Survey survey, String base_path){
        return SerializationHelper.serialize(
                Survey.class, survey, base_path, survey.fileName
        );
    }

    public static Survey deserialize(String basePath){
        String selectedSurvey = FileUtils.listAndPickFileFromDir(basePath);
        return deserialize_(selectedSurvey);
    }

    public static Survey deserialize_(String path){
        Survey survey = SerializationHelper.deserialize(Survey.class, path);
        return survey;
    }

    public Survey takeSurvey(){
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Survey newSurvey = new Survey(fileName+currentTime);
        int counter = 1;
        for (Question q : listOfQuestions) {
            StringBuilder displayString = new StringBuilder();
            displayString.append(counter).append(". ");
            displayString.append(q.display());
            displayString.toString();
            Output.output(String.valueOf(displayString));
            Output.output("Enter "+ String.valueOf(q.getNumOfResponses()) + " responses");
            counter ++;
            q.takeQuestion();
            newSurvey.addQuestion(q);
        }
        return newSurvey;
    }

    private ArrayList<Survey> loadAllResponses(String basePath) {
        ArrayList<Survey> surveys = new ArrayList<>();
        List<String> responseFiles = FileUtils.getFilesStartingWith(this.fileName, basePath);
        for (String filePath : responseFiles) {
            Survey survey = deserialize_(filePath);
            surveys.add(survey);
        }
        return surveys;
    }

    public void tabulate(String basePath) {
        ArrayList<Survey> allResponses = loadAllResponses(basePath);

        if (allResponses.isEmpty()) {
            Output.output("No responses found for tabulation.");
            return;
        }

        int counter = 1;
        for (Question q : listOfQuestions) {
            ArrayList<Response> allResponsesQuestion = new ArrayList<>();
            StringBuilder displayString = new StringBuilder();
            displayString.append("QUESTION\n");
            displayString.append(counter).append(". ").append(q.getPrompt()).append("\n");
            displayString.append("RESPONSES\n");
            Map<String, Integer> tally = new HashMap<>();
            for (Survey survey : allResponses) {
                Question responseQuestion = survey.getQuestion(counter - 1);
                if (responseQuestion != null) {
                    for (Response r : responseQuestion.getUserResponse()) {
                        displayString.append(r.getResponse()).append("\n");
                        allResponsesQuestion.add(r);
                        tally.put(r.getResponse(), tally.getOrDefault(r.getResponse(), 0) + 1);
                    }
                }
            }
            if (!q.isEssayQuestion()){
                displayString.append("TABULATION\n");
                String tabulation;
                tabulation = q.generateTabulation(tally, allResponsesQuestion);
                displayString.append(tabulation);
            }
            Output.output(String.valueOf(displayString));
            counter++;
        }
    }

}

