package com.hungrybandits.rest.recipes.services;

import com.hungrybandits.rest.clients.auth.AuthClient;
import com.hungrybandits.rest.exceptions.ApiOperationException;
import com.hungrybandits.rest.recipes.models.entities.Recipe;
import com.hungrybandits.rest.recipes.repositories.RecipeRepository;
import com.hungrybandits.rest.recipes.services.dtos.entities.RecipeDto;
import com.hungrybandits.rest.recipes.services.dtos.mappers.RecipeEditMapper;
import com.hungrybandits.rest.recipes.services.dtos.mappers.RecipeMapper;
import com.hungrybandits.rest.recipes.services.util.RecipeServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeCRUDServices {
    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;
    private final RecipeEditMapper recipeEditMapper;
    private final RecipeServiceUtil recipeServiceUtil;

    @Autowired
    public RecipeCRUDServices(RecipeRepository recipeRepository, RecipeMapper recipeMapper, RecipeEditMapper recipeEditMapper,
                              RecipeServiceUtil recipeServiceUtil) {
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
        this.recipeEditMapper = recipeEditMapper;
        this.recipeServiceUtil = recipeServiceUtil;
    }

    @Transactional
    public Long addRecipe(RecipeDto recipeDto) {
        Optional.ofNullable(recipeDto).orElseThrow(()-> new ApiOperationException("Wrong format"));
        recipeServiceUtil.saveUnavailableIngredients(recipeDto);
        Recipe recipe = recipeMapper.toRecipe(recipeDto);

        Recipe newRecipe = recipeRepository.save(recipe);
        return newRecipe.getId();
    }

    @Transactional
    public void modifyRecipe(RecipeDto recipeDto) {
        Recipe recipe = recipeRepository.findById(recipeDto.getId()).orElseThrow(() -> new ApiOperationException("Recipe is not present"));
        recipeServiceUtil.saveUnavailableIngredients(recipeDto);
        recipeServiceUtil.addOrRemoveRecipeIngredients(recipeDto, recipe);
        recipeEditMapper.updateRecipe(recipeDto, recipe);
    }

    public RecipeDto getRecipeWithId(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new ApiOperationException("Recipe is not present"));
        return recipeMapper.toRecipeDto(recipe);
    }

    public List<RecipeDto> getAllRecipes(){
        List<Recipe> recipesList =  recipeRepository.findAll();
        return recipeMapper.toRecipeDtoList(recipesList);
    }

    public void deleteRecipe(Long recipeId) {
        recipeRepository.findById(recipeId).orElseThrow(() -> new ApiOperationException("Recipe is not present"));
        recipeRepository.deleteById(recipeId);
    }

}
