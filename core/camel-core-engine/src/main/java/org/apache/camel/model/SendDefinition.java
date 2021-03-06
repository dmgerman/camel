begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
package|;
end_package

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
name|XmlAttribute
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
name|XmlTransient
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|EndpointProducerBuilder
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
name|Metadata
import|;
end_import

begin_comment
comment|/**  * Sends the message to an endpoint  */
end_comment

begin_class
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|SendDefinition
specifier|public
specifier|abstract
class|class
name|SendDefinition
parameter_list|<
name|Type
extends|extends
name|ProcessorDefinition
parameter_list|<
name|Type
parameter_list|>
parameter_list|>
extends|extends
name|NoOutputDefinition
argument_list|<
name|Type
argument_list|>
implements|implements
name|EndpointRequiredDefinition
block|{
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|uri
specifier|protected
name|String
name|uri
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|endpoint
specifier|protected
name|Endpoint
name|endpoint
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|endpointProducerBuilder
specifier|protected
name|EndpointProducerBuilder
name|endpointProducerBuilder
decl_stmt|;
DECL|method|SendDefinition ()
specifier|public
name|SendDefinition
parameter_list|()
block|{     }
DECL|method|SendDefinition (String uri)
specifier|public
name|SendDefinition
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpointUri ()
specifier|public
name|String
name|getEndpointUri
parameter_list|()
block|{
if|if
condition|(
name|endpointProducerBuilder
operator|!=
literal|null
condition|)
block|{
return|return
name|endpointProducerBuilder
operator|.
name|getUri
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|endpoint
operator|!=
literal|null
condition|)
block|{
return|return
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|uri
return|;
block|}
block|}
DECL|method|getUri ()
specifier|public
name|String
name|getUri
parameter_list|()
block|{
return|return
name|uri
return|;
block|}
comment|/**      * Sets the uri of the endpoint to send to.      *      * @param uri the uri of the endpoint      */
DECL|method|setUri (String uri)
specifier|public
name|void
name|setUri
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|clear
argument_list|()
expr_stmt|;
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
block|}
comment|/**      * Gets the endpoint if an {@link Endpoint} instance was set.      *<p/>      * This implementation may return<tt>null</tt> which means you need to use      * {@link #getEndpointUri()} to get information about the endpoint.      *      * @return the endpoint instance, or<tt>null</tt>      */
DECL|method|getEndpoint ()
specifier|public
name|Endpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|endpoint
return|;
block|}
DECL|method|setEndpoint (Endpoint endpoint)
specifier|public
name|void
name|setEndpoint
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|clear
argument_list|()
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|uri
operator|=
name|endpoint
operator|!=
literal|null
condition|?
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
else|:
literal|null
expr_stmt|;
block|}
DECL|method|getEndpointProducerBuilder ()
specifier|public
name|EndpointProducerBuilder
name|getEndpointProducerBuilder
parameter_list|()
block|{
return|return
name|endpointProducerBuilder
return|;
block|}
DECL|method|setEndpointProducerBuilder (EndpointProducerBuilder endpointProducerBuilder)
specifier|public
name|void
name|setEndpointProducerBuilder
parameter_list|(
name|EndpointProducerBuilder
name|endpointProducerBuilder
parameter_list|)
block|{
name|clear
argument_list|()
expr_stmt|;
name|this
operator|.
name|endpointProducerBuilder
operator|=
name|endpointProducerBuilder
expr_stmt|;
block|}
DECL|method|getPattern ()
specifier|public
name|String
name|getPattern
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getLabel ()
specifier|public
name|String
name|getLabel
parameter_list|()
block|{
name|String
name|uri
init|=
name|getEndpointUri
argument_list|()
decl_stmt|;
return|return
name|uri
operator|!=
literal|null
condition|?
name|uri
else|:
literal|"no uri supplied"
return|;
block|}
DECL|method|clear ()
specifier|protected
name|void
name|clear
parameter_list|()
block|{
name|this
operator|.
name|endpointProducerBuilder
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|uri
operator|=
literal|null
expr_stmt|;
block|}
block|}
end_class

end_unit

