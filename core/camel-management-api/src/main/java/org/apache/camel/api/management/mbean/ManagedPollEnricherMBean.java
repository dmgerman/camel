begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.api.management.mbean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|api
operator|.
name|management
operator|.
name|mbean
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|TabularData
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
name|api
operator|.
name|management
operator|.
name|ManagedAttribute
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
name|api
operator|.
name|management
operator|.
name|ManagedOperation
import|;
end_import

begin_interface
DECL|interface|ManagedPollEnricherMBean
specifier|public
interface|interface
name|ManagedPollEnricherMBean
extends|extends
name|ManagedProcessorMBean
extends|,
name|ManagedExtendedInformation
block|{
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"The language for the expression"
argument_list|)
DECL|method|getExpressionLanguage ()
name|String
name|getExpressionLanguage
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Expression that computes the endpoint uri to use as the resource endpoint to poll enrich from"
argument_list|,
name|mask
operator|=
literal|true
argument_list|)
DECL|method|getExpression ()
name|String
name|getExpression
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Timeout in millis when polling from the external service"
argument_list|)
DECL|method|getTimeout ()
name|Long
name|getTimeout
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Sets the maximum size used by the ConsumerCache which is used to cache and reuse consumers"
argument_list|)
DECL|method|getCacheSize ()
name|Integer
name|getCacheSize
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Ignore the invalidate endpoint exception when try to create a consumer with that endpoint"
argument_list|)
DECL|method|isIgnoreInvalidEndpoint ()
name|Boolean
name|isIgnoreInvalidEndpoint
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether to aggregate when there was an exception thrown during calling the resource endpoint"
argument_list|)
DECL|method|isAggregateOnException ()
name|Boolean
name|isAggregateOnException
parameter_list|()
function_decl|;
annotation|@
name|Override
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Statistics of the endpoints that has been poll enriched from"
argument_list|)
DECL|method|extendedInformation ()
name|TabularData
name|extendedInformation
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

