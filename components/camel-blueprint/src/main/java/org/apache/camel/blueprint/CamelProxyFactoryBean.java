begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.blueprint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|blueprint
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|XmlRootElement
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
name|aries
operator|.
name|blueprint
operator|.
name|services
operator|.
name|ExtendedBlueprintContainer
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|FailedToCreateProducerException
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
name|Producer
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
name|component
operator|.
name|bean
operator|.
name|ProxyHelper
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
name|core
operator|.
name|xml
operator|.
name|AbstractCamelFactoryBean
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
name|service
operator|.
name|ServiceHelper
import|;
end_import

begin_comment
comment|/**  * A factory to create a Proxy to a a Camel Pojo Endpoint.  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"proxy"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|CamelProxyFactoryBean
specifier|public
class|class
name|CamelProxyFactoryBean
extends|extends
name|AbstractCamelFactoryBean
argument_list|<
name|Object
argument_list|>
block|{
annotation|@
name|XmlAttribute
DECL|field|serviceUrl
specifier|private
name|String
name|serviceUrl
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|serviceRef
specifier|private
name|String
name|serviceRef
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|serviceInterface
specifier|private
name|String
name|serviceInterface
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|binding
specifier|private
name|Boolean
name|binding
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|endpoint
specifier|private
name|Endpoint
name|endpoint
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|serviceProxy
specifier|private
name|Object
name|serviceProxy
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|producer
specifier|private
name|Producer
name|producer
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|blueprintContainer
specifier|private
name|ExtendedBlueprintContainer
name|blueprintContainer
decl_stmt|;
annotation|@
name|Override
DECL|method|getObject ()
specifier|public
name|Object
name|getObject
parameter_list|()
block|{
return|return
name|serviceProxy
return|;
block|}
annotation|@
name|Override
DECL|method|getObjectType ()
specifier|public
name|Class
argument_list|<
name|Object
argument_list|>
name|getObjectType
parameter_list|()
block|{
return|return
name|Object
operator|.
name|class
return|;
block|}
annotation|@
name|Override
DECL|method|getCamelContextWithId (String camelContextId)
specifier|protected
name|CamelContext
name|getCamelContextWithId
parameter_list|(
name|String
name|camelContextId
parameter_list|)
block|{
if|if
condition|(
name|blueprintContainer
operator|!=
literal|null
condition|)
block|{
return|return
operator|(
name|CamelContext
operator|)
name|blueprintContainer
operator|.
name|getComponentInstance
argument_list|(
name|camelContextId
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|discoverDefaultCamelContext ()
specifier|protected
name|CamelContext
name|discoverDefaultCamelContext
parameter_list|()
block|{
if|if
condition|(
name|blueprintContainer
operator|!=
literal|null
condition|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|ids
init|=
name|BlueprintCamelContextLookupHelper
operator|.
name|lookupBlueprintCamelContext
argument_list|(
name|blueprintContainer
argument_list|)
decl_stmt|;
if|if
condition|(
name|ids
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
comment|// there is only 1 id for a BlueprintCamelContext so fallback and use this
return|return
name|getCamelContextWithId
argument_list|(
name|ids
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|afterPropertiesSet ()
specifier|public
name|void
name|afterPropertiesSet
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|endpoint
operator|==
literal|null
condition|)
block|{
name|getCamelContext
argument_list|()
expr_stmt|;
if|if
condition|(
name|getServiceUrl
argument_list|()
operator|==
literal|null
operator|&&
name|getServiceRef
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"serviceUrl or serviceRef must be specified."
argument_list|)
throw|;
block|}
if|if
condition|(
name|getServiceInterface
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"serviceInterface must be specified."
argument_list|)
throw|;
block|}
comment|// lookup endpoint or we have the url for it
if|if
condition|(
name|getServiceRef
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|=
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
name|getServiceRef
argument_list|()
argument_list|,
name|Endpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|endpoint
operator|=
name|getCamelContext
argument_list|()
operator|.
name|getEndpoint
argument_list|(
name|getServiceUrl
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|endpoint
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Could not resolve endpoint: "
operator|+
name|getServiceUrl
argument_list|()
argument_list|)
throw|;
block|}
block|}
comment|// binding is enabled by default
name|boolean
name|bind
init|=
name|getBinding
argument_list|()
operator|!=
literal|null
condition|?
name|getBinding
argument_list|()
else|:
literal|true
decl_stmt|;
try|try
block|{
comment|// need to start endpoint before we create producer
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|producer
operator|=
name|endpoint
operator|.
name|createProducer
argument_list|()
expr_stmt|;
comment|// add and start producer
name|getCamelContext
argument_list|()
operator|.
name|addService
argument_list|(
name|producer
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|blueprintContainer
operator|.
name|loadClass
argument_list|(
name|getServiceInterface
argument_list|()
argument_list|)
decl_stmt|;
name|serviceProxy
operator|=
name|ProxyHelper
operator|.
name|createProxy
argument_list|(
name|endpoint
argument_list|,
name|bind
argument_list|,
name|producer
argument_list|,
name|clazz
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|FailedToCreateProducerException
argument_list|(
name|endpoint
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
throws|throws
name|Exception
block|{
comment|// we let CamelContext manage the lifecycle of the producer and shut it down when Camel stops
block|}
DECL|method|getServiceUrl ()
specifier|public
name|String
name|getServiceUrl
parameter_list|()
block|{
return|return
name|serviceUrl
return|;
block|}
DECL|method|setServiceUrl (String serviceUrl)
specifier|public
name|void
name|setServiceUrl
parameter_list|(
name|String
name|serviceUrl
parameter_list|)
block|{
name|this
operator|.
name|serviceUrl
operator|=
name|serviceUrl
expr_stmt|;
block|}
DECL|method|getServiceRef ()
specifier|public
name|String
name|getServiceRef
parameter_list|()
block|{
return|return
name|serviceRef
return|;
block|}
DECL|method|setServiceRef (String serviceRef)
specifier|public
name|void
name|setServiceRef
parameter_list|(
name|String
name|serviceRef
parameter_list|)
block|{
name|this
operator|.
name|serviceRef
operator|=
name|serviceRef
expr_stmt|;
block|}
DECL|method|getBinding ()
specifier|public
name|Boolean
name|getBinding
parameter_list|()
block|{
return|return
name|binding
return|;
block|}
DECL|method|setBinding (Boolean binding)
specifier|public
name|void
name|setBinding
parameter_list|(
name|Boolean
name|binding
parameter_list|)
block|{
name|this
operator|.
name|binding
operator|=
name|binding
expr_stmt|;
block|}
DECL|method|getServiceInterface ()
specifier|public
name|String
name|getServiceInterface
parameter_list|()
block|{
return|return
name|serviceInterface
return|;
block|}
DECL|method|setServiceInterface (String serviceInterface)
specifier|public
name|void
name|setServiceInterface
parameter_list|(
name|String
name|serviceInterface
parameter_list|)
block|{
name|this
operator|.
name|serviceInterface
operator|=
name|serviceInterface
expr_stmt|;
block|}
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
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
DECL|method|getProducer ()
specifier|public
name|Producer
name|getProducer
parameter_list|()
block|{
return|return
name|producer
return|;
block|}
DECL|method|setProducer (Producer producer)
specifier|public
name|void
name|setProducer
parameter_list|(
name|Producer
name|producer
parameter_list|)
block|{
name|this
operator|.
name|producer
operator|=
name|producer
expr_stmt|;
block|}
DECL|method|getBlueprintContainer ()
specifier|public
name|ExtendedBlueprintContainer
name|getBlueprintContainer
parameter_list|()
block|{
return|return
name|blueprintContainer
return|;
block|}
DECL|method|setBlueprintContainer (ExtendedBlueprintContainer blueprintContainer)
specifier|public
name|void
name|setBlueprintContainer
parameter_list|(
name|ExtendedBlueprintContainer
name|blueprintContainer
parameter_list|)
block|{
name|this
operator|.
name|blueprintContainer
operator|=
name|blueprintContainer
expr_stmt|;
block|}
block|}
end_class

end_unit

