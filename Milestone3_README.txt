Two methods are added to the XML class (src/main/java/org/json).
They are:
    - parseAndReplaceKey(XMLTokener, JSONObject, String, Function<String, String>)
    - toJSONObject(Reader, Function<String, String>)
    
Search with "@author Mandy Tsai" to locate them quickly.

Three methods and a private inner class are added to the XMLTest class (src/test/java/org/json/junit/data).
The test cases are the last three methods in the XMLTest class. The private inner class is named 
AddPrefix, and it is right above the new test cases created for Milestone3.



FINDINGS:

The performance of replacing the keys inside the library during the parsing of the XML should be
significantly faster than replacing them in the client code for large XML documents. In the library,
the XML document will only have to be read once. In the client code, however, I had no choice but to replace
the keys after first retrieving the JSONObject; this forces me to traverse the XML document twice. In theory,
the performance of this key replacement action should be at most two times faster in the library than in the
client code. 
