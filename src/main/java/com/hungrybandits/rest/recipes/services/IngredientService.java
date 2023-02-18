package com.hungrybandits.rest.recipes.services;

import com.hungrybandits.rest.exceptions.ApiOperationException;
import com.hungrybandits.rest.recipes.models.entities.Ingredient;
import com.hungrybandits.rest.recipes.repositories.IngredientRepository;
import com.hungrybandits.rest.recipes.services.dtos.entities.IngredientDto;
import com.hungrybandits.rest.recipes.services.dtos.mappers.IngredientMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IngredientService {
    private final IngredientRepository ingredientRepository;
    private final IngredientMapper ingredientMapper;

    @Autowired
    public IngredientService(IngredientRepository ingredientRepository, IngredientMapper ingredientMapper) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientMapper = ingredientMapper;
    }

    public void addListOfIngredients(List<IngredientDto> ingredientDtoList){
        Optional.ofNullable(ingredientDtoList).orElseThrow(() -> new ApiOperationException("empty list"));
        List<Ingredient> ingredientList= ingredientMapper.toIngredientList(ingredientDtoList);
        ingredientRepository.saveAll(ingredientList);
    }

    public List<IngredientDto> getIngredients() {
        List<Ingredient> ingredientList = ingredientRepository.findAll();
        return ingredientMapper.toIngredientDtoList(ingredientList);
    }

    public void updateIngredient(IngredientDto ingredientDto) {
        Ingredient ingredient = ingredientRepository.findById(ingredientDto.getId()).orElseThrow(() -> new ApiOperationException("No such ingredient"));
        ingredientMapper.editIngredient(ingredientDto, ingredient);
    }

    public List<IngredientDto> getAllIngredientsStartingWith(String ingredientName) {
        List<Ingredient> ingredientList = ingredientRepository.findIngredientsStartingWith(ingredientName.strip());
        return ingredientMapper.toIngredientDtoList(ingredientList);
    }
}
