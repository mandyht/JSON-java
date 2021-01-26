/**
 * @author Mandy Tsai
 * 1/20/2021
 */

package org.json.junit;

import static org.junit.Assert.assertTrue;

import java.io.StringReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONPointer;
import org.json.XML;
import org.junit.Test;

public class XMLM2Test {
    private final static String STRINGS_XML = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                                            + "<resources>\n"
                                            + "<!-- labels -->\n"
                                            + "<string name=\"app_name\">Agenda</string>\n"
                                            + "<string name=\"task_lists\">Task Lists</string>\n"
                                            + "<string-array name=\"sort_lists_menu\">\n"
                                            + "<item>Creation date</item>\n"
                                            + "<item>List name</item>\n"
                                            + "</string-array>\n"
                                            + "<string-array name=\"sort_tasks_menu\">\n"
                                            + "<item>Creation date</item>\n"
                                            + "<item>Due date</item>\n"
                                            + "<item>Name</item>\n"
                                            + "<item>Status</item>\n"
                                            + "</string-array>\n"
                                            + "<string name=\"list_name_error\">List name must contain at least 1 alphanumeric character</string>\n"
                                            + "</resources>";

    private final static String BOOKS_XML = "<?xml version=\"1.0\"?>\n"
                                            + "<catalog>\n"
                                            + "<book id=\"bk101\">\n"
                                            + "<author>Gambardella, Matthew</author>\n"
                                            + "<title>XML Developer's Guide</title>\n"
                                            + "<publish_date>2000-10-01</publish_date>\n" 
                                            + "</book>\n" 
                                            + "<book id=\"bk102\">\n"
                                            + "<author>Ralls, Kim</author>\n"
                                            + "<title>Midnight Rain</title>\n"
                                            + "<publish_date>2000-12-16</publish_date>\n"
                                            + "</book>\n"
                                            + "<book id=\"bk103\">\n"
                                            + "<author>Corets, Eva</author>\n"
                                            + "<title>Maeve Ascendant</title>\n"
                                            + "<publish_date>2000-11-17</publish_date>\n"
                                            + "</book>\n"
                                            + "</catalog>";

    private final static String NESTED_XML = "<nested-array><ch><ch>ch2</ch></ch></nested-array>";
                                            
    private final static String CORRUPTED_XML = "<root><string>content</root></string>";
    private final static String CORRUPTED_XML2 = "<root><></root>";

    @Test
    public void toJSONObjectStringsXMLExtractRoot() {
        JSONPointer path = new JSONPointer("/resources");
        JSONObject o = XML.toJSONObject(new StringReader(STRINGS_XML), path);
        assertTrue(o.get("resources") instanceof JSONObject);
    }

    @Test
    public void toJSONObjectStringsXMLExtractNestedKey() {
        JSONPointer path = new JSONPointer("/resources/string-array");
        JSONObject o = XML.toJSONObject(new StringReader(STRINGS_XML), path);
        assertTrue(((JSONArray) o.get("string-array")).length() == 2);
    }

    @Test
    public void toJSONObjectBooksXMLExtractRoot() {
        JSONPointer path = new JSONPointer("/catalog");
        JSONObject o = XML.toJSONObject(new StringReader(BOOKS_XML), path);
        assertTrue(o.get("catalog") instanceof JSONObject);
    }

    @Test
    public void toJSONObjectBooksXMLExtractNestedKey() {
        JSONPointer path = new JSONPointer("/catalog/book");
        JSONObject o = XML.toJSONObject(new StringReader(BOOKS_XML), path);
        assertTrue(((JSONArray) o.get("book")).length() == 3);
    }

    @Test
    public void toJSONOjectBooksXMLExtractJSONArrayElement() {
        JSONPointer path = new JSONPointer("/catalog/book/0/author");
        JSONObject o = XML.toJSONObject(new StringReader(BOOKS_XML), path);
        assertTrue(o.get("author").equals("Gambardella, Matthew"));
        
        path = new JSONPointer("/catalog/book/1/publish_date");
        o = XML.toJSONObject(new StringReader(BOOKS_XML), path);
        assertTrue(o.get("publish_date").equals("2000-12-16"));

        path = new JSONPointer("/catalog/book/2");
        o = XML.toJSONObject(new StringReader(BOOKS_XML), path);
        
        JSONObject o2 = XML.toJSONObject(new StringReader(BOOKS_XML));
        Object value = o2.optQuery(path);
        assertTrue(o.get("book").toString().equals(((JSONObject) value).toString()));
    }

    @Test
    public void toJSONObjectTestXMLExtractNestedKey() {
        JSONPointer path = new JSONPointer("/nested-array");
        JSONObject o = XML.toJSONObject(new StringReader(NESTED_XML), path);
        assertTrue(o.get("nested-array") instanceof JSONObject);
        assertTrue(((JSONObject) o.get("nested-array")).get("ch") instanceof JSONObject);
    }

