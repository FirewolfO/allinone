package com.firewolf.common.utils;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/6/15
 */
public class AiparkLogUtils {

    private static String TRACKID_STRART = "trackId=";
    private static String TRACKID_END = ",";


    private static String SCORE_STRART = "score=";
    private static String SCORE_END = ")";


    public static void main(String[] args) throws Exception {
        countScoreBigThan("C:\\Users\\liuxing\\Desktop\\fsdownload\\aa.log", 90D);
    }

    /**
     * 统计分数大于多少数量
     *
     * @param filePath
     * @param score
     * @return
     */
    private static void countScoreBigThan(String filePath, Double score) throws Exception {
        Map<String, Double> trackScores = trackScoreMap(filePath);
        long count = trackScores.values().stream().filter(x -> x > score).count();
        System.out.println(trackScores.size() + "," + count);
    }

    private static Map<String, Double> trackScoreMap(String filePath) throws IOException {
        // 各个track的最高分
        Map<String, Double> trackScores = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                String trackStr = line.substring(line.indexOf(TRACKID_STRART) + TRACKID_STRART.length());
                String trackId = trackStr.substring(0, trackStr.indexOf(TRACKID_END));

                String scoreStr = trackStr.substring(trackStr.indexOf(SCORE_STRART) + SCORE_STRART.length());
                String score = scoreStr.substring(0, scoreStr.indexOf(SCORE_END));

                double scores = Double.parseDouble(score);
                if (trackScores.containsKey(trackId)) {
                    double exScore = trackScores.get(trackId);
                    if (exScore > scores) {
                        continue;
                    }
                }
                trackScores.put(trackId, scores);
            }
        }

        System.out.println(new Gson().toJson(trackScores));
        return trackScores;
    }

}
