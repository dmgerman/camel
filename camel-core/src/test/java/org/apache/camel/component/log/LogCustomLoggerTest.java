begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.log
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|log
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
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
name|ResolveEndpointFailedException
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
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|PropertyPlaceholderDelegateRegistry
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
name|SimpleRegistry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|logging
operator|.
name|log4j
operator|.
name|Level
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
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|equalTo
import|;
end_import

begin_comment
comment|/**  * Custom Logger test.  */
end_comment

begin_class
DECL|class|LogCustomLoggerTest
specifier|public
class|class
name|LogCustomLoggerTest
extends|extends
name|ContextTestSupport
block|{
comment|// to capture the logs
DECL|field|sw1
specifier|private
specifier|static
name|StringWriter
name|sw1
decl_stmt|;
comment|// to capture the warnings from LogComponent
DECL|field|sw2
specifier|private
specifier|static
name|StringWriter
name|sw2
decl_stmt|;
annotation|@
name|Before
annotation|@
name|Override
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
name|sw1
operator|=
operator|new
name|StringWriter
argument_list|()
expr_stmt|;
name|sw2
operator|=
operator|new
name|StringWriter
argument_list|()
expr_stmt|;
name|ConsumingAppender
operator|.
name|newAppender
argument_list|(
name|LogCustomLoggerTest
operator|.
name|class
operator|.
name|getCanonicalName
argument_list|()
argument_list|,
literal|"LogCustomLoggerTest"
argument_list|,
name|Level
operator|.
name|TRACE
argument_list|,
name|event
lambda|->
name|sw1
operator|.
name|append
argument_list|(
name|event
operator|.
name|getLoggerName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|ConsumingAppender
operator|.
name|newAppender
argument_list|(
literal|"provided.logger1.name"
argument_list|,
literal|"logger1"
argument_list|,
name|Level
operator|.
name|TRACE
argument_list|,
name|event
lambda|->
name|sw1
operator|.
name|append
argument_list|(
name|event
operator|.
name|getLoggerName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|ConsumingAppender
operator|.
name|newAppender
argument_list|(
literal|"provided.logger2.name"
argument_list|,
literal|"logger2"
argument_list|,
name|Level
operator|.
name|TRACE
argument_list|,
name|event
lambda|->
name|sw1
operator|.
name|append
argument_list|(
name|event
operator|.
name|getLoggerName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|ConsumingAppender
operator|.
name|newAppender
argument_list|(
literal|"irrelevant.logger.name"
argument_list|,
literal|"irrelevant"
argument_list|,
name|Level
operator|.
name|TRACE
argument_list|,
name|event
lambda|->
name|sw1
operator|.
name|append
argument_list|(
name|event
operator|.
name|getLoggerName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|ConsumingAppender
operator|.
name|newAppender
argument_list|(
name|LogComponent
operator|.
name|class
operator|.
name|getCanonicalName
argument_list|()
argument_list|,
literal|"LogComponent"
argument_list|,
name|Level
operator|.
name|TRACE
argument_list|,
name|event
lambda|->
name|sw2
operator|.
name|append
argument_list|(
name|event
operator|.
name|getLoggerName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFallbackLogger ()
specifier|public
name|void
name|testFallbackLogger
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointUri
init|=
literal|"log:"
operator|+
name|LogCustomLoggerTest
operator|.
name|class
operator|.
name|getCanonicalName
argument_list|()
decl_stmt|;
name|template
operator|.
name|requestBody
argument_list|(
name|endpointUri
argument_list|,
literal|"hello"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|sw1
operator|.
name|toString
argument_list|()
argument_list|,
name|equalTo
argument_list|(
name|LogCustomLoggerTest
operator|.
name|class
operator|.
name|getCanonicalName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testEndpointURIParametrizedLogger ()
specifier|public
name|void
name|testEndpointURIParametrizedLogger
parameter_list|()
throws|throws
name|Exception
block|{
name|getRegistry
argument_list|()
operator|.
name|put
argument_list|(
literal|"logger1"
argument_list|,
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
literal|"provided.logger1.name"
argument_list|)
argument_list|)
expr_stmt|;
name|getRegistry
argument_list|()
operator|.
name|put
argument_list|(
literal|"logger2"
argument_list|,
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
literal|"provided.logger2.name"
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|requestBody
argument_list|(
literal|"log:irrelevant.logger.name?logger=#logger2"
argument_list|,
literal|"hello"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|sw1
operator|.
name|toString
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"provided.logger2.name"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testEndpointURIParametrizedNotResolvableLogger ()
specifier|public
name|void
name|testEndpointURIParametrizedNotResolvableLogger
parameter_list|()
block|{
name|getRegistry
argument_list|()
operator|.
name|put
argument_list|(
literal|"logger1"
argument_list|,
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
literal|"provided.logger1.name"
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|requestBody
argument_list|(
literal|"log:irrelevant.logger.name?logger=#logger2"
argument_list|,
literal|"hello"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ResolveEndpointFailedException
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
annotation|@
name|Test
DECL|method|testDefaultRegistryLogger ()
specifier|public
name|void
name|testDefaultRegistryLogger
parameter_list|()
throws|throws
name|Exception
block|{
name|getRegistry
argument_list|()
operator|.
name|put
argument_list|(
literal|"logger"
argument_list|,
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
literal|"provided.logger1.name"
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|requestBody
argument_list|(
literal|"log:irrelevant.logger.name"
argument_list|,
literal|"hello"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|sw1
operator|.
name|toString
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"provided.logger1.name"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTwoRegistryLoggers ()
specifier|public
name|void
name|testTwoRegistryLoggers
parameter_list|()
throws|throws
name|Exception
block|{
name|getRegistry
argument_list|()
operator|.
name|put
argument_list|(
literal|"logger1"
argument_list|,
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
literal|"provided.logger1.name"
argument_list|)
argument_list|)
expr_stmt|;
name|getRegistry
argument_list|()
operator|.
name|put
argument_list|(
literal|"logger2"
argument_list|,
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
literal|"provided.logger2.name"
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|requestBody
argument_list|(
literal|"log:irrelevant.logger.name"
argument_list|,
literal|"hello"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|sw1
operator|.
name|toString
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"irrelevant.logger.name"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|sw2
operator|.
name|toString
argument_list|()
argument_list|,
name|equalTo
argument_list|(
name|LogComponent
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
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
return|return
operator|new
name|DefaultCamelContext
argument_list|(
operator|new
name|SimpleRegistry
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getRegistry ()
specifier|private
name|SimpleRegistry
name|getRegistry
parameter_list|()
block|{
name|SimpleRegistry
name|registry
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|context
operator|.
name|getRegistry
argument_list|()
operator|instanceof
name|PropertyPlaceholderDelegateRegistry
condition|)
block|{
name|registry
operator|=
call|(
name|SimpleRegistry
call|)
argument_list|(
operator|(
name|PropertyPlaceholderDelegateRegistry
operator|)
name|context
operator|.
name|getRegistry
argument_list|()
argument_list|)
operator|.
name|getRegistry
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|fail
argument_list|(
literal|"Could not determine Registry type"
argument_list|)
expr_stmt|;
block|}
return|return
name|registry
return|;
block|}
block|}
end_class

end_unit

