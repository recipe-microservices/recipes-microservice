package com.hungrybandits.rest.recipes.controllers;

import com.hungrybandits.rest.clients.UserProxyDto;
import com.hungrybandits.rest.recipes.services.IngredientService;
import com.hungrybandits.rest.recipes.services.RecipeCRUDServices;
import com.hungrybandits.rest.recipes.services.UserService;
import com.hungrybandits.rest.recipes.services.dtos.entities.IngredientDto;
import com.hungrybandits.rest.recipes.services.dtos.entities.RecipeDto;
import com.hungrybandits.rest.recipes.services.dtos.entities.responses.ApiMessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${app.uri.prefix}")
@RolesAllowed("ROLE_USER")
public class RecipesController {

    private final RecipeCRUDServices recipeCRUDServices;
    private final IngredientService ingredientService;
    private final UserService userService;

    @Autowired
    public RecipesController(RecipeCRUDServices recipeCRUDServices, IngredientService ingredientService, UserService userService) {
        this.recipeCRUDServices = recipeCRUDServices;
        this.ingredientService = ingredientService;
        this.userService = userService;
    }

    @PostMapping("recipe")
    public ResponseEntity<ApiMessageResponse> addRecipe(@RequestBody RecipeDto recipeDto, HttpServletRequest request){
        Long generatedId = recipeCRUDServices.addRecipe(recipeDto);
        return ResponseEntity.ok(ApiMessageResponse.defaultCreationSuccessResponse(generatedId));
    }

    @PutMapping("recipe")
    public ResponseEntity<ApiMessageResponse> updateRecipe(@RequestBody RecipeDto recipeDto){
        recipeCRUDServices.modifyRecipe(recipeDto);
        return ResponseEntity.ok(ApiMessageResponse.defaultSuccessResponse());
    }

    @DeleteMapping("recipe/{recipeId}")
    public ResponseEntity<ApiMessageResponse> deleteRecipe(@PathVariable("recipeId") Long recipeId){
        recipeCRUDServices.deleteRecipe(recipeId);
        return ResponseEntity.ok(ApiMessageResponse.defaultSuccessResponse());
    }

    @GetMapping("recipes")
    public ResponseEntity<List<RecipeDto>> getRecipes(){
        List<RecipeDto> recipeDtoList = recipeCRUDServices.getAllRecipes();
        List<RecipeDto> modifiedRecipeDtoList = recipeDtoList.stream()
            .peek(recipeDto -> recipeDto.setUser(userService.getUser(recipeDto.getUser().getId()))).collect(Collectors.toList());
        return ResponseEntity.ok().body(modifiedRecipeDtoList);
    }

    @GetMapping("recipe/{recipeId}")
    public ResponseEntity<RecipeDto> getRecipe(@PathVariable("recipeId") Long recipeId){
        RecipeDto recipeDto = recipeCRUDServices.getRecipeWithId(recipeId);
        UserProxyDto userProxyDto = userService.getUser(recipeDto.getUser().getId());
        recipeDto.setUser(userProxyDto);
        return ResponseEntity.ok().body(recipeDto);
    }

    @GetMapping("ingredients")
    public ResponseEntity<List<IngredientDto>> getIngredientsStartingWith(@RequestParam(name="startsWith") String startsWith) {
        return ResponseEntity.ok().body(ingredientService.getAllIngredientsStartingWith(startsWith));
    }

    @GetMapping("ingredients/all")
    public ResponseEntity<List<IngredientDto>> getAllIngredients() {
        return ResponseEntity.ok().body(ingredientService.getIngredients());
    }

}
