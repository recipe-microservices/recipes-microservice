package com.hungrybandits.rest.recipes.repositories;

import com.hungrybandits.rest.recipes.models.entities.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    @Query("select ingredient from Ingredient ingredient where upper(ingredient.name) like upper(concat(:name,'%'))")
    List<Ingredient> findIngredientsStartingWith(String name);

    Optional<Ingredient> findByName(String name);

    boolean existsIngredientByName(String name);

    //List<Ingredient> findIngredientsByNameIn(List<String> names);

    List<Ingredient> findIngredientsByNameIn(List<String> names);
}
