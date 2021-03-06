begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Endpoint
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
name|ExchangePattern
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
name|SendDefinition
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
name|SendProcessor
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
DECL|class|SendReifier
specifier|public
class|class
name|SendReifier
extends|extends
name|ProcessorReifier
argument_list|<
name|SendDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
block|{
DECL|method|SendReifier (ProcessorDefinition<?> definition)
specifier|public
name|SendReifier
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
name|SendDefinition
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
name|Endpoint
name|endpoint
init|=
name|resolveEndpoint
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
return|return
operator|new
name|SendProcessor
argument_list|(
name|endpoint
argument_list|,
name|parse
argument_list|(
name|routeContext
argument_list|,
name|ExchangePattern
operator|.
name|class
argument_list|,
name|definition
operator|.
name|getPattern
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|resolveEndpoint (RouteContext context)
specifier|public
name|Endpoint
name|resolveEndpoint
parameter_list|(
name|RouteContext
name|context
parameter_list|)
block|{
if|if
condition|(
name|definition
operator|.
name|getEndpoint
argument_list|()
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|definition
operator|.
name|getEndpointProducerBuilder
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
name|context
operator|.
name|resolveEndpoint
argument_list|(
name|definition
operator|.
name|getEndpointUri
argument_list|()
argument_list|,
operator|(
name|String
operator|)
literal|null
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|definition
operator|.
name|getEndpointProducerBuilder
argument_list|()
operator|.
name|resolve
argument_list|(
name|context
operator|.
name|getCamelContext
argument_list|()
argument_list|)
return|;
block|}
block|}
else|else
block|{
return|return
name|definition
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

