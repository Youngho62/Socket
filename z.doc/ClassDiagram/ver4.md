@startuml
interface ISocket{
    {static} HEADER_SIZE : int
    {static} FILE_BUFFER_SIZE : int
    run() : void
}

interface ISocketReceive extends ISocket{
    receive() : void
}

interface ISocketSend extends ISocket{
	send(int) :boolean
}

class SocketClient implements ISocket{
    - client : Socket
    - address : InetSocketAddress
      scan : Scanner
}

class SocketServer implements ISocket{
    - server : ServerSocket
    - client : Socket
    - address : InetSocketAddress
      scan : Scanner
}

class ReceiveThread extends Thread implements ISocketReceive{
    - client : Socket
    - receiveMsg : InputStream
    
    setClient(Socket) : void
    receive() : void
}
class SendThread extends Thread implements ISocketSend{
    - client : Socket
    - receiveMsg : OutputStream
    - 
    setClient(Socket) : void
    send(int) : boolean
}
class SocketMain{
    {static} main() : void
    {static} choiceSC() : int
}

class Header {
    - dataSize : long
    - dataType : DataType
    - filePath : String
    - fileName : String
    - fileType : Stirng
}

enum DataType{
    CONNECTION_START
    TYPE_STRING
    TYPE_OBJECT
    TYPE_FILE
}
SocketMain --* SocketServer
SocketMain --* SocketClient
@enduml

