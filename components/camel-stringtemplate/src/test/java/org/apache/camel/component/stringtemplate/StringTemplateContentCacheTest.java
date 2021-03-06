begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.stringtemplate
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|stringtemplate
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|Exchange
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
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
comment|/**  * Unit test the cahce when reloading .tm files in the classpath  */
end_comment

begin_class
DECL|class|StringTemplateContentCacheTest
specifier|public
class|class
name|StringTemplateContentCacheTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
comment|// create a tm file in the classpath as this is the tricky reloading stuff
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/test-classes/org/apache/camel/component/stringtemplate"
argument_list|,
literal|"Hello<headers.name>"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.tm"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|useJmx ()
specifier|public
name|boolean
name|useJmx
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Test
DECL|method|testNotCached ()
specifier|public
name|void
name|testNotCached
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello London"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:a"
argument_list|,
literal|"Body"
argument_list|,
literal|"name"
argument_list|,
literal|"London"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
comment|// now change content in the file in the classpath and try again
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/test-classes/org/apache/camel/component/stringtemplate"
argument_list|,
literal|"Bye<headers.name>"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.tm"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|reset
argument_list|()
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye Paris"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:a"
argument_list|,
literal|"Body"
argument_list|,
literal|"name"
argument_list|,
literal|"Paris"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCached ()
specifier|public
name|void
name|testCached
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello London"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:b"
argument_list|,
literal|"Body"
argument_list|,
literal|"name"
argument_list|,
literal|"London"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
comment|// now change content in the file in the classpath and try again
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/test-classes/org/apache/camel/component/stringtemplate"
argument_list|,
literal|"Bye<headers.name>"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.tm"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|reset
argument_list|()
expr_stmt|;
comment|// we must expected the original filecontent as the cache is enabled, so its Hello and not Bye
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello Paris"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:b"
argument_list|,
literal|"Body"
argument_list|,
literal|"name"
argument_list|,
literal|"Paris"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testClearCacheViaJmx ()
specifier|public
name|void
name|testClearCacheViaJmx
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello London"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:b"
argument_list|,
literal|"Body"
argument_list|,
literal|"name"
argument_list|,
literal|"London"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
comment|// now change content in the file in the classpath and try again
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/test-classes/org/apache/camel/component/stringtemplate"
argument_list|,
literal|"Bye<headers.name>"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.tm"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|reset
argument_list|()
expr_stmt|;
comment|// we must expected the original filecontent as the cache is enabled, so its Hello and not Bye
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello Paris"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:b"
argument_list|,
literal|"Body"
argument_list|,
literal|"name"
argument_list|,
literal|"Paris"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
comment|// clear the cache using jmx
name|MBeanServer
name|mbeanServer
init|=
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementAgent
argument_list|()
operator|.
name|getMBeanServer
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|ObjectName
argument_list|>
name|objNameSet
init|=
name|mbeanServer
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
literal|"org.apache.camel:type=endpoints,name=\"string-template:*contentCache=true*\",*"
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|ObjectName
name|managedObjName
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|objNameSet
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|managedObjName
argument_list|,
literal|"clearContentCache"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|mock
operator|.
name|reset
argument_list|()
expr_stmt|;
comment|// we expect that the new resource will be set as the cached value, since the cache has been cleared
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye Paris"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/test-classes/org/apache/camel/component/stringtemplate"
argument_list|,
literal|"Bye<headers.name>"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.tm"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:b"
argument_list|,
literal|"Body"
argument_list|,
literal|"name"
argument_list|,
literal|"Paris"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|mock
operator|.
name|reset
argument_list|()
expr_stmt|;
comment|// we expect that the cached value will not be replaced by a different resource since the cache is now re-established
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye Paris"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/test-classes/org/apache/camel/component/stringtemplate"
argument_list|,
literal|"Hello<headers.name>"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.tm"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:b"
argument_list|,
literal|"Body"
argument_list|,
literal|"name"
argument_list|,
literal|"Paris"
argument_list|)
expr_stmt|;
name|mock
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
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|to
argument_list|(
literal|"string-template://org/apache/camel/component/stringtemplate/hello.tm?contentCache=false"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:b"
argument_list|)
operator|.
name|to
argument_list|(
literal|"string-template://org/apache/camel/component/stringtemplate/hello.tm?contentCache=true"
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

