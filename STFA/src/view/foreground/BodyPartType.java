package view.foreground;

/**
 * Created by auntfox on 22.05.2016.
 *
 */
public enum BodyPartType {

    HEAD, //Stickman head, can take damage
    MEMBER, //Stickman member, can take damage and make damage
    INVULNERABLE, //Stickman extremity, can make damage
    INERT, //Inert bodys, uncontrollable, can make damage
    NONE //Do nothing but still here

}
