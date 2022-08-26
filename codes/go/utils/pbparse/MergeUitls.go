package pbparse

import (
	"bufio"
	"github.com/jhump/protoreflect/desc/builder"
	"github.com/jhump/protoreflect/desc/protoparse"
	"github.com/jhump/protoreflect/desc/protoprint"
	"io/ioutil"
	"log"
	"os"
	"strings"
)

func MergePbs(outFile string, dir string) {
	os.Remove(outFile)
	os.Create(outFile)
	//var parser protoparse.Parser
	files := findAllFiles(dir)
	var parser protoparse.Parser
	fBuilder := builder.NewFile("aaa")
	for _, file := range files {
		fileDescriptors, _ := parser.ParseFiles(file)
		for _, fileDescriptor := range fileDescriptors {
			services := fileDescriptor.GetServices()
			for _, svc := range services {
				svcBuilder, _ := builder.FromService(svc)
				fBuilder.AddService(svcBuilder)

			}
		}
	}
	fd2, _ := fBuilder.Build()
	f, err := os.OpenFile(outFile, os.O_RDWR|os.O_CREATE|os.O_TRUNC, os.ModePerm)
	if err != nil {
		return
	}
	defer f.Close()

	w := bufio.NewWriter(f)

	var print protoprint.Printer
	print.PrintProtoFile(fd2, w)
	w.Flush()

}

func findAllFiles(dir string) []string {
	files, err := ioutil.ReadDir(dir)
	if err != nil {
		log.Fatal(err)
	}
	var myFiles []string
	for _, file := range files {
		if file.IsDir() {
			nextFiles := findAllFiles(dir + "/" + file.Name())
			if len(nextFiles) > 0 {
				myFiles = append(myFiles, nextFiles...)
			}
		}
		if strings.HasSuffix(file.Name(), ".proto") {
			myFiles = append(myFiles, dir+"/"+file.Name())
		}
	}
	return myFiles
}
