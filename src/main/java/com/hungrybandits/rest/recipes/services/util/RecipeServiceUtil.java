package com.hungrybandits.rest.recipes.services.util;

import com.hungrybandits.rest.recipes.models.entities.Ingredient;
import com.hungrybandits.rest.recipes.models.entities.Recipe;
import com.hungrybandits.rest.recipes.models.entities.RecipeIngredient;
import com.hungrybandits.rest.recipes.repositories.IngredientRepository;
import com.hungrybandits.rest.recipes.repositories.RecipeIngredientRepository;
import com.hungrybandits.rest.recipes.services.dtos.entities.IngredientDto;
import com.hungrybandits.rest.recipes.services.dtos.entities.RecipeDto;
import com.hungrybandits.rest.recipes.services.dtos.entities.RecipeIngredientDto;
import com.hungrybandits.rest.recipes.services.dtos.mappers.IngredientMapper;
import com.hungrybandits.rest.recipes.services.dtos.mappers.RecipeIngredientMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class RecipeServiceUtil {
    private final IngredientRepository ingredientRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final IngredientMapper ingredientMapper;
    private final RecipeIngredientMapper recipeIngredientMapper;

    @Autowired
    public RecipeServiceUtil(IngredientRepository ingredientRepository, IngredientMapper ingredientMapper, RecipeIngredientMapper recipeIngredientMapper, RecipeIngredientRepository recipeIngredientRepository) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientMapper = ingredientMapper;
        this.recipeIngredientMapper = recipeIngredientMapper;
        this.recipeIngredientRepository = recipeIngredientRepository;
    }

    public void saveUnavailableIngredients(RecipeDto recipeDto) {
        //This method persists those ingredients that don't already exist in DB, but were part of users input.
        //Then user input is updated with persisted ingredients.

        List<Ingredient> ingredientsToBePersisted;
        List<IngredientDto> ingredientsToBePersistedDtos;

        recipeDto.getRecipeIngredients().forEach(recipeIngredientDto -> {
            IngredientDto ingredientDto = recipeIngredientDto.getIngredient();
            ingredientDto.setName(ingredientDto.getName().toUpperCase());

        });

        //Newly added ingredients
        List<IngredientDto> newlyAddedIngredientsInInput = recipeDto.getRecipeIngredients().stream().map(RecipeIngredientDto::getIngredient).filter(ingredient ->
                ingredient.getId() == null).collect(Collectors.toCollection(ArrayList::new));

        //Segregate already persisted ingredients to a map
        Map<String, Ingredient> newlyAddedIngredientsAvailInDb = ingredientRepository.findIngredientsByNameIn(
                newlyAddedIngredientsInInput.stream()
                //Map ingredient to corresponding to its name
                .map(IngredientDto::getName).collect(Collectors.toList())
                )
                .stream().collect(Collectors.toMap(Ingredient::getName, ingredient -> ingredient ));

        //Populate id fields for ingredients that are already persisted. Otherwise transient object error.
        newlyAddedIngredientsInInput.forEach(ingredientDto -> {
            String ingredientNameToSearch = ingredientDto.getName();
            if(newlyAddedIngredientsAvailInDb.containsKey(ingredientNameToSearch)) {
                ingredientDto.setId(newlyAddedIngredientsAvailInDb.get(ingredientNameToSearch).getId());
            }
        });

        //Now find actual ingredients that have not been persisted
        ingredientsToBePersistedDtos = newlyAddedIngredientsInInput.stream().filter(ingredientDto -> ingredientDto.getId() == null).collect(Collectors.toList());

        ingredientsToBePersisted = ingredientMapper.toIngredientList(ingredientsToBePersistedDtos);

        //Save them
        ingredientRepository.saveAll(ingredientsToBePersisted);

        //populate ids
        IntStream.range(0, ingredientsToBePersisted.size())
                .forEach(i -> ingredientsToBePersistedDtos.get(i).setId(ingredientsToBePersisted.get(i).getId()));
    }

    public void addOrRemoveRecipeIngredients(RecipeDto updatedRecipeDto, Recipe recipeFromDb) {

        // This method is called to update recipe such that any new ingredients are added to ingredient list
        //and any old ingredients which were deleted are removed from the ingredient list of the recipe

        List<RecipeIngredient> existingRecipeIngredientList = recipeFromDb.getRecipeIngredients();
        List<RecipeIngredientDto> updatedRecipeIngredientList = updatedRecipeDto.getRecipeIngredients();

        //Find out newly added ingredients
        List<RecipeIngredient> newlyAddedIngredients = updatedRecipeIngredientList.stream()
                .filter(recipeIngredientDto -> recipeIngredientDto.getId() == null)
                .map(recipeIngredientDto -> {
                    RecipeIngredient recipeIngredient = recipeIngredientMapper.toRecipeIngredient(recipeIngredientDto);
                    recipeIngredient.setRecipe(recipeFromDb);
                    return recipeIngredient;
                }).collect(Collectors.toList());


        //All ingredients in updated recipe except newly added ones
        Set<Long> nonNewRecipeIngredientsInUpdatedRecipe = updatedRecipeIngredientList.stream()
                .map(RecipeIngredientDto::getId)
                .filter(Objects::nonNull).collect(Collectors.toSet());

        //All ingredients that were removed from recipe in the update
        List<RecipeIngredient> removedIngredients = existingRecipeIngredientList.stream()
                .filter(recipeIngredient -> !nonNewRecipeIngredientsInUpdatedRecipe.contains(recipeIngredient.getId()))
                .collect(Collectors.toList());

        recipeIngredientRepository.deleteAllInBatch(removedIngredients);
        recipeIngredientRepository.saveAll(newlyAddedIngredients);

        existingRecipeIngredientList.removeAll(removedIngredients);
        existingRecipeIngredientList.addAll(newlyAddedIngredients);
    }

}
