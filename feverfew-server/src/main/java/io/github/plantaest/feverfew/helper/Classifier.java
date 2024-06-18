package io.github.plantaest.feverfew.helper;

import ai.onnxruntime.OnnxMap;
import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import io.github.plantaest.feverfew.config.ml.FeverfewNextModel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class Classifier {

    @Inject
    FeverfewNextModel feverfewNextModel;

    public List<ClassificationResult> classify(List<RequestResult> requestResults) {
        try {
            List<ClassificationResult> classificationResults = new ArrayList<>();

            if (requestResults.isEmpty()) {
                return classificationResults;
            }

            var env = OrtEnvironment.getEnvironment();
            var session = env.createSession(feverfewNextModel.getInstance(), new OrtSession.SessionOptions());
            var featureValueRows = requestResults.stream()
                    .map(this::convertRequestResultToFeatureArray)
                    .toArray(float[][]::new);
            var tensor = OnnxTensor.createTensor(env, featureValueRows);
            var inputs = Map.of("float_input", tensor);

            try (OrtSession.Result results = session.run(inputs)) {
                if (results.get("output_label").isPresent() && results.get("output_probability").isPresent()) {
                    var onnxLabels = (long[]) results.get("output_label").get().getValue();
                    List<Long> labels = Arrays.stream(onnxLabels).boxed().toList();

                    @SuppressWarnings("unchecked")
                    var onnxProbs = (List<OnnxMap>) results.get("output_probability").get().getValue();
                    List<Float> probs = new ArrayList<>();
                    for (var prob : onnxProbs) {
                        probs.add((float) prob.getValue().get(1L));
                    }

                    for (int i = 0; i < labels.size(); i++) {
                        ClassificationResult classificationResult;

                        if (requestResults.get(i).type() == RequestResult.Type.IGNORED) {
                            classificationResult = ClassificationResultBuilder.builder()
                                    .label(0)
                                    .probability(0)
                                    .build();
                        } else {
                            classificationResult = ClassificationResultBuilder.builder()
                                    .label(labels.get(i))
                                    .probability(probs.get(i))
                                    .build();
                        }

                        classificationResults.add(classificationResult);
                    }
                }
            }

            return classificationResults;
        } catch (OrtException e) {
            throw new RuntimeException(e);
        }
    }

    private float[] convertRequestResultToFeatureArray(RequestResult requestResult) {
        float[] floats = new float[9];

        // (1) result_type
        floats[0] = switch (requestResult.type()) {
            case SUCCESS, IGNORED -> 1; // Actually, the model's dataset does not contain any IGNORED cases.
            case ERROR -> 0;
        };

        // (2) request_duration
        floats[1] = (float) requestResult.requestDuration();

        // (3) response_status
        // Python:
        // statuses = sorted(set(df['response_status'].to_list()))
        // encoded_labels = LabelEncoder().fit_transform(statuses)
        floats[2] = switch (requestResult.responseStatus()) {
            case 0 -> 0;
            case 200 -> 1;
            case 301 -> 2;
            case 302 -> 3;
            case 303 -> 4;
            case 307 -> 5;
            case 308 -> 6;
            case 400 -> 7;
            case 401 -> 8;
            case 403 -> 9;
            case 404 -> 10;
            case 410 -> 11;
            case 500 -> 12;
            case 503 -> 13;
            case 504 -> 14;
            default -> 1;
        };

        // (4) content_length
        floats[3] = requestResult.contentLength();

        // (5) contains_page_not_found_words
        floats[4] = requestResult.containsPageNotFoundWords() ? 1 : 0;

        // (6) contains_paywall_words
        floats[5] = requestResult.containsPaywallWords() ? 1 : 0;

        // (7) contains_domain_expired_words
        floats[6] = requestResult.containsDomainExpiredWords() ? 1 : 0;

        // (8) number_of_redirects
        floats[7] = requestResult.redirects().size();

        // (9) redirect_to_homepage
        floats[8] = requestResult.redirectToHomepage() ? 1 : 0;

        return floats;
    }

}
