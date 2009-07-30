begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.issues
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|issues
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
name|model
operator|.
name|ProcessorDefinition
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

begin_comment
comment|/**  * Trying to reproduce CAMEL-927.  */
end_comment

begin_class
DECL|class|TwoTimerWithJMXIssue
specifier|public
class|class
name|TwoTimerWithJMXIssue
extends|extends
name|ContextTestSupport
block|{
DECL|field|counter
specifier|private
specifier|static
name|int
name|counter
decl_stmt|;
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|enableJMX
argument_list|()
expr_stmt|;
comment|// the bug was in the JMX so it must be enabled
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
DECL|method|testFromWithNoOutputs ()
specifier|public
name|void
name|testFromWithNoOutputs
parameter_list|()
throws|throws
name|Exception
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Counter should be 2 or higher"
argument_list|,
name|counter
operator|>=
literal|2
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
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addInterceptStrategy
argument_list|(
operator|new
name|MyTracer
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"timer://kickoff_1?period=250"
argument_list|)
operator|.
name|from
argument_list|(
literal|"timer://kickoff_2?period=250&delay=10"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyTracer
specifier|private
class|class
name|MyTracer
implements|implements
name|InterceptStrategy
block|{
DECL|method|wrapProcessorInInterceptors (CamelContext context, ProcessorDefinition definition, Processor target, Processor nextTarget)
specifier|public
name|Processor
name|wrapProcessorInInterceptors
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|ProcessorDefinition
name|definition
parameter_list|,
name|Processor
name|target
parameter_list|,
name|Processor
name|nextTarget
parameter_list|)
throws|throws
name|Exception
block|{
name|assertNotNull
argument_list|(
name|target
argument_list|)
expr_stmt|;
name|counter
operator|++
expr_stmt|;
return|return
name|target
return|;
block|}
block|}
block|}
end_class

end_unit

