begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.health
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|health
package|;
end_package

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
name|Collections
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CopyOnWriteArraySet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Stream
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
name|CamelContextAware
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
name|health
operator|.
name|HealthCheck
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
name|health
operator|.
name|HealthCheckRegistry
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
name|health
operator|.
name|HealthCheckRepository
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

begin_class
DECL|class|DefaultHealthCheckRegistry
specifier|public
class|class
name|DefaultHealthCheckRegistry
implements|implements
name|HealthCheckRegistry
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
name|DefaultHealthCheckRegistry
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|checks
specifier|private
specifier|final
name|Set
argument_list|<
name|HealthCheck
argument_list|>
name|checks
decl_stmt|;
DECL|field|repositories
specifier|private
specifier|final
name|Set
argument_list|<
name|HealthCheckRepository
argument_list|>
name|repositories
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|method|DefaultHealthCheckRegistry ()
specifier|public
name|DefaultHealthCheckRegistry
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|DefaultHealthCheckRegistry (CamelContext camelContext)
specifier|public
name|DefaultHealthCheckRegistry
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|checks
operator|=
operator|new
name|CopyOnWriteArraySet
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|repositories
operator|=
operator|new
name|CopyOnWriteArraySet
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|repositories
operator|.
name|add
argument_list|(
operator|new
name|RegistryRepository
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|repositories
operator|.
name|addAll
argument_list|(
name|repositories
argument_list|)
expr_stmt|;
name|setCamelContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
comment|// ************************************
comment|// Properties
comment|// ************************************
annotation|@
name|Override
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
specifier|final
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
for|for
control|(
name|HealthCheck
name|check
range|:
name|checks
control|)
block|{
if|if
condition|(
name|check
operator|instanceof
name|CamelContextAware
condition|)
block|{
operator|(
operator|(
name|CamelContextAware
operator|)
name|check
operator|)
operator|.
name|setCamelContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|HealthCheckRepository
name|repository
range|:
name|repositories
control|)
block|{
if|if
condition|(
name|repository
operator|instanceof
name|CamelContextAware
condition|)
block|{
operator|(
operator|(
name|CamelContextAware
operator|)
name|repository
operator|)
operator|.
name|setCamelContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|getCamelContext ()
specifier|public
specifier|final
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
annotation|@
name|Override
DECL|method|register (HealthCheck check)
specifier|public
name|boolean
name|register
parameter_list|(
name|HealthCheck
name|check
parameter_list|)
block|{
name|boolean
name|result
init|=
name|checks
operator|.
name|add
argument_list|(
name|check
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
condition|)
block|{
if|if
condition|(
name|check
operator|instanceof
name|CamelContextAware
condition|)
block|{
operator|(
operator|(
name|CamelContextAware
operator|)
name|check
operator|)
operator|.
name|setCamelContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"HealthCheck with id {} successfully registered"
argument_list|,
name|check
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|unregister (HealthCheck check)
specifier|public
name|boolean
name|unregister
parameter_list|(
name|HealthCheck
name|check
parameter_list|)
block|{
name|boolean
name|result
init|=
name|checks
operator|.
name|remove
argument_list|(
name|check
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"HealthCheck with id {} successfully un-registered"
argument_list|,
name|check
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|setRepositories (Collection<HealthCheckRepository> repositories)
specifier|public
name|void
name|setRepositories
parameter_list|(
name|Collection
argument_list|<
name|HealthCheckRepository
argument_list|>
name|repositories
parameter_list|)
block|{
name|repositories
operator|.
name|clear
argument_list|()
expr_stmt|;
name|repositories
operator|.
name|addAll
argument_list|(
name|repositories
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getRepositories ()
specifier|public
name|Collection
argument_list|<
name|HealthCheckRepository
argument_list|>
name|getRepositories
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableCollection
argument_list|(
name|repositories
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|addRepository (HealthCheckRepository repository)
specifier|public
name|boolean
name|addRepository
parameter_list|(
name|HealthCheckRepository
name|repository
parameter_list|)
block|{
name|boolean
name|result
init|=
name|repositories
operator|.
name|add
argument_list|(
name|repository
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
condition|)
block|{
if|if
condition|(
name|repository
operator|instanceof
name|CamelContextAware
condition|)
block|{
operator|(
operator|(
name|CamelContextAware
operator|)
name|repository
operator|)
operator|.
name|setCamelContext
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"HealthCheckRepository {} successfully registered"
argument_list|,
name|repository
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|removeRepository (HealthCheckRepository repository)
specifier|public
name|boolean
name|removeRepository
parameter_list|(
name|HealthCheckRepository
name|repository
parameter_list|)
block|{
name|boolean
name|result
init|=
name|repositories
operator|.
name|remove
argument_list|(
name|repository
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"HealthCheckRepository with {} successfully un-registered"
argument_list|,
name|repository
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
comment|// ************************************
comment|//
comment|// ************************************
annotation|@
name|Override
DECL|method|stream ()
specifier|public
name|Stream
argument_list|<
name|HealthCheck
argument_list|>
name|stream
parameter_list|()
block|{
return|return
name|Stream
operator|.
name|concat
argument_list|(
name|checks
operator|.
name|stream
argument_list|()
argument_list|,
name|repositories
operator|.
name|stream
argument_list|()
operator|.
name|flatMap
argument_list|(
name|HealthCheckRepository
operator|::
name|stream
argument_list|)
argument_list|)
operator|.
name|distinct
argument_list|()
return|;
block|}
block|}
end_class

end_unit

