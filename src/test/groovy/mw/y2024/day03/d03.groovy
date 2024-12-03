package mw.y2024.day03


import spock.lang.Specification

class d03 extends Specification {

  def "Day 03"() {
    given:
      def input = """1,0,1~1,2,1
0,0,2~2,0,2
0,2,3~2,2,3
0,0,4~0,2,4
2,0,5~2,2,5
0,1,6~2,1,6
1,1,8~1,1,9"""

      input = this.getClass().getResource( '/y2024/data03.txt' ).readLines()

    when:
      def c = load(input)
    then:
      c.collect { Mul mul -> mul.execute() }.sum() == 187825547

    when: def d = load2(input)
    then: d.collect { Mul mul -> mul.execute() }.sum() == 85508223
  }


  def load( List<String> lines ) {
    def s = lines.join().findAll(/mul\((\d+),(\d+)\)/)

    def muls = s.collect { String line ->
      Mul.parse( line )
    }

    return muls
  }


  def load2( List<String> lines ) {
    def hits = lines.join().findAll(/mul\((\d+),(\d+)\)|do\(\)|don't\(\)/)

    boolean doIt = true
    def muls = []

    // Step through the hits and parse the Mul objects,
    // using the doIt flag to decide if we should parse or not.
    hits.each { String hit ->
      if( doIt && hit.startsWith( "mul" ) ) { muls << Mul.parse( hit ) }
      else if( hit == "do()" ) { doIt = true }
      else if ( hit == "don't()" ) { doIt = false }
    }

    return muls
  }
}
