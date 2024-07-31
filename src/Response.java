    import utils.FileUtils;
    import utils.SerializationHelper;
    
    import java.io.Serializable;
    import java.util.ArrayList;
    import java.util.Scanner;
    
    public class Response implements Serializable {
        private static final long serialVersionUID = 1L;
        private String response;
    
        public Response(String response){
            this.response = response;
        }
    
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Response other = (Response) obj;
            return response.equals(other.response);
        }

        public String getResponse(){
            return this.response;
        }
    
        @Override
        public int hashCode() {
            return response.toUpperCase().hashCode();
        }
    
        public static boolean compareResponses(ArrayList<Response> responses1, ArrayList<Response> responses2) {
            if (responses1.size() != responses2.size()) {
                return false;
            }
            ArrayList<Response> copy2 = new ArrayList<>(responses2);
            for (Response response1 : responses1) {
                boolean matchFound = false;
                for (Response response2 : copy2) {
                    if (response1.equals(response2)) {
                        matchFound = true;
                        copy2.remove(response2);
                        break;
                    }
                }
                if (!matchFound) {
                    return false;
                }
            }
            return copy2.isEmpty();
        }
    
    
        public static Test deserialize(String testName, String basePath){
            String selectedSurvey = FileUtils.listAndPickFileStartingWith(testName, basePath);
            return deserialize_(selectedSurvey);
        }
    
        public static Test deserialize_(String path){
            Test survey = SerializationHelper.deserialize(Test.class, path);
            return survey;
        }
    
    }
