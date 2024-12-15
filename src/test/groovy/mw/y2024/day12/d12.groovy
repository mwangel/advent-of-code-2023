package mw.y2024.day12

import spock.lang.Shared
import spock.lang.Specification

class d12 extends Specification {

  @Shared
  def www = []

  def directions = [
    [0, -1, "up"],
    [1, 0, "right"],
    [0, 1, "down"],
    [-1, 0, "left"]
  ]

  def "Day 12 - size and circumference of areas"() {
    given:
//      def( fileName, expectedRangeCount1, expectedTotal1 ) = [ '/y2024/data12-test.txt', 11, 1930 ]
      def( fileName, expectedRangeCount1, expectedTotal1 ) = [ '/y2024/data12.txt', 624, 1477762 ]
      def input = this.getClass().getResource( fileName ).readLines()
      def (map, width, height) = load( input )
      www = map

    when:
      //map = ["AB", "AB"]
      def plots = findAllPlots( map )
      println(plots)
      long total = 0
      plots.eachWithIndex {Plot entry, int i ->
        println( "Plot $i: ${entry.name} - area: ${entry.getArea()} - circumference: ${entry.getCircumference()}" )
        total += entry.getArea() * entry.getCircumference()
      }

    then:
      plots.size() == expectedRangeCount1
      total == expectedTotal1
  }


  class Square {
    int x
    int y
    Square( int x, int y ) {
      this.x = x
      this.y = y
    }
    int hashCode() {
      return x.hashCode() + y.hashCode()
    }
    boolean equals( Object o ) {
      if( o == null ) return false
      if( o.is(this) ) return true
      if( o.getClass() != this.getClass() ) return false
      Square s = (Square)o
      return ((s.x == x) && (s.y == y))
    }
    String toString() {
      return "($x,$y)"
    }
  }

  class Plot {
    private static int nextId = 0
    int id
    String name
    Set<Square> squares

    Plot( String name ) {
      this.id = nextId++
      this.name = name
      this.squares = [] as Set<Square>
    }

    String toString() {
      return "Plot $id: $name - ${squares.size()} squares"
    }

    void addSquare( Square square ) {
      squares.add( square )
    }

    int getArea(){
      return squares.size()
    }

    int getCircumference() {
      int result = 0
      squares.each { Square square ->
        directions.each { int dx, int dy, String dirName ->
          int newX = square.x + dx
          int newY = square.y + dy
          if( !squares.contains( new Square(newX, newY) ) ) {
            result++
          }
        }
      }
      return result
    }

    boolean contains( int x, int y ) {
      return squares.contains( new Square(x, y) )
    }

    boolean contains( Square square ) {
      return squares.contains( square )
    }
  }


  def findAllPlots( List<String> map ) {
    def plots = [] as List<Plot>

    for( int y=0; y < map.size(); y++ ) {
      for( int x=0; x < map[y].size(); x++ ) {
        // Is there some neighbouring plot that we can add this square to?
        def c = getChar( map, x, y )
        def neighbours = plots.findAll { Plot pp ->
          pp.name == c &&
          ( pp.contains( x-1, y ) || pp.contains( x+1, y ) || pp.contains( x, y-1 ) || pp.contains( x, y+1 ) )
        }
        if( neighbours ) {
          //println( "Adding $x,$y to plot ${neighbours[0]}" )
          neighbours[0].addSquare( new Square(x, y) )

          if( neighbours.size() > 1 ) {
            // Merge the plots and remove one of them.
            for( int i=1; i < neighbours.size(); i++ ) {
              //println( "Merging plot ${neighbours[i]} into ${neighbours[0]}" )
              neighbours[i].squares.each { Square s ->
                neighbours[0].addSquare( s )
              }
              plots.remove( neighbours[i] )
            }
          }
          continue
        }

        // Create a new plot.
        Plot p = new Plot(c)
        //println( "Creating new plot $p with $x,$y" )
        p.addSquare( new Square(x, y) )
        plots.add( p )
      }
    }
    return plots
  }




  def getChar( List<String> data, int x, int y ) {
    if( x < 0 || y < 0 || y >= data.size() || x >= data[y].size() ) { throw new RuntimeException( "Out of bounds: x:$x, y:$y" ) }
    return data[y][x]
  }


  def load( List<String> data ) {
    return [data, data[0].size(), data.size()]
  }

}
