package com.ecommerce.project.payload;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private Long categoryId;
    @NotBlank
    @Size(min=5, message="Please keep Category's Name of at-least 5 Letters!")
    private String categoryName;

    //Here, We could have skipped or add data as compared to Actual Model-Category
    //OR What we want to show user or take input from user of Entity Category
}
