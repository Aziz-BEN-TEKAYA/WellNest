package tn.esprit.ms2.service;

import tn.esprit.ms2.dto.CategoryRequest;
import tn.esprit.ms2.dto.CategoryResponse;

/** Creation, modification et suppression des categories. */
public interface CategoryCommandService {

    CategoryResponse create(CategoryRequest request);

    CategoryResponse update(Long id, CategoryRequest request);

    void delete(Long id);
}
