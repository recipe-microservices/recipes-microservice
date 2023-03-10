package com.hungrybandits.rest.recipes.services;

import com.hungrybandits.rest.clients.UserProxyDTO;
import com.hungrybandits.rest.exceptions.ApiAccessException;
import com.hungrybandits.rest.exceptions.ApiOperationException;
import com.hungrybandits.rest.recipes.models.entities.Recipe;
import com.hungrybandits.rest.recipes.repositories.RecipeRepository;
import com.hungrybandits.rest.recipes.security.SecurityUtils;
import com.hungrybandits.rest.recipes.services.dtos.entities.RecipeDto;
import com.hungrybandits.rest.recipes.services.dtos.entities.UserProxyDto;
import com.hungrybandits.rest.recipes.services.dtos.mappers.RecipeEditMapper;
import com.hungrybandits.rest.recipes.services.dtos.mappers.RecipeMapper;
import com.hungrybandits.rest.recipes.services.util.RecipeServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeCRUDServices {
    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;
    private final RecipeEditMapper recipeEditMapper;
    private final RecipeServiceUtil recipeServiceUtil;

    private final UserService userService;

    @Autowired
    public RecipeCRUDServices(RecipeRepository recipeRepository, RecipeMapper recipeMapper, RecipeEditMapper recipeEditMapper,
                              RecipeServiceUtil recipeServiceUtil, UserService userService) {
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
        this.recipeEditMapper = recipeEditMapper;
        this.recipeServiceUtil = recipeServiceUtil;
        this.userService = userService;
    }

    @Transactional
    public Long addRecipe(RecipeDto recipeDto) {
        Optional.ofNullable(recipeDto).orElseThrow(()-> new ApiOperationException("Wrong format"));
        recipeServiceUtil.saveUnavailableIngredients(recipeDto);
        UserProxyDTO user = SecurityUtils.getUserFromSession();
        recipeDto.setUser(user);
        Recipe recipe = recipeMapper.toRecipe(recipeDto);
        Recipe newRecipe = recipeRepository.save(recipe);
        return newRecipe.getId();
    }

    @Transactional
    public void modifyRecipe(RecipeDto recipeDto) {
        UserProxyDTO user = SecurityUtils.getUserFromSession();
        Recipe recipe = recipeRepository.findById(recipeDto.getId()).orElseThrow(() -> new ApiOperationException("Recipe is not present"));
        if (recipe.getUserId().compareTo(user.getId()) != 0) {
            throw new ApiAccessException("You are not authenticated to modify this item");
        }
        recipeServiceUtil.saveUnavailableIngredients(recipeDto);
        recipeServiceUtil.addOrRemoveRecipeIngredients(recipeDto, recipe);
        recipeEditMapper.updateRecipe(recipeDto, recipe);
    }

    public RecipeDto getRecipeWithId(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new ApiOperationException("Recipe is not present"));
        UserProxyDTO userProxyDTO = userService.getUser(recipe.getUserId());
        RecipeDto recipeDto = recipeMapper.toRecipeDto(recipe);
        recipeDto.setUser(userProxyDTO);
        return recipeDto;
    }

    public List<RecipeDto> getAllRecipes(){
        List<Recipe> recipesList =  recipeRepository.findAll();
        List<RecipeDto> recipeDtoList = recipeMapper.toRecipeDtoList(recipesList);
        return recipeDtoList.stream()
                .peek(recipeDto -> recipeDto.setUser(userService.getUser(recipeDto.getUser().getId()))).collect(Collectors.toList());
    }

    public void deleteRecipe(Long recipeId) {
        recipeRepository.findById(recipeId).orElseThrow(() -> new ApiOperationException("Recipe is not present"));
        recipeRepository.deleteById(recipeId);
    }

}
