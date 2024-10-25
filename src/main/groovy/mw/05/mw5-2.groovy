def data = ("data.txt" as File).text.split('\n') // list of strings
// def data = ("data-test.txt" as File).text.split('\n') // list of strings
def seeds = (data[0] - "seeds: ").trim().split(" ").collect{ Long.parseLong(it) }

@groovy.transform.CompileStatic
class Mapping {
  long drs // destination range start
  long srs // source range start
  long len // range length
  long sre // source range end
  long mapstart

  Mapping( String s ) {
    def a = s.split(" ")
    drs = Long.parseLong( a[0] )
    srs = Long.parseLong( a[1] )
    len = Long.parseLong( a[2] )
    sre = srs + len
    mapstart = drs - srs
  }

  @groovy.transform.CompileStatic
  boolean maps( long source ) { source >= srs && source <= sre }

  @groovy.transform.CompileStatic
  long get( long source ) {
    if( source >= srs && source <= sre )
      return mapstart + source
    return source
  }

  String toString() { "($srs-$sre -> $drs * $len)" }
}

class Map {
  def mappings = []

  @groovy.transform.CompileStatic
  long map( long source ) {
    Mapping mapping = mappings.find { Mapping m -> m.maps(source) } as Mapping
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

long chain( long v, List<Map> maps ) {
  //print "${v.toString().padLeft(10, ' ')}"
  // def o = v
  maps.each { m ->
    v = m.map( v )
    // print " -> ${v.toString().padLeft(10, ' ')}"
  }
  // println " $o -> $v"
  return v
}

def ds = seeds.collect { s -> chain( s, all ) }
println( "part 1: ${ds.min()}")


// part 2:
import groovyx.gpars.GParsPool

// Min after 0 (515785082-603690121): local 572193399, global 572193399
// Min after 2 (2104518691-2607668534): local 2201651584, global 572193399
// Min after 4 (720333403-1105567596): local 720678708, global 572193399
// Min after 6 (1357904101-1641290268): local 1547352475, global 572193399
// Min after 8 (93533455-222103138): local 105591520, global 105591520
// Min after 10 (2844655470-2869650099): local 2848640506, global 105591520
// Min after 12 (3934515023-4001842841): local 3998614525, global 105591520
// Min after 14 (2655687716-2664091133): local 2655687716, global 105591520
// Min after 16 (3120497449-3228254330): local 3155809403, global 105591520
// Min after 18 (4055128129-4064626837): local 4055128129, global 105591520

// 105_591_520 is too high, so is 1_081_323_768
// chain( 105 591 520 ) = 1_081_323_768 (1081323768)

@groovy.transform.CompileStatic
void bruteforce( def seeds, def allmaps ) {
  long globalMin = 99999999999
  for( int x=0; x < seeds.size(); x+=2 ) {
    def start = seeds[x]
    def length = seeds[x+1]
    def end = start + length
    def min = 999999999
    println "\nTest $x/${seeds.size()}: $start-$end ($length) ..."

    def range = (start..end)
    println range
    GParsPool.withPool {
      long pmin = ((List)(range.collectParallel { Long v -> chain( v, all ) })).min()
      if( pmin < globalMin ) globalMin = pmin
      println "Min after $x ($start-$end): local $pmin, global $globalMin"
    }

    // for( long src=start; src < end; src++ ) {
      // if( src % 1000000 == 0 ) println "${end-src}..."
      // def v = chain( src, all )
      // if( v < min ) min = v
    // }
    // println "Min after $x ($start-$end): $pmin"
  }
}

bruteforce( seeds, all )
