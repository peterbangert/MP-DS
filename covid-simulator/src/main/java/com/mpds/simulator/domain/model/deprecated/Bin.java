package com.mpds.simulator.domain.model.deprecated;

import com.mpds.simulator.application.runner.CovidSimulatorRunner;
import com.mpds.simulator.domain.model.Coordinate;
import com.mpds.simulator.domain.model.GridBins;
import com.mpds.simulator.domain.model.Person;
import com.mpds.simulator.domain.model.deprecated.datastructures.BinarySearchLeaf;
import com.mpds.simulator.domain.model.deprecated.datastructures.BinarySearchTree2d;
import com.mpds.simulator.domain.model.events.DomainEvent;
import com.mpds.simulator.domain.model.events.InfectionReported;
import com.mpds.simulator.domain.model.events.PersonHealed;
import com.mpds.simulator.port.adapter.kafka.DomainEventPublisherReactive;
import it.unimi.dsi.util.XorShift1024StarPhiRandom;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

@Data
@Slf4j
public class Bin {

    private Coordinate ulCorner;
    private Coordinate lrCorner;
    private Coordinate overlapCorner;
    private GridBins grid;
    private ArrayList<Person> peopleInBin;
    private ArrayList<Person> peopleInOverlap;
    private ArrayList<Person> toMove;
    private long time;
    private int gridSizeRow;
    private int gridSizeCol;
    private DomainEventPublisherReactive publisher;
    private XorShift1024StarPhiRandom randomGen;

    private ArrayList<Person[]> contacts;

    private BinarySearchTree2d searchTree;
    private BinarySearchLeaf firstLeaf;

    public Bin(Coordinate ulCorner, Coordinate lrCorner, Coordinate overlapSize , GridBins grid, int searchTreeBinSize, DomainEventPublisherReactive publisher){
        this.ulCorner = ulCorner;
        this.lrCorner = lrCorner;
        overlapCorner = this.lrCorner.addCoordinate(overlapSize);
        this.grid = grid;
        gridSizeRow = GridBins.size.getRow();
        gridSizeCol = GridBins.size.getCol();
        peopleInBin = new ArrayList<>();
        peopleInOverlap = new ArrayList<>();
        this.publisher = publisher;
        randomGen = new XorShift1024StarPhiRandom();
        searchTree = new BinarySearchTree2d(true, ulCorner, lrCorner, searchTreeBinSize, null);
        firstLeaf = searchTree.connectLeaves().getLeft();
    }

    public void publishContact(int id1, int id2){
        System.out.println("contact:" + String.valueOf(id1) + " - " + String.valueOf(id2));
        //DomainEvent personContactEvent = new PersonContact(time, (long) id1, (long) id2, LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
        //this.grid.getDomainEventPublisher().sendMessages(personContactEvent).subscribe();
    }

