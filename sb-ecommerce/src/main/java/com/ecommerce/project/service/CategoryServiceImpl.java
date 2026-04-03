package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{

    private List<Category>categories = new ArrayList<>();
    private static Long catgId = 101l;

    @Override
    public List<Category> getAllCategories() {
        return categories;
    }

    @Override
    public void createCategory(Category category) {
//        category.setCategoryId(CategoryServiceImpl.catgId++);
        categories.add(category);
    }

    @Override
    public String deleteCategroy(Long categoryId) {
        Category category = categories
                .stream()
                .filter(c->c.getCategoryId()
                .equals(categoryId))
                .findFirst()
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with id "+categoryId+" dosen't found!"));

        categories.remove(category);
        return "Category with Id: "+categoryId+" Deleted Successfully !!";
    }

    @Override
    public Category updateCategory(Category newCategory, Long categoryId) {
          Optional<Category> optionalCategory = categories
                        .stream()
                        .filter(c->c.getCategoryId()
                        .equals(categoryId))
                        .findFirst();
          if(optionalCategory.isPresent()){
              Category oldCategory =  optionalCategory.get();
              //Making changes to the same object/reference inside the List
              oldCategory.setCategoryName(newCategory.getCategoryName());
              return oldCategory;
          }
          else{
              throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with id "+categoryId+" dosen't Exists!");
          }
    }
}
