begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|component
operator|.
name|aws
operator|.
name|xray
operator|.
name|TestDataBuilder
operator|.
name|TestTrace
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
name|processor
operator|.
name|interceptor
operator|.
name|Tracer
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
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Rule
import|;
end_import

begin_class
DECL|class|CamelAwsXRayTestSupport
specifier|public
class|class
name|CamelAwsXRayTestSupport
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Rule
DECL|field|socketListener
specifier|public
name|FakeAWSDaemon
name|socketListener
init|=
operator|new
name|FakeAWSDaemon
argument_list|()
decl_stmt|;
DECL|field|testData
specifier|private
name|List
argument_list|<
name|TestTrace
argument_list|>
name|testData
decl_stmt|;
DECL|method|CamelAwsXRayTestSupport (TestTrace... testData)
specifier|public
name|CamelAwsXRayTestSupport
parameter_list|(
name|TestTrace
modifier|...
name|testData
parameter_list|)
block|{
name|this
operator|.
name|testData
operator|=
name|Arrays
operator|.
name|asList
argument_list|(
name|testData
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|postProcessTest ()
specifier|protected
name|void
name|postProcessTest
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|postProcessTest
argument_list|()
expr_stmt|;
name|socketListener
operator|.
name|getReceivedData
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|resetMocks ()
specifier|protected
name|void
name|resetMocks
parameter_list|()
block|{
name|super
operator|.
name|resetMocks
argument_list|()
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
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|setTracing
argument_list|(
literal|true
argument_list|)
expr_stmt|;
specifier|final
name|Tracer
name|tracer
init|=
operator|new
name|Tracer
argument_list|()
decl_stmt|;
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|.
name|setShowBody
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|tracer
operator|.
name|setLogLevel
argument_list|(
name|LoggingLevel
operator|.
name|INFO
argument_list|)
expr_stmt|;
name|context
operator|.
name|getInterceptStrategies
argument_list|()
operator|.
name|add
argument_list|(
name|tracer
argument_list|)
expr_stmt|;
name|XRayTracer
name|xRayTracer
init|=
operator|new
name|XRayTracer
argument_list|()
decl_stmt|;
name|xRayTracer
operator|.
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|xRayTracer
operator|.
name|setTracingStrategy
argument_list|(
name|getTracingStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|xRayTracer
operator|.
name|setExcludePatterns
argument_list|(
name|getExcludePatterns
argument_list|()
argument_list|)
expr_stmt|;
name|xRayTracer
operator|.
name|init
argument_list|(
name|context
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
DECL|method|getTracingStrategy ()
specifier|protected
name|InterceptStrategy
name|getTracingStrategy
parameter_list|()
block|{
return|return
operator|new
name|NoopTracingStrategy
argument_list|()
return|;
block|}
DECL|method|getExcludePatterns ()
specifier|protected
name|Set
argument_list|<
name|String
argument_list|>
name|getExcludePatterns
parameter_list|()
block|{
return|return
operator|new
name|HashSet
argument_list|<>
argument_list|()
return|;
block|}
DECL|method|verify ()
specifier|protected
name|void
name|verify
parameter_list|()
block|{
try|try
block|{
comment|// give the socket listener a bit time to receive the data and transform it to Java objects
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|iEx
parameter_list|)
block|{
comment|// ignore
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|TestTrace
argument_list|>
name|receivedData
init|=
name|socketListener
operator|.
name|getReceivedData
argument_list|()
decl_stmt|;
name|TestUtils
operator|.
name|checkData
argument_list|(
name|receivedData
argument_list|,
name|testData
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

