
package tuwien.aic.crowdsourcing.mobileworks.json.deserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class AnswerDeserializer extends JsonDeserializer<List<Map<String, ?>>> {

    public AnswerDeserializer() {
        // this.mapper = mapper;
    }

    // @Override
    // public JsonDeserializer<?> createContextual(DeserializationContext ctxt,
    // BeanProperty property) throws JsonMappingException {
    // return new
    // AnswerDeserializer<Object>(property.getType().containedType(1).getRawClass(),
    // mapper);
    // }

    @Override
    public List<Map<String, ?>> deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = jp.readValueAsTree();
        List<Map<String, ?>> ret = new ArrayList<Map<String, ?>>();

        if (node.isTextual()) {
            // textual node -> special handling
            if (!("".equals(node.asText()))) {
                Map<String, String> elem = new HashMap<String, String>();
                elem.put("Invalid", node.asText());
                ret.add(elem);
            }
        } else if (node.isArray()) {
            // array -> default handling
            AnswerType type = null;
            Map<String, String> stringAnswerMap = null;
            Map<String, List<String>> listAnswerMap = null;
            for (JsonNode answerElement : node) {
                if (!answerElement.isObject()) {
                    // no object
                    continue;
                }

                Iterator<Entry<String, JsonNode>> it = answerElement.fields();
                Entry<String, JsonNode> answer;
                while (it.hasNext()) {
                    answer = it.next();
                    if (type == null) {
                        if (answer.getValue().isArray()) {
                            type = AnswerType.MULTIPLE;
                            listAnswerMap = new TreeMap<String, List<String>>();
                        } else {
                            type = AnswerType.SINGLE;
                            stringAnswerMap = new TreeMap<String, String>();
                        }
                    }

                    if (AnswerType.MULTIPLE.equals(type)) {
                        // multiple
                        List<String> answers = new ArrayList<String>();
                        for (JsonNode a : answer.getValue()) {
                            answers.add(a.asText());
                        }
                        listAnswerMap.put(answer.getKey(), answers);
                    } else if (AnswerType.SINGLE.equals(type)) {
                        // single
                        stringAnswerMap.put(answer.getKey(), answer.getValue().asText());
                    }
                }

                // check which type it is and add the right map
                // and init a new map
                if (AnswerType.MULTIPLE.equals(type)) {
                    ret.add(listAnswerMap);
                    listAnswerMap = new TreeMap<String, List<String>>();
                } else if (AnswerType.SINGLE.equals(type)) {
                    ret.add(stringAnswerMap);
                    stringAnswerMap = new TreeMap<String, String>();
                }
            }
        }
        return ret;
    }

    private static enum AnswerType {
        SINGLE, MULTIPLE
    }
}
