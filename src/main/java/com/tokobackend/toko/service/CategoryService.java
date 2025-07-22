package com.tokobackend.toko.service;

import com.tokobackend.toko.model.Category;
import com.tokobackend.toko.repository.CategoryRepository;
import com.tokobackend.toko.payload.request.CategoryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
   public Category createCategory(CategoryRequest categoryRequest){
        if(categoryRepository.findByName(categoryRequest.getName()).isPresent()){
            throw new RuntimeException("Kategori dengan nama '"+categoryRequest.getName()+"'sudah ada.");
        }
        Category category = new Category();
        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());

        return categoryRepository.save(category);
    }

   public List<Category> getAllCategories(){
        return categoryRepository.findAll();
   }

    public Optional<Category> getCategoryById(Long id){
        return categoryRepository.findById(id);
    }

    public void deleteCategory(Long id){
        categoryRepository.deleteById(id);
    }

    @Transactional
    public Category updateCategory(Long id, CategoryRequest categoriesDetailisRequest){
        return categoryRepository.findById(id).map(category -> {category.setName(categoriesDetailisRequest.getName());
        category.setDescription(categoriesDetailisRequest.getDescription());
        category.setUpdatedAt(LocalDateTime.now());

        return categoryRepository.save(category);})
                .orElseThrow(() -> new RuntimeException("Category tidak ditemukan dengan id "+id));
    }

    public List<Category> getRootCategories(){
        return categoryRepository.findByParentCategoryIsNull();
    }

    public List<Category> getSubCategoriesParentId(Long perentId){
        return categoryRepository.findByParentCategoryId(perentId);
    }


}
