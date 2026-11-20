package tn.esprit.ms2.service;

import tn.esprit.ms2.dto.CategoryResponse;

import java.util.List;

/** Consultation des categories (ISP : separee des operations d'ecriture). */
public interface CategoryQueryService {

    List<CategoryResponse> findAll();

    CategoryResponse findById(Long id);
}
