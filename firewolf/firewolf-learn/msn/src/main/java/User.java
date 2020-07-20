/**
 * 描述：
 * Author：liuxing
 * Date：2020/7/20
 */
public class User {
    private String name;
    private Integer id;

    public User(String name, Integer id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
