package Objects;


public class QuarantineItem {

    private String term;
    private String[] recommendationList;
    private String reason;

    public QuarantineItem(String term, String[] recommendations, String reason) {
        this.term = term;
        this.recommendationList = recommendations;
        this.reason = reason;
    }

    public QuarantineItem() {
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String[] getRecommendations() {
        return recommendationList;
    }

    public void setRecommendations(String[] recommendations) {
        this.recommendationList = recommendations;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
