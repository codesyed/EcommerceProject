package com.ecommerce.project.service;

import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ModelMapper modelMapper; //FOR DTO<->POJO/Entity/Model

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortObj = null;
        if(sortOrder.equalsIgnoreCase("asc"))
            sortObj = Sort.by(sortBy).ascending();
        else sortObj = Sort.by(sortBy).descending();

        Pageable pgbObject = PageRequest.of(pageNumber, pageSize, sortObj); // .of() is factory method
        Page<Category> categoryPage = categoryRepository.findAll(pgbObject); //Pass obj of Pageable
        List<Category> categories = categoryPage.getContent(); //This contains only specific page Data with pageSize

        if(categories.isEmpty())throw new APIException("No Category Exists!!!");

        //From DB -> MODEL -> POJO <-> To convert each Category to CategoryDTO
        List<CategoryDTO> dtoList = categories.stream()
                .map(obj-> modelMapper.map(obj, CategoryDTO.class))
                .collect(Collectors.toList());

        CategoryResponse categoryResponse = new CategoryResponse();
        //Setting other Properties - Meta data for page//
        categoryResponse.setContent(dtoList);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());
        return categoryResponse;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category categoryModel = modelMapper.map(categoryDTO, Category.class);
        boolean exist = categoryRepository.existsByCategoryName(categoryModel.getCategoryName());
        if(exist)throw new APIException("Category already exist with Name: "+categoryModel.getCategoryName()+"!!!");

        Category savedCat = categoryRepository.save(categoryModel);
        return modelMapper.map(savedCat, CategoryDTO.class);
    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        Category category =
                categoryRepository
                .findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category", "CategoryId", categoryId));

        categoryRepository.delete(category);
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO newCategory, Long categoryId) {
          Optional<Category> optionalCategory =
                        categoryRepository
                        .findById(categoryId);

          if(optionalCategory.isPresent()){
              Category oldCategory =  optionalCategory.get();
              //Making changes to the same object/reference inside the List
              oldCategory.setCategoryName(newCategory.getCategoryName());
              Category newOne = categoryRepository.save(oldCategory);
              return modelMapper.map(newOne, CategoryDTO.class);
          }
          else{
              throw new ResourceNotFoundException("Category", "CategoryId", categoryId);
          }
    }
}
