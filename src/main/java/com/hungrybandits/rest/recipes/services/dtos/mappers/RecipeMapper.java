package com.hungrybandits.rest.recipes.services.dtos.mappers;

import com.hungrybandits.rest.recipes.models.entities.Recipe;
import com.hungrybandits.rest.recipes.services.dtos.entities.RecipeDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {RecipeIngredientMapper.class})
public abstract class RecipeMapper {
    public abstract Recipe toRecipe(RecipeDto recipeDto);
    public abstract List<RecipeDto> toRecipeDtoList(List<Recipe> recipes);
    @Mapping(target = "userId", ignore = true)
    public abstract RecipeDto toRecipeDto(Recipe recipe);

    @AfterMapping
    protected void afterToRecipeDto(RecipeDto recipeDto , @MappingTarget Recipe recipe){
        if(recipeDto != null){
            recipe.setUserId(recipeDto.getUser().getId());
        }
    }
}
