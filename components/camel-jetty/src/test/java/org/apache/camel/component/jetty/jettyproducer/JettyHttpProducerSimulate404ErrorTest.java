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
name|http
operator|.
name|HttpOperationFailedException
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
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|JettyHttpProducerSimulate404ErrorTest
specifier|public
class|class
name|JettyHttpProducerSimulate404ErrorTest
extends|extends
name|BaseJettyTest
block|{
DECL|field|url
specifier|private
name|String
name|url
init|=
literal|"jetty://http://0.0.0.0:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/bar"
decl_stmt|;
annotation|@
name|Test
DECL|method|test404 ()
specifier|public
name|void
name|test404
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
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
name|url
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
name|HttpOperationFailedException
name|cause
init|=
name|assertIsInstanceOf
argument_list|(
name|HttpOperationFailedException
operator|.
name|class
argument_list|,
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|404
argument_list|,
name|cause
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http//0.0.0.0:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/bar"
argument_list|,
name|cause
operator|.
name|getUri
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Page not found"
argument_list|,
name|cause
operator|.
name|getResponseBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|cause
operator|.
name|getResponseHeaders
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
name|from
argument_list|(
name|url
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
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
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Page not found"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|,
literal|404
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

