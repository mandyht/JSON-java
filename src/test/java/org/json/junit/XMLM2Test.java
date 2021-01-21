package org.json.junit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.json.JSONPointer;
import org.json.XML;

public class XMLM2Test {
    public static void main(String[] args) {
        String file = "C:\\Users\\Mandy\\Desktop\\MSWE\\Winter2021\\SWE262P\\JSON-java\\samples\\enwiki-20201220-abstract19.xml";
        JSONPointer path = new JSONPointer("/feed/doc");
        try {
            long start = System.nanoTime();
            XML.toJSONObject(new FileReader(new File(file)), path);
            System.out.println(System.nanoTime() - start);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
