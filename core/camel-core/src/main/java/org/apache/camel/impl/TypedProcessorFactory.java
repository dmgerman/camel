begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|NamedNode
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
name|spi
operator|.
name|ProcessorFactory
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
DECL|class|TypedProcessorFactory
specifier|public
class|class
name|TypedProcessorFactory
parameter_list|<
name|T
extends|extends
name|ProcessorDefinition
parameter_list|<
name|T
parameter_list|>
parameter_list|>
implements|implements
name|ProcessorFactory
block|{
DECL|field|type
specifier|private
specifier|final
name|Class
argument_list|<
name|T
argument_list|>
name|type
decl_stmt|;
DECL|method|TypedProcessorFactory (Class<T> type)
specifier|protected
name|TypedProcessorFactory
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createChildProcessor (RouteContext routeContext, NamedNode definition, boolean mandatory)
specifier|public
name|Processor
name|createChildProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|NamedNode
name|definition
parameter_list|,
name|boolean
name|mandatory
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|definition
operator|!=
literal|null
operator|&&
name|type
operator|.
name|isInstance
argument_list|(
name|definition
argument_list|)
condition|)
block|{
return|return
name|doCreateChildProcessor
argument_list|(
name|routeContext
argument_list|,
name|type
operator|.
name|cast
argument_list|(
name|definition
argument_list|)
argument_list|,
name|mandatory
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createProcessor (RouteContext routeContext, NamedNode definition)
specifier|public
name|Processor
name|createProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|NamedNode
name|definition
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|definition
operator|!=
literal|null
operator|&&
name|type
operator|.
name|isInstance
argument_list|(
name|definition
argument_list|)
condition|)
block|{
return|return
name|doCreateProcessor
argument_list|(
name|routeContext
argument_list|,
name|type
operator|.
name|cast
argument_list|(
name|definition
argument_list|)
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|doCreateChildProcessor (RouteContext routeContext, T definition, boolean mandatory)
specifier|protected
name|Processor
name|doCreateChildProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|T
name|definition
parameter_list|,
name|boolean
name|mandatory
parameter_list|)
throws|throws
name|Exception
block|{
return|return
literal|null
return|;
block|}
DECL|method|doCreateProcessor (RouteContext routeContext, T definition)
specifier|public
name|Processor
name|doCreateProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|T
name|definition
parameter_list|)
throws|throws
name|Exception
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

