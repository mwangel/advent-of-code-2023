package mw.day17

import spock.lang.Specification
import spock.lang.Unroll

import static mw.day17.Direction.LEFT
import static mw.day17.Direction.RIGHT

class Main17Test extends Specification {
  private f( int a, int b, int c ) {
    return [a+1, b+1, c+1]
  }

  def "groovy can unpack a list of return values"() {
    when:
      int a=0
      int b=0
      int c=0
      (a,b,c) = f(1, 2, 3 )
    then:
      a == 2
      b == 3
      c == 4
  }


  def "test test"() {
    given:
    def s = """2413432311323
3215453535623
3255245654254
3446585845452
4546657867536
1438598798454
4457876987766
3637877979653
4654967986887
4564679986453
1224686865563
2546548887735
4322674655533
"""
    def main = new Main17()
    def data = main.convertInput( s.readLines() )
    def width = 13
    def height = 13

    expect:
      main.minimizeCost_part_a(
        new Place( 0, 0, null, null, 0 ),
        new Place( 12, 12, null, null, 0 ),
        data,
        width,
        height ).totalCost == 102


    and:
      main.minimizeCost(
        new Place( 0, 0, null, null, 0 ),
        new Place( 12, 12, null, null, 0 ),
        data,
        width,
        height ).totalCost == 94
  }


  @Unroll
  def "verify cost=#cost for path #path"() {
    given:
      def s = """2413432311323
3215453535623
3255245654254
3446585845452
4546657867536
1438598798454
4457876987766
3637877979653
4654967986887
4564679986453
1224686865563
2546548887735
4322674655533
"""
      def main = new Main17()
      def data = main.convertInput( s.readLines() )

    when:
      def result = main.calculateCost(
        new Place( 0, 0, null, null, 0 ),
        new Place( 12, 12, null, null, 0 ),
        data,
        path.collect() as List<String> )

    then:
      result == cost

    where:
      cost | path
      102 | "RRDRRRURRRDDRRDDRDDDRDDDLDDR"
      102 | "RRRDRRRURRRDDRDDRDDDRDDDLDDR"
      113 | "DRRRDRRRURRRDRDDRDRDDDLDRDDD"
  }

  def "Steps in same direction works"() {
    given:
      def start = new Place( 0, 0, null, null, 0 )

    when: Place a = new Place( 0, 0, start, RIGHT, 0 )
    then: a.stepsInSameDirection == 1

    when: Place b = new Place( 0, 0, a, RIGHT, 0 )
    then: b.stepsInSameDirection == 2

    when: Place c = new Place( 0, 0, b, LEFT, 0 )
    then: c.stepsInSameDirection == 1
  }

  def "test sortedset"() {
    when:
    def s1 = [5,2,8,0,1,3] as SortedSet
    then:
    s1.head(  ) == 0
    s1.take( 3 ).asList(  ) == [0,1,2]
  }

  def "Main test"() {
    given:
      def main = new Main17()
      def data = main.convertInput( this.getClass().getResource( '/data17a.txt' ).readLines() )
      def width = data.first().size()
      def height = data.size()

      def resultA = main.minimizeCost_part_a(
        new Place( 0, 0, null, null, 0 ),
        new Place( width-1, height-1, null, null, 0 ),
        data, width, height )
      def resultB = main.minimizeCost(
        new Place( 0, 0, null, null, 0 ),
        new Place( width-1, height-1, null, null, 0 ),
        data, width, height )

    expect:
      width == 141
      height == 141
      resultA.totalCost == 928
      resultB.totalCost == 1104
      // expect cost to be 928 for part A
      // expect cost to be 1104 for part B
  }
}
