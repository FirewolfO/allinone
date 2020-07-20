/**
 * 描述：
 * Author：liuxing
 * Date：2020/7/20
 */
public class Score {
    private Integer id;
    private Integer score;

    public Score(Integer id, Integer score) {
        this.id = id;
        this.score = score;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
