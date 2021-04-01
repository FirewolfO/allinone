package com.firewol;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * 描述：
 * Author：liuxing
 * Date：2020-09-24
 */
public class HtmlUtils {

    public static void main(String[] args) throws Exception {

        List<String> toAddMulu = Arrays.asList(
                "基本说明",
                "接口鉴权",
                "接口流程",
                "错误码",
                "业务数据推送的数据结构"
        );
        Document doc = Jsoup.parse(new File("/Users/liuxing/Desktop/open/api.html"), "UTF-8");
        Element mulu = doc.getElementsByClass("table-of-contents").get(0).getElementsByTag("ul").get(0);
//        mulu.empty();
        Element right = doc.getElementById("right");
        for (int i = toAddMulu.size() - 1; i >= 0; i--) {
            Elements hs = right.getElementsContainingText(toAddMulu.get(i));
            for (Element h : hs) {
                if (h.tagName().equalsIgnoreCase("h1")) {
                    String id = UUID.randomUUID().toString();
                    h.attr("id", id);
                    String toAppend = "<li><a href=\"#" + id + "\">" + toAddMulu.get(i) + "</a>";
                    toAppend = toAppend + getHElements(h, 2);
                    toAppend = toAppend + "</li>";
                    mulu.prepend(toAppend);

                }
            }
        }

//        removeSpecialTest(right);

        FileOutputStream fos = new FileOutputStream("openApi.html", false);
        fos.write(doc.html().getBytes("UTF-8"));
        fos.flush();
        fos.close();
    }

    private static void removeSpecialTest(Element right) {
        Elements specialTestElements = right.getElementsContainingText("SpecialTest");
        specialTestElements.forEach(e -> {
            if (e.tagName().equalsIgnoreCase("h1")) {
                removeNext(e);
            }
        });
    }

    private static void removeNext(Element toRemove) {
        if ("footer".equals(toRemove.tagName())) {
            toRemove.remove();
            return;
        }
        removeNext(toRemove.nextElementSibling());
        toRemove.remove();
    }

    private static String getHElements(Element element, int h) {
        List<Element> elements = new ArrayList<>();
        Element nextEl = element.nextElementSibling();

        while (true) {
            if (nextEl == null) {
                break;
            }
            if (nextEl.tagName().equalsIgnoreCase("h" + (h - 1))) {
                break;
            }
            if (nextEl.tagName().equalsIgnoreCase("h" + h)) {
                elements.add(nextEl);
            }
            nextEl = nextEl.nextElementSibling();

        }

        if (elements != null && elements.size() > 0) {
            String toAppend = "<ul>";
            for (Element e : elements) {
                String id = UUID.randomUUID().toString();
                e.attr("id", id);
                String li = "<li><a href=\"#" + id + "\">" + e.text() + "</a>";
                li = li + getHElements(e, h + 1);
                li = li + "</li>";
                toAppend = toAppend + li;
            }
            return toAppend;
        } else {
            return "";
        }
    }
}