    @Test
    public void toJSONObjectEmptyPath() {
        JSONPointer path = new JSONPointer("/");
        JSONObject o = XML.toJSONObject(new StringReader(STRINGS_XML), path);
        assertTrue(o == null);

        JSONObject o2 = XML.toJSONObject(new StringReader(STRINGS_XML));
        Object o3 = o2.optQuery("/");
        assertTrue(o3 == null);
    }
    
    @Test
    public void toJSONObjectInvalidEmptyPath() {
        JSONPointer path = new JSONPointer("//");
        JSONObject o =XML.toJSONObject(new StringReader(STRINGS_XML), path);
        assertTrue(o == null);
    }

    @Test
    public void toJSONObjectInvalidPath() {
        JSONPointer path = new JSONPointer("/resources//string-array");
        JSONObject o = XML.toJSONObject(new StringReader(STRINGS_XML), path);
        assertTrue(o == null);

        path = new JSONPointer("/3/string");
        o = XML.toJSONObject(new StringReader(STRINGS_XML), path);
        assertTrue(o == null);

        path = new JSONPointer("/resources/string-array/0/0");
        o = XML.toJSONObject(new StringReader(STRINGS_XML), path);
        assertTrue(o == null);

        path = new JSONPointer("/resources/string-array/3");
        o = XML.toJSONObject(new StringReader(STRINGS_XML), path);
        assertTrue(o == null);
    }

    @Test (expected = JSONException.class) 
    public void toJSONObjectFaultyInput() {
        JSONPointer path = new JSONPointer("/root");
        XML.toJSONObject(new StringReader(CORRUPTED_XML), path);
    }

    @Test (expected = JSONException.class)
    public void toJSONObjectFaultyInput2() {
        JSONPointer path = new JSONPointer("/root");
        XML.toJSONObject(new StringReader(CORRUPTED_XML2), path);
    }

    @Test
    public void toJSONObjectStringsXMLReplaceRoot() {
        JSONPointer path = new JSONPointer("/resources");
        JSONObject replacement = new JSONObject();
        JSONObject o = XML.toJSONObject(new StringReader(STRINGS_XML), path, replacement);
        assertTrue(o.get("resources") == replacement);
    }

    @Test
    public void toJSONObjectStringsXMLReplaceNestedKey() {
        JSONPointer path = new JSONPointer("/resources/string-array");
        JSONObject replacement = new JSONObject();
        JSONObject o = XML.toJSONObject(new StringReader(STRINGS_XML), path, replacement);
        Object value = o.optQuery("/resources/string-array");
        assertTrue(value == replacement);
    }

    @Test
    public void toJSONObjectBookssXMLReplaceRoot() {
        JSONPointer path = new JSONPointer("/catalog");
        JSONObject replacement = new JSONObject();
        replacement.put("key", "value");

        JSONObject o = XML.toJSONObject(new StringReader(BOOKS_XML), path, replacement);
        assertTrue(o.get("catalog") == replacement);
    }

    @Test
    public void toJSONObjectBooksXMLReplaceNestedKey() {
        JSONPointer path = new JSONPointer("/catalog/book");
        JSONObject replacement = new JSONObject();
        replacement.put("key", "value");
        replacement.put("key2", "value2");
        
        JSONObject o = XML.toJSONObject(new StringReader(BOOKS_XML), path, replacement);
        Object value = o.optQuery("/catalog/book");
        assertTrue(value == replacement);
    }

    @Test
    public void toJSONObjectReplaceEmptyPath() {
        JSONPointer path = new JSONPointer("/");
        JSONObject o = XML.toJSONObject(new StringReader(STRINGS_XML), path, new JSONObject());
        assertTrue(o == null);

        JSONObject o2 = XML.toJSONObject(new StringReader(STRINGS_XML));
        Object o3 = o2.optQuery("/");
        assertTrue(o3 == null);
    }
    
    @Test
    public void toJSONObjectReplaceInvalidEmptyPath() {
        JSONPointer path = new JSONPointer("//");
        JSONObject o = XML.toJSONObject(new StringReader(STRINGS_XML), path, new JSONObject());
        assertTrue(o == null);
    }

    @Test 
    public void toJSONObjectReplaceInvalidPath() {
        JSONPointer path = new JSONPointer("/resources//string-array");
        JSONObject o = XML.toJSONObject(new StringReader(STRINGS_XML), path, new JSONObject());
        assertTrue(o == null);
    }

    @Test (expected = JSONException.class) 
    public void toJSONObjectReplaceFaultyInput() {
        JSONPointer path = new JSONPointer("/root");
        XML.toJSONObject(new StringReader(CORRUPTED_XML), path, new JSONObject());
    }    

    @Test (expected = JSONException.class) 
    public void toJSONObjectReplaceFaultyInput2() {
        JSONPointer path = new JSONPointer("/root");
        XML.toJSONObject(new StringReader(CORRUPTED_XML2), path, new JSONObject());
    }
}
