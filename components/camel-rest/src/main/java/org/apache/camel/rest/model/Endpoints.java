begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.rest.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|rest
operator|.
name|model
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
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
name|Endpoint
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|XmlRootElement
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|Endpoints
specifier|public
class|class
name|Endpoints
block|{
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"endpoint"
argument_list|)
DECL|field|endpoints
specifier|private
name|List
argument_list|<
name|EndpointLink
argument_list|>
name|endpoints
init|=
operator|new
name|ArrayList
argument_list|<
name|EndpointLink
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|Endpoints ()
specifier|public
name|Endpoints
parameter_list|()
block|{     }
DECL|method|Endpoints (CamelContext camelContext)
specifier|public
name|Endpoints
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|load
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Endpoints"
operator|+
name|endpoints
return|;
block|}
DECL|method|getEndpoints ()
specifier|public
name|List
argument_list|<
name|EndpointLink
argument_list|>
name|getEndpoints
parameter_list|()
block|{
return|return
name|endpoints
return|;
block|}
DECL|method|setEndpoints (List<EndpointLink> endpoints)
specifier|public
name|void
name|setEndpoints
parameter_list|(
name|List
argument_list|<
name|EndpointLink
argument_list|>
name|endpoints
parameter_list|)
block|{
name|this
operator|.
name|endpoints
operator|=
name|endpoints
expr_stmt|;
block|}
DECL|method|load (CamelContext camelContext)
specifier|public
name|void
name|load
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|Collection
argument_list|<
name|Endpoint
argument_list|>
name|endpoints
init|=
name|camelContext
operator|.
name|getSingletonEndpoints
argument_list|()
decl_stmt|;
for|for
control|(
name|Endpoint
name|endpoint
range|:
name|endpoints
control|)
block|{
name|addEndpoint
argument_list|(
name|createEndpointLink
argument_list|(
name|endpoint
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createEndpointLink (Endpoint endpoint)
specifier|protected
name|EndpointLink
name|createEndpointLink
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|EndpointLink
name|answer
init|=
operator|new
name|EndpointLink
argument_list|()
decl_stmt|;
name|answer
operator|.
name|load
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|addEndpoint (EndpointLink endpointLink)
specifier|public
name|void
name|addEndpoint
parameter_list|(
name|EndpointLink
name|endpointLink
parameter_list|)
block|{
name|getEndpoints
argument_list|()
operator|.
name|add
argument_list|(
name|endpointLink
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

