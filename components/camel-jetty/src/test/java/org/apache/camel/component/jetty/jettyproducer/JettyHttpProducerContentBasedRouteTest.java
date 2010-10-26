begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jetty.jettyproducer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jetty
operator|.
name|jettyproducer
package|;
end_package

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
name|jetty
operator|.
name|BaseJettyTest
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
comment|/**  * Unit test with a simple route test.  */
end_comment

begin_class
DECL|class|JettyHttpProducerContentBasedRouteTest
specifier|public
class|class
name|JettyHttpProducerContentBasedRouteTest
extends|extends
name|BaseJettyTest
block|{
DECL|field|serverUri
specifier|private
name|String
name|serverUri
init|=
literal|"jetty://http://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/myservice"
decl_stmt|;
annotation|@
name|Test
DECL|method|testSendOne ()
specifier|public
name|void
name|testSendOne
parameter_list|()
throws|throws
name|Exception
block|{
comment|// these tests does not run well on Windows
if|if
condition|(
name|isPlatform
argument_list|(
literal|"windows"
argument_list|)
condition|)
block|{
return|return;
block|}
comment|// give Jetty time to startup properly
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:one"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"one"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|template
operator|.
name|requestBody
argument_list|(
name|serverUri
operator|+
literal|"?one=true"
argument_list|,
literal|null
argument_list|,
name|Object
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendOther ()
specifier|public
name|void
name|testSendOther
parameter_list|()
throws|throws
name|Exception
block|{
comment|// these tests does not run well on Windows
if|if
condition|(
name|isPlatform
argument_list|(
literal|"windows"
argument_list|)
condition|)
block|{
return|return;
block|}
comment|// give Jetty time to startup properly
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:other"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"two"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|template
operator|.
name|requestBody
argument_list|(
name|serverUri
operator|+
literal|"?two=true"
argument_list|,
literal|null
argument_list|,
name|Object
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
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
name|serverUri
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|()
operator|.
name|simple
argument_list|(
literal|"in.header.one"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:one"
argument_list|)
operator|.
name|otherwise
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:other"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

