package mw.day20

class Tjugo {
  static Button load( List<String> lines ) {
    Map<String, List<String>> connections = [:]
    List<Module> modules = []

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
        modules << new FlipFlop( name )
      else if( parts[0].startsWith( "&" ) )
        modules << new AndGate( name )
      else if( name == "broadcaster" )
        modules << new Broadcaster( name )
      else
        throw new IllegalStateException( "Unknown module: $name on line $nr" )

      nr++
    }

    def button = new Button()
    def broadcaster = modules.find { it.name == "broadcaster" }
    button.addOutputConnection( broadcaster )
    broadcaster.addInputConnection( button )

    connections.each { senderName, receiverNames ->
      def sender = modules.find { it.name == senderName }
      receiverNames.each { receiverName ->
        def receiver = modules.find { it.name == receiverName }
        if( receiver == null ) {
          receiver = new UntypedModule( receiverName )
        }
        sender.addOutputConnection( receiver )
        receiver.addInputConnection( sender )
      }
    }
    return button
  }


  def solve( def model ) {
    println( "Solving..." )
    return 0
  }
}
