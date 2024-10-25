def lines = ("data.txt" as File).readLines()
println "${lines.size()} lines"

def getAt( def lines, int row, int column ) {
  if( row < 0 || column < 0 || row >= lines.size() || column >= lines[0].size() )
    return null
  return lines[ row ][ column ]
}

def isNearSymbol( def lines, int row, int column ) {
  def ul = getAt( lines, row-1, column-1 )
  def u  = getAt( lines, row-1, column )
  def ur = getAt( lines, row-1, column+1 )
  def l  = getAt( lines, row, column-1 )
  def r  = getAt( lines, row, column+1 )
  def dl = getAt( lines, row+1, column-1 )
  def d  = getAt( lines, row+1, column )
  def dr = getAt( lines, row+1, column+1 )
  def v = [ul,u,ur,l,r,dl,d,dr]
  def x = v.any { it && it != '.' && !it.isNumber() }
  //println( "$v -> $x" )
  return x
}

def nearStarCoordinate( def lines, int row, int column ) {
  if( getAt( lines, row-1, column-1 ) == '*' ) return [row-1, column-1]
  if( getAt( lines, row-1, column ) == '*' ) return [row-1, column]
  if( getAt( lines, row-1, column+1 ) == '*' ) return [row-1, column+1]
  if( getAt( lines, row, column-1 ) == '*' ) return [row, column-1]
  if( getAt( lines, row, column+1 ) == '*' ) return [row, column+1]
  if( getAt( lines, row+1, column-1 ) == '*' ) return [row+1, column-1]
  if( getAt( lines, row+1, column ) == '*' ) return [row+1, column]
  if( getAt( lines, row+1, column+1 ) == '*' ) return [row+1, column+1]
  return null
}

def isNearStar( def lines, int row, int column ) {
  def ul = getAt( lines, row-1, column-1 )
  def u  = getAt( lines, row-1, column )
  def ur = getAt( lines, row-1, column+1 )
  def l  = getAt( lines, row, column-1 )
  def r  = getAt( lines, row, column+1 )
  def dl = getAt( lines, row+1, column-1 )
  def d  = getAt( lines, row+1, column )
  def dr = getAt( lines, row+1, column+1 )
  def v = [ul,u,ur,l,r,dl,d,dr]
  def x = v.any { it && it=='*' }
  //println( "$v -> $x" )
  return x
}

def starKey( List coordinates ) {
  def x = coordinates[0]
  def y = coordinates[1]
  if(!x||!y) throw new Exception("$x - $y")
  "${x};${y}"
}

long sum = 0
long sumsum = 0
Map<String,List> gears = [:].withDefault { [] }

lines.eachWithIndex { line, row ->
  def currentNumber = ""
  def shouldCount = false
  def starClose = false
  def starCoordinates = null

  line.eachWithIndex { c, column ->

    if( c.isNumber() ) {
      currentNumber += c
      shouldCount |= isNearSymbol( lines, row, column )
      def ins = isNearStar( lines, row, column )
      if( ins ) {
        starClose = true
        starCoordinates = nearStarCoordinate( lines, row, column )
      }
      //println "shouldCount is $shouldCount"
    }
    else {
      if( currentNumber ) {
        if( shouldCount ) {
          // println currentNumber
          def nn = Long.parseLong( currentNumber )
          sum += nn
          if( starClose ) {
            println "A star is close to $nn: $starCoordinates"
            gears[ starKey(starCoordinates) ] << nn
          }
        }
        else {
          // println "(ignore $currentNumber)"
        }
        currentNumber = ""
        shouldCount = false
        starClose = false
        starCoordinates = null
      }
    }
  }

  if( currentNumber ) {
    if( shouldCount ) {
      // println currentNumber
      def nn = Long.parseLong( currentNumber )
      sum += nn
      if( starClose ) {
        println "A star is close to $nn: $starCoordinates"
        gears[ starKey(starCoordinates) ] << nn
      }

      sum += Long.parseLong( currentNumber )
    }
    else {
      // println "(ignore $currentNumber)"
    }
    currentNumber = ""
    shouldCount = false
    starClose = false
    starCoordinates = null
  }

  println "Line $row sum = $sum"
  sumsum += sum
  sum = 0
}
println sumsum

def gg = gears.findAll { k, v -> v.size() == 2 }
println( gg.collect{ k,v -> "$k : $v"}.join('\n') )
println "3 b: " + gg.values().sum { v -> v[0] * v[1] } // 72514855 för martin
