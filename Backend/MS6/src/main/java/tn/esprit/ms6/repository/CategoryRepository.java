package tn.esprit.ms6.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.ms6.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByNameIgnoreCase(String name);

    /** Sert à la modification : le nom existe-t-il pour une AUTRE catégorie ? */
    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
}