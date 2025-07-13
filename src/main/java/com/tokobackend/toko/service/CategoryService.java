package com.tokobackend.toko.service;

import com.tokobackend.toko.model.Category;
import com.tokobackend.toko.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Long id){
        return categoryRepository.findById(id);
    }

    public Category saveCategory(Category category){
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id){
        categoryRepository.deleteById(id);
    }

    public Category updateCategory(Long id, Category categoriesDetails){
        return categoryRepository.findById(id).map(category -> {
            category.setName(categoriesDetails.getName());
            category.setDescription(categoriesDetails.getDescription());
            category.setParentCategory(categoriesDetails.getParentCategory());
            return categoryRepository.save(category);
        }).orElseThrow(() -> new RuntimeException("Category tidak ditemukan dengan id "+id));
    }

    public List<Category> getRootCategories(){
        return categoryRepository.findByParentCategoryIsNull();
    }

    public List<Category> getSubCategoriesParentId(Long perentId){
        return categoryRepository.findByParentCategoryId(perentId);
    }


}
