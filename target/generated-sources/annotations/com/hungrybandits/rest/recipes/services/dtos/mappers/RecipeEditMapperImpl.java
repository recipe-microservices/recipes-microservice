package com.hungrybandits.rest.recipes.services.dtos.mappers;

import com.hungrybandits.rest.recipes.models.entities.Recipe;
import com.hungrybandits.rest.recipes.services.dtos.entities.RecipeDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-02-19T00:10:00+0530",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.33.0.v20221215-1352, environment: Java 17.0.5 (Eclipse Adoptium)"
)
@Component
public class RecipeEditMapperImpl extends RecipeEditMapper {

    @Override
    public void updateRecipe(RecipeDto request, Recipe recipe) {
        if ( request == null ) {
            return;
        }

        if ( request.getCookingInstructions() != null ) {
            recipe.setCookingInstructions( request.getCookingInstructions() );
        }
        if ( request.getDescription() != null ) {
            recipe.setDescription( request.getDescription() );
        }
        if ( request.getId() != null ) {
            recipe.setId( request.getId() );
        }
        if ( request.getItemType() != null ) {
            recipe.setItemType( request.getItemType() );
        }
        if ( request.getName() != null ) {
            recipe.setName( request.getName() );
        }
        if ( request.getRecipeImageAddress() != null ) {
            recipe.setRecipeImageAddress( request.getRecipeImageAddress() );
        }
        if ( request.getServing() != null ) {
            recipe.setServing( request.getServing() );
        }
        if ( request.getCreatedOn() != null ) {
            recipe.setCreatedOn( request.getCreatedOn() );
        }

        afterUpdateRecipe( request, recipe );
    }
}
