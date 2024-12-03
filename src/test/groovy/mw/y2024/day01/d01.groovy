package mw.y2024.day01


import spock.lang.Specification

class d01 extends Specification {

  def "Day 01 part 1 and 2"() {
    given:
      def input = """1,0,1~1,2,1
0,0,2~2,0,2
0,2,3~2,2,3
0,0,4~0,2,4
2,0,5~2,2,5
0,1,6~2,1,6
1,1,8~1,1,9"""

      input = this.getClass().getResource( '/y2024/data01.txt' ).readLines()

    when:
      def a = [] as List<Long>
      def b = [] as List<Long>
      Map<Long, Long> map = [:].withDefault { 0L } // Map number to number of occurences
      input.each { line ->
        def parts = line.split( "   " )
        assert parts.size(  ) == 2
        a.add( parts[0].toLong() )
        b.add( parts[1].toLong() )
        map[ parts[1].toLong() ]++
      }
      a.sort( true )
      b.sort( true )

      def diff = 0
      a.eachWithIndex {long entry, int index ->
        def d =  Math.abs( entry - b[index] )
        // println( "$entry - ${b[index]} = $d" )
        diff += d
      }

    then:
      diff == 1765812


    // Part 2
    when:
      def similar = 0
      // This is the stupid way to do it with an effort of O(n^2)
      def startTime = System.currentTimeMillis(  )
      a.each { Long left ->
        def n = b.findAll { it == left }.size(  )
        similar += n  * left
      }
      println( "Time 2: ${System.currentTimeMillis() - startTime}" )
    then:
      similar == 20520794

    // Part 2 again, but with a map.
    // This is O(n) and a lot faster but with these small lists it doesn't matter much.
    when:
      startTime = System.currentTimeMillis(  )
      long similar2 = a.sum { map[ it ] * it }
      println( "Time 2: ${System.currentTimeMillis() - startTime}" )
    then:
      similar2 == similar
  }

}
