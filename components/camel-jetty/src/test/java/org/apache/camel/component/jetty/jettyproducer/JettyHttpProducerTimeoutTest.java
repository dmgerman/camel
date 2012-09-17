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
name|io
operator|.
name|ByteArrayInputStream
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
name|ExchangeTimedOutException
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
DECL|class|JettyHttpProducerTimeoutTest
specifier|public
class|class
name|JettyHttpProducerTimeoutTest
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
literal|"/timeout?httpClient.timeout=2000"
decl_stmt|;
annotation|@
name|Test
DECL|method|testTimeout ()
specifier|public
name|void
name|testTimeout
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
specifier|final
name|MyInputStream
name|is
init|=
operator|new
name|MyInputStream
argument_list|(
literal|"Content"
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|Exchange
name|reply
init|=
name|template
operator|.
name|request
argument_list|(
name|url
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|Exception
name|e
init|=
name|reply
operator|.
name|getException
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have thrown an exception"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|ExchangeTimedOutException
name|cause
init|=
name|assertIsInstanceOf
argument_list|(
name|ExchangeTimedOutException
operator|.
name|class
argument_list|,
name|e
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2000
argument_list|,
name|cause
operator|.
name|getTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"The input stream should be closed"
argument_list|,
name|is
operator|.
name|isClosed
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
name|delay
argument_list|(
literal|5000
argument_list|)
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
literal|"Bye World"
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

