package com.hungrybandits.rest.recipes.repositories;

import com.hungrybandits.rest.recipes.models.entities.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Override
    @Query("select distinct r as recipe from Recipe r join fetch r.recipeIngredients ri join fetch ri.ingredient join fetch r.user where r.id=:id")
    Optional<Recipe> findById(Long id);

    @Override
    @Query("select distinct r from Recipe r join fetch r.recipeIngredients ri join fetch ri.ingredient join fetch r.user")
    List<Recipe> findAll();
}
