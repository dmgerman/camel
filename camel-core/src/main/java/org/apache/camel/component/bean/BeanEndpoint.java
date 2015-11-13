begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|Component
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
name|impl
operator|.
name|DefaultEndpoint
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
name|UriEndpoint
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
name|UriParam
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
name|UriPath
import|;
end_import

begin_comment
comment|/**  * Endpoint for the bean component.  *  * @version   */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"bean"
argument_list|,
name|title
operator|=
literal|"Bean"
argument_list|,
name|syntax
operator|=
literal|"bean:beanName"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"core,java"
argument_list|)
DECL|class|BeanEndpoint
specifier|public
class|class
name|BeanEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|beanHolder
specifier|private
specifier|transient
name|BeanHolder
name|beanHolder
decl_stmt|;
DECL|field|processor
specifier|private
specifier|transient
name|BeanProcessor
name|processor
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"Sets the name of the bean to invoke"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|beanName
specifier|private
name|String
name|beanName
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"Sets the name of the method to invoke on the bean"
argument_list|)
DECL|field|method
specifier|private
name|String
name|method
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"If enabled, Camel will cache the result of the first Registry look-up."
operator|+
literal|" Cache can be enabled if the bean in the Registry is defined as a singleton scope."
argument_list|)
DECL|field|cache
specifier|private
name|boolean
name|cache
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"How to treat the parameters which are passed from the message body."
operator|+
literal|"true means the message body should be an array of parameters. Note: This option is used internally by Camel, and is not intended for end users to use."
argument_list|)
annotation|@
name|Deprecated
DECL|field|multiParameterArray
specifier|private
name|boolean
name|multiParameterArray
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"Used for configuring additional properties on the bean"
argument_list|)
DECL|field|parameters
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
decl_stmt|;
DECL|method|BeanEndpoint ()
specifier|public
name|BeanEndpoint
parameter_list|()
block|{
name|setExchangePattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
block|}
DECL|method|BeanEndpoint (String endpointUri, Component component, BeanProcessor processor)
specifier|public
name|BeanEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|,
name|BeanProcessor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|processor
operator|=
name|processor
expr_stmt|;
name|setExchangePattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
block|}
DECL|method|BeanEndpoint (String endpointUri, Component component)
specifier|public
name|BeanEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|setExchangePattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|BeanProducer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"You cannot consume from a bean endpoint"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|getProcessor ()
specifier|public
name|BeanProcessor
name|getProcessor
parameter_list|()
block|{
return|return
name|processor
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
if|if
condition|(
name|processor
operator|==
literal|null
condition|)
block|{
name|BeanHolder
name|holder
init|=
name|getBeanHolder
argument_list|()
decl_stmt|;
if|if
condition|(
name|holder
operator|==
literal|null
condition|)
block|{
name|RegistryBean
name|registryBean
init|=
operator|new
name|RegistryBean
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|beanName
argument_list|)
decl_stmt|;
if|if
condition|(
name|cache
condition|)
block|{
name|holder
operator|=
name|registryBean
operator|.
name|createCacheHolder
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|holder
operator|=
name|registryBean
expr_stmt|;
block|}
block|}
name|processor
operator|=
operator|new
name|BeanProcessor
argument_list|(
name|holder
argument_list|)
expr_stmt|;
if|if
condition|(
name|method
operator|!=
literal|null
condition|)
block|{
name|processor
operator|.
name|setMethod
argument_list|(
name|method
argument_list|)
expr_stmt|;
block|}
name|processor
operator|.
name|setMultiParameterArray
argument_list|(
name|isMultiParameterArray
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|parameters
operator|!=
literal|null
condition|)
block|{
name|setProperties
argument_list|(
name|processor
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
comment|// noop
block|}
comment|// Properties
comment|//-------------------------------------------------------------------------
DECL|method|getBeanName ()
specifier|public
name|String
name|getBeanName
parameter_list|()
block|{
return|return
name|beanName
return|;
block|}
comment|/**      * Sets the name of the bean to invoke      */
DECL|method|setBeanName (String beanName)
specifier|public
name|void
name|setBeanName
parameter_list|(
name|String
name|beanName
parameter_list|)
block|{
name|this
operator|.
name|beanName
operator|=
name|beanName
expr_stmt|;
block|}
DECL|method|isMultiParameterArray ()
specifier|public
name|boolean
name|isMultiParameterArray
parameter_list|()
block|{
return|return
name|multiParameterArray
return|;
block|}
comment|/**      * How to treat the parameters which are passed from the message body;      * if it is true, the message body should be an array of parameters.      *<p/>      * Note: This option is used internally by Camel, and is not intended for end users to use.      *      * @deprecated this option is used internally by Camel, and is not intended for end users to use      */
annotation|@
name|Deprecated
DECL|method|setMultiParameterArray (boolean mpArray)
specifier|public
name|void
name|setMultiParameterArray
parameter_list|(
name|boolean
name|mpArray
parameter_list|)
block|{
name|multiParameterArray
operator|=
name|mpArray
expr_stmt|;
block|}
DECL|method|isCache ()
specifier|public
name|boolean
name|isCache
parameter_list|()
block|{
return|return
name|cache
return|;
block|}
comment|/**      * If enabled, Camel will cache the result of the first Registry look-up.      * Cache can be enabled if the bean in the Registry is defined as a singleton scope.      */
DECL|method|setCache (boolean cache)
specifier|public
name|void
name|setCache
parameter_list|(
name|boolean
name|cache
parameter_list|)
block|{
name|this
operator|.
name|cache
operator|=
name|cache
expr_stmt|;
block|}
DECL|method|getMethod ()
specifier|public
name|String
name|getMethod
parameter_list|()
block|{
return|return
name|method
return|;
block|}
comment|/**      * Sets the name of the method to invoke on the bean      */
DECL|method|setMethod (String method)
specifier|public
name|void
name|setMethod
parameter_list|(
name|String
name|method
parameter_list|)
block|{
name|this
operator|.
name|method
operator|=
name|method
expr_stmt|;
block|}
DECL|method|getBeanHolder ()
specifier|public
name|BeanHolder
name|getBeanHolder
parameter_list|()
block|{
return|return
name|beanHolder
return|;
block|}
DECL|method|setBeanHolder (BeanHolder beanHolder)
specifier|public
name|void
name|setBeanHolder
parameter_list|(
name|BeanHolder
name|beanHolder
parameter_list|)
block|{
name|this
operator|.
name|beanHolder
operator|=
name|beanHolder
expr_stmt|;
block|}
DECL|method|getParameters ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getParameters
parameter_list|()
block|{
return|return
name|parameters
return|;
block|}
comment|/**      * Used for configuring additional properties on the bean      */
DECL|method|setParameters (Map<String, Object> parameters)
specifier|public
name|void
name|setParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
name|this
operator|.
name|parameters
operator|=
name|parameters
expr_stmt|;
block|}
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
annotation|@
name|Override
DECL|method|createEndpointUri ()
specifier|protected
name|String
name|createEndpointUri
parameter_list|()
block|{
return|return
literal|"bean:"
operator|+
name|getBeanName
argument_list|()
operator|+
operator|(
name|method
operator|!=
literal|null
condition|?
literal|"?method="
operator|+
name|method
else|:
literal|""
operator|)
return|;
block|}
block|}
end_class

end_unit

