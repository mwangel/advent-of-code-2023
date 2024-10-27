package mw.day20

class Tjugo {
  static def load( List<String> lines ) {
    Map<String, List<String>> connections = [:]
    List<Module> connected = []
    List<Module> disconnected = []

    int nr = 0

    lines.each { String line ->
      def parts = line.split( " -> " )
      if( parts.size() != 2 )
        throw new IllegalStateException( "Invalid line: $line" )

      String name = parts[0]
      if( name.startsWith( "%" ) || name.startsWith( "&" ) )
        name = name.substring( 1 )

      connections.put( name, parts[1].split( ", " ).toList() )

      if( parts[0].startsWith( "%" ) )
        disconnected << new FlipFlop( name )
      else if( parts[0].startsWith( "&" ) )
        disconnected << new AndGate( name )
      else if( name == "broadcaster" )
        disconnected << new Broadcaster( name )
      else
        throw new IllegalStateException( "Unknown module: $name on line $nr" )

      nr++
    }

    def button = new Button()
    def broadcaster = disconnected.find { it.name == "broadcaster" }
    button.setAsInputTo( broadcaster )
    disconnected.remove( broadcaster )
    connected << broadcaster

    while( disconnected.size() > 0 ) {
      disconnected.each { Module module ->
        def sources = connections.findAll {
          it.value.contains( module.name )
        }.keySet()

        if( sources.size() == 0 )
          throw new IllegalStateException( "No sources for module: $module.name" )
        sources.each {String sourceName ->
          def source = connected.find {
            it.name == sourceName
          }
          if( source == null )
            println( "Source not found: $sourceName" )
          else {
            source.setAsInputTo( module )
            disconnected.remove( module )
          }
        }
      }

//      if( receivers != null ) {
//        receivers.each { String receiverName ->
//          def receiver = disconnected.find { it.name == receiverName }
//          if( receiver == null )
//            receiver = connected.find { it.name == receiverName }
//          if( receiver == null )
//            throw new IllegalStateException( "Receiver not found: $receiverName" )
//          module.setAsInputTo( receiver )
//        }
//      }
//      connected << module
      return button
    }
  }

  def solve( def model ) {
    println( "Solving..." )
    return 0
  }
}
