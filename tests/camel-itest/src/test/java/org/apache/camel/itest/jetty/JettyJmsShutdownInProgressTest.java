begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.jetty
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|jetty
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
name|junit
operator|.
name|framework
operator|.
name|Assert
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
name|ProducerTemplate
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
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Autowired
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|AbstractJUnit4SpringContextTests
import|;
end_import

begin_class
annotation|@
name|ContextConfiguration
DECL|class|JettyJmsShutdownInProgressTest
specifier|public
class|class
name|JettyJmsShutdownInProgressTest
extends|extends
name|AbstractJUnit4SpringContextTests
block|{
annotation|@
name|Autowired
DECL|field|camelContext
specifier|protected
name|CamelContext
name|camelContext
decl_stmt|;
annotation|@
name|Autowired
DECL|field|template
specifier|protected
name|ProducerTemplate
name|template
decl_stmt|;
annotation|@
name|Test
DECL|method|testShutdownInProgress ()
specifier|public
name|void
name|testShutdownInProgress
parameter_list|()
throws|throws
name|Exception
block|{
name|Future
argument_list|<
name|String
argument_list|>
name|reply1
init|=
name|template
operator|.
name|asyncRequestBody
argument_list|(
literal|"http://localhost:9002/test"
argument_list|,
literal|"World"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Future
argument_list|<
name|String
argument_list|>
name|reply2
init|=
name|template
operator|.
name|asyncRequestBody
argument_list|(
literal|"http://localhost:9002/test"
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
name|JettyJmsShutdownInProgressTest
operator|.
name|this
operator|.
name|camelContext
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
comment|// this one should fail
try|try
block|{
name|template
operator|.
name|requestBody
argument_list|(
literal|"http://localhost:9002/test"
argument_list|,
literal|"Tiger"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|fail
argument_list|(
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|HttpOperationFailedException
name|hofe
init|=
operator|(
name|HttpOperationFailedException
operator|)
name|e
operator|.
name|getCause
argument_list|()
decl_stmt|;
name|Assert
operator|.
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
comment|// but the 2 first should still return valid replies
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|reply1
operator|.
name|get
argument_list|(
literal|10
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"Bye Camel"
argument_list|,
name|reply2
operator|.
name|get
argument_list|(
literal|10
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

