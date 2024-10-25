def lines = ("data.txt" as File).readLines()
println "${lines.size()} lines"

// part 1. Answer martin = 25231
def sum = 0
lines.each { line ->
  def a = line.split('\\:')
  def b = a[1].split('\\|')
  def winners = b[0].trim().replace('  ', ' ').split(' ').collect( w -> Integer.parseInt(w) ).sort()
  def mine = b[1].trim().replace('  ', ' ').split(' ').collect( w -> Integer.parseInt(w) ).sort()

  def p = winners.intersect( mine )
  if( p.size() ) {
    def v = Math.pow( 2, p.size()-1 )
    sum += v
    //println( "$sum, $winners - $mine + $p * $v")
  }
}
println "Part A: sum = ${(long)sum}"

// part 2
Map<Integer,Integer> counts = [:].withDefault { 1 }
lines.eachWithIndex { line, index ->
  def a = line.split('\\:')
  def b = a[1].split('\\|')
  def winners = b[0].trim().replace('  ', ' ').split(' ').collect( w -> Integer.parseInt(w) ).sort()
  def mine = b[1].trim().replace('  ', ' ').split(' ').collect( w -> Integer.parseInt(w) ).sort()

  def p = winners.intersect( mine )
  println( "Line $index wins ${p.size()} ... $p" )
  p.size().times { i ->
    counts[index + i + 1] += counts[index]
    println( "  Add a copy of ${index+i+1} -> ${counts[index + i+1]}")
  }
}
def part2 = 0
for( int k=0; k < lines.size(); k++ ) {
  part2 += counts[k]
  println( "count $k = ${counts[k]}" )
}
println "Part B: amount = $part2" // 417 is too low, 761 is too low
