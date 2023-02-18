package com.hungrybandits.rest.recipes.services.dtos.mappers;

import com.hungrybandits.rest.recipes.models.entities.Ingredient;
import com.hungrybandits.rest.recipes.services.dtos.entities.IngredientDto;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-02-19T00:10:01+0530",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.33.0.v20221215-1352, environment: Java 17.0.5 (Eclipse Adoptium)"
)
@Component
public class IngredientMapperImpl extends IngredientMapper {

    @Override
    public Ingredient toIngredient(IngredientDto ingredientDto) {
        if ( ingredientDto == null ) {
            return null;
        }

        Ingredient ingredient = new Ingredient();

        ingredient.setDescription( ingredientDto.getDescription() );
        ingredient.setId( ingredientDto.getId() );
        ingredient.setName( ingredientDto.getName() );

        return ingredient;
    }

    @Override
    public IngredientDto toIngredientDto(Ingredient ingredient) {
        if ( ingredient == null ) {
            return null;
        }

        IngredientDto ingredientDto = new IngredientDto();

        ingredientDto.setDescription( ingredient.getDescription() );
        ingredientDto.setId( ingredient.getId() );
        ingredientDto.setName( ingredient.getName() );

        return ingredientDto;
    }

    @Override
    public List<Ingredient> toIngredientList(List<IngredientDto> ingredientDtoList) {
        if ( ingredientDtoList == null ) {
            return null;
        }

        List<Ingredient> list = new ArrayList<Ingredient>( ingredientDtoList.size() );
        for ( IngredientDto ingredientDto : ingredientDtoList ) {
            list.add( toIngredient( ingredientDto ) );
        }

        return list;
    }

    @Override
    public List<IngredientDto> toIngredientDtoList(List<Ingredient> ingredientList) {
        if ( ingredientList == null ) {
            return null;
        }

        List<IngredientDto> list = new ArrayList<IngredientDto>( ingredientList.size() );
        for ( Ingredient ingredient : ingredientList ) {
            list.add( toIngredientDto( ingredient ) );
        }

        return list;
    }

    @Override
    public void editIngredient(IngredientDto request, Ingredient ingredient) {
        if ( request == null ) {
            return;
        }

        if ( request.getDescription() != null ) {
            ingredient.setDescription( request.getDescription() );
        }
        if ( request.getId() != null ) {
            ingredient.setId( request.getId() );
        }
        if ( request.getName() != null ) {
            ingredient.setName( request.getName() );
        }
    }
}
