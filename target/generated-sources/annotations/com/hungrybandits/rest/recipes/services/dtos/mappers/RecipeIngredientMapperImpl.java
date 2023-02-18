package com.hungrybandits.rest.recipes.services.dtos.mappers;

import com.hungrybandits.rest.recipes.models.entities.Ingredient;
import com.hungrybandits.rest.recipes.models.entities.RecipeIngredient;
import com.hungrybandits.rest.recipes.services.dtos.entities.RecipeIngredientDto;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-02-19T00:10:01+0530",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.33.0.v20221215-1352, environment: Java 17.0.5 (Eclipse Adoptium)"
)
@Component
public class RecipeIngredientMapperImpl extends RecipeIngredientMapper {

    @Autowired
    private IngredientMapper ingredientMapper;

    @Override
    public RecipeIngredient toRecipeIngredient(RecipeIngredientDto recipeIngredientDto) {
        if ( recipeIngredientDto == null ) {
            return null;
        }

        RecipeIngredient recipeIngredient = new RecipeIngredient();

        recipeIngredient.setId( recipeIngredientDto.getId() );
        recipeIngredient.setIngredient( ingredientMapper.toIngredient( recipeIngredientDto.getIngredient() ) );
        recipeIngredient.setQuantity( recipeIngredientDto.getQuantity() );
        recipeIngredient.setUom( recipeIngredientDto.getUom() );

        return recipeIngredient;
    }

    @Override
    public RecipeIngredientDto toRecipeIngredientDto(RecipeIngredient recipeIngredient) {
        if ( recipeIngredient == null ) {
            return null;
        }

        RecipeIngredientDto recipeIngredientDto = new RecipeIngredientDto();

        recipeIngredientDto.setId( recipeIngredient.getId() );
        recipeIngredientDto.setIngredient( ingredientMapper.toIngredientDto( recipeIngredient.getIngredient() ) );
        recipeIngredientDto.setQuantity( recipeIngredient.getQuantity() );
        recipeIngredientDto.setUom( recipeIngredient.getUom() );

        return recipeIngredientDto;
    }

    @Override
    public void updateRecipeIngredient(RecipeIngredientDto request, RecipeIngredient recipeIngredient) {
        if ( request == null ) {
            return;
        }

        if ( request.getId() != null ) {
            recipeIngredient.setId( request.getId() );
        }
        if ( request.getIngredient() != null ) {
            if ( recipeIngredient.getIngredient() == null ) {
                recipeIngredient.setIngredient( new Ingredient() );
            }
            ingredientMapper.editIngredient( request.getIngredient(), recipeIngredient.getIngredient() );
        }
        if ( request.getQuantity() != null ) {
            recipeIngredient.setQuantity( request.getQuantity() );
        }
        if ( request.getUom() != null ) {
            recipeIngredient.setUom( request.getUom() );
        }
    }
}
