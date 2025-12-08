package model;
import util.Position;
import java.util.List;
import java.util.Map;
import java.util.Set;


public interface Element {
    Set<Position> getPositions();
    List<Position> update(Map<Position, List<Position>> neighbors, FirefighterBoard board);
    ModelElement getType();


}
