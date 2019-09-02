begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jpa
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jpa
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutorService
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|EntityManagerFactory
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
name|DefaultComponent
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
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|transaction
operator|.
name|PlatformTransactionManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|transaction
operator|.
name|support
operator|.
name|TransactionTemplate
import|;
end_import

begin_comment
comment|/**  * A JPA Component  */
end_comment

begin_class
annotation|@
name|Component
argument_list|(
literal|"jpa"
argument_list|)
DECL|class|JpaComponent
specifier|public
class|class
name|JpaComponent
extends|extends
name|DefaultComponent
block|{
DECL|field|pollingConsumerExecutorService
specifier|private
name|ExecutorService
name|pollingConsumerExecutorService
decl_stmt|;
DECL|field|entityManagerFactory
specifier|private
name|EntityManagerFactory
name|entityManagerFactory
decl_stmt|;
DECL|field|transactionManager
specifier|private
name|PlatformTransactionManager
name|transactionManager
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|joinTransaction
specifier|private
name|boolean
name|joinTransaction
init|=
literal|true
decl_stmt|;
DECL|field|sharedEntityManager
specifier|private
name|boolean
name|sharedEntityManager
decl_stmt|;
DECL|method|JpaComponent ()
specifier|public
name|JpaComponent
parameter_list|()
block|{     }
comment|// Properties
comment|//-------------------------------------------------------------------------
DECL|method|getEntityManagerFactory ()
specifier|public
name|EntityManagerFactory
name|getEntityManagerFactory
parameter_list|()
block|{
return|return
name|entityManagerFactory
return|;
block|}
comment|/**      * To use the {@link EntityManagerFactory}. This is strongly recommended to configure.      */
DECL|method|setEntityManagerFactory (EntityManagerFactory entityManagerFactory)
specifier|public
name|void
name|setEntityManagerFactory
parameter_list|(
name|EntityManagerFactory
name|entityManagerFactory
parameter_list|)
block|{
name|this
operator|.
name|entityManagerFactory
operator|=
name|entityManagerFactory
expr_stmt|;
block|}
DECL|method|getTransactionManager ()
specifier|public
name|PlatformTransactionManager
name|getTransactionManager
parameter_list|()
block|{
return|return
name|transactionManager
return|;
block|}
comment|/**      * To use the {@link PlatformTransactionManager} for managing transactions.      */
DECL|method|setTransactionManager (PlatformTransactionManager transactionManager)
specifier|public
name|void
name|setTransactionManager
parameter_list|(
name|PlatformTransactionManager
name|transactionManager
parameter_list|)
block|{
name|this
operator|.
name|transactionManager
operator|=
name|transactionManager
expr_stmt|;
block|}
DECL|method|isJoinTransaction ()
specifier|public
name|boolean
name|isJoinTransaction
parameter_list|()
block|{
return|return
name|joinTransaction
return|;
block|}
comment|/**      * The camel-jpa component will join transaction by default.      * You can use this option to turn this off, for example if you use LOCAL_RESOURCE and join transaction      * doesn't work with your JPA provider. This option can also be set globally on the JpaComponent,      * instead of having to set it on all endpoints.      */
DECL|method|setJoinTransaction (boolean joinTransaction)
specifier|public
name|void
name|setJoinTransaction
parameter_list|(
name|boolean
name|joinTransaction
parameter_list|)
block|{
name|this
operator|.
name|joinTransaction
operator|=
name|joinTransaction
expr_stmt|;
block|}
DECL|method|isSharedEntityManager ()
specifier|public
name|boolean
name|isSharedEntityManager
parameter_list|()
block|{
return|return
name|sharedEntityManager
return|;
block|}
comment|/**      * Whether to use Spring's SharedEntityManager for the consumer/producer.      * Note in most cases joinTransaction should be set to false as this is not an EXTENDED EntityManager.      */
DECL|method|setSharedEntityManager (boolean sharedEntityManager)
specifier|public
name|void
name|setSharedEntityManager
parameter_list|(
name|boolean
name|sharedEntityManager
parameter_list|)
block|{
name|this
operator|.
name|sharedEntityManager
operator|=
name|sharedEntityManager
expr_stmt|;
block|}
DECL|method|getOrCreatePollingConsumerExecutorService ()
specifier|synchronized
name|ExecutorService
name|getOrCreatePollingConsumerExecutorService
parameter_list|()
block|{
if|if
condition|(
name|pollingConsumerExecutorService
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Creating thread pool for JpaPollingConsumer to support polling using timeout"
argument_list|)
expr_stmt|;
name|pollingConsumerExecutorService
operator|=
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newDefaultThreadPool
argument_list|(
name|this
argument_list|,
literal|"JpaPollingConsumer"
argument_list|)
expr_stmt|;
block|}
return|return
name|pollingConsumerExecutorService
return|;
block|}
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String path, Map<String, Object> options)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|path
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
parameter_list|)
throws|throws
name|Exception
block|{
name|JpaEndpoint
name|endpoint
init|=
operator|new
name|JpaEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setJoinTransaction
argument_list|(
name|isJoinTransaction
argument_list|()
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setSharedEntityManager
argument_list|(
name|isSharedEntityManager
argument_list|()
argument_list|)
expr_stmt|;
comment|// lets interpret the next string as a class
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|path
argument_list|)
condition|)
block|{
comment|// provide the class loader of this component to work in OSGi environments as camel-jpa must be able
comment|// to resolve the entity classes
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveClass
argument_list|(
name|path
argument_list|,
name|JpaComponent
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setEntityType
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
block|}
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|options
argument_list|)
expr_stmt|;
return|return
name|endpoint
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
comment|// lookup entity manager factory and use it if only one provided
if|if
condition|(
name|entityManagerFactory
operator|==
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|EntityManagerFactory
argument_list|>
name|map
init|=
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|findByTypeWithName
argument_list|(
name|EntityManagerFactory
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|map
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|map
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|entityManagerFactory
operator|=
name|map
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Using EntityManagerFactory found in registry with id ["
operator|+
name|map
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|+
literal|"] "
operator|+
name|entityManagerFactory
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Could not find a single EntityManagerFactory in registry as there was {} instances."
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Using EntityManagerFactory configured: {}"
argument_list|,
name|entityManagerFactory
argument_list|)
expr_stmt|;
block|}
comment|// lookup transaction manager and use it if only one provided
if|if
condition|(
name|transactionManager
operator|==
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|PlatformTransactionManager
argument_list|>
name|map
init|=
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|findByTypeWithName
argument_list|(
name|PlatformTransactionManager
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|map
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|map
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|transactionManager
operator|=
name|map
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Using TransactionManager found in registry with id ["
operator|+
name|map
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|+
literal|"] "
operator|+
name|transactionManager
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Could not find a single TransactionManager in registry as there was {} instances."
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Using TransactionManager configured on this component: {}"
argument_list|,
name|transactionManager
argument_list|)
expr_stmt|;
block|}
comment|// transaction manager could also be hidden in a template
if|if
condition|(
name|transactionManager
operator|==
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|TransactionTemplate
argument_list|>
name|map
init|=
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|findByTypeWithName
argument_list|(
name|TransactionTemplate
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|map
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|map
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|transactionManager
operator|=
name|map
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getTransactionManager
argument_list|()
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Using TransactionManager found in registry with id ["
operator|+
name|map
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|+
literal|"] "
operator|+
name|transactionManager
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Could not find a single TransactionTemplate in registry as there was {} instances."
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// warn about missing configuration
if|if
condition|(
name|entityManagerFactory
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"No EntityManagerFactory has been configured on this JpaComponent. Each JpaEndpoint will auto create their own EntityManagerFactory."
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|transactionManager
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"No TransactionManager has been configured on this JpaComponent. Each JpaEndpoint will auto create their own JpaTransactionManager."
argument_list|)
expr_stmt|;
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
if|if
condition|(
name|pollingConsumerExecutorService
operator|!=
literal|null
condition|)
block|{
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdown
argument_list|(
name|pollingConsumerExecutorService
argument_list|)
expr_stmt|;
name|pollingConsumerExecutorService
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