    public void publishInfection(int id){
        //log.info("infection:" + infectedPerson.getId() + " - " + healthyPerson.getId());
        DomainEvent domainEvent = new InfectionReported(time, (long) id, CovidSimulatorRunner.city, LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
        //this.grid.getDomainEventPublisher().sendMessages(domainEvent).subscribe();
    }

    public void publishHealed(int id){
        log.info("Person healed: " + id);
        System.out.println("healed: " + id);
        DomainEvent domainEvent = new PersonHealed(time, (long) id, CovidSimulatorRunner.city, LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
        //this.grid.getDomainEventPublisher().sendMessages(domainEvent).subscribe();
    }

    // Check if the infected person is within distance
    private void checkInfection(Person infectedPerson, Person healthyPerson, int distance) {
        if(GridBins.randomGen.nextInt(101) > distance + 1){
            healthyPerson.setInfected(GridBins.infectionTime+1);
            //log.info("infection:" + infectedPerson.getId() + " - " + healthyPerson.getId());
            //DomainEvent domainEvent = new InfectionReported(time, (long) healthyPerson.getId(), LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
            //this.grid.getDomainEventPublisher().sendMessages(domainEvent).subscribe();
        }
    }

    public void contactsInfections(){
        Person p1, p2;
        int distance;
        for(int i=0; i<peopleInBin.size(); i++){
            p1 = peopleInBin.get(i);
            for(int j=i+1; j<peopleInBin.size(); j++){
                p2 = peopleInBin.get(j);
                //calcContactInfection(p1, p2);
            }

            for (Person person : peopleInOverlap) {
                p2 = person;
                //calcContactInfection(p1, p2);
            }
        }
        toMove = peopleInBin;
        peopleInBin = new ArrayList<>();
        peopleInOverlap = new ArrayList<>();
    }

    public void movePeople(){
        Person p;
        for (Person person : toMove) {
            p = person;
            //p.move();
            if (p.getInfected() > 0) {
                p.decrementInfection();
                if (p.getInfected() == 0) {
                    //log.info("Person healed: " + p.getId());
//                    System.out.println("healed: " + p.getId());
                    //DomainEvent domainEvent = new PersonHealed(time, (long) p.getId(), LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
                    //this.grid.getDomainEventPublisher().sendMessages(domainEvent).subscribe();
                }
            }
            //grid.insertPerson(p);
        }
        toMove = null;
    }

    public void calcInteractionsInList(Person currentPerson, Person iterNode){
        System.out.println("calcInteractionsInList");
        while (iterNode != null){
            System.out.println(String.valueOf(iterNode.getId()));
            //calcContactInfection(currentPerson, iterNode.getContent());
            iterNode = iterNode.getNext();
        }
    }


    public void recursiveRightTreeInteractions(BinarySearchTree2d tree, Person currentPerson){
        System.out.println("recursiveRightTreeInteractions");
        System.out.println(tree.getUpperLeft());
        System.out.println(tree.getLowerRight());
        System.out.println();

        if(tree.getLeftTree() != null){
            if(currentPerson.getPos().infectionRangeOverlaps(GridBins.infectionDistance, tree.getUpperLeft(), tree.getLeftLowerRight())){
                recursiveRightTreeInteractions(tree.getLeftTree(), currentPerson);
            }
            if(currentPerson.getPos().infectionRangeOverlaps(GridBins.infectionDistance, tree.getRightUpperLeft(), tree.getLowerRight())){
                recursiveRightTreeInteractions(tree.getRightTree(), currentPerson);
            }
        } else {
            if(currentPerson.getPos().infectionRangeOverlaps(GridBins.infectionDistance, tree.getUpperLeft(), tree.getLeftLowerRight())){
                calcInteractionsInList(currentPerson, tree.getLeftLeaf().getPeople().getStart());
            }
            if(currentPerson.getPos().infectionRangeOverlaps(GridBins.infectionDistance, tree.getRightUpperLeft(), tree.getLowerRight())){
                calcInteractionsInList(currentPerson, tree.getRightLeaf().getPeople().getStart());
            }
        }
    }

    public int isBorderCase(Coordinate upperLeft, Coordinate lowerRight){

        if (upperLeft.getRow() != 0){
            if(upperLeft.getCol() != 0){
                if(lowerRight.getRow() != gridSizeRow){

                    if (lowerRight.getCol() != gridSizeCol) {
                        return 0; // normal
                    } else{
                        return 1; // right
                    }

                } else {
                    if (lowerRight.getCol() != gridSizeCol){
                        return 2; // bottom
                    } else {
                        return 3; // bottom right
                    }
                }

            } else {
                if (lowerRight.getRow() != gridSizeRow){
                    return 4; // left
                } else {
                    return 5; // bottom left
                }
            }
        } else {
            if(upperLeft.getCol() != 0){
                if(lowerRight.getCol() != gridSizeCol){
                    return 6; // top
                } else {
                    return 7; // top right
                }
            } else {
                return 8; // top left
            }
        }
    }

    public void calcInteractions(BinarySearchLeaf currentLeaf, Person currentPerson){

        System.out.println("calcInteractions");
        System.out.println(currentLeaf.getUpperLeft());
        System.out.println(currentPerson.getId());
        System.out.println();

        calcInteractionsInList(currentPerson, currentPerson.getNext());

        Coordinate position = currentPerson.getPos();

        if (position.infectionRangeContained(GridBins.infectionDistance, currentLeaf.getUpperLeft(), currentLeaf.getLowerRight(), isBorderCase(currentLeaf.getUpperLeft(), currentLeaf.getLowerRight()))){System.out.println("all in leaf"); return; }

        BinarySearchTree2d parent = currentLeaf.getParent();

        System.out.println("parent");
        System.out.println(parent.getUpperLeft());
        System.out.println(parent.getLowerRight());
        System.out.println();

        if(currentLeaf == parent.getLeftLeaf()) {
            calcInteractionsInList(currentPerson, parent.getRightLeaf().getPeople().getStart());
        }

        BinarySearchTree2d child = parent;
        parent = child.getParent();

        System.out.println("parent");
        System.out.println(parent.getUpperLeft());
        System.out.println(parent.getLowerRight());
        System.out.println();
        while (!position.infectionRangeContained(GridBins.infectionDistance, child.getUpperLeft(), child.getLowerRight(), isBorderCase(child.getUpperLeft(), child.getLowerRight())) && parent != null){

            System.out.println("parent loop");
            System.out.println(parent.getUpperLeft());
            System.out.println(parent.getLowerRight());
            System.out.println();

            if (child == parent.getLeftTree()){
                    if (position.infectionRangeOverlaps(GridBins.infectionDistance, parent.getRightUpperLeft(), parent.getLowerRight())){
                        recursiveRightTreeInteractions(parent.getRightTree(),currentPerson);
                    }
            }

            child = parent;
            parent = child.getParent();
        }

    }


    public void iteration(){
        BinarySearchLeaf currentLeaf = firstLeaf;
        //<Person> currentList;
        Person currentNode;
        while (currentLeaf != null){
            currentNode = firstLeaf.getPeople().getStart();
            while (currentNode != null){
                calcInteractions(currentLeaf, currentNode);
                return;
                //currentNode = currentNode.getNext();
            }
            //currentLeaf = currentLeaf.getNext();
        }
    }
}
