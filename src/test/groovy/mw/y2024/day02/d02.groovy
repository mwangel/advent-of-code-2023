package mw.y2024.day02


import spock.lang.Specification

class d02 extends Specification {

  def "Day 02 part 1"() {
    given:
      def input = """1,0,1~1,2,1
0,0,2~2,0,2
0,2,3~2,2,3
0,0,4~0,2,4
2,0,5~2,2,5
0,1,6~2,1,6
1,1,8~1,1,9"""

      input = this.getClass().getResource( '/y2024/data02.txt' ).readLines()

    when:
      def data = [] as List<List<Long>>
      def safeCounter = 0

      input.each { line ->
        def parts = line.split( " " )
        def numbers = parts*.toLong()
        data.add( numbers )

        if( isSafe( numbers ) ) {
          safeCounter++
        }
      }

    then:
      safeCounter == 257


    // Part 2
    // Later try to do this by counting the number of errors in each list instead.
    // If there is only one error, then we can remove that number and check if the list is safe.
    //
    // For now, do a brute force approach and try to remove each number in each unsafe list.
    when:
      data.each { List<Long> list ->
        if( !isSafe( list ) ) {
          println( "Unsafe: $list" )
          for( int i = 0; i < list.size(); i++ ) {
            def shortList = new ArrayList<Long>( list )
            shortList.remove( i )
            if( isSafe( shortList ) ) {
              println( "Safe by removing index $i: $list -> $shortList" )
              safeCounter++
              break
            }
          }
        }
      }
    then:
      safeCounter == 257
  }

  private boolean isSafe( List<Long> numbers ) {
    def diffs = []
    for( int i = 0; i < numbers.size() - 1; i++ ) {
      diffs.add( numbers[i+1] - numbers[i] )
    }

    if( !(diffs.every { it > 0 } || diffs.every { it < 0 }) ) return false
    return diffs.collect{ Math.abs(it) }.every{ it > 0 && it < 4 }
  }
}
