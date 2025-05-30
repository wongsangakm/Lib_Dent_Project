package com.example.project.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.repository.AcademicFieldRepository;
import com.example.project.repository.BookRepository;

@RestController
@RequestMapping("/api/fields")

@CrossOrigin(origins = "https://requestbooks-dentkku.vercel.app")
public class FieldController {

     @Autowired
    private AcademicFieldRepository academicFieldRepository;

    @GetMapping
    public List<Map<String, Object>> getAllFields() {
        return academicFieldRepository.findAll().stream()
            .map(field -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", field.getId());
                map.put("name_th", field.getNameTh());
                return map;
            })
            .collect(Collectors.toList());
    }


}
