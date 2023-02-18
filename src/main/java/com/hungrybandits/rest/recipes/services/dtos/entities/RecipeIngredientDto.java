package com.hungrybandits.rest.recipes.services.dtos.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hungrybandits.rest.recipes.enums.UnitOfMeasurement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecipeIngredientDto {
    private Long id;
    private IngredientDto ingredient;
    private Double quantity;
    private UnitOfMeasurement uom;
}
