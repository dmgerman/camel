begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Arrays
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
name|CamelContext
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
name|properties
operator|.
name|PropertiesComponent
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
name|rest
operator|.
name|DummyRestConsumerFactory
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
name|model
operator|.
name|rest
operator|.
name|CollectionFormat
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
name|model
operator|.
name|rest
operator|.
name|RestParamType
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

begin_class
DECL|class|ManagedFromRestPlaceholderTest
specifier|public
class|class
name|ManagedFromRestPlaceholderTest
extends|extends
name|ManagementTestSupport
block|{
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|answer
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|answer
operator|.
name|getRegistry
argument_list|()
operator|.
name|bind
argument_list|(
literal|"dummy-test"
argument_list|,
operator|new
name|DummyRestConsumerFactory
argument_list|()
argument_list|)
expr_stmt|;
name|PropertiesComponent
name|pc
init|=
operator|new
name|PropertiesComponent
argument_list|()
decl_stmt|;
name|pc
operator|.
name|setLocation
argument_list|(
literal|"classpath:org/apache/camel/management/rest.properties"
argument_list|)
expr_stmt|;
name|answer
operator|.
name|addComponent
argument_list|(
literal|"properties"
argument_list|,
name|pc
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
annotation|@
name|Test
DECL|method|testFromRestModelPlaceholder ()
specifier|public
name|void
name|testFromRestModelPlaceholder
parameter_list|()
throws|throws
name|Exception
block|{
comment|// JMX tests dont work well on AIX CI servers (hangs them)
if|if
condition|(
name|isPlatform
argument_list|(
literal|"aix"
argument_list|)
condition|)
block|{
return|return;
block|}
name|MBeanServer
name|mbeanServer
init|=
name|getMBeanServer
argument_list|()
decl_stmt|;
name|ObjectName
name|on
init|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=camel-1,type=context,name=\"camel-1\""
argument_list|)
decl_stmt|;
name|String
name|xml
init|=
operator|(
name|String
operator|)
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"dumpRestsAsXml"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|true
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"boolean"
block|}
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|xml
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
name|xml
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|xml
operator|.
name|contains
argument_list|(
literal|"<rests"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|xml
operator|.
name|contains
argument_list|(
literal|"<rest path=\"/say/hello\">"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|xml
operator|.
name|contains
argument_list|(
literal|"<rest path=\"/say/bye\">"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|xml
operator|.
name|contains
argument_list|(
literal|"</rest>"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|xml
operator|.
name|contains
argument_list|(
literal|"<get"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|xml
operator|.
name|contains
argument_list|(
literal|"application/json"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|xml
operator|.
name|contains
argument_list|(
literal|"<post"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|xml
operator|.
name|contains
argument_list|(
literal|"application/json"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|xml
operator|.
name|contains
argument_list|(
literal|"</rests>"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|xml
operator|.
name|contains
argument_list|(
literal|"<param collectionFormat=\"multi\" dataType=\"string\" defaultValue=\"b\" description=\"header param description2\" "
operator|+
literal|"name=\"header_letter\" required=\"false\" type=\"query\">"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|xml
operator|.
name|contains
argument_list|(
literal|"<param dataType=\"integer\" defaultValue=\"1\" description=\"header param description1\" "
operator|+
literal|"name=\"header_count\" required=\"true\" type=\"header\">"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|xml
operator|.
name|contains
argument_list|(
literal|"<value>1</value>"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|xml
operator|.
name|contains
argument_list|(
literal|"<value>a</value>"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|xml
operator|.
name|contains
argument_list|(
literal|"<responseMessage code=\"300\" message=\"test msg\" responseModel=\"java.lang.Integer\"/>"
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|xml2
init|=
operator|(
name|String
operator|)
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"dumpRoutesAsXml"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
name|xml2
argument_list|)
expr_stmt|;
comment|// and we should have rest in the routes that indicate its from a rest dsl
name|assertTrue
argument_list|(
name|xml2
operator|.
name|contains
argument_list|(
literal|"rest=\"true\""
argument_list|)
argument_list|)
expr_stmt|;
comment|// there should be 3 + 2 routes
name|assertEquals
argument_list|(
literal|3
operator|+
literal|2
argument_list|,
name|context
operator|.
name|getRouteDefinitions
argument_list|()
operator|.
name|size
argument_list|()
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
name|restConfiguration
argument_list|()
operator|.
name|host
argument_list|(
literal|"localhost"
argument_list|)
expr_stmt|;
name|rest
argument_list|(
literal|"{{foo}}"
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|to
argument_list|(
literal|"direct:hello"
argument_list|)
expr_stmt|;
name|rest
argument_list|(
literal|"{{bar}}"
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|consumes
argument_list|(
literal|"application/json"
argument_list|)
operator|.
name|param
argument_list|()
operator|.
name|type
argument_list|(
name|RestParamType
operator|.
name|header
argument_list|)
operator|.
name|description
argument_list|(
literal|"header param description1"
argument_list|)
operator|.
name|dataType
argument_list|(
literal|"integer"
argument_list|)
operator|.
name|allowableValues
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
operator|.
name|defaultValue
argument_list|(
literal|"1"
argument_list|)
operator|.
name|name
argument_list|(
literal|"header_count"
argument_list|)
operator|.
name|required
argument_list|(
literal|true
argument_list|)
operator|.
name|endParam
argument_list|()
operator|.
name|param
argument_list|()
operator|.
name|type
argument_list|(
name|RestParamType
operator|.
name|query
argument_list|)
operator|.
name|description
argument_list|(
literal|"header param description2"
argument_list|)
operator|.
name|dataType
argument_list|(
literal|"string"
argument_list|)
operator|.
name|allowableValues
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"c"
argument_list|,
literal|"d"
argument_list|)
argument_list|)
operator|.
name|defaultValue
argument_list|(
literal|"b"
argument_list|)
operator|.
name|collectionFormat
argument_list|(
name|CollectionFormat
operator|.
name|multi
argument_list|)
operator|.
name|name
argument_list|(
literal|"header_letter"
argument_list|)
operator|.
name|required
argument_list|(
literal|false
argument_list|)
operator|.
name|endParam
argument_list|()
operator|.
name|responseMessage
argument_list|()
operator|.
name|code
argument_list|(
literal|300
argument_list|)
operator|.
name|message
argument_list|(
literal|"test msg"
argument_list|)
operator|.
name|responseModel
argument_list|(
name|Integer
operator|.
name|class
argument_list|)
operator|.
name|endResponseMessage
argument_list|()
operator|.
name|to
argument_list|(
literal|"direct:bye"
argument_list|)
operator|.
name|post
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:update"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:hello"
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|constant
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:bye"
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|constant
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

