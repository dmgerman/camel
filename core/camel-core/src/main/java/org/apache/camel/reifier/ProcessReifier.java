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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|AsyncProcessor
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
name|Service
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
name|ProcessDefinition
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
name|support
operator|.
name|processor
operator|.
name|DelegateAsyncProcessor
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
name|support
operator|.
name|processor
operator|.
name|DelegateSyncProcessor
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_class
DECL|class|ProcessReifier
class|class
name|ProcessReifier
extends|extends
name|ProcessorReifier
argument_list|<
name|ProcessDefinition
argument_list|>
block|{
DECL|method|ProcessReifier (ProcessorDefinition<?> definition)
name|ProcessReifier
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
name|ProcessDefinition
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
block|{
name|Processor
name|answer
init|=
name|definition
operator|.
name|getProcessor
argument_list|()
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|definition
operator|.
name|getRef
argument_list|()
argument_list|,
literal|"ref"
argument_list|,
name|definition
argument_list|)
expr_stmt|;
name|answer
operator|=
name|routeContext
operator|.
name|mandatoryLookup
argument_list|(
name|definition
operator|.
name|getRef
argument_list|()
argument_list|,
name|Processor
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
comment|// ensure its wrapped in a Service so we can manage it from eg. JMX
comment|// (a Processor must be a Service to be enlisted in JMX)
if|if
condition|(
operator|!
operator|(
name|answer
operator|instanceof
name|Service
operator|)
condition|)
block|{
if|if
condition|(
name|answer
operator|instanceof
name|AsyncProcessor
condition|)
block|{
comment|// the processor is async by nature so use the async delegate
name|answer
operator|=
operator|new
name|DelegateAsyncProcessor
argument_list|(
name|answer
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// the processor is sync by nature so use the sync delegate
name|answer
operator|=
operator|new
name|DelegateSyncProcessor
argument_list|(
name|answer
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit
