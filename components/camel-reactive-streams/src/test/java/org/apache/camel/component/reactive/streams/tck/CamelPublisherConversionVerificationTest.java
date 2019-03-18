begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.reactive.streams.tck
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|reactive
operator|.
name|streams
operator|.
name|tck
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
name|reactive
operator|.
name|streams
operator|.
name|api
operator|.
name|CamelReactiveStreams
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
name|DefaultShutdownStrategy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|reactivestreams
operator|.
name|Publisher
import|;
end_import

begin_import
import|import
name|org
operator|.
name|reactivestreams
operator|.
name|tck
operator|.
name|PublisherVerification
import|;
end_import

begin_import
import|import
name|org
operator|.
name|reactivestreams
operator|.
name|tck
operator|.
name|TestEnvironment
import|;
end_import

begin_import
import|import
name|org
operator|.
name|testng
operator|.
name|annotations
operator|.
name|AfterTest
import|;
end_import

begin_class
DECL|class|CamelPublisherConversionVerificationTest
specifier|public
class|class
name|CamelPublisherConversionVerificationTest
extends|extends
name|PublisherVerification
argument_list|<
name|Long
argument_list|>
block|{
DECL|field|context
specifier|private
name|CamelContext
name|context
decl_stmt|;
DECL|method|CamelPublisherConversionVerificationTest ()
specifier|public
name|CamelPublisherConversionVerificationTest
parameter_list|()
block|{
name|super
argument_list|(
operator|new
name|TestEnvironment
argument_list|(
literal|2000L
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createPublisher (long l)
specifier|public
name|Publisher
argument_list|<
name|Long
argument_list|>
name|createPublisher
parameter_list|(
name|long
name|l
parameter_list|)
block|{
name|init
argument_list|()
expr_stmt|;
name|RouteBuilder
name|builder
init|=
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
literal|"timer:tick?delay=500&period=50&repeatCount="
operator|+
name|l
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|simple
argument_list|(
literal|"${random(1000)}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"reactive-streams:prod"
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
try|try
block|{
name|builder
operator|.
name|addRoutesToCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
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
name|Publisher
argument_list|<
name|Long
argument_list|>
name|pub
init|=
name|CamelReactiveStreams
operator|.
name|get
argument_list|(
name|context
argument_list|)
operator|.
name|fromStream
argument_list|(
literal|"prod"
argument_list|,
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|pub
return|;
block|}
annotation|@
name|Override
DECL|method|maxElementsFromPublisher ()
specifier|public
name|long
name|maxElementsFromPublisher
parameter_list|()
block|{
comment|// It's an active publisher
return|return
name|publisherUnableToSignalOnComplete
argument_list|()
return|;
comment|// == Long.MAX_VALUE == unbounded
block|}
annotation|@
name|Override
DECL|method|createFailedPublisher ()
specifier|public
name|Publisher
argument_list|<
name|Long
argument_list|>
name|createFailedPublisher
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
DECL|method|init ()
specifier|protected
name|void
name|init
parameter_list|()
block|{
name|tearDown
argument_list|()
expr_stmt|;
name|this
operator|.
name|context
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|DefaultShutdownStrategy
name|shutdownStrategy
init|=
operator|new
name|DefaultShutdownStrategy
argument_list|()
decl_stmt|;
name|shutdownStrategy
operator|.
name|setShutdownNowOnTimeout
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|shutdownStrategy
operator|.
name|setTimeout
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|this
operator|.
name|context
operator|.
name|setShutdownStrategy
argument_list|(
name|shutdownStrategy
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AfterTest
DECL|method|tearDown ()
specifier|protected
name|void
name|tearDown
parameter_list|()
block|{
try|try
block|{
if|if
condition|(
name|this
operator|.
name|context
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

