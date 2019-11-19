begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.resilience4j
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|resilience4j
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
name|impl
operator|.
name|engine
operator|.
name|TypedProcessorFactory
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
name|CircuitBreakerDefinition
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

begin_comment
comment|/**  * To integrate camel-resilience4j with the Camel routes using the Circuit  * Breaker EIP.  */
end_comment

begin_class
DECL|class|ResilienceProcessorFactory
specifier|public
class|class
name|ResilienceProcessorFactory
extends|extends
name|TypedProcessorFactory
argument_list|<
name|CircuitBreakerDefinition
argument_list|>
block|{
DECL|method|ResilienceProcessorFactory ()
specifier|public
name|ResilienceProcessorFactory
parameter_list|()
block|{
name|super
argument_list|(
name|CircuitBreakerDefinition
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doCreateProcessor (RouteContext routeContext, CircuitBreakerDefinition definition)
specifier|public
name|Processor
name|doCreateProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|CircuitBreakerDefinition
name|definition
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|ResilienceReifier
argument_list|(
name|definition
argument_list|)
operator|.
name|createProcessor
argument_list|(
name|routeContext
argument_list|)
return|;
block|}
block|}
end_class

end_unit

