begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.catalog
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|catalog
package|;
end_package

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
name|List
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
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|JsonNode
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|ObjectMapper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
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

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|catalog
operator|.
name|CatalogHelper
operator|.
name|loadText
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_class
DECL|class|CamelCatalogTest
specifier|public
class|class
name|CamelCatalogTest
block|{
DECL|field|catalog
specifier|private
name|CamelCatalog
name|catalog
init|=
operator|new
name|DefaultCamelCatalog
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testGetVersion ()
specifier|public
name|void
name|testGetVersion
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|version
init|=
name|catalog
operator|.
name|getCatalogVersion
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|version
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFindLanguageNames ()
specifier|public
name|void
name|testFindLanguageNames
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
name|catalog
operator|.
name|findLanguageNames
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|names
operator|.
name|contains
argument_list|(
literal|"el"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|names
operator|.
name|contains
argument_list|(
literal|"simple"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|names
operator|.
name|contains
argument_list|(
literal|"spel"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|names
operator|.
name|contains
argument_list|(
literal|"xpath"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFindNames ()
specifier|public
name|void
name|testFindNames
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
name|catalog
operator|.
name|findComponentNames
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|names
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|names
operator|.
name|contains
argument_list|(
literal|"file"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|names
operator|.
name|contains
argument_list|(
literal|"log"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|names
operator|.
name|contains
argument_list|(
literal|"docker"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|names
operator|.
name|contains
argument_list|(
literal|"jms"
argument_list|)
argument_list|)
expr_stmt|;
name|names
operator|=
name|catalog
operator|.
name|findDataFormatNames
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|names
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|names
operator|.
name|contains
argument_list|(
literal|"bindy-csv"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|names
operator|.
name|contains
argument_list|(
literal|"hl7"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|names
operator|.
name|contains
argument_list|(
literal|"jaxb"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|names
operator|.
name|contains
argument_list|(
literal|"syslog"
argument_list|)
argument_list|)
expr_stmt|;
name|names
operator|=
name|catalog
operator|.
name|findLanguageNames
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|names
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|names
operator|.
name|contains
argument_list|(
literal|"simple"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|names
operator|.
name|contains
argument_list|(
literal|"groovy"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|names
operator|.
name|contains
argument_list|(
literal|"mvel"
argument_list|)
argument_list|)
expr_stmt|;
name|names
operator|=
name|catalog
operator|.
name|findModelNames
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|names
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|names
operator|.
name|contains
argument_list|(
literal|"from"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|names
operator|.
name|contains
argument_list|(
literal|"to"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|names
operator|.
name|contains
argument_list|(
literal|"recipientList"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|names
operator|.
name|contains
argument_list|(
literal|"aggregate"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|names
operator|.
name|contains
argument_list|(
literal|"split"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|names
operator|.
name|contains
argument_list|(
literal|"loadBalance"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testJsonSchema ()
specifier|public
name|void
name|testJsonSchema
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|schema
init|=
name|catalog
operator|.
name|componentJSonSchema
argument_list|(
literal|"docker"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|schema
argument_list|)
expr_stmt|;
name|schema
operator|=
name|catalog
operator|.
name|dataFormatJSonSchema
argument_list|(
literal|"hl7"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|schema
argument_list|)
expr_stmt|;
name|schema
operator|=
name|catalog
operator|.
name|languageJSonSchema
argument_list|(
literal|"groovy"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|schema
argument_list|)
expr_stmt|;
name|schema
operator|=
name|catalog
operator|.
name|modelJSonSchema
argument_list|(
literal|"aggregate"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|schema
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testXmlSchema ()
specifier|public
name|void
name|testXmlSchema
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|schema
init|=
name|catalog
operator|.
name|blueprintSchemaAsXml
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|schema
argument_list|)
expr_stmt|;
name|schema
operator|=
name|catalog
operator|.
name|springSchemaAsXml
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|schema
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testArchetypeCatalog ()
specifier|public
name|void
name|testArchetypeCatalog
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|schema
init|=
name|catalog
operator|.
name|archetypeCatalogAsXml
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|schema
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAsEndpointUriMapFile ()
specifier|public
name|void
name|testAsEndpointUriMapFile
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
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
name|map
operator|.
name|put
argument_list|(
literal|"directoryName"
argument_list|,
literal|"src/data/inbox"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"noop"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"delay"
argument_list|,
literal|"5000"
argument_list|)
expr_stmt|;
name|String
name|uri
init|=
name|catalog
operator|.
name|asEndpointUri
argument_list|(
literal|"file"
argument_list|,
name|map
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"file:src/data/inbox?delay=5000&noop=true"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
name|String
name|uri2
init|=
name|catalog
operator|.
name|asEndpointUriXml
argument_list|(
literal|"file"
argument_list|,
name|map
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"file:src/data/inbox?delay=5000&amp;noop=true"
argument_list|,
name|uri2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAsEndpointUriMapFtp ()
specifier|public
name|void
name|testAsEndpointUriMapFtp
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
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
name|map
operator|.
name|put
argument_list|(
literal|"host"
argument_list|,
literal|"someserver"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"port"
argument_list|,
literal|"21"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"directoryName"
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"connectTimeout"
argument_list|,
literal|"5000"
argument_list|)
expr_stmt|;
name|String
name|uri
init|=
name|catalog
operator|.
name|asEndpointUri
argument_list|(
literal|"ftp"
argument_list|,
name|map
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"ftp:someserver:21/foo?connectTimeout=5000"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
name|String
name|uri2
init|=
name|catalog
operator|.
name|asEndpointUriXml
argument_list|(
literal|"ftp"
argument_list|,
name|map
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"ftp:someserver:21/foo?connectTimeout=5000"
argument_list|,
name|uri2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAsEndpointUriMapJms ()
specifier|public
name|void
name|testAsEndpointUriMapJms
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
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
name|map
operator|.
name|put
argument_list|(
literal|"destinationType"
argument_list|,
literal|"queue"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"destinationName"
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|String
name|uri
init|=
name|catalog
operator|.
name|asEndpointUri
argument_list|(
literal|"jms"
argument_list|,
name|map
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"jms:queue:foo"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAsEndpointUriNetty4http ()
specifier|public
name|void
name|testAsEndpointUriNetty4http
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
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
comment|// use default protocol
name|map
operator|.
name|put
argument_list|(
literal|"host"
argument_list|,
literal|"localhost"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"port"
argument_list|,
literal|"8080"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"path"
argument_list|,
literal|"foo/bar"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"disconnect"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|String
name|uri
init|=
name|catalog
operator|.
name|asEndpointUri
argument_list|(
literal|"netty4-http"
argument_list|,
name|map
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"netty4-http:http:localhost:8080/foo/bar?disconnect=true"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
comment|// lets add a protocol
name|map
operator|.
name|put
argument_list|(
literal|"protocol"
argument_list|,
literal|"https"
argument_list|)
expr_stmt|;
name|uri
operator|=
name|catalog
operator|.
name|asEndpointUri
argument_list|(
literal|"netty4-http"
argument_list|,
name|map
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"netty4-http:https:localhost:8080/foo/bar?disconnect=true"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
comment|// lets set a query parameter in the path
name|map
operator|.
name|put
argument_list|(
literal|"path"
argument_list|,
literal|"foo/bar?verbose=true"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"disconnect"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|uri
operator|=
name|catalog
operator|.
name|asEndpointUri
argument_list|(
literal|"netty4-http"
argument_list|,
name|map
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"netty4-http:https:localhost:8080/foo/bar?verbose=true&disconnect=true"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAsEndpointUriTimer ()
specifier|public
name|void
name|testAsEndpointUriTimer
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
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
name|map
operator|.
name|put
argument_list|(
literal|"timerName"
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"period"
argument_list|,
literal|"5000"
argument_list|)
expr_stmt|;
name|String
name|uri
init|=
name|catalog
operator|.
name|asEndpointUri
argument_list|(
literal|"timer"
argument_list|,
name|map
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"timer:foo?period=5000"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAsEndpointUriPropertiesPlaceholders ()
specifier|public
name|void
name|testAsEndpointUriPropertiesPlaceholders
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
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
name|map
operator|.
name|put
argument_list|(
literal|"timerName"
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"period"
argument_list|,
literal|"{{howoften}}"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"repeatCount"
argument_list|,
literal|"5"
argument_list|)
expr_stmt|;
name|String
name|uri
init|=
name|catalog
operator|.
name|asEndpointUri
argument_list|(
literal|"timer"
argument_list|,
name|map
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"timer:foo?period=%7B%7Bhowoften%7D%7D&repeatCount=5"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
name|uri
operator|=
name|catalog
operator|.
name|asEndpointUri
argument_list|(
literal|"timer"
argument_list|,
name|map
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"timer:foo?period={{howoften}}&repeatCount=5"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAsEndpointUriBeanLookup ()
specifier|public
name|void
name|testAsEndpointUriBeanLookup
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
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
name|map
operator|.
name|put
argument_list|(
literal|"resourceUri"
argument_list|,
literal|"foo.xslt"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"converter"
argument_list|,
literal|"#myConverter"
argument_list|)
expr_stmt|;
name|String
name|uri
init|=
name|catalog
operator|.
name|asEndpointUri
argument_list|(
literal|"xslt"
argument_list|,
name|map
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"xslt:foo.xslt?converter=%23myConverter"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
name|uri
operator|=
name|catalog
operator|.
name|asEndpointUri
argument_list|(
literal|"xslt"
argument_list|,
name|map
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"xslt:foo.xslt?converter=#myConverter"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAsEndpointUriMapJmsRequiredOnly ()
specifier|public
name|void
name|testAsEndpointUriMapJmsRequiredOnly
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
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
name|map
operator|.
name|put
argument_list|(
literal|"destinationName"
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|String
name|uri
init|=
name|catalog
operator|.
name|asEndpointUri
argument_list|(
literal|"jms"
argument_list|,
name|map
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"jms:foo"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"deliveryPersistent"
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"allowNullBody"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|uri
operator|=
name|catalog
operator|.
name|asEndpointUri
argument_list|(
literal|"jms"
argument_list|,
name|map
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"jms:foo?allowNullBody=true&deliveryPersistent=false"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
name|String
name|uri2
init|=
name|catalog
operator|.
name|asEndpointUriXml
argument_list|(
literal|"jms"
argument_list|,
name|map
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"jms:foo?allowNullBody=true&amp;deliveryPersistent=false"
argument_list|,
name|uri2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAsEndpointUriJson ()
specifier|public
name|void
name|testAsEndpointUriJson
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|json
init|=
name|loadText
argument_list|(
name|CamelCatalogTest
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"sample.json"
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|uri
init|=
name|catalog
operator|.
name|asEndpointUri
argument_list|(
literal|"ftp"
argument_list|,
name|json
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"ftp:someserver:21/foo?connectTimeout=5000"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testEndpointProperties ()
specifier|public
name|void
name|testEndpointProperties
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
name|catalog
operator|.
name|endpointProperties
argument_list|(
literal|"ftp:someserver:21/foo?connectTimeout=5000"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"someserver"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"host"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"21"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"port"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"directoryName"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"5000"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"connectTimeout"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testEndpointPropertiesPlaceholders ()
specifier|public
name|void
name|testEndpointPropertiesPlaceholders
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
name|catalog
operator|.
name|endpointProperties
argument_list|(
literal|"timer:foo?period={{howoften}}&repeatCount=5"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"timerName"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"{{howoften}}"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"period"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"5"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"repeatCount"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|Ignore
argument_list|(
literal|"TODO: fix me later"
argument_list|)
DECL|method|testEndpointPropertiesNetty4http ()
specifier|public
name|void
name|testEndpointPropertiesNetty4http
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
name|catalog
operator|.
name|endpointProperties
argument_list|(
literal|"netty4-http:http:localhost:8080/foo/bar?disconnect=true&keepAlive=false"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"protocol"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"localhost"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"host"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"8080"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"port"
argument_list|)
argument_list|)
expr_stmt|;
comment|// TODO: fix me later
comment|//assertEquals("foo/bar", map.get("path"));
name|assertEquals
argument_list|(
literal|"true"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"disconnect"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"false"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"keepAlive"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testEndpointPropertiesJms ()
specifier|public
name|void
name|testEndpointPropertiesJms
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
name|catalog
operator|.
name|endpointProperties
argument_list|(
literal|"jms:queue:foo"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"queue"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"destinationType"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"destinationName"
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|=
name|catalog
operator|.
name|endpointProperties
argument_list|(
literal|"jms:foo"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"destinationName"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testEndpointPropertiesJmsRequired ()
specifier|public
name|void
name|testEndpointPropertiesJmsRequired
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
name|catalog
operator|.
name|endpointProperties
argument_list|(
literal|"jms:foo"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"destinationName"
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|=
name|catalog
operator|.
name|endpointProperties
argument_list|(
literal|"jms:foo?allowNullBody=true&deliveryPersistent=false"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"destinationName"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"true"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"allowNullBody"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"false"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"deliveryPersistent"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testEndpointPropertiesAtom ()
specifier|public
name|void
name|testEndpointPropertiesAtom
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
name|catalog
operator|.
name|endpointProperties
argument_list|(
literal|"atom:file:src/test/data/feed.atom"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"file:src/test/data/feed.atom"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"feedUri"
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|=
name|catalog
operator|.
name|endpointProperties
argument_list|(
literal|"atom:file:src/test/data/feed.atom?splitEntries=false&delay=5000"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"file:src/test/data/feed.atom"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"feedUri"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"false"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"splitEntries"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"5000"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"delay"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testEndpointComponentName ()
specifier|public
name|void
name|testEndpointComponentName
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|name
init|=
name|catalog
operator|.
name|endpointComponentName
argument_list|(
literal|"jms:queue:foo"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"jms"
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testListComponentsAsJson ()
specifier|public
name|void
name|testListComponentsAsJson
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|json
init|=
name|catalog
operator|.
name|listComponentsAsJson
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|json
argument_list|)
expr_stmt|;
comment|// validate we can parse the json
name|ObjectMapper
name|mapper
init|=
operator|new
name|ObjectMapper
argument_list|()
decl_stmt|;
name|JsonNode
name|tree
init|=
name|mapper
operator|.
name|readTree
argument_list|(
name|json
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|tree
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testListDataFormatsAsJson ()
specifier|public
name|void
name|testListDataFormatsAsJson
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|json
init|=
name|catalog
operator|.
name|listDataFormatsAsJson
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|json
argument_list|)
expr_stmt|;
comment|// validate we can parse the json
name|ObjectMapper
name|mapper
init|=
operator|new
name|ObjectMapper
argument_list|()
decl_stmt|;
name|JsonNode
name|tree
init|=
name|mapper
operator|.
name|readTree
argument_list|(
name|json
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|tree
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testListLanguagesAsJson ()
specifier|public
name|void
name|testListLanguagesAsJson
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|json
init|=
name|catalog
operator|.
name|listLanguagesAsJson
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|json
argument_list|)
expr_stmt|;
comment|// validate we can parse the json
name|ObjectMapper
name|mapper
init|=
operator|new
name|ObjectMapper
argument_list|()
decl_stmt|;
name|JsonNode
name|tree
init|=
name|mapper
operator|.
name|readTree
argument_list|(
name|json
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|tree
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testListModelsAsJson ()
specifier|public
name|void
name|testListModelsAsJson
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|json
init|=
name|catalog
operator|.
name|listModelsAsJson
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|json
argument_list|)
expr_stmt|;
comment|// validate we can parse the json
name|ObjectMapper
name|mapper
init|=
operator|new
name|ObjectMapper
argument_list|()
decl_stmt|;
name|JsonNode
name|tree
init|=
name|mapper
operator|.
name|readTree
argument_list|(
name|json
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|tree
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSummaryAsJson ()
specifier|public
name|void
name|testSummaryAsJson
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|json
init|=
name|catalog
operator|.
name|summaryAsJson
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|json
argument_list|)
expr_stmt|;
comment|// validate we can parse the json
name|ObjectMapper
name|mapper
init|=
operator|new
name|ObjectMapper
argument_list|()
decl_stmt|;
name|JsonNode
name|tree
init|=
name|mapper
operator|.
name|readTree
argument_list|(
name|json
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|tree
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

