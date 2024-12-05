package mw.y2024.day05


import spock.lang.Specification

class d05 extends Specification {

  def "Day 05 - ordering sequences"() {
    given:
      def data = this.getClass().getResource( '/y2024/data05.txt' ).readLines()
//      def data = this.getClass().getResource( '/y2024/data05-test.txt' ).readLines()
      def (rules, sequences) = load( data )

    // Part 1, find the middle number in each correct sequence.
    when:
      def incorrectSequences = [] as List<List<Long>>
      def result = sequences.findAll {seq ->
        for( int i = 0; i < seq.size()-1; i++ ) {
          def mustNotAppearAfter = rules[seq[i]]
          if( seq[i+1..-1].any{ Long following -> mustNotAppearAfter.contains( following ) }  ) {
            println("${seq[i]} is incorrect, it depends on ${rules[seq[i]]} but has ${seq[0..(i - 1)]}")
            incorrectSequences << seq
            return false
          }
          else println("${seq[i]} is correct, it depends on ${rules[seq[i]]}")
        }
        println()
        return true
      }

    // sum the middle values of the sequences
    def sums = result.sum { List<Long> seq ->
      def mid = seq[ seq.size() >> 1 ]
      return mid
    }

    then:
      sums == 6384 // test data has 143

    // Part 2, reordering the sequences
    when:
      def result2 = reordering( incorrectSequences, rules )
    then:
      result2 == 5353 // test data has 123
  }


  def reordering( List<List<Long>> sequences, Map<Long, List<Long>> rules ) {
    def result = [] as List<List<Long>>

    sequences.each { List<Long> seq ->
      def ordered = seq.sort { Long a, Long b ->
        if( rules[a].contains( b ) ) { return -1 }
        else if( rules[b].contains( a ) ) { return 1 }
        else { return 0 }
      }
      result << ordered
    }

    def sums = result.sum { List<Long> seq ->
      def mid = seq[ seq.size() >> 1 ]
      return mid
    }

    return sums
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
