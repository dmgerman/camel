begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
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
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanServer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|ObjectName
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|ManagedBrowseableEndpointAsXmlTest
specifier|public
class|class
name|ManagedBrowseableEndpointAsXmlTest
extends|extends
name|ManagementTestSupport
block|{
DECL|method|testBrowseableEndpointAsXml ()
specifier|public
name|void
name|testBrowseableEndpointAsXml
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|7
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<foo>Camel&gt; Donkey</foo>"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Camel> Donkey"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<foo>Camel&gt; Donkey</foo>"
argument_list|,
literal|"name"
argument_list|,
literal|"Me& You"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<foo>Camel&gt; Donkey</foo>"
argument_list|,
literal|"title"
argument_list|,
literal|"<title>Me&amp; You</title>"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Camel> Donkey"
argument_list|,
literal|"name"
argument_list|,
literal|"Me& You"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|123
argument_list|,
literal|"user"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"user"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"uid"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"title"
argument_list|,
literal|"Camel rocks"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<animal><name>Donkey</name><age>17</age></animal>"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|MBeanServer
name|mbeanServer
init|=
name|getMBeanServer
argument_list|()
decl_stmt|;
name|ObjectName
name|name
init|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=localhost/camel-1,type=endpoints,name=\"mock://result\""
argument_list|)
decl_stmt|;
name|String
name|out
init|=
operator|(
name|String
operator|)
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|name
argument_list|,
literal|"browseMessageAsXml"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|0
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.Integer"
block|}
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<message>\n<body type=\"java.lang.String\">&lt;foo&gt;Camel&amp;gt; Donkey&lt;/foo&gt;</body>\n</message>"
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|out
operator|=
operator|(
name|String
operator|)
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|name
argument_list|,
literal|"browseMessageAsXml"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|1
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.Integer"
block|}
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<message>\n<body type=\"java.lang.String\">Camel&gt; Donkey</body>\n</message>"
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|out
operator|=
operator|(
name|String
operator|)
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|name
argument_list|,
literal|"browseMessageAsXml"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|2
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.Integer"
block|}
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<message>\n<headers>\n<header key=\"name\" type=\"java.lang.String\">Me&amp; You</header>\n</headers>\n"
operator|+
literal|"<body type=\"java.lang.String\">&lt;foo&gt;Camel&amp;gt; Donkey&lt;/foo&gt;</body>\n</message>"
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|out
operator|=
operator|(
name|String
operator|)
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|name
argument_list|,
literal|"browseMessageAsXml"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|3
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.Integer"
block|}
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<message>\n<headers>\n<header key=\"title\" type=\"java.lang.String\">&lt;title&gt;Me&amp;amp; You&lt;/title&gt;</header>\n</headers>\n"
operator|+
literal|"<body type=\"java.lang.String\">&lt;foo&gt;Camel&amp;gt; Donkey&lt;/foo&gt;</body>\n</message>"
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|out
operator|=
operator|(
name|String
operator|)
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|name
argument_list|,
literal|"browseMessageAsXml"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|4
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.Integer"
block|}
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<message>\n<headers>\n<header key=\"name\" type=\"java.lang.String\">Me&amp; You</header>\n</headers>\n"
operator|+
literal|"<body type=\"java.lang.String\">Camel&gt; Donkey</body>\n</message>"
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|out
operator|=
operator|(
name|String
operator|)
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|name
argument_list|,
literal|"browseMessageAsXml"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|5
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.Integer"
block|}
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<message>\n<headers>\n<header key=\"user\" type=\"java.lang.Boolean\">true</header>\n</headers>\n"
operator|+
literal|"<body type=\"java.lang.Integer\">123</body>\n</message>"
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|out
operator|=
operator|(
name|String
operator|)
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|name
argument_list|,
literal|"browseMessageAsXml"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|6
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.Integer"
block|}
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<message>\n<headers>\n<header key=\"title\" type=\"java.lang.String\">Camel rocks</header>\n"
operator|+
literal|"<header key=\"uid\" type=\"java.lang.Integer\">123</header>\n"
operator|+
literal|"<header key=\"user\" type=\"java.lang.Boolean\">false</header>\n</headers>\n"
operator|+
literal|"<body type=\"java.lang.String\">&lt;animal&gt;&lt;name&gt;Donkey&lt;/name&gt;&lt;age&gt;17&lt;/age&gt;&lt;/animal&gt;</body>\n</message>"
argument_list|,
name|out
argument_list|)
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
name|context
operator|.
name|setUseBreadcrumb
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

