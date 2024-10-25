def data = ("data1.txt" as File).text.split('\n') // list of strings

def nums = data.collect { s ->
  def v = s.findAll { c -> c.isNumber() }
  if( v.size() == 1 ) v << v[0]
  if( v.size() > 2 ) v = [v.first(), v.last()]

  Integer.parseInt( v.join() )
}.sum()


println( "part 1: ${nums}" )
