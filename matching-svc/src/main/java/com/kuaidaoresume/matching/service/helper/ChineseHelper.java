package com.kuaidaoresume.matching.service.helper;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import com.huaban.analysis.jieba.JiebaSegmenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class ChineseHelper {

    private static ILogger logger = SLoggerFactory.getLogger(ChineseHelper.class);

    @Autowired
    private JiebaSegmenter jiebaSegmenter;

    @Autowired
    private ServiceHelper serviceHelper;

    private Pattern chinesePattern = Pattern.compile("\\p{IsHan}");

    public boolean containsChinese(String input) {
        return chinesePattern.matcher(input).find();
    }

    public String convertChineseForSearch(String input) {
        String result;
        try {
            result = String.join(" ", jiebaSegmenter.sentenceProcess(input))
                    .replaceAll("[^\\p{IsHan}\\w\\s]", "")
                    .replaceAll("\\s+", " ").trim();
        } catch (Throwable throwable) {
            serviceHelper.handleError(logger, throwable, String.format("Failed to convert %s", input));
            result = input;
        }
        return result;
    }
}
