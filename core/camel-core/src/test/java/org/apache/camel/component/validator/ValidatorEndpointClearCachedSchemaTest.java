begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.validator
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|validator
package|;
end_package

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|concurrent
operator|.
name|ExecutorService
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
name|Endpoint
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
name|impl
operator|.
name|DefaultCamelContext
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Tests whether the ValidatorEndpoint.clearCachedSchema() can be executed when  * several sender threads are running.  */
end_comment

begin_class
DECL|class|ValidatorEndpointClearCachedSchemaTest
specifier|public
class|class
name|ValidatorEndpointClearCachedSchemaTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ValidatorEndpointClearCachedSchemaTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|context
specifier|private
name|CamelContext
name|context
decl_stmt|;
annotation|@
name|Test
DECL|method|testClearCachedSchema ()
specifier|public
name|void
name|testClearCachedSchema
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
comment|// send one message for start up to finish.
operator|new
name|Sender
argument_list|()
operator|.
name|run
argument_list|()
expr_stmt|;
comment|// send with 5 sender threads in parallel and call clear cache in
comment|// between
name|ExecutorService
name|senderPool
init|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
literal|5
argument_list|)
decl_stmt|;
name|ExecutorService
name|executorClearCache
init|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
literal|1
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|5
condition|;
name|i
operator|++
control|)
block|{
name|senderPool
operator|.
name|execute
argument_list|(
operator|new
name|Sender
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|i
operator|==
literal|2
condition|)
block|{
comment|/**                  * The clear cache thread calls xsdEndpoint.clearCachedSchema                  */
name|executorClearCache
operator|.
name|execute
argument_list|(
operator|new
name|ClearCache
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|senderPool
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|executorClearCache
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|senderPool
operator|.
name|awaitTermination
argument_list|(
literal|2
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|exchanges
init|=
name|mock
operator|.
name|getExchanges
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|exchanges
argument_list|)
expr_stmt|;
comment|// expect at least 5 correct sent messages, the messages sent before
comment|// the clearCacheSchema method is called will fail with a validation
comment|// error and will nor result in an exchange
name|assertTrue
argument_list|(
literal|"Less then expected exchanges"
argument_list|,
name|exchanges
operator|.
name|size
argument_list|()
operator|>
literal|5
argument_list|)
expr_stmt|;
block|}
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
name|String
name|handlerPackageSystemProp
init|=
literal|"java.protocol.handler.pkgs"
decl_stmt|;
name|String
name|customUrlHandlerPackage
init|=
literal|"org.apache.camel.urlhandler"
decl_stmt|;
name|registerSystemProperty
argument_list|(
name|handlerPackageSystemProp
argument_list|,
name|customUrlHandlerPackage
argument_list|,
literal|"|"
argument_list|)
expr_stmt|;
return|return
operator|new
name|DefaultCamelContext
argument_list|()
return|;
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
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"validator:pd:somefile.xsd"
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:after"
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
DECL|class|Sender
specifier|private
class|class
name|Sender
implements|implements
name|Runnable
block|{
DECL|field|message
specifier|private
specifier|final
name|String
name|message
init|=
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
operator|+
comment|//
literal|"<p:TestMessage xmlns:p=\"http://apache.camel.org/test\">"
operator|+
comment|//
literal|"<MessageContent>MessageContent</MessageContent>"
operator|+
comment|//
literal|"</p:TestMessage>"
decl_stmt|;
DECL|field|messageBytes
specifier|private
specifier|final
name|byte
index|[]
name|messageBytes
init|=
name|message
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
comment|// send up to 5 messages
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
literal|5
condition|;
name|j
operator|++
control|)
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|messageBytes
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|class|ClearCache
specifier|private
class|class
name|ClearCache
implements|implements
name|Runnable
block|{
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
comment|// start later after the first sender
comment|// threads are running
name|Thread
operator|.
name|sleep
argument_list|(
literal|200
argument_list|)
expr_stmt|;
name|clearCachedSchema
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
DECL|method|clearCachedSchema ()
specifier|private
name|void
name|clearCachedSchema
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|this
operator|.
name|context
decl_stmt|;
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|Collection
argument_list|<
name|Endpoint
argument_list|>
name|endpoints
init|=
name|context
operator|.
name|getEndpoints
argument_list|()
decl_stmt|;
for|for
control|(
name|Endpoint
name|endpoint
range|:
name|endpoints
control|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Endpoint URI: "
operator|+
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"validator:"
argument_list|)
condition|)
block|{
name|ValidatorEndpoint
name|xsltEndpoint
init|=
operator|(
name|ValidatorEndpoint
operator|)
name|endpoint
decl_stmt|;
name|xsltEndpoint
operator|.
name|clearCachedSchema
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"schema cache cleared"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

