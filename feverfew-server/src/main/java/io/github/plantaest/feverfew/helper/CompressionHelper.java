package io.github.plantaest.feverfew.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.github.plantaest.feverfew.config.jackson.ObjectMappers;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@ApplicationScoped
public class CompressionHelper {

    @Inject
    ObjectMappers objectMappers;

    public List<EvaluationResultSchemaV1> convertToSchema(List<EvaluationResult> evaluationResults) {
        return objectMappers.getNonUseAnnotationsMapper()
                .convertValue(evaluationResults, new TypeReference<>() {});
    }

    public List<EvaluationResult> schemaToTarget(List<EvaluationResultSchemaV1> evaluationResultSchemaV1s) {
        return objectMappers.getNonUseAnnotationsMapper()
                .convertValue(evaluationResultSchemaV1s, new TypeReference<>() {});
    }

    public byte[] compressJson(Object object) {
        try {
            byte[] raw = objectMappers.getNumericBooleanMapper().writeValueAsBytes(object);
            return compressGzip(raw);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T decompressJson(byte[] compressedData, TypeReference<T> typeReference) {
        try {
            byte[] decompressed = decompressGzip(compressedData);
            return objectMappers.getNumericBooleanMapper().readValue(decompressed, typeReference);
        } catch (IOException e) {
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
        } catch (IOException e) {
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
