package com.hungrybandits.rest.recipes.controllers;

import com.hungrybandits.rest.recipes.annotations.VerifyUser;
import com.hungrybandits.rest.recipes.services.IngredientService;
import com.hungrybandits.rest.recipes.services.RecipeCRUDServices;
import com.hungrybandits.rest.recipes.services.dtos.entities.IngredientDto;
import com.hungrybandits.rest.recipes.services.dtos.entities.RecipeDto;
import com.hungrybandits.rest.recipes.services.dtos.entities.responses.ApiMessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class RecipesController {

    private final RecipeCRUDServices recipeCRUDServices;
    private final IngredientService ingredientService;

    @Autowired
    public RecipesController(RecipeCRUDServices recipeCRUDServices, IngredientService ingredientService) {
        this.recipeCRUDServices = recipeCRUDServices;
        this.ingredientService = ingredientService;
    }

    @PostMapping("recipe")
    @VerifyUser(roles = {"ROLE_USER_CREATE_RESOURCE"})
    public ResponseEntity<ApiMessageResponse> addRecipe(@RequestBody RecipeDto recipeDto, HttpServletRequest request){
        Long generatedId = recipeCRUDServices.addRecipe(recipeDto);
        return ResponseEntity.ok(ApiMessageResponse.defaultCreationSuccessResponse(generatedId));
    }

    @PutMapping("recipe")
    @VerifyUser(roles = {"ROLE_USER_UPDATE_RESOURCE"})
    public ResponseEntity<ApiMessageResponse> updateRecipe(@RequestBody RecipeDto recipeDto, HttpServletRequest request){
        recipeCRUDServices.modifyRecipe(recipeDto);
        return ResponseEntity.ok(ApiMessageResponse.defaultSuccessResponse());
    }

    @DeleteMapping("recipe/{recipeId}")
    @VerifyUser(roles = {"ROLE_USER_DELETE_RESOURCE"})
    public ResponseEntity<ApiMessageResponse> deleteRecipe(@PathVariable("recipeId") Long recipeId){
        recipeCRUDServices.deleteRecipe(recipeId);
        return ResponseEntity.ok(ApiMessageResponse.defaultSuccessResponse());
    }

    @GetMapping("recipes")
    public ResponseEntity<List<RecipeDto>> getRecipes(){
        List<RecipeDto> recipeDtoList = recipeCRUDServices.getAllRecipes();
        return ResponseEntity.ok().body(recipeDtoList);
    }

    @GetMapping("recipe/{recipeId}")
    @VerifyUser(roles = {"ROLE_USER_VIEW_RESOURCE"})
    public ResponseEntity<RecipeDto> getRecipe(@PathVariable("recipeId") Long recipeId){
        RecipeDto recipeDto = recipeCRUDServices.getRecipeWithId(recipeId);
        return ResponseEntity.ok().body(recipeDto);
    }

    @GetMapping("ingredients")
    @VerifyUser(roles = {"ROLE_USER_VIEW_RESOURCE"})
    public ResponseEntity<List<IngredientDto>> getIngredientsStartingWith(@RequestParam(name="startsWith") String startsWith) {
        return ResponseEntity.ok().body(ingredientService.getAllIngredientsStartingWith(startsWith));
    }

    @GetMapping("ingredient/{ingredientId}")
    @VerifyUser(roles = {"ROLE_ADMIN"})
    public ResponseEntity<ApiMessageResponse> deleteIngredientById(@PathVariable("ingredientId") Long ingredientId) {
        ingredientService.deleteIngredient(ingredientId);
        return ResponseEntity.ok().body(ApiMessageResponse.defaultSuccessResponse());
    }

    @PostMapping("ingredient")
    @VerifyUser(roles = {"ROLE_ADMIN"})
    public ResponseEntity<ApiMessageResponse> addIngredientsToDB(@RequestBody List<IngredientDto> ingredientList) {
        ingredientService.addListOfIngredients(ingredientList);
        return ResponseEntity.ok().body(ApiMessageResponse.defaultSuccessResponse());
    }

    @GetMapping("ingredients/all")
    @VerifyUser(roles = {"ROLE_USER_VIEW_RESOURCE"})
    public ResponseEntity<List<IngredientDto>> getAllIngredients() {
        return ResponseEntity.ok().body(ingredientService.getIngredients());
    }

}
