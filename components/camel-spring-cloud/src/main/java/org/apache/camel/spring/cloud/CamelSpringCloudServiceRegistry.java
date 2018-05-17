begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.cloud
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|cloud
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|Objects
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
name|cloud
operator|.
name|ServiceDefinition
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
name|cloud
operator|.
name|AbstractServiceRegistry
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|cloud
operator|.
name|client
operator|.
name|serviceregistry
operator|.
name|Registration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|cloud
operator|.
name|client
operator|.
name|serviceregistry
operator|.
name|ServiceRegistry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|convert
operator|.
name|ConversionService
import|;
end_import

begin_class
DECL|class|CamelSpringCloudServiceRegistry
specifier|public
class|class
name|CamelSpringCloudServiceRegistry
extends|extends
name|AbstractServiceRegistry
block|{
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CamelSpringCloudServiceRegistry
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|definitions
specifier|private
specifier|final
name|List
argument_list|<
name|ServiceDefinition
argument_list|>
name|definitions
decl_stmt|;
DECL|field|conversionServices
specifier|private
specifier|final
name|List
argument_list|<
name|ConversionService
argument_list|>
name|conversionServices
decl_stmt|;
DECL|field|serviceRegistry
specifier|private
specifier|final
name|ServiceRegistry
name|serviceRegistry
decl_stmt|;
DECL|field|registrationType
specifier|private
specifier|final
name|Class
argument_list|<
name|?
extends|extends
name|Registration
argument_list|>
name|registrationType
decl_stmt|;
DECL|method|CamelSpringCloudServiceRegistry (Collection<ConversionService> conversionServices, ServiceRegistry serviceRegistry)
specifier|public
name|CamelSpringCloudServiceRegistry
parameter_list|(
name|Collection
argument_list|<
name|ConversionService
argument_list|>
name|conversionServices
parameter_list|,
name|ServiceRegistry
name|serviceRegistry
parameter_list|)
block|{
name|this
operator|.
name|definitions
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|conversionServices
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|conversionServices
argument_list|)
expr_stmt|;
name|this
operator|.
name|serviceRegistry
operator|=
name|serviceRegistry
expr_stmt|;
name|this
operator|.
name|registrationType
operator|=
name|determineRegistrationType
argument_list|(
literal|"register"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|register (ServiceDefinition definition)
specifier|public
name|void
name|register
parameter_list|(
name|ServiceDefinition
name|definition
parameter_list|)
block|{
name|Registration
name|result
init|=
name|convertServiceDefinition
argument_list|(
name|definition
argument_list|)
decl_stmt|;
synchronized|synchronized
init|(
name|this
init|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Register service with definition: {} with registrations: {}"
argument_list|,
name|definition
argument_list|,
name|registrationType
argument_list|)
expr_stmt|;
name|serviceRegistry
operator|.
name|register
argument_list|(
name|result
argument_list|)
expr_stmt|;
comment|// keep track of registered definition to remove them upon registry
comment|// shutdown
if|if
condition|(
name|definitions
operator|.
name|stream
argument_list|()
operator|.
name|noneMatch
argument_list|(
name|d
lambda|->
name|matchById
argument_list|(
name|d
argument_list|,
name|definition
argument_list|)
argument_list|)
condition|)
block|{
name|definitions
operator|.
name|add
argument_list|(
name|definition
argument_list|)
block|;             }
block|}
block|}
annotation|@
name|Override
DECL|method|deregister (ServiceDefinition definition)
specifier|public
name|void
name|deregister
parameter_list|(
name|ServiceDefinition
name|definition
parameter_list|)
block|{
name|Registration
name|result
init|=
name|convertServiceDefinition
argument_list|(
name|definition
argument_list|)
decl_stmt|;
synchronized|synchronized
init|(
name|this
init|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Deregister service with definition: {} with registrations: {}"
argument_list|,
name|definition
argument_list|,
name|registrationType
argument_list|)
expr_stmt|;
name|serviceRegistry
operator|.
name|deregister
argument_list|(
name|result
argument_list|)
expr_stmt|;
comment|// remove any instance with the same id
name|definitions
operator|.
name|removeIf
argument_list|(
name|d
lambda|->
name|matchById
argument_list|(
name|d
argument_list|,
name|definition
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
block|{     }
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
comment|// TODO: need to be improved
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|definitions
argument_list|)
operator|.
name|forEach
argument_list|(
name|this
operator|::
name|deregister
argument_list|)
expr_stmt|;
block|}
DECL|method|getNativeServiceRegistry ()
specifier|public
name|ServiceRegistry
name|getNativeServiceRegistry
parameter_list|()
block|{
return|return
name|this
operator|.
name|serviceRegistry
return|;
block|}
DECL|method|getNativeServiceRegistry (Class<T> type)
specifier|public
parameter_list|<
name|R
extends|extends
name|Registration
parameter_list|,
name|T
extends|extends
name|ServiceRegistry
argument_list|<
name|R
argument_list|>
parameter_list|>
name|T
name|getNativeServiceRegistry
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|type
operator|.
name|cast
argument_list|(
name|this
operator|.
name|serviceRegistry
argument_list|)
return|;
block|}
comment|/**      * Determine the native registration type. This is needed because the registry      * specific implementation provided by spring-cloud-xyz does not handle generic      * Registration object but needs a Registration specific to the underlying      * technology used.      *      * @return the registration type      */
DECL|method|determineRegistrationType (String methodName)
specifier|private
name|Class
argument_list|<
name|?
extends|extends
name|Registration
argument_list|>
name|determineRegistrationType
parameter_list|(
name|String
name|methodName
parameter_list|)
block|{
name|Class
argument_list|<
name|?
extends|extends
name|Registration
argument_list|>
name|type
init|=
literal|null
decl_stmt|;
name|Method
index|[]
name|methods
init|=
name|serviceRegistry
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredMethods
argument_list|()
decl_stmt|;
for|for
control|(
name|Method
name|method
range|:
name|methods
control|)
block|{
if|if
condition|(
operator|!
name|methodName
operator|.
name|equals
argument_list|(
name|method
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|method
operator|.
name|getParameterCount
argument_list|()
operator|!=
literal|1
condition|)
block|{
continue|continue;
block|}
name|Class
argument_list|<
name|?
argument_list|>
name|parameterType
init|=
name|method
operator|.
name|getParameterTypes
argument_list|()
index|[
literal|0
index|]
decl_stmt|;
if|if
condition|(
name|Registration
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|parameterType
argument_list|)
condition|)
block|{
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
name|type
operator|=
operator|(
name|Class
argument_list|<
name|?
extends|extends
name|Registration
argument_list|>
operator|)
name|parameterType
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|type
operator|.
name|isAssignableFrom
argument_list|(
name|parameterType
argument_list|)
condition|)
block|{
name|type
operator|=
operator|(
name|Class
argument_list|<
name|?
extends|extends
name|Registration
argument_list|>
operator|)
name|parameterType
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|type
operator|!=
literal|null
condition|?
name|type
else|:
name|Registration
operator|.
name|class
return|;
block|}
DECL|method|matchById (ServiceDefinition definition, ServiceDefinition reference)
specifier|private
name|boolean
name|matchById
parameter_list|(
name|ServiceDefinition
name|definition
parameter_list|,
name|ServiceDefinition
name|reference
parameter_list|)
block|{
if|if
condition|(
name|definition
operator|.
name|getId
argument_list|()
operator|==
literal|null
operator|||
name|reference
operator|.
name|getId
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|Objects
operator|.
name|equals
argument_list|(
name|definition
operator|.
name|getId
argument_list|()
argument_list|,
name|reference
operator|.
name|getId
argument_list|()
argument_list|)
return|;
block|}
DECL|method|convertServiceDefinition (ServiceDefinition definition)
specifier|private
name|Registration
name|convertServiceDefinition
parameter_list|(
name|ServiceDefinition
name|definition
parameter_list|)
block|{
for|for
control|(
name|ConversionService
name|conversionService
range|:
name|conversionServices
control|)
block|{
if|if
condition|(
name|conversionService
operator|.
name|canConvert
argument_list|(
name|ServiceDefinition
operator|.
name|class
argument_list|,
name|registrationType
argument_list|)
condition|)
block|{
return|return
name|conversionService
operator|.
name|convert
argument_list|(
name|definition
argument_list|,
name|registrationType
argument_list|)
return|;
block|}
block|}
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unable to convert service definition to native registration of type:"
operator|+
name|registrationType
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

