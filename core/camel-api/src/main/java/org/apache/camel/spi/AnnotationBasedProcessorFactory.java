begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
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
name|DynamicRouter
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
name|RecipientList
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
name|RoutingSlip
import|;
end_import

begin_comment
comment|/**  * Factory to create {@link Processor} for annotation based EIPs.  */
end_comment

begin_interface
DECL|interface|AnnotationBasedProcessorFactory
specifier|public
interface|interface
name|AnnotationBasedProcessorFactory
block|{
comment|/**      * Creates dynamic router processor from the configured annotation.      */
DECL|method|createDynamicRouter (CamelContext camelContext, DynamicRouter annotation)
name|Processor
name|createDynamicRouter
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|DynamicRouter
name|annotation
parameter_list|)
function_decl|;
comment|/**      * Creates recipient list processor from the configured annotation.      */
DECL|method|createRecipientList (CamelContext camelContext, RecipientList annotation)
name|Processor
name|createRecipientList
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|RecipientList
name|annotation
parameter_list|)
function_decl|;
comment|/**      * Creates routing slip processor from the configured annotation.      */
DECL|method|createRoutingSlip (CamelContext camelContext, RoutingSlip annotation)
name|Processor
name|createRoutingSlip
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|RoutingSlip
name|annotation
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

