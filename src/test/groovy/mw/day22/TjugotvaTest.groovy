package mw.day22


import spock.lang.Specification

class TjugotvaTest extends Specification {

  class Brick {
    Range<Integer> xRange
    Range<Integer> yRange
    Range<Integer> zRange

//    Brick( Range<Integer> xRange, Range<Integer> yRange, Range<Integer> zRange ) {
//      this.xRange = xRange
//      this.yRange = yRange
//      this.zRange = zRange
//    }

    boolean intersects( Brick other ) {
      if( other.is( this ) ) return false // definition: you don't intersect yourself
      return xRange.intersect( other.xRange ) && yRange.intersect( other.yRange ) && zRange.intersect( other.zRange )
    }

    void moveZ( int delta ) {
      zRange = (zRange.min() + delta)..(zRange.max() + delta)
    }

    List<Brick> supportedBy = []
    List<Brick> supporting = []

    String toString() {
      return "(${xRange.size()==1?xRange.min():"${xRange.min()}-${xRange.max()}"};" +
        "${yRange.size()==1?yRange.min():"${yRange.min()}-${yRange.max()}"};" +
        "${zRange.size()==1?zRange.min():"${zRange.min()}-${zRange.max()}"})"
    }
  }

  def load( List<String> lines ) {
    List<Brick> bricks = []
    lines.each { line ->
      def parts = line.split( /[,~]/ ).collect { it.toInteger() }
      if( parts[3] < parts[0] ) throw new IllegalStateException( "Invalid line: $line" )
      if( parts[4] < parts[1] ) throw new IllegalStateException( "Invalid line: $line" )
      if( parts[5] < parts[2] ) throw new IllegalStateException( "Invalid line: $line" )
      bricks << new Brick( xRange: parts[0]..parts[3], yRange: parts[1]..parts[4], zRange: parts[2]..parts[5] )
    }
    bricks
  }

  /** Returns true if the brick was dropped by any amount. */
  def drop( Brick brick, List<Brick> bricks ) {
    int dropped = 0
    while( true ) {
      if( brick.zRange.min() == 1 ) break // can't drop any further

      brick.moveZ( -1 ) // move down by 1
      dropped++
      def intersecting = bricks.findAll { it.intersects( brick ) }
      if( intersecting.size() > 0 ) {
        brick.supportedBy.clear(  )
        brick.supportedBy.addAll( intersecting )
        intersecting.each { it.supporting.add( brick ) }
        brick.moveZ( 1 ) // undo the move
        dropped--
        break
      }
    }

    if( dropped > 0 ) {
      println( "Dropped $brick by $dropped levels." )
      brick.supporting.clear(  )
    }

    return dropped > 0
  }

  def run( List<Brick> bricks ) {
    while( true ) {
      println( "Sorting and dropping bricks" )
      bricks.sort( true, {it.zRange.min() } )
      boolean anyDropped = false
      bricks.each {Brick brick ->
        anyDropped |= drop( brick, bricks.findAll { it.zRange.max() <= brick.zRange.min()} )
      }

      if( !anyDropped ) {
        println( "No more bricks dropped" )
        break
      }
    }
  }

  def findRemovableBricks( List<Brick> bricks ) {
    Set<Brick> result = bricks.findAll { it.supportedBy.size() == 0 }
    result.each { println( " $it can be removed" ) }
    bricks.each { Brick brick ->
      brick.supporting.each { Brick supported ->
        if( supported.supportedBy.size() > 1 && !result.contains( brick ) ) {
          result.add( brick )
          println( " $brick can be removed." )
        }
      }
    }
    return result
  }


  def "Test case 1"() {
    given:
      def input = """1,0,1~1,2,1
0,0,2~2,0,2
0,2,3~2,2,3
0,0,4~0,2,4
2,0,5~2,2,5
0,1,6~2,1,6
1,1,8~1,1,9"""

      input = this.getClass().getResource( '/data22.txt' ).text

    when:
      def bricks = load( input.readLines(  ) )
      println( bricks.join( "\n" ) )
      run( bricks )
      println( bricks.join( "\n" ) )

      def removable = findRemovableBricks( bricks )

    then: // expect that the broadcaster has the only active signal
      !bricks[0].intersects( bricks[1] )
      !bricks[0].intersects( bricks[0] )
      removable.size(  ) == 232
      // 232 is too low
  }

}
