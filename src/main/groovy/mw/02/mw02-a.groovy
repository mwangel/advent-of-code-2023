def data = ("data.txt" as File).text.split('\n') // list of strings

class Play {
  int r, g, b

  Play parse( def values ) {
    values.each { v ->
      def parts = v.trim().split(" ")
      def color = parts[1]
      if( color.contains("red") ) { this.r = Integer.parseInt(parts[0]) }
      else if( color.contains("green") ) { this.g = Integer.parseInt(parts[0]) }
      else if( color.contains("blue") ) { this.b = Integer.parseInt(parts[0]) }
      else throw new Exception("problem $value // $parts // $color")
    }
    return this
  }

  String toString() { "{$r r, $g g, $b b}" }
}

class Game {
  int n
  List<Play> plays = []
  String toString() { "Game $n: " + plays.collect{ it.toString()} }
}

def games = []
data.each { line ->
  Game g = new Game()
  def lrsplit = line.split(':')
  def gs = Integer.parseInt( lrsplit[0] - "Game " )
  g.n = gs

  def playsplit = lrsplit[1].split(";")
  playsplit.each { ps ->
    def colorsplit = ps.split(",")
    g.plays << new Play().parse( colorsplit )
  }
  games << g
}

//-- part 1 --------------
def possibleGames = games.findAll { g ->
  g.plays.every { p -> p.r <= 12 && p.g <= 13 && p.b <= 14 }
}
println "${possibleGames.join('\n')}"
println "part 1: ${possibleGames.sum { g -> g.n }}"


//-- part 2 --------------
long sum = 0
games.each { g ->
  def maxr = g.plays.collect { p -> p.r }.max()
  def maxg = g.plays.collect { p -> p.g }.max()
  def maxb = g.plays.collect { p -> p.b }.max()
  def power = maxr * maxg * maxb
  sum += power
}
println "part 2: $sum"
