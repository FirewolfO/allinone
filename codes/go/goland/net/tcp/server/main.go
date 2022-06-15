package main

import (
	"bufio"
	"fmt"
	"net"
)

func main() {
	listen, err := net.Listen("tcp", "127.0.0.1:9999")
	if err != nil {
		fmt.Println("conn error , err = ", err)
		return
	}
	// 循环接受客户端连接
	for {
		conn, err := listen.Accept()
		if err != nil {
			fmt.Println("accept error:", err)
			continue
		}
		// 启动线程
		go processConn(conn)
	}
}

func processConn(conn net.Conn) {
	defer conn.Close()
	for { // 死循环
		reader := bufio.NewReader(conn)
		var buf [128]byte
		len, err := reader.Read(buf[:])
		if err != nil {
			fmt.Println("read error ", err)
			break
		}
		readStr := string(buf[:len])
		sprintf := fmt.Sprintf("server had read you message  messaage is %s \n", readStr)
		fmt.Println("receive message is : ", readStr)
		conn.Write([]byte(sprintf))
	}
}
