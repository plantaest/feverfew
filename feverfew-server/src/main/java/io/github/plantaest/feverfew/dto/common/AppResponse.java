package io.github.plantaest.feverfew.dto.common;

import jakarta.ws.rs.core.Response;

public record AppResponse<T>(
        int status,
        T data
) {

    public static <T> AppResponse<T> ok(T data) {
        return new AppResponse<>(Response.Status.OK.getStatusCode(), data);
    }

    public static <T> AppResponse<T> created(T data) {
        return new AppResponse<>(Response.Status.CREATED.getStatusCode(), data);
    }

    public static <T> AppResponse<T> accepted(T data) {
        return new AppResponse<>(Response.Status.ACCEPTED.getStatusCode(), data);
    }

}
