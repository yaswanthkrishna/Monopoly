package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/property")
public class PropertyMicroservice {

    @Autowired
    private PropertyRepository propertyRepository;

    public PropertyRepository getPropertyRepository() {
        return propertyRepository;
    }
}
