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
name|FilterDefinition
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
name|processor
operator|.
name|FilterProcessor
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
DECL|class|FilterReifier
class|class
name|FilterReifier
extends|extends
name|ExpressionReifier
argument_list|<
name|FilterDefinition
argument_list|>
block|{
DECL|method|FilterReifier (ProcessorDefinition<?> definition)
name|FilterReifier
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
name|FilterDefinition
operator|.
name|class
operator|.
name|cast
argument_list|(
name|definition
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProcessor (RouteContext routeContext)
specifier|public
name|FilterProcessor
name|createProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|createFilterProcessor
argument_list|(
name|routeContext
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createFilterProcessor (RouteContext routeContext)
specifier|protected
name|FilterProcessor
name|createFilterProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
throws|throws
name|Exception
block|{
comment|// filter EIP should have child outputs
name|Processor
name|childProcessor
init|=
name|this
operator|.
name|createChildProcessor
argument_list|(
name|routeContext
argument_list|,
literal|true
argument_list|)
decl_stmt|;
return|return
operator|new
name|FilterProcessor
argument_list|(
name|createPredicate
argument_list|(
name|routeContext
argument_list|)
argument_list|,
name|childProcessor
argument_list|)
return|;
block|}
block|}
end_class

end_unit

