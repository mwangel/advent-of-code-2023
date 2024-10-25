def data = ("data.txt" as File).text.split('\n') // list of strings
// def data = ("data-test.txt" as File).text.split('\n') // list of strings
def seeds = (data[0] - "seeds: ").trim().split(" ").collect{ Long.parseLong(it) }

class Mapping {
  long drs // destination range start
  long srs // source range start
  long len // range length
  long sre // source range end

  Mapping( String s ) {
    def a = s.split(" ")
    drs = Long.parseLong( a[0] )
    srs = Long.parseLong( a[1] )
    len = Long.parseLong( a[2] )
    sre = srs + len
  }

  def maps( long source ) { source >= srs && source <= sre }
  def get( long source ) {
    if( source >= srs && source <= sre )
      return drs + (source - srs)
    return source
  }

  String toString() { "($srs-$sre -> $drs * $len)" }
}

class Map {
  def mappings = []

  long map( long source ) {
    def mapping = mappings.find { m -> m.maps(source) }
    if( !mapping ) return source //throw new Exception("missing mapping for $source")
    return mapping.get( source )
  }
}

def loadMap( data, header ) {
  def index = data.findIndexOf{ header == it } + 1
  println("Load $header from $index")
  def result = new Map()
  while( index < data.size() && data[index] ) {
    result.mappings << new Mapping( data[index] )
    index++
  }
  result
}


def seedToSoil = loadMap( data, 'seed-to-soil map:' )
def soilToFertilizer = loadMap( data, 'soil-to-fertilizer map:' )
def fertilizerToWater = loadMap( data, 'fertilizer-to-water map:' )
def waterToLight = loadMap( data, 'water-to-light map:' )
def lightToTemperature = loadMap( data, 'light-to-temperature map:' )
def temperatureToHumidity = loadMap( data, 'temperature-to-humidity map:' )
def humidityToLocation = loadMap( data, 'humidity-to-location map:' )
def all = [seedToSoil, soilToFertilizer, fertilizerToWater, waterToLight, lightToTemperature, temperatureToHumidity, humidityToLocation]

def chain( long v, List<Map> maps ) {
  print "${v.toString().padLeft(10, ' ')}"
  maps.each { m ->
    v = m.map( v )
    print " -> ${v.toString().padLeft(10, ' ')}"
  }
  println ""
  return v
}

def ds = seeds.collect { s -> chain( s, all ) }
println( "part 1: ${ds.min()}")


// part 2:
for( int x=0; x < seeds.size(); x+=2 ) {
  def start = seeds[x]
  def length = seeds[x+1]
  def end = start + length
  def min = 999999999
  println "Test $x/${seeds.size()}: $start-$end ($length) ..."
  for( long src=start; src < end; src++ ) {
    // if( src % 1000000 == 0 ) println "${end-src}..."
    // def v = chain( src, all )
    // if( v < min ) min = v
  }
  println "Min after $x ($start-$end): $min"
}
