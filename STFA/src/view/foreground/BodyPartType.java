package view.foreground;

public enum BodyPartType {

    HEAD,           //Stickman head, can take damage
    MEMBER,         //Stickman member, can take damage and make damage
    INVULNERABLE,   //Stickman extremity, can make damage
    INERT,          //Inert bodies, uncontrollable, can make damage
    NONE            //Used to check if not part of the stickman
}
