begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.reifier
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|reifier
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
name|model
operator|.
name|SamplingDefinition
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
name|SamplingThrottler
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
name|RouteContext
import|;
end_import

begin_class
DECL|class|SamplingReifier
class|class
name|SamplingReifier
extends|extends
name|ProcessorReifier
argument_list|<
name|SamplingDefinition
argument_list|>
block|{
DECL|method|SamplingReifier (ProcessorDefinition<?> definition)
name|SamplingReifier
parameter_list|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|)
block|{
name|super
argument_list|(
operator|(
name|SamplingDefinition
operator|)
name|definition
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProcessor (RouteContext routeContext)
specifier|public
name|Processor
name|createProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|definition
operator|.
name|getMessageFrequency
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|SamplingThrottler
argument_list|(
name|definition
operator|.
name|getMessageFrequency
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
comment|// should default be 1 sample period
name|long
name|time
init|=
name|definition
operator|.
name|getSamplePeriod
argument_list|()
operator|!=
literal|null
condition|?
name|definition
operator|.
name|getSamplePeriod
argument_list|()
else|:
literal|1L
decl_stmt|;
comment|// should default be in seconds
name|TimeUnit
name|tu
init|=
name|definition
operator|.
name|getUnits
argument_list|()
operator|!=
literal|null
condition|?
name|definition
operator|.
name|getUnits
argument_list|()
else|:
name|TimeUnit
operator|.
name|SECONDS
decl_stmt|;
return|return
operator|new
name|SamplingThrottler
argument_list|(
name|time
argument_list|,
name|tu
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

