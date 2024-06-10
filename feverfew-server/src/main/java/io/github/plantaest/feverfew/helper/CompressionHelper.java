package io.github.plantaest.feverfew.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@ApplicationScoped
public class CompressionHelper {

    @Inject
    ObjectMapper objectMapper;

    public List<EvaluationResultSchemaV1> convertToSchema(List<EvaluationResult> evaluationResults) {
        ObjectMapper mapper = JsonMapper.builder()
                .configure(MapperFeature.USE_ANNOTATIONS, false)
                .build();
        return mapper.convertValue(evaluationResults, new TypeReference<>() {});
    }

    public byte[] compressJson(Object object) {
        try {
            byte[] raw = objectMapper.writeValueAsBytes(object);
            return compressGzip(raw);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] compressGzip(byte[] data) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
            GZIPOutputStream gzipOS = new GZIPOutputStream(bos);
            gzipOS.write(data);
            gzipOS.close();
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] decompressGzip(byte[] compressedData) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ByteArrayInputStream bis = new ByteArrayInputStream(compressedData);
            GZIPInputStream gzipIS = new GZIPInputStream(bis);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = gzipIS.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            gzipIS.close();
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
