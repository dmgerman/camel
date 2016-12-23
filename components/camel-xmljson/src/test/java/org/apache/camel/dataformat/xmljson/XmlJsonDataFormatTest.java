begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.xmljson
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|xmljson
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|dom
operator|.
name|DOMSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|sax
operator|.
name|SAXSource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|json
operator|.
name|JSON
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|json
operator|.
name|JSONArray
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|json
operator|.
name|JSONObject
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|json
operator|.
name|JSONSerializer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * Basic tests for the XML JSON data format  */
end_comment

begin_class
DECL|class|XmlJsonDataFormatTest
specifier|public
class|class
name|XmlJsonDataFormatTest
extends|extends
name|AbstractJsonTestSupport
block|{
annotation|@
name|Test
DECL|method|testMarshalAndUnmarshal ()
specifier|public
name|void
name|testMarshalAndUnmarshal
parameter_list|()
throws|throws
name|Exception
block|{
name|InputStream
name|inStream
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"testMessage1.xml"
argument_list|)
decl_stmt|;
name|String
name|in
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|inStream
argument_list|)
decl_stmt|;
name|MockEndpoint
name|mockJSON
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:json"
argument_list|)
decl_stmt|;
name|mockJSON
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mockJSON
operator|.
name|expectedHeaderReceived
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"application/json"
argument_list|)
expr_stmt|;
name|mockJSON
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mockXML
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:xml"
argument_list|)
decl_stmt|;
name|mockXML
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mockXML
operator|.
name|expectedHeaderReceived
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"application/xml"
argument_list|)
expr_stmt|;
name|mockXML
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|Object
name|json
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:marshal"
argument_list|,
name|in
argument_list|)
decl_stmt|;
name|String
name|jsonString
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|json
argument_list|)
decl_stmt|;
name|JSONObject
name|obj
init|=
operator|(
name|JSONObject
operator|)
name|JSONSerializer
operator|.
name|toJSON
argument_list|(
name|jsonString
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"JSONObject doesn't contain 7 keys"
argument_list|,
literal|7
argument_list|,
name|obj
operator|.
name|entrySet
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:unmarshal"
argument_list|,
name|jsonString
argument_list|)
expr_stmt|;
name|mockJSON
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|mockXML
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUnmarshalJSONObject ()
specifier|public
name|void
name|testUnmarshalJSONObject
parameter_list|()
throws|throws
name|Exception
block|{
name|InputStream
name|inStream
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"testMessage1.json"
argument_list|)
decl_stmt|;
name|String
name|in
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|inStream
argument_list|)
decl_stmt|;
name|JSON
name|json
init|=
name|JSONSerializer
operator|.
name|toJSON
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|MockEndpoint
name|mockXML
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:xml"
argument_list|)
decl_stmt|;
name|mockXML
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mockXML
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|Object
name|marshalled
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:unmarshal"
argument_list|,
name|json
argument_list|)
decl_stmt|;
name|Document
name|document
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Document
operator|.
name|class
argument_list|,
name|marshalled
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"The XML document has an unexpected root node"
argument_list|,
literal|"o"
argument_list|,
name|document
operator|.
name|getDocumentElement
argument_list|()
operator|.
name|getLocalName
argument_list|()
argument_list|)
expr_stmt|;
name|mockXML
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMarshalXMLSources ()
specifier|public
name|void
name|testMarshalXMLSources
parameter_list|()
throws|throws
name|Exception
block|{
name|InputStream
name|inStream
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"testMessage1.xml"
argument_list|)
decl_stmt|;
name|DOMSource
name|inDOM
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|DOMSource
operator|.
name|class
argument_list|,
name|inStream
argument_list|)
decl_stmt|;
name|inStream
operator|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"testMessage1.xml"
argument_list|)
expr_stmt|;
name|SAXSource
name|inSAX
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|SAXSource
operator|.
name|class
argument_list|,
name|inStream
argument_list|)
decl_stmt|;
name|inStream
operator|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"testMessage1.xml"
argument_list|)
expr_stmt|;
name|Document
name|inDocument
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Document
operator|.
name|class
argument_list|,
name|inStream
argument_list|)
decl_stmt|;
comment|// save the expected body of the message to set it later
name|Object
name|expectedBody
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:marshal"
argument_list|,
name|inDOM
argument_list|)
decl_stmt|;
name|MockEndpoint
name|mockJSON
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:json"
argument_list|)
decl_stmt|;
comment|// reset the mock endpoint to get rid of the previous message
name|mockJSON
operator|.
name|reset
argument_list|()
expr_stmt|;
comment|// all three messages should arrive, should be of type byte[] and
comment|// identical to one another
name|mockJSON
operator|.
name|expectedMessageCount
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|mockJSON
operator|.
name|allMessages
argument_list|()
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
expr_stmt|;
name|mockJSON
operator|.
name|expectedBodiesReceived
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|expectedBody
argument_list|,
name|expectedBody
argument_list|,
name|expectedBody
argument_list|)
argument_list|)
expr_stmt|;
comment|// start bombarding the route
name|Object
name|json
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:marshal"
argument_list|,
name|inDOM
argument_list|)
decl_stmt|;
name|String
name|jsonString
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|json
argument_list|)
decl_stmt|;
name|JSONObject
name|obj
init|=
operator|(
name|JSONObject
operator|)
name|JSONSerializer
operator|.
name|toJSON
argument_list|(
name|jsonString
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"JSONObject doesn't contain 7 keys"
argument_list|,
literal|7
argument_list|,
name|obj
operator|.
name|entrySet
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:marshal"
argument_list|,
name|inSAX
argument_list|)
expr_stmt|;
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:marshal"
argument_list|,
name|inDocument
argument_list|)
expr_stmt|;
name|mockJSON
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMarshalAndUnmarshalInline ()
specifier|public
name|void
name|testMarshalAndUnmarshalInline
parameter_list|()
throws|throws
name|Exception
block|{
name|InputStream
name|inStream
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"testMessage1.xml"
argument_list|)
decl_stmt|;
name|String
name|in
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|inStream
argument_list|)
decl_stmt|;
name|MockEndpoint
name|mockJSON
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:jsonInline"
argument_list|)
decl_stmt|;
name|mockJSON
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mockJSON
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mockXML
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:xmlInline"
argument_list|)
decl_stmt|;
name|mockXML
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mockXML
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|Object
name|json
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:marshalInline"
argument_list|,
name|in
argument_list|)
decl_stmt|;
name|String
name|jsonString
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|json
argument_list|)
decl_stmt|;
name|JSONObject
name|obj
init|=
operator|(
name|JSONObject
operator|)
name|JSONSerializer
operator|.
name|toJSON
argument_list|(
name|jsonString
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"JSONObject doesn't contain 7 keys"
argument_list|,
literal|7
argument_list|,
name|obj
operator|.
name|entrySet
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:unmarshalInline"
argument_list|,
name|jsonString
argument_list|)
expr_stmt|;
name|mockJSON
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|mockXML
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNamespacesDroppedInlineWithOptions ()
specifier|public
name|void
name|testNamespacesDroppedInlineWithOptions
parameter_list|()
throws|throws
name|Exception
block|{
name|InputStream
name|inStream
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"testMessage2-namespaces.xml"
argument_list|)
decl_stmt|;
name|String
name|in
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|inStream
argument_list|)
decl_stmt|;
name|MockEndpoint
name|mockJSON
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:jsonInlineOptions"
argument_list|)
decl_stmt|;
name|mockJSON
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mockJSON
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
expr_stmt|;
name|Object
name|json
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:marshalInlineOptions"
argument_list|,
name|in
argument_list|)
decl_stmt|;
name|String
name|jsonString
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|json
argument_list|)
decl_stmt|;
name|JSONObject
name|obj
init|=
operator|(
name|JSONObject
operator|)
name|JSONSerializer
operator|.
name|toJSON
argument_list|(
name|jsonString
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"JSON must contain 1 top-level element"
argument_list|,
literal|1
argument_list|,
name|obj
operator|.
name|entrySet
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Top-level element must be named root"
argument_list|,
name|obj
operator|.
name|has
argument_list|(
literal|"root"
argument_list|)
argument_list|)
expr_stmt|;
comment|// check that no child of the top-level element has a colon in its key,
comment|// which would denote that
comment|// a namespace prefix exists
for|for
control|(
name|Object
name|key
range|:
name|obj
operator|.
name|getJSONObject
argument_list|(
literal|"root"
argument_list|)
operator|.
name|keySet
argument_list|()
control|)
block|{
name|assertFalse
argument_list|(
literal|"A key contains a colon"
argument_list|,
operator|(
operator|(
name|String
operator|)
name|key
operator|)
operator|.
name|contains
argument_list|(
literal|":"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|mockJSON
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUnmarshalToXMLInlineOptions ()
specifier|public
name|void
name|testUnmarshalToXMLInlineOptions
parameter_list|()
throws|throws
name|Exception
block|{
name|InputStream
name|inStream
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"testMessage1.json"
argument_list|)
decl_stmt|;
name|String
name|in
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|inStream
argument_list|)
decl_stmt|;
name|MockEndpoint
name|mockXML
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:xmlInlineOptions"
argument_list|)
decl_stmt|;
name|mockXML
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mockXML
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|Object
name|marshalled
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:unmarshalInlineOptions"
argument_list|,
name|in
argument_list|)
decl_stmt|;
name|Document
name|document
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Document
operator|.
name|class
argument_list|,
name|marshalled
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"The XML document doesn't carry newRoot as the root name"
argument_list|,
literal|"newRoot"
argument_list|,
name|document
operator|.
name|getDocumentElement
argument_list|()
operator|.
name|getLocalName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The number of direct child elements of newRoot with tag d (expandable property) is not 3"
argument_list|,
literal|3
argument_list|,
name|document
operator|.
name|getDocumentElement
argument_list|()
operator|.
name|getElementsByTagName
argument_list|(
literal|"d"
argument_list|)
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The number of direct child elements of newRoot with tag e (expandable property) is not 3"
argument_list|,
literal|3
argument_list|,
name|document
operator|.
name|getDocumentElement
argument_list|()
operator|.
name|getElementsByTagName
argument_list|(
literal|"e"
argument_list|)
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
name|mockXML
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testJsonArraysToXml ()
specifier|public
name|void
name|testJsonArraysToXml
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mockXML
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:xmlInlineOptionsArray"
argument_list|)
decl_stmt|;
name|mockXML
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mockXML
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|Object
name|marshalled
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:unmarshalInlineOptionsArray"
argument_list|,
literal|"[1, 2, 3, 4]"
argument_list|)
decl_stmt|;
name|Document
name|document
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Document
operator|.
name|class
argument_list|,
name|marshalled
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"There should be exactly 4 XML elements with tag 'el' (each array element)"
argument_list|,
literal|4
argument_list|,
name|document
operator|.
name|getDocumentElement
argument_list|()
operator|.
name|getElementsByTagName
argument_list|(
literal|"el"
argument_list|)
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The document root should be named 'ar' (the array root)"
argument_list|,
literal|"ar"
argument_list|,
name|document
operator|.
name|getDocumentElement
argument_list|()
operator|.
name|getLocalName
argument_list|()
argument_list|)
expr_stmt|;
name|mockXML
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testXmlArraysToJson ()
specifier|public
name|void
name|testXmlArraysToJson
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mockJSON
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:jsonInlineOptionsArray"
argument_list|)
decl_stmt|;
name|mockJSON
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mockJSON
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
expr_stmt|;
name|Object
name|json
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:marshalInlineOptionsArray"
argument_list|,
literal|"<ar><el>1</el><el>2</el><el>3</el><el>4</el></ar>"
argument_list|)
decl_stmt|;
name|String
name|jsonString
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|json
argument_list|)
decl_stmt|;
name|JSONArray
name|array
init|=
operator|(
name|JSONArray
operator|)
name|JSONSerializer
operator|.
name|toJSON
argument_list|(
name|jsonString
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Expected a JSON array with string elements: 1, 2, 3, 4"
argument_list|,
name|array
operator|.
name|containsAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"1"
argument_list|,
literal|"2"
argument_list|,
literal|"3"
argument_list|,
literal|"4"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|mockJSON
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|XmlJsonDataFormat
name|format
init|=
operator|new
name|XmlJsonDataFormat
argument_list|()
decl_stmt|;
comment|// from XML to JSON
name|from
argument_list|(
literal|"direct:marshal"
argument_list|)
operator|.
name|marshal
argument_list|(
name|format
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:json"
argument_list|)
expr_stmt|;
comment|// from JSON to XML
name|from
argument_list|(
literal|"direct:unmarshal"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|format
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:xml"
argument_list|)
expr_stmt|;
comment|// from XML to JSON - inline dataformat
name|from
argument_list|(
literal|"direct:marshalInline"
argument_list|)
operator|.
name|marshal
argument_list|()
operator|.
name|xmljson
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:jsonInline"
argument_list|)
expr_stmt|;
comment|// from JSON to XML - inline dataformat
name|from
argument_list|(
literal|"direct:unmarshalInline"
argument_list|)
operator|.
name|unmarshal
argument_list|()
operator|.
name|xmljson
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:xmlInline"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|xmlJsonOptions
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|xmlJsonOptions
operator|.
name|put
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|dataformat
operator|.
name|XmlJsonDataFormat
operator|.
name|ENCODING
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
name|xmlJsonOptions
operator|.
name|put
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|dataformat
operator|.
name|XmlJsonDataFormat
operator|.
name|FORCE_TOP_LEVEL_OBJECT
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|xmlJsonOptions
operator|.
name|put
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|dataformat
operator|.
name|XmlJsonDataFormat
operator|.
name|TRIM_SPACES
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|xmlJsonOptions
operator|.
name|put
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|dataformat
operator|.
name|XmlJsonDataFormat
operator|.
name|ROOT_NAME
argument_list|,
literal|"newRoot"
argument_list|)
expr_stmt|;
name|xmlJsonOptions
operator|.
name|put
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|dataformat
operator|.
name|XmlJsonDataFormat
operator|.
name|SKIP_NAMESPACES
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|xmlJsonOptions
operator|.
name|put
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|dataformat
operator|.
name|XmlJsonDataFormat
operator|.
name|REMOVE_NAMESPACE_PREFIXES
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|xmlJsonOptions
operator|.
name|put
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|dataformat
operator|.
name|XmlJsonDataFormat
operator|.
name|EXPANDABLE_PROPERTIES
argument_list|,
literal|"d e"
argument_list|)
expr_stmt|;
comment|// from XML to JSON - inline dataformat w/ options
name|from
argument_list|(
literal|"direct:marshalInlineOptions"
argument_list|)
operator|.
name|marshal
argument_list|()
operator|.
name|xmljson
argument_list|(
name|xmlJsonOptions
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:jsonInlineOptions"
argument_list|)
expr_stmt|;
comment|// from JSON to XML - inline dataformat w/ options
name|from
argument_list|(
literal|"direct:unmarshalInlineOptions"
argument_list|)
operator|.
name|unmarshal
argument_list|()
operator|.
name|xmljson
argument_list|(
name|xmlJsonOptions
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:xmlInlineOptions"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|xmlJsonOptionsArrays
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|xmlJsonOptionsArrays
operator|.
name|put
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|dataformat
operator|.
name|XmlJsonDataFormat
operator|.
name|ELEMENT_NAME
argument_list|,
literal|"el"
argument_list|)
expr_stmt|;
name|xmlJsonOptionsArrays
operator|.
name|put
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|dataformat
operator|.
name|XmlJsonDataFormat
operator|.
name|ARRAY_NAME
argument_list|,
literal|"ar"
argument_list|)
expr_stmt|;
comment|// from XML arrays to JSON - inline dataformat w/ options
name|from
argument_list|(
literal|"direct:marshalInlineOptionsArray"
argument_list|)
operator|.
name|marshal
argument_list|()
operator|.
name|xmljson
argument_list|(
name|xmlJsonOptionsArrays
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:jsonInlineOptionsArray"
argument_list|)
expr_stmt|;
comment|// from JSON arrays to XML - inline dataformat w/ options
name|from
argument_list|(
literal|"direct:unmarshalInlineOptionsArray"
argument_list|)
operator|.
name|unmarshal
argument_list|()
operator|.
name|xmljson
argument_list|(
name|xmlJsonOptionsArrays
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:xmlInlineOptionsArray"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

