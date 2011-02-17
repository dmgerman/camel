begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|ContextTestSupport
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
name|Processor
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
name|impl
operator|.
name|JndiRegistry
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|LoadRouteFromXmlWithOnExceptionTest
specifier|public
class|class
name|LoadRouteFromXmlWithOnExceptionTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"myProcessor"
argument_list|,
operator|new
name|MyProcessor
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|testLoadRouteFromXmlWitOnException ()
specifier|public
name|void
name|testLoadRouteFromXmlWitOnException
parameter_list|()
throws|throws
name|Exception
block|{
name|InputStream
name|is
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"barOnExceptionRoute.xml"
argument_list|)
decl_stmt|;
name|RoutesDefinition
name|routes
init|=
name|context
operator|.
name|loadRoutesDefinition
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|context
operator|.
name|addRouteDefinitions
argument_list|(
name|routes
operator|.
name|getRoutes
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Loaded bar route should be there"
argument_list|,
name|context
operator|.
name|getRoute
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|context
operator|.
name|getRoutes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// test that loaded route works
name|getMockEndpoint
argument_list|(
literal|"mock:bar"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:error"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Kabom"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:bar"
argument_list|,
literal|"Bye World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:bar"
argument_list|,
literal|"Kabom"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|class|MyProcessor
specifier|private
specifier|final
class|class
name|MyProcessor
implements|implements
name|Processor
block|{
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"Kabom"
operator|.
name|equals
argument_list|(
name|body
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Damn"
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

