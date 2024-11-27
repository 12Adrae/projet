package model;

import util.Position;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FirefighterStrategy {
    void moveTowardsFire(Set<Position> firePositions, Map<Position, List<Position>> neighbors);
    void extinguishFiresAround(Set<Position> firePositions, Map<Position, List<Position>> neighbors);
}
