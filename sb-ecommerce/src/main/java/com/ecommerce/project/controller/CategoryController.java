package com.ecommerce.project.controller;

import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    public CategoryController() {
        System.out.println("CategoryController Constructor Called");
    }

//    @GetMapping("/public/categories")
    @RequestMapping(value = "/public/categories", method = RequestMethod.GET)
    public ResponseEntity<CategoryResponse>
    getAllCategories(@RequestParam(required = false, defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber ,
                     @RequestParam(required = false, defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                     @RequestParam(required = false, defaultValue = AppConstants.SORT_CATEGORY_BY) String sortBy,
                     @RequestParam(required = false, defaultValue = AppConstants.SORT_ORDER) String sortOrder
    )

    {
        CategoryResponse categoryResponse = categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    @PostMapping("/admin/categories")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO){
        CategoryDTO categoryDTOSaved = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(categoryDTOSaved, HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId){
           CategoryDTO deletedCategroyDTO = categoryService.deleteCategory(categoryId);
           return new ResponseEntity<>(deletedCategroyDTO, HttpStatus.OK);
    }

    @PutMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO category,
                                                 @PathVariable Long categoryId){
            CategoryDTO savedCategoryDTO = categoryService.updateCategory(category, categoryId);
            return new ResponseEntity<>(savedCategoryDTO, HttpStatus.OK);
    }
}

/*
return new ResponseEntity<>(HttpbodyContent, HttpStatus.OK);
return ResponseEntity.ok(HttpbodyContent);
return ResponseEntity.status(HttpStatus.Ok).body(HttpbodyContent);
return ResponseEntity.ok().build(); //When only status code to send with NO-Body.
*/