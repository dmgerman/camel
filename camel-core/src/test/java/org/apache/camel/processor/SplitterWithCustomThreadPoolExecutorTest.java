begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
package|;
end_package

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
name|LinkedBlockingQueue
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
name|ThreadPoolExecutor
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
name|ProcessorType
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
name|RouteType
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
name|SplitterType
import|;
end_import

begin_class
DECL|class|SplitterWithCustomThreadPoolExecutorTest
specifier|public
class|class
name|SplitterWithCustomThreadPoolExecutorTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|customThreadPoolExecutor
specifier|protected
name|ThreadPoolExecutor
name|customThreadPoolExecutor
init|=
operator|new
name|ThreadPoolExecutor
argument_list|(
literal|8
argument_list|,
literal|16
argument_list|,
literal|0L
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|,
operator|new
name|LinkedBlockingQueue
argument_list|()
argument_list|)
decl_stmt|;
DECL|method|testSplitterWithCustomThreadPoolExecutor ()
specifier|public
name|void
name|testSplitterWithCustomThreadPoolExecutor
parameter_list|()
throws|throws
name|Exception
block|{
name|ThreadPoolExecutor
name|threadPoolExecutor
init|=
operator|(
name|ThreadPoolExecutor
operator|)
name|getSplitter
argument_list|()
operator|.
name|getExecutor
argument_list|()
decl_stmt|;
comment|// this should be sufficient as core pool size is the only thing I changed from the default
name|assertTrue
argument_list|(
name|threadPoolExecutor
operator|.
name|getCorePoolSize
argument_list|()
operator|==
name|customThreadPoolExecutor
operator|.
name|getCorePoolSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|threadPoolExecutor
operator|.
name|getMaximumPoolSize
argument_list|()
operator|==
name|customThreadPoolExecutor
operator|.
name|getMaximumPoolSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getSplitter ()
specifier|protected
name|SplitterType
name|getSplitter
parameter_list|()
block|{
name|SplitterType
name|result
init|=
literal|null
decl_stmt|;
name|List
argument_list|<
name|RouteType
argument_list|>
name|routeDefinitions
init|=
name|context
operator|.
name|getRouteDefinitions
argument_list|()
decl_stmt|;
for|for
control|(
name|RouteType
name|routeType
range|:
name|routeDefinitions
control|)
block|{
name|result
operator|=
name|firstSplitterType
argument_list|(
name|routeType
operator|.
name|getOutputs
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|result
operator|!=
literal|null
condition|)
block|{
break|break;
block|}
block|}
return|return
name|result
return|;
block|}
DECL|method|firstSplitterType (List<ProcessorType> outputs)
specifier|protected
name|SplitterType
name|firstSplitterType
parameter_list|(
name|List
argument_list|<
name|ProcessorType
argument_list|>
name|outputs
parameter_list|)
block|{
name|SplitterType
name|result
init|=
literal|null
decl_stmt|;
for|for
control|(
name|ProcessorType
name|processorType
range|:
name|outputs
control|)
block|{
if|if
condition|(
name|processorType
operator|instanceof
name|SplitterType
condition|)
block|{
name|result
operator|=
operator|(
name|SplitterType
operator|)
name|processorType
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
name|firstSplitterType
argument_list|(
name|processorType
operator|.
name|getOutputs
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|result
operator|!=
literal|null
condition|)
block|{
break|break;
block|}
block|}
return|return
name|result
return|;
block|}
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
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
block|{
name|from
argument_list|(
literal|"direct:parallel-custom-pool"
argument_list|)
operator|.
name|split
argument_list|(
name|body
argument_list|()
operator|.
name|tokenize
argument_list|(
literal|","
argument_list|)
argument_list|)
operator|.
name|parallelProcessing
argument_list|(
literal|true
argument_list|)
operator|.
name|executor
argument_list|(
name|customThreadPoolExecutor
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
block|}
end_class

end_unit

