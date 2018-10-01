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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Executors
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Future
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
name|RuntimeCamelException
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
name|http
operator|.
name|common
operator|.
name|HttpOperationFailedException
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

begin_class
annotation|@
name|Ignore
DECL|class|JettyHttpProducerSuspendWhileInProgressTest
specifier|public
class|class
name|JettyHttpProducerSuspendWhileInProgressTest
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
literal|"/cool"
decl_stmt|;
annotation|@
name|Test
DECL|method|testJettySuspendWhileInProgress ()
specifier|public
name|void
name|testJettySuspendWhileInProgress
parameter_list|()
throws|throws
name|Exception
block|{
comment|// these tests does not run well on AIX or Windows
if|if
condition|(
name|isPlatform
argument_list|(
literal|"aix"
argument_list|)
operator|||
name|isPlatform
argument_list|(
literal|"windows"
argument_list|)
condition|)
block|{
return|return;
block|}
name|context
operator|.
name|getShutdownStrategy
argument_list|()
operator|.
name|setTimeout
argument_list|(
literal|50
argument_list|)
expr_stmt|;
comment|// send a request/reply and have future handle so we can shutdown while in progress
name|Future
argument_list|<
name|String
argument_list|>
name|reply
init|=
name|template
operator|.
name|asyncRequestBodyAndHeader
argument_list|(
name|serverUri
argument_list|,
literal|null
argument_list|,
literal|"name"
argument_list|,
literal|"Camel"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// shutdown camel while in progress, wait 2 sec so the first req has been received in Camel route
name|Executors
operator|.
name|newSingleThreadExecutor
argument_list|()
operator|.
name|execute
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
argument_list|)
expr_stmt|;
comment|// wait a bit more before sending next
name|Thread
operator|.
name|sleep
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
comment|// now send a new req/reply
name|Future
argument_list|<
name|String
argument_list|>
name|reply2
init|=
name|template
operator|.
name|asyncRequestBodyAndHeader
argument_list|(
name|serverUri
argument_list|,
literal|null
argument_list|,
literal|"name"
argument_list|,
literal|"Tiger"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// the first should wait to have its reply returned
name|assertEquals
argument_list|(
literal|"Bye Camel"
argument_list|,
name|reply
operator|.
name|get
argument_list|(
literal|20
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
comment|// the 2nd should have a 503 returned as we are shutting down
try|try
block|{
name|reply2
operator|.
name|get
argument_list|(
literal|20
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should throw exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|RuntimeCamelException
name|rce
init|=
name|assertIsInstanceOf
argument_list|(
name|RuntimeCamelException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
decl_stmt|;
name|HttpOperationFailedException
name|hofe
init|=
name|assertIsInstanceOf
argument_list|(
name|HttpOperationFailedException
operator|.
name|class
argument_list|,
name|rce
operator|.
name|getCause
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|503
argument_list|,
name|hofe
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|serverUri
argument_list|)
operator|.
name|log
argument_list|(
literal|"Got data will wait 10 sec with reply"
argument_list|)
operator|.
name|delay
argument_list|(
literal|10000
argument_list|)
operator|.
name|transform
argument_list|(
name|simple
argument_list|(
literal|"Bye ${header.name}"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

