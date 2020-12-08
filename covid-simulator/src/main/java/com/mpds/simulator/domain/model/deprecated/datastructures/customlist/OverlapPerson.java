package com.mpds.simulator.domain.model.deprecated.datastructures.customlist;

import com.mpds.simulator.domain.model.Coordinate;
import com.mpds.simulator.domain.model.Person;
import lombok.Data;

@Data
public class OverlapPerson{

    Person person;
    private Coordinate originalPos;
    private boolean originalInfected;
    private boolean newlyInfected;

    public OverlapPerson(Person person, boolean originalInfected) {
        originalPos = new Coordinate(person.getPos().getRow(), person.getPos().getCol());
        this.originalInfected = originalInfected;
    }

    public boolean getOriginalInfected(){
        return originalInfected;
    }

    public void setNewlyInfected(){
        newlyInfected = true;
    }
}
