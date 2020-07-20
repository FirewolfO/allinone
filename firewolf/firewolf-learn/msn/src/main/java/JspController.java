import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class JspController {

    private String hello = "Jsp";

    @RequestMapping("/hello")
    public String helloJsp(Map<String, Object> map) {
        System.out.println("hello," + hello);
        map.put("name", hello);
        return "hello";
    }

}