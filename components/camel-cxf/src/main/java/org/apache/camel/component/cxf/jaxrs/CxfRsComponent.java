begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.jaxrs
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|jaxrs
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

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
name|SSLContextParametersAware
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
name|cxf
operator|.
name|blueprint
operator|.
name|BlueprintSupport
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
name|cxf
operator|.
name|common
operator|.
name|message
operator|.
name|CxfConstants
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
name|annotations
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
name|support
operator|.
name|CamelContextHelper
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
name|HeaderFilterStrategyComponent
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
name|ObjectHelper
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
name|util
operator|.
name|CastUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|AbstractJAXRSFactoryBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Defines the<a href="http://camel.apache.org/cxfrs.html">CXF RS Component</a>   */
end_comment

begin_class
annotation|@
name|Component
argument_list|(
literal|"cxfrs"
argument_list|)
DECL|class|CxfRsComponent
specifier|public
class|class
name|CxfRsComponent
extends|extends
name|HeaderFilterStrategyComponent
implements|implements
name|SSLContextParametersAware
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CxfRsComponent
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|useGlobalSslContextParameters
specifier|private
name|boolean
name|useGlobalSslContextParameters
decl_stmt|;
DECL|method|CxfRsComponent ()
specifier|public
name|CxfRsComponent
parameter_list|()
block|{     }
DECL|method|CxfRsComponent (CamelContext context)
specifier|public
name|CxfRsComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|CxfRsEndpoint
name|answer
decl_stmt|;
name|Object
name|value
init|=
name|parameters
operator|.
name|remove
argument_list|(
literal|"setDefaultBus"
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"The option setDefaultBus is @deprecated, use name defaultBus instead"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|parameters
operator|.
name|containsKey
argument_list|(
literal|"defaultBus"
argument_list|)
condition|)
block|{
name|parameters
operator|.
name|put
argument_list|(
literal|"defaultBus"
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|remaining
operator|.
name|startsWith
argument_list|(
name|CxfConstants
operator|.
name|SPRING_CONTEXT_ENDPOINT
argument_list|)
condition|)
block|{
comment|// Get the bean from the Spring context
name|String
name|beanId
init|=
name|remaining
operator|.
name|substring
argument_list|(
name|CxfConstants
operator|.
name|SPRING_CONTEXT_ENDPOINT
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|beanId
operator|.
name|startsWith
argument_list|(
literal|"//"
argument_list|)
condition|)
block|{
name|beanId
operator|=
name|beanId
operator|.
name|substring
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
name|AbstractJAXRSFactoryBean
name|bean
init|=
name|CamelContextHelper
operator|.
name|mandatoryLookup
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|beanId
argument_list|,
name|AbstractJAXRSFactoryBean
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|bean
operator|instanceof
name|BlueprintSupport
condition|)
block|{
name|answer
operator|=
operator|new
name|CxfRsBlueprintEndpoint
argument_list|(
name|this
argument_list|,
name|remaining
argument_list|,
name|bean
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
operator|new
name|CxfRsSpringEndpoint
argument_list|(
name|this
argument_list|,
name|remaining
argument_list|,
name|bean
argument_list|)
expr_stmt|;
block|}
comment|// Apply Spring bean properties (including # notation referenced bean).  Note that the
comment|// Spring bean properties values can be overridden by property defined in URI query.
comment|// The super class (DefaultComponent) will invoke "setProperties" after this method
comment|// with to apply properties defined by URI query.
if|if
condition|(
name|bean
operator|.
name|getProperties
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|copy
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|copy
operator|.
name|putAll
argument_list|(
name|bean
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|answer
argument_list|,
name|copy
argument_list|)
expr_stmt|;
block|}
comment|// setup the skipFaultLogging
name|answer
operator|.
name|setBeanId
argument_list|(
name|beanId
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// endpoint URI does not specify a bean
name|answer
operator|=
operator|new
name|CxfRsEndpoint
argument_list|(
name|remaining
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
name|String
name|resourceClass
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"resourceClass"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|resourceClass
operator|!=
literal|null
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveMandatoryClass
argument_list|(
name|resourceClass
argument_list|)
decl_stmt|;
name|answer
operator|.
name|addResourceClass
argument_list|(
name|clazz
argument_list|)
expr_stmt|;
block|}
name|String
name|resourceClasses
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"resourceClasses"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|ObjectHelper
operator|.
name|createIterator
argument_list|(
name|resourceClasses
argument_list|)
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|name
init|=
operator|(
name|String
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveMandatoryClass
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|answer
operator|.
name|addResourceClass
argument_list|(
name|clazz
argument_list|)
expr_stmt|;
block|}
name|setProperties
argument_list|(
name|answer
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|parameters
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setParameters
argument_list|(
name|params
argument_list|)
expr_stmt|;
name|setEndpointHeaderFilterStrategy
argument_list|(
name|answer
argument_list|)
expr_stmt|;
comment|// use global ssl config if set
if|if
condition|(
name|answer
operator|.
name|getSslContextParameters
argument_list|()
operator|==
literal|null
condition|)
block|{
name|answer
operator|.
name|setSslContextParameters
argument_list|(
name|retrieveGlobalSslContextParameters
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|afterConfiguration (String uri, String remaining, Endpoint endpoint, Map<String, Object> parameters)
specifier|protected
name|void
name|afterConfiguration
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|CxfRsEndpoint
name|cxfRsEndpoint
init|=
operator|(
name|CxfRsEndpoint
operator|)
name|endpoint
decl_stmt|;
name|cxfRsEndpoint
operator|.
name|updateEndpointUri
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isUseGlobalSslContextParameters ()
specifier|public
name|boolean
name|isUseGlobalSslContextParameters
parameter_list|()
block|{
return|return
name|this
operator|.
name|useGlobalSslContextParameters
return|;
block|}
comment|/**      * Enable usage of global SSL context parameters.      */
annotation|@
name|Override
DECL|method|setUseGlobalSslContextParameters (boolean useGlobalSslContextParameters)
specifier|public
name|void
name|setUseGlobalSslContextParameters
parameter_list|(
name|boolean
name|useGlobalSslContextParameters
parameter_list|)
block|{
name|this
operator|.
name|useGlobalSslContextParameters
operator|=
name|useGlobalSslContextParameters
expr_stmt|;
block|}
block|}
end_class

end_unit

