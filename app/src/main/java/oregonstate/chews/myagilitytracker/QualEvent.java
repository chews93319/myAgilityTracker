/*
 * Reference List
 *
 * 1. CodingWithMitch Android Beginner Tutorial #8 - Custom ListView Adapter]
 *    https://www.youtube.com/watch?annotation_id=annotation_3104328239&feature=iv&src_vid=8K-6gdTlGEA&v=E6vE8fqQPTE
 *
 *
 *
 * */


package oregonstate.chews.myagilitytracker;

//[ref: 1]
public class QualEvent {
    private String dogname;
    private String game;
    private String date;
    private String points;

    public QualEvent(String dogname, String game, String date, String points) {
        this.dogname = dogname;
        this.game = game;
        this.date = date;
        this.points = points;
    }

    public String getDogname() {
        return dogname;
    }

    public void setDogname(String dogname) {
        this.dogname = dogname;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}
