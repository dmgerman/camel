begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management.mbean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
operator|.
name|mbean
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
name|Consumer
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
name|ManagedResource
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
name|mbean
operator|.
name|ManagedConsumerMBean
import|;
end_import

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed Consumer"
argument_list|)
DECL|class|ManagedConsumer
specifier|public
class|class
name|ManagedConsumer
extends|extends
name|ManagedService
implements|implements
name|ManagedConsumerMBean
block|{
DECL|field|consumer
specifier|private
specifier|final
name|Consumer
name|consumer
decl_stmt|;
DECL|method|ManagedConsumer (CamelContext context, Consumer consumer)
specifier|public
name|ManagedConsumer
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Consumer
name|consumer
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|consumer
argument_list|)
expr_stmt|;
name|this
operator|.
name|consumer
operator|=
name|consumer
expr_stmt|;
block|}
DECL|method|getConsumer ()
specifier|public
name|Consumer
name|getConsumer
parameter_list|()
block|{
return|return
name|consumer
return|;
block|}
annotation|@
name|Override
DECL|method|getEndpointUri ()
specifier|public
name|String
name|getEndpointUri
parameter_list|()
block|{
return|return
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getInflightExchanges ()
specifier|public
name|Integer
name|getInflightExchanges
parameter_list|()
block|{
if|if
condition|(
name|getRouteId
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|getContext
argument_list|()
operator|.
name|getInflightRepository
argument_list|()
operator|.
name|size
argument_list|(
name|getRouteId
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

