begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.xray
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|xray
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|invoke
operator|.
name|MethodHandles
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
name|Body
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
name|Handler
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
name|LoggingLevel
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
name|RoutesBuilder
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
name|NotifyBuilder
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
name|spi
operator|.
name|InterceptStrategy
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

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|is
import|;
end_import

begin_class
DECL|class|ErrorHandlingTest
specifier|public
class|class
name|ErrorHandlingTest
extends|extends
name|CamelAwsXRayTestSupport
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
name|MethodHandles
operator|.
name|lookup
argument_list|()
operator|.
name|lookupClass
argument_list|()
argument_list|)
decl_stmt|;
comment|// FIXME: check why processors invoked in onRedelivery do not generate a subsegment
DECL|method|ErrorHandlingTest ()
specifier|public
name|ErrorHandlingTest
parameter_list|()
block|{
name|super
argument_list|(
name|TestDataBuilder
operator|.
name|createTrace
argument_list|()
operator|.
name|withSegment
argument_list|(
name|TestDataBuilder
operator|.
name|createSegment
argument_list|(
literal|"start"
argument_list|)
operator|.
name|withSubsegment
argument_list|(
name|TestDataBuilder
operator|.
name|createSubsegment
argument_list|(
literal|"bean:TraceBean"
argument_list|)
argument_list|)
comment|//                      .withSubsegment(TestDataBuilder.createSubsegment("ExceptionRetryProcessor"))
operator|.
name|withSubsegment
argument_list|(
name|TestDataBuilder
operator|.
name|createSubsegment
argument_list|(
literal|"bean:TraceBean"
argument_list|)
argument_list|)
comment|//                      .withSubsegment(TestDataBuilder.createSubsegment("ExceptionRetryProcessor"))
operator|.
name|withSubsegment
argument_list|(
name|TestDataBuilder
operator|.
name|createSubsegment
argument_list|(
literal|"bean:TraceBean"
argument_list|)
argument_list|)
comment|//                      .withSubsegment(TestDataBuilder.createSubsegment("ExceptionRetryProcessor"))
operator|.
name|withSubsegment
argument_list|(
name|TestDataBuilder
operator|.
name|createSubsegment
argument_list|(
literal|"bean:TraceBean"
argument_list|)
argument_list|)
operator|.
name|withSubsegment
argument_list|(
name|TestDataBuilder
operator|.
name|createSubsegment
argument_list|(
literal|"seda:otherRoute"
argument_list|)
argument_list|)
operator|.
name|withSubsegment
argument_list|(
name|TestDataBuilder
operator|.
name|createSubsegment
argument_list|(
literal|"mock:end"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|withSegment
argument_list|(
name|TestDataBuilder
operator|.
name|createSegment
argument_list|(
literal|"otherRoute"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RoutesBuilder
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
name|onException
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|ExceptionProcessor
argument_list|()
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|3
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|200
argument_list|)
operator|.
name|useExponentialBackOff
argument_list|()
operator|.
name|backOffMultiplier
argument_list|(
literal|1.5D
argument_list|)
operator|.
name|onRedelivery
argument_list|(
operator|new
name|ExceptionRetryProcessor
argument_list|()
argument_list|)
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
operator|.
name|log
argument_list|(
name|LoggingLevel
operator|.
name|WARN
argument_list|,
literal|"Caught error while performing task. Reason: ${exception.message} Stacktrace: ${exception.stacktrace}"
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"start"
argument_list|)
operator|.
name|log
argument_list|(
literal|"start has been called"
argument_list|)
operator|.
name|bean
argument_list|(
name|TraceBean
operator|.
name|class
argument_list|)
operator|.
name|delay
argument_list|(
name|simple
argument_list|(
literal|"${random(1000,2000)}"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:otherRoute"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:end"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:otherRoute"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"otherRoute"
argument_list|)
operator|.
name|log
argument_list|(
literal|"otherRoute has been called"
argument_list|)
operator|.
name|delay
argument_list|(
name|simple
argument_list|(
literal|"${random(0,500)}"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|getTracingStrategy ()
specifier|protected
name|InterceptStrategy
name|getTracingStrategy
parameter_list|()
block|{
return|return
operator|new
name|TraceAnnotatedTracingStrategy
argument_list|()
return|;
block|}
annotation|@
name|Test
DECL|method|testRoute ()
specifier|public
name|void
name|testRoute
parameter_list|()
throws|throws
name|Exception
block|{
name|NotifyBuilder
name|notify
init|=
operator|new
name|NotifyBuilder
argument_list|(
name|context
argument_list|)
operator|.
name|whenDone
argument_list|(
literal|2
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|MockEndpoint
name|mockEndpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:end"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|mockEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"HELLO"
argument_list|)
expr_stmt|;
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
literal|"Not all exchanges were fully processed"
argument_list|,
name|notify
operator|.
name|matches
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|,
name|is
argument_list|(
name|equalTo
argument_list|(
literal|true
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|verify
argument_list|()
expr_stmt|;
block|}
annotation|@
name|XRayTrace
DECL|class|TraceBean
specifier|public
specifier|static
class|class
name|TraceBean
block|{
DECL|field|counter
specifier|private
specifier|static
name|int
name|counter
decl_stmt|;
annotation|@
name|Handler
DECL|method|convertBodyToUpperCase (@ody String body)
specifier|public
name|String
name|convertBodyToUpperCase
parameter_list|(
annotation|@
name|Body
name|String
name|body
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|converted
init|=
name|body
operator|.
name|toUpperCase
argument_list|()
decl_stmt|;
if|if
condition|(
name|counter
operator|<
literal|3
condition|)
block|{
name|counter
operator|++
expr_stmt|;
throw|throw
operator|new
name|Exception
argument_list|(
literal|"test"
argument_list|)
throw|;
block|}
return|return
name|converted
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"TraceBean"
return|;
block|}
block|}
annotation|@
name|XRayTrace
DECL|class|ExceptionProcessor
specifier|public
specifier|static
class|class
name|ExceptionProcessor
implements|implements
name|Processor
block|{
annotation|@
name|Override
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
name|Exception
name|ex
init|=
operator|(
name|Exception
operator|)
name|exchange
operator|.
name|getProperties
argument_list|()
operator|.
name|get
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_CAUGHT
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Processing caught exception {}"
argument_list|,
name|ex
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|put
argument_list|(
literal|"HandledError"
argument_list|,
name|ex
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"ExceptionProcessor"
return|;
block|}
block|}
annotation|@
name|XRayTrace
DECL|class|ExceptionRetryProcessor
specifier|public
specifier|static
class|class
name|ExceptionRetryProcessor
implements|implements
name|Processor
block|{
annotation|@
name|Override
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
name|Exception
name|ex
init|=
operator|(
name|Exception
operator|)
name|exchange
operator|.
name|getProperties
argument_list|()
operator|.
name|get
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_CAUGHT
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|">> Attempting redelivery of handled exception {} with message: {}"
argument_list|,
name|ex
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|,
name|ex
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"ExceptionRetryProcessor"
return|;
block|}
block|}
block|}
end_class

end_unit

