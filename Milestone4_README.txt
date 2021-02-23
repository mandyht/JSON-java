Two methods are added to the JSONObject class (src/main/java/org/json).
They are:
    - buildStream(Stream.Builder<Map.Entry<String, Object>>, String)
    - toStream()
    
Search with "@author Mandy Tsai" to locate them quickly.

Four methods are added to the JSONObjectTest class (src/test/java/org/json/junit/data).
The test cases are the last four methods in the XMLTest class; the method names are:
    - jsonObjectToStreamTest()
    - jsonObjectToStreamOnlyTransformValuesOfTagItems()
    - jsonObjectToStreamMap()
    - jsonObjectToStreamFilter()

Note: jsonObjectToStreamTest() uses System.out::println to test the forEach() method of Stream, so no 
assertions are written for it. Match the output with the following:
    /Books/book/author=ASmith
    /Books/book/title=AAA
    /Books/book/author=BSmith
    /Books/book/title=BBB
