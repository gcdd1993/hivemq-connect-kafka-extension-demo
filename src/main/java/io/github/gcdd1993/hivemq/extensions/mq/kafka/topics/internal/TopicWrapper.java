package io.github.gcdd1993.hivemq.extensions.mq.kafka.topics.internal;

import lombok.Getter;
import lombok.ToString;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by gaochen on 2020/7/11.
 */
@Getter
@ToString
public class TopicWrapper {

    private static final String VARIABLE_START_KEY = "${";
    private static final char VARIABLE_END_KEY = '}';

    private static final String PATTERN_PARAM = "([a-zA-Z0-9]+)";

    private String topic;
    private Pattern pattern;
    private List<String> variables;

    public boolean isPattern() {
        return pattern != null;
    }

    public TopicWrapper withTopicName(String topicName) {
        this.topic = topicName;
        if (topicName.contains(VARIABLE_START_KEY)) {
            var sb = new StringBuilder();
            variables = new ArrayList<>();
            lookup(topicName, sb, variables);
            pattern = Pattern.compile(sb.toString());
        }
        return this;
    }

    private void lookup(String target, StringBuilder sb, List<String> variables) {
        var variableStartIndex = target.indexOf(VARIABLE_START_KEY);
        if (variableStartIndex == -1) {
            sb.append(target);
        } else {
            sb.append(target, 0, variableStartIndex);

            var index = variableStartIndex + VARIABLE_START_KEY.length();
            var remainingStr = target.substring(index);
            var variableSB = new StringBuilder();
            for (var c : remainingStr.toCharArray()) {
                if (c == VARIABLE_END_KEY) {
                    break;
                } else {
                    variableSB.append(c);
                    index++;
                }
            }
            sb.append(PATTERN_PARAM);
            variables.add(variableSB.toString());

            if (index < target.length()) {
                lookup(target.substring(index + 1), sb, variables);
            }
        }
    }

}
