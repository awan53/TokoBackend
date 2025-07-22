package com.tokobackend.toko.controller;

import com.tokobackend.toko.model.Category;
import com.tokobackend.toko.payload.request.CategoryRequest;
import com.tokobackend.toko.payload.response.MessageRespone;
import com.tokobackend.toko.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;




@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MODERATOR')")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MODERATOR')")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id).map(category -> new ResponseEntity<>(category, HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryRequest categoryRequest, BindingResult bindingReult) {
        if(bindingReult.hasErrors()){
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingReult.getFieldErrors()){
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);

        }
        try {
            Category savedCategory = categoryService.createCategory(categoryRequest);
            return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
        } catch (RuntimeException e){
            System.err.println("Error Creating Category : "+e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(new MessageRespone(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody CategoryRequest categoryDetailsRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()){
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        try {
            Category updateCategory = categoryService.updateCategory(id, categoryDetailsRequest);
            return new ResponseEntity<>(updateCategory, HttpStatus.OK);

        }catch (RuntimeException e){

            System.err.println("Error updateing Category :"+e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(new MessageRespone(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        try{
            categoryService.deleteCategory(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (RuntimeException e)
        {
            System.err.println("Error Deleting Category : "+ e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/root")
    @PreAuthorize("hasAnyRole('USER','ADMIN', 'MODERATOR')")
    public ResponseEntity<List<Category>> getRootCategories() {
        List<Category> categories = categoryService.getRootCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/parent/{parentId}/subcategories")
    public ResponseEntity<List<Category>> getSubCategoriesByParentId(@PathVariable Long parentId) {
        List<Category> categories = categoryService.getSubCategoriesParentId(parentId);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
}
