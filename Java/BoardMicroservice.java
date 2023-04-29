package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/board")
public class BoardMicroservice {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @GetMapping("/{id}")
    public Board getBoardById(@PathVariable("id") Long id) {
        return boardRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Board createBoard(@RequestBody Board board) {
        return boardRepository.save(board);
    }
    
    @PostMapping("/property")
    public Property createProperty(@RequestBody Property property) {
        return propertyRepository.save(property);
    }

    @GetMapping("/property/{id}")
    public Property getPropertyById(@PathVariable("id") Long id) {
        Property propertyEntity = propertyRepository.findById(id).orElse(null);
        if (propertyEntity != null) {
            String color = propertyEntity.getColor();
            int cost = propertyEntity.getCost();
            int rent = propertyEntity.getRent();
            int houseCost = propertyEntity.getHouseCost();
            int hotelCost = propertyEntity.getHotelCost();
            int mortgageValue = propertyEntity.getMortgageValue();
            return new Property(id, color, cost, rent, houseCost, hotelCost, mortgageValue);
        } else {
            return null;
        }
    }

}
