package com.mpds.simulator.domain.model.datastructures;

import com.mpds.simulator.domain.model.Person;
import lombok.Data;

@Data
public class CustomLinkedList{

    private Person start;
    private Person end;

    public CustomLinkedList(){
        start = end = null;
    }

    public void addPerson(Person person){
        if(start == null){
            end = person;
        }
        person.setNext(start);
        start = person;
    }

    public void addPeople(CustomLinkedList newPeople){
        newPeople.end.setNext(start);
        if(start == null){
            end = newPeople.end;
        }
        start = newPeople.start;
        newPeople.reset();
    }

    public void reset(){
        start = null;
        end = null;
    }
}
