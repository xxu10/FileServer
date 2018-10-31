# FileServer
File server based on Netty

Netty is an asynchronous network application framework. When I was
working on the IM software, I found I need to write heavy codes for socket programming. So recently I tried Netty.

Sometimes if the buffer is much bigger than the package, there will be several packages stick together and if the buffer is too small, even 
smaller than the package, then maybe we can only get a part of the package. So we need to fix that. Usually you can use header and body structure.
In Netty, we can use the tools DelimiterBasedFrameDecoder or FixedLengthFrameDecoder, also you can design your own protocol and own decoder.

In this project, I used a simple header+body structure. First is a 4-Byte filelength header, then 128-Byte filename header and then the file content

* 
* +-----+----------+-------------+
* | len | filename | file content|
* +-----+----------+-------------+
* 

Netty is really good!
