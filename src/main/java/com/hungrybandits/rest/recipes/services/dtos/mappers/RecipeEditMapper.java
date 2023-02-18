package com.hungrybandits.rest.recipes.services.dtos.mappers;

import com.hungrybandits.rest.recipes.models.entities.Recipe;
import com.hungrybandits.rest.recipes.services.dtos.entities.RecipeDto;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring")
public abstract class RecipeEditMapper {

    private RecipeIngredientMapper recipeIngredientMapper;

    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "recipeIngredients", ignore = true)
    public abstract void updateRecipe(RecipeDto request, @MappingTarget Recipe recipe);

    @AfterMapping
    public void afterUpdateRecipe(RecipeDto request, @MappingTarget Recipe recipe){

        recipeIngredientMapper.updatedRecipeIngredientList(request.getRecipeIngredients(), recipe.getRecipeIngredients(), recipe);

    }

    public RecipeIngredientMapper getRecipeIngredientMapper() {
        return recipeIngredientMapper;
    }

    @Autowired
    public void setRecipeIngredientMapper(RecipeIngredientMapper recipeIngredientMapper) {
        this.recipeIngredientMapper = recipeIngredientMapper;
    }
}
