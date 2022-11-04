package com.example.familymapclient.model;

import Models.PersonModel;

public class RelationshipModel {
    private PersonModel person;
    private String relationship;

    public RelationshipModel(PersonModel person, String relationship) {
        this.person = person;
        this.relationship = relationship;
    }

    public PersonModel getPerson() {
        return person;
    }
    public void setPerson(PersonModel person) {
        this.person = person;
    }

    public String getRelationship() {
        return relationship;
    }
    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }
}
