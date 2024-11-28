package model;

import util.Position;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FireStrategy {
    void spread(Map<Position, List<Position>> neighbor, Set<Position> mountainPositions);
    void extinguish(Position position);
    void extinguishAll(Set<Position> positions);
}
