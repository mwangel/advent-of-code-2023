def data = ("data1.txt" as File).text.split('\n') // list of strings

def ints = ['one', 'two', 'three', 'four', 'five', 'six', 'seven', 'eight', 'nine']

def values = []
def nums = data.collect { s ->
  def v = []
  def acc = ""

  s.each { c ->
    if( c.isNumber() ) {
      v << Integer.parseInt( c )
      acc = ""
    }
    else {
      acc += c
      def i = ints.find { n -> acc.endsWith(n) }
      if( i ) {
        def x = ints.indexOf(i) + 1
        println "acc ends with $i as $x"
        v << x
      }
    }
  }

  if( v.size() == 1 ) v << v[0]
  if( v.size() > 2 ) v = [v.first(), v.last()]
  values << Integer.parseInt( v.join() )
}

println values
println( "part 2: ${values.size()} lines -> ${values.sum()}" )
