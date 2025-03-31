package project.diary.util;

import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

public class HtmlSanitizerUtil {
    private static final PolicyFactory POLICY = Sanitizers.FORMATTING
            .and(Sanitizers.BLOCKS)
            .and(Sanitizers.STYLES); // 허용할 태그 종류 선택

    public static String sanitize(String input) {
        return POLICY.sanitize(input);
    }
}
