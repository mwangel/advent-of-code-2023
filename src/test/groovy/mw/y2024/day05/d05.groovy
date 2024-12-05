package mw.y2024.day05


import spock.lang.Specification

class d05 extends Specification {

  def "Day 05 - ordering sequences"() {
    given:
//      def data = this.getClass().getResource( '/y2024/data05-test.txt' ).readLines()
      def data = this.getClass().getResource( '/y2024/data05.txt' ).readLines()
      def (rules, sequences) = load( data )

    when:
      println(rules)
      def result = sequences.findAll {seq ->
        for( int i = 0; i < seq.size()-1; i++ ) {
          def mustNotAppearAfter = rules[seq[i]]
          if( seq[i+1..-1].any{ Long following -> mustNotAppearAfter.contains( following ) }  ) {
            println("${seq[i]} is incorrect, it depends on ${rules[seq[i]]} but has ${seq[0..(i - 1)]}")
            return false
          }
          else println("${seq[i]} is correct, it depends on ${rules[seq[i]]}")
        }
        println()
        return true
      }

    def sums = result.sum { List<Long> seq ->
      def mid = seq[ seq.size() >> 1 ]
      return mid
    }

    then:
      sums == 143 // test data has 143
  }

  def load( List<String> data ) {
    def rules = [:].withDefault {
      [] as Set<Long>
    }
    def sequences = [] as List<List<Long>>
    data.each {line ->
      if( line.contains( "|" ) ) {
        def parts = line.split( "\\|" )
        rules[parts[1].trim().toLong()].add( parts[0].trim().toLong() )
      }
      else if( line.contains( "," ) ) {
        sequences << line.split( "," ).collect {
          it.toLong()
        }
      }
    }
    return [rules, sequences]
  }

}
