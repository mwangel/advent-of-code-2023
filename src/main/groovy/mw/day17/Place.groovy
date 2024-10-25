package mw.day17

class Place {
  int x, y
  Place parent = null
  Direction prevDirection = null
  int totalCost = 0
  int sisd = 1
  int h = UUID.randomUUID(  ).hashCode(  )

  Place( int x, int y, Place parent, Direction prevDirection, int totalCost ) {
    this.x = x
    this.y = y
    this.parent = parent
    this.prevDirection = prevDirection
    this.totalCost = totalCost
    if( parent != null && parent.prevDirection == prevDirection ) {
      this.sisd = parent.sisd + 1
    }
  }

  @Override int hashCode() {
    return h
  }

  int getTotalCost() {
    return totalCost
  }

  int getStepsInSameDirection() {
    return sisd
  }

  String path() {
    def path = ""
    def place = this
    while( place.parent != null ) {
      path = place.prevDirection.toString(  )[0] + path
      place = place.parent
    }
    return path
  }

  String getDirIndicator() {
    return switch( prevDirection ) {
      case Direction.UP -> "^"
      case Direction.DOWN -> "v"
      case Direction.LEFT -> "<"
      case Direction.RIGHT -> ">"
      default -> "*"
    }
  }

  List<Place> getParents() {
    def result = []
    def place = this
    while( place != null ) {
      result.add( place )
      place = place.parent
    }
    return result
  }

  boolean placeExistsInPath( int testX, int testY ) {
    def p = this
    while( p != null ) {
      if( p.x == testX && p.y == testY ) {
        return true
      }
      p = p.parent
    }
    return false
  }

  @Override String toString() {
    return "($x,$y, went:$prevDirection, cost:$totalCost)"
  }
}
