/**
 * @author Mandy Tsai
 * 1/20/2021
 */

package org.json.junit;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONPointer;
import org.json.XML;
import org.junit.Test;

public class XMLM2Test {
    private final static String DIR = Paths.get("samples").toAbsolutePath().toString();

    @Test
    public void toJSONObjectStringsXMLExtractRoot() throws FileNotFoundException {
        JSONPointer path = new JSONPointer("/resources");
        JSONObject o = XML.toJSONObject(new FileReader(new File(DIR, "strings.xml")), path);
        assertTrue(o.get("resources") instanceof JSONObject);
    }

    @Test
    public void toJSONObjectStringsXMLExtractNestedKey() throws FileNotFoundException {
        JSONPointer path = new JSONPointer("/resources/string-array");
        JSONObject o = XML.toJSONObject(new FileReader(new File(DIR, "strings.xml")), path);
        assertTrue(((JSONArray) o.get("string-array")).length() == 2);
    }

    @Test
    public void toJSONObjectBooksXMLExtractRoot() throws FileNotFoundException {
        JSONPointer path = new JSONPointer("/catalog");
        JSONObject o = XML.toJSONObject(new FileReader(new File(DIR, "books.xml")), path);
        assertTrue(o.get("catalog") instanceof JSONObject);
    }

    @Test
    public void toJSONObjectBooksXMLExtractNestedKey() throws FileNotFoundException {
        JSONPointer path = new JSONPointer("/catalog/book");
        JSONObject o = XML.toJSONObject(new FileReader(new File(DIR, "books.xml")), path);
        assertTrue(((JSONArray) o.get("book")).length() == 12);
    }

    @Test
    public void toJSONObjectTestXMLExtractNestedKey() throws FileNotFoundException {
        JSONPointer path = new JSONPointer("/nested-array");
        JSONObject o = XML.toJSONObject(new FileReader(new File(DIR, "test.xml")), path);
        assertTrue(o.get("nested-array") instanceof JSONObject);
        assertTrue(((JSONObject) o.get("nested-array")).get("ch") instanceof JSONObject);
    }

    @Test
    public void toJSONObjectEmptyPath() throws FileNotFoundException {
        JSONPointer path = new JSONPointer("/");
        JSONObject o = XML.toJSONObject(new FileReader(new File(DIR, "strings.xml")), path);
        assertTrue(o == null);

        JSONObject o2 = XML.toJSONObject(new FileReader(new File(DIR, "strings.xml")));
        Object o3 = o2.optQuery("/");
        assertTrue(o3 == null);
    }
    
    @Test
    public void toJSONObjectInvalidEmptyPath() throws FileNotFoundException {
        JSONPointer path = new JSONPointer("//");
        JSONObject o =XML.toJSONObject(new FileReader(new File(DIR, "strings.xml")), path);
        assertTrue(o == null);
    }

    @Test
    public void toJSONObjectInvalidPath() throws FileNotFoundException {
        JSONPointer path = new JSONPointer("/resources//string-array");
        JSONObject o = XML.toJSONObject(new FileReader(new File(DIR, "strings.xml")), path);
        assertTrue(o == null);
    }

    @Test
    public void toJSONObjectStringsXMLReplaceRoot() throws FileNotFoundException {
        JSONPointer path = new JSONPointer("/resources");
        JSONObject replacement = new JSONObject();
        JSONObject o = XML.toJSONObject(new FileReader(new File(DIR, "strings.xml")), path, replacement);
        assertTrue(o.get("resources") == replacement);
    }

    @Test
    public void toJSONObjectStringsXMLReplaceNestedKey() throws FileNotFoundException {
        JSONPointer path = new JSONPointer("/resources/string-array");
        JSONObject replacement = new JSONObject();
        JSONObject o = XML.toJSONObject(new FileReader(new File(DIR, "strings.xml")), path, replacement);
        Object value = o.optQuery("/resources/string-array");
        assertTrue(value == replacement);
    }

    @Test
    public void toJSONObjectBookssXMLReplaceRoot() throws FileNotFoundException {
        JSONPointer path = new JSONPointer("/catalog");
        JSONObject replacement = new JSONObject();
        replacement.put("key", "value");

        JSONObject o = XML.toJSONObject(new FileReader(new File(DIR, "books.xml")), path, replacement);
        assertTrue(o.get("catalog") == replacement);
    }

    @Test
    public void toJSONObjectBooksXMLReplaceNestedKey() throws FileNotFoundException {
        JSONPointer path = new JSONPointer("/catalog/book");
        JSONObject replacement = new JSONObject();
        replacement.put("key", "value");
        replacement.put("key2", "value2");
        
        JSONObject o = XML.toJSONObject(new FileReader(new File(DIR, "books.xml")), path, replacement);
        Object value = o.optQuery("/catalog/book");
        assertTrue(value == replacement);
    }

    @Test
    public void toJSONObjectReplaceEmptyPath() throws FileNotFoundException {
        JSONPointer path = new JSONPointer("/");
        JSONObject o = XML.toJSONObject(new FileReader(new File(DIR, "strings.xml")), path, new JSONObject());
        assertTrue(o == null);

        JSONObject o2 = XML.toJSONObject(new FileReader(new File(DIR, "strings.xml")));
        Object o3 = o2.optQuery("/");
        assertTrue(o3 == null);
    }
    
    @Test
    public void toJSONObjectReplaceInvalidEmptyPath() throws FileNotFoundException {
        JSONPointer path = new JSONPointer("//");
        JSONObject o = XML.toJSONObject(new FileReader(new File(DIR, "strings.xml")), path, new JSONObject());
        assertTrue(o == null);
    }

    @Test 
    public void toJSONObjectReplaceInvalidPath() throws FileNotFoundException {
        JSONPointer path = new JSONPointer("/resources//string-array");
        JSONObject o = XML.toJSONObject(new FileReader(new File(DIR, "strings.xml")), path, new JSONObject());
        assertTrue(o == null);
    }
}
