begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.cxf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|cxf
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|java
operator|.
name|util
operator|.
name|Properties
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
name|AvailablePortFinder
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
name|karaf
operator|.
name|AbstractFeatureTest
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|BusFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|frontend
operator|.
name|ClientProxy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|frontend
operator|.
name|ClientProxyFactoryBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|Option
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|junit
operator|.
name|PaxExam
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|tinybundles
operator|.
name|core
operator|.
name|InnerClassStrategy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|tinybundles
operator|.
name|core
operator|.
name|TinyBundle
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Bundle
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Constants
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|PaxExam
operator|.
name|class
argument_list|)
DECL|class|CamelCxfBeanInjectTest
specifier|public
class|class
name|CamelCxfBeanInjectTest
extends|extends
name|AbstractFeatureTest
block|{
DECL|field|PORT
specifier|private
specifier|static
specifier|final
name|int
name|PORT
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|(
literal|30000
argument_list|)
decl_stmt|;
DECL|field|ENDPOINT_ADDRESS
specifier|private
specifier|static
specifier|final
name|String
name|ENDPOINT_ADDRESS
init|=
name|String
operator|.
name|format
argument_list|(
literal|"http://localhost:%s/CamelCxfBeanInjectTest/router"
argument_list|,
name|PORT
argument_list|)
decl_stmt|;
annotation|@
name|Before
DECL|method|installBlueprintXML ()
specifier|public
name|void
name|installBlueprintXML
parameter_list|()
throws|throws
name|Exception
block|{
comment|// install the camel blueprint xml file we use in this test
name|URL
name|url
init|=
name|ObjectHelper
operator|.
name|loadResourceAsURL
argument_list|(
literal|"org/apache/camel/itest/cxf/CamelCxfBeanInjectTest.xml"
argument_list|,
name|CamelCxfBeanInjectTest
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
decl_stmt|;
name|Bundle
name|bundle
init|=
name|installBlueprintAsBundle
argument_list|(
literal|"CamelCxfBeanInjectTest"
argument_list|,
name|url
argument_list|,
literal|false
argument_list|,
name|b
lambda|->
block|{
operator|(
operator|(
name|TinyBundle
operator|)
name|b
operator|)
operator|.
name|add
argument_list|(
name|BeanInjectRouteBuilder
operator|.
name|class
argument_list|,
name|InnerClassStrategy
operator|.
name|NONE
argument_list|)
operator|.
name|add
argument_list|(
name|SimpleService
operator|.
name|class
argument_list|,
name|InnerClassStrategy
operator|.
name|NONE
argument_list|)
operator|.
name|add
argument_list|(
name|SimpleBean
operator|.
name|class
argument_list|,
name|InnerClassStrategy
operator|.
name|NONE
argument_list|)
operator|.
name|set
argument_list|(
name|Constants
operator|.
name|DYNAMICIMPORT_PACKAGE
argument_list|,
literal|"*"
argument_list|)
expr_stmt|;
block|}
argument_list|)
decl_stmt|;
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"router.address"
argument_list|,
name|ENDPOINT_ADDRESS
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"router.port"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|PORT
argument_list|)
argument_list|)
expr_stmt|;
name|overridePropertiesWithConfigAdmin
argument_list|(
literal|"my-placeholders"
argument_list|,
name|props
argument_list|)
expr_stmt|;
name|bundle
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Configuration
DECL|method|configure ()
specifier|public
name|Option
index|[]
name|configure
parameter_list|()
block|{
return|return
name|configure
argument_list|(
literal|"camel-test-karaf"
argument_list|,
literal|"camel-cxf"
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testReverseProxy ()
specifier|public
name|void
name|testReverseProxy
parameter_list|()
block|{
name|SimpleService
name|client
init|=
name|createClient
argument_list|()
decl_stmt|;
name|setHttpHeaders
argument_list|(
name|client
argument_list|,
literal|"X-Forwarded-Proto"
argument_list|,
literal|"https"
argument_list|)
expr_stmt|;
name|String
name|result
init|=
name|client
operator|.
name|op
argument_list|(
literal|"test"
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"Scheme should be set to 'https'"
argument_list|,
literal|"scheme: https, x-forwarded-proto: https"
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
DECL|method|setHttpHeaders (SimpleService client, String header, String value)
specifier|private
name|void
name|setHttpHeaders
parameter_list|(
name|SimpleService
name|client
parameter_list|,
name|String
name|header
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|header
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|client
argument_list|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|,
name|headers
argument_list|)
expr_stmt|;
block|}
DECL|method|createClient ()
specifier|private
name|SimpleService
name|createClient
parameter_list|()
block|{
name|ClientProxyFactoryBean
name|factory
init|=
operator|new
name|ClientProxyFactoryBean
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setAddress
argument_list|(
name|ENDPOINT_ADDRESS
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setServiceClass
argument_list|(
name|SimpleService
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setBus
argument_list|(
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
argument_list|)
expr_stmt|;
return|return
operator|(
name|SimpleService
operator|)
name|factory
operator|.
name|create
argument_list|()
return|;
block|}
block|}
end_class

end_unit

