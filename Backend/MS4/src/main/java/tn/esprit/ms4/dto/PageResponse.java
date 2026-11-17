package tn.esprit.ms4.dto;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

/** Reponse paginee stable (independante de la serialisation interne de Spring Data). */
public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
    public static <E, T> PageResponse<T> of(Page<E> page, Function<E, T> mapper) {
        return new PageResponse<>(
                page.getContent().stream().map(mapper).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages());
    }
}
