package model;

import util.Position;
import java.util.*;



public class ElementFactory {

    public static Element create(ModelElement type,
                                 Position position,
                                 Map<Position, List<Position>> neighbors) {

        return switch (type) {
            case FIRE -> new Fire(Collections.singleton(position), neighbors);
            case FIREFIGHTER -> new Firefighter(position);
            case CLOUD -> new Cloud(position);
            case MOTORIZEDFIREFIGHTER -> new MotorizedFirefighter(position);
            default -> throw new IllegalArgumentException("Type non dynamique : " + type);
        };
    }
}
