begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.properties
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|properties
package|;
end_package

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
name|spring
operator|.
name|SpringTestSupport
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
name|spring
operator|.
name|spi
operator|.
name|BridgePropertyPlaceholderConfigurer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|AbstractXmlApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|CamelSpringPropertyPlaceholderConfigurer3Test
specifier|public
class|class
name|CamelSpringPropertyPlaceholderConfigurer3Test
extends|extends
name|SpringTestSupport
block|{
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
comment|// inside the used properties file (cheese.properties) we've defined the following key/value mapping:
comment|// hi2=Guten Tag
comment|// however as we make use of the PropertyPlaceholderConfigurer.SYSTEM_PROPERTIES_MODE_OVERRIDE mode
comment|// (which is NOT the default mode) we expect that setting the system property below should override
comment|// the mapping being defined above. that's we expect the following key/value mapping taking effect:
comment|// hi2=Gute Nacht
name|System
operator|.
name|setProperty
argument_list|(
literal|"hi2"
argument_list|,
literal|"Gute Nacht"
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
comment|// clear the property to avoid any side effect by the other tests
name|System
operator|.
name|clearProperty
argument_list|(
literal|"hi2"
argument_list|)
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/component/properties/CamelSpringPropertyPlaceholderConfigurer3Test.xml"
argument_list|)
return|;
block|}
DECL|method|testCamelSpringPropertyPlaceholderConfigurerTest ()
specifier|public
name|void
name|testCamelSpringPropertyPlaceholderConfigurerTest
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Gute Nacht Camel"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:bar"
argument_list|,
literal|"Camel"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
DECL|class|MyBridgePropertyPlaceholderConfigurer
specifier|private
specifier|static
class|class
name|MyBridgePropertyPlaceholderConfigurer
extends|extends
name|BridgePropertyPlaceholderConfigurer
block|{
annotation|@
name|Override
DECL|method|resolveProperties (CamelContext context, boolean ignoreMissingLocation, String... uri)
specifier|public
name|Properties
name|resolveProperties
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|boolean
name|ignoreMissingLocation
parameter_list|,
name|String
modifier|...
name|uri
parameter_list|)
throws|throws
name|Exception
block|{
name|Properties
name|answer
init|=
name|super
operator|.
name|resolveProperties
argument_list|(
name|context
argument_list|,
name|ignoreMissingLocation
argument_list|,
name|uri
argument_list|)
decl_stmt|;
comment|// define the additional properties we need to provide so that the uri "direct:{{foo}}" by the "from" clause
comment|// as well as "{{scheme}}{{separator}}{{context-path}}" by the "to" clause can be properly resolved. please
comment|// note that in this simple test we just add these properties hard-coded below but of course the mechanism to
comment|// retrieve these extra properties can be anything else, e.g. through the entries inside a database table etc.
name|answer
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|answer
operator|.
name|put
argument_list|(
literal|"scheme"
argument_list|,
literal|"mock"
argument_list|)
expr_stmt|;
name|answer
operator|.
name|put
argument_list|(
literal|"separator"
argument_list|,
literal|":"
argument_list|)
expr_stmt|;
name|answer
operator|.
name|put
argument_list|(
literal|"context-path"
argument_list|,
literal|"result"
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
block|}
end_class

end_unit

