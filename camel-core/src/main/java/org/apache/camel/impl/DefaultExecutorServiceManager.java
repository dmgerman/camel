begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|List
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ScheduledExecutorService
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
name|ThreadFactory
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
name|ThreadPoolExecutor
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
name|TimeUnit
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
name|NamedNode
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
name|ThreadPoolRejectedPolicy
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
name|model
operator|.
name|OptionalIdentifiedDefinition
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
name|model
operator|.
name|ProcessorDefinition
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
name|model
operator|.
name|ProcessorDefinitionHelper
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
name|model
operator|.
name|RouteDefinition
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
name|ExecutorServiceManager
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
name|LifecycleStrategy
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
name|ThreadPoolFactory
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
name|ThreadPoolProfile
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
name|ServiceSupport
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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|URISupport
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
name|concurrent
operator|.
name|CamelThreadFactory
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
name|concurrent
operator|.
name|SizedScheduledExecutorService
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
name|concurrent
operator|.
name|ThreadHelper
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
comment|/**  * @version   */
end_comment

begin_class
DECL|class|DefaultExecutorServiceManager
specifier|public
class|class
name|DefaultExecutorServiceManager
extends|extends
name|ServiceSupport
implements|implements
name|ExecutorServiceManager
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
name|DefaultExecutorServiceManager
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|threadPoolFactory
specifier|private
name|ThreadPoolFactory
name|threadPoolFactory
init|=
operator|new
name|DefaultThreadPoolFactory
argument_list|()
decl_stmt|;
DECL|field|executorServices
specifier|private
specifier|final
name|List
argument_list|<
name|ExecutorService
argument_list|>
name|executorServices
init|=
operator|new
name|ArrayList
argument_list|<
name|ExecutorService
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|threadNamePattern
specifier|private
name|String
name|threadNamePattern
decl_stmt|;
DECL|field|defaultThreadPoolProfileId
specifier|private
name|String
name|defaultThreadPoolProfileId
init|=
literal|"defaultThreadPoolProfile"
decl_stmt|;
DECL|field|threadPoolProfiles
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|ThreadPoolProfile
argument_list|>
name|threadPoolProfiles
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|ThreadPoolProfile
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|builtIndefaultProfile
specifier|private
name|ThreadPoolProfile
name|builtIndefaultProfile
decl_stmt|;
DECL|method|DefaultExecutorServiceManager (CamelContext camelContext)
specifier|public
name|DefaultExecutorServiceManager
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
name|builtIndefaultProfile
operator|=
operator|new
name|ThreadPoolProfile
argument_list|(
name|defaultThreadPoolProfileId
argument_list|)
expr_stmt|;
name|builtIndefaultProfile
operator|.
name|setDefaultProfile
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|builtIndefaultProfile
operator|.
name|setPoolSize
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|builtIndefaultProfile
operator|.
name|setMaxPoolSize
argument_list|(
literal|20
argument_list|)
expr_stmt|;
name|builtIndefaultProfile
operator|.
name|setKeepAliveTime
argument_list|(
literal|60L
argument_list|)
expr_stmt|;
name|builtIndefaultProfile
operator|.
name|setTimeUnit
argument_list|(
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|builtIndefaultProfile
operator|.
name|setMaxQueueSize
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|builtIndefaultProfile
operator|.
name|setRejectedPolicy
argument_list|(
name|ThreadPoolRejectedPolicy
operator|.
name|CallerRuns
argument_list|)
expr_stmt|;
name|registerThreadPoolProfile
argument_list|(
name|builtIndefaultProfile
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getThreadPoolFactory ()
specifier|public
name|ThreadPoolFactory
name|getThreadPoolFactory
parameter_list|()
block|{
return|return
name|threadPoolFactory
return|;
block|}
annotation|@
name|Override
DECL|method|setThreadPoolFactory (ThreadPoolFactory threadPoolFactory)
specifier|public
name|void
name|setThreadPoolFactory
parameter_list|(
name|ThreadPoolFactory
name|threadPoolFactory
parameter_list|)
block|{
name|this
operator|.
name|threadPoolFactory
operator|=
name|threadPoolFactory
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|registerThreadPoolProfile (ThreadPoolProfile profile)
specifier|public
name|void
name|registerThreadPoolProfile
parameter_list|(
name|ThreadPoolProfile
name|profile
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|profile
argument_list|,
literal|"profile"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|profile
operator|.
name|getId
argument_list|()
argument_list|,
literal|"id"
argument_list|,
name|profile
argument_list|)
expr_stmt|;
name|threadPoolProfiles
operator|.
name|put
argument_list|(
name|profile
operator|.
name|getId
argument_list|()
argument_list|,
name|profile
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getThreadPoolProfile (String id)
specifier|public
name|ThreadPoolProfile
name|getThreadPoolProfile
parameter_list|(
name|String
name|id
parameter_list|)
block|{
return|return
name|threadPoolProfiles
operator|.
name|get
argument_list|(
name|id
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getDefaultThreadPoolProfile ()
specifier|public
name|ThreadPoolProfile
name|getDefaultThreadPoolProfile
parameter_list|()
block|{
return|return
name|getThreadPoolProfile
argument_list|(
name|defaultThreadPoolProfileId
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|setDefaultThreadPoolProfile (ThreadPoolProfile defaultThreadPoolProfile)
specifier|public
name|void
name|setDefaultThreadPoolProfile
parameter_list|(
name|ThreadPoolProfile
name|defaultThreadPoolProfile
parameter_list|)
block|{
name|threadPoolProfiles
operator|.
name|remove
argument_list|(
name|defaultThreadPoolProfileId
argument_list|)
expr_stmt|;
name|defaultThreadPoolProfile
operator|.
name|addDefaults
argument_list|(
name|builtIndefaultProfile
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Using custom DefaultThreadPoolProfile: "
operator|+
name|defaultThreadPoolProfile
argument_list|)
expr_stmt|;
name|this
operator|.
name|defaultThreadPoolProfileId
operator|=
name|defaultThreadPoolProfile
operator|.
name|getId
argument_list|()
expr_stmt|;
name|defaultThreadPoolProfile
operator|.
name|setDefaultProfile
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|registerThreadPoolProfile
argument_list|(
name|defaultThreadPoolProfile
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getThreadNamePattern ()
specifier|public
name|String
name|getThreadNamePattern
parameter_list|()
block|{
return|return
name|threadNamePattern
return|;
block|}
annotation|@
name|Override
DECL|method|setThreadNamePattern (String threadNamePattern)
specifier|public
name|void
name|setThreadNamePattern
parameter_list|(
name|String
name|threadNamePattern
parameter_list|)
block|{
comment|// must set camel id here in the pattern and let the other placeholders be resolved on demand
name|String
name|name
init|=
name|threadNamePattern
operator|.
name|replaceFirst
argument_list|(
literal|"#camelId#"
argument_list|,
name|this
operator|.
name|camelContext
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|this
operator|.
name|threadNamePattern
operator|=
name|name
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|resolveThreadName (String name)
specifier|public
name|String
name|resolveThreadName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|ThreadHelper
operator|.
name|resolveThreadName
argument_list|(
name|threadNamePattern
argument_list|,
name|name
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|newDefaultThreadPool (Object source, String name)
specifier|public
name|ExecutorService
name|newDefaultThreadPool
parameter_list|(
name|Object
name|source
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
name|newThreadPool
argument_list|(
name|source
argument_list|,
name|name
argument_list|,
name|getDefaultThreadPoolProfile
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|newDefaultScheduledThreadPool (Object source, String name)
specifier|public
name|ScheduledExecutorService
name|newDefaultScheduledThreadPool
parameter_list|(
name|Object
name|source
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
name|newScheduledThreadPool
argument_list|(
name|source
argument_list|,
name|name
argument_list|,
name|getDefaultThreadPoolProfile
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|newThreadPool (Object source, String name, String profileId)
specifier|public
name|ExecutorService
name|newThreadPool
parameter_list|(
name|Object
name|source
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|profileId
parameter_list|)
block|{
name|ThreadPoolProfile
name|profile
init|=
name|getThreadPoolProfile
argument_list|(
name|profileId
argument_list|)
decl_stmt|;
if|if
condition|(
name|profile
operator|!=
literal|null
condition|)
block|{
return|return
name|newThreadPool
argument_list|(
name|source
argument_list|,
name|name
argument_list|,
name|profile
argument_list|)
return|;
block|}
else|else
block|{
comment|// no profile with that id
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|newThreadPool (Object source, String name, ThreadPoolProfile profile)
specifier|public
name|ExecutorService
name|newThreadPool
parameter_list|(
name|Object
name|source
parameter_list|,
name|String
name|name
parameter_list|,
name|ThreadPoolProfile
name|profile
parameter_list|)
block|{
name|String
name|sanitizedName
init|=
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|profile
argument_list|,
literal|"ThreadPoolProfile"
argument_list|)
expr_stmt|;
name|ThreadPoolProfile
name|defaultProfile
init|=
name|getDefaultThreadPoolProfile
argument_list|()
decl_stmt|;
name|profile
operator|.
name|addDefaults
argument_list|(
name|defaultProfile
argument_list|)
expr_stmt|;
name|ThreadFactory
name|threadFactory
init|=
name|createThreadFactory
argument_list|(
name|sanitizedName
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|ExecutorService
name|executorService
init|=
name|threadPoolFactory
operator|.
name|newThreadPool
argument_list|(
name|profile
argument_list|,
name|threadFactory
argument_list|)
decl_stmt|;
name|onThreadPoolCreated
argument_list|(
name|executorService
argument_list|,
name|source
argument_list|,
name|profile
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Created new ThreadPool for source: {} with name: {}. -> {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|source
block|,
name|sanitizedName
block|,
name|executorService
block|}
argument_list|)
expr_stmt|;
block|}
return|return
name|executorService
return|;
block|}
annotation|@
name|Override
DECL|method|newThreadPool (Object source, String name, int poolSize, int maxPoolSize)
specifier|public
name|ExecutorService
name|newThreadPool
parameter_list|(
name|Object
name|source
parameter_list|,
name|String
name|name
parameter_list|,
name|int
name|poolSize
parameter_list|,
name|int
name|maxPoolSize
parameter_list|)
block|{
name|ThreadPoolProfile
name|profile
init|=
operator|new
name|ThreadPoolProfile
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|profile
operator|.
name|setPoolSize
argument_list|(
name|poolSize
argument_list|)
expr_stmt|;
name|profile
operator|.
name|setMaxPoolSize
argument_list|(
name|maxPoolSize
argument_list|)
expr_stmt|;
return|return
name|newThreadPool
argument_list|(
name|source
argument_list|,
name|name
argument_list|,
name|profile
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|newSingleThreadExecutor (Object source, String name)
specifier|public
name|ExecutorService
name|newSingleThreadExecutor
parameter_list|(
name|Object
name|source
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
name|newFixedThreadPool
argument_list|(
name|source
argument_list|,
name|name
argument_list|,
literal|1
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|newCachedThreadPool (Object source, String name)
specifier|public
name|ExecutorService
name|newCachedThreadPool
parameter_list|(
name|Object
name|source
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|String
name|sanitizedName
init|=
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|ExecutorService
name|answer
init|=
name|threadPoolFactory
operator|.
name|newCachedThreadPool
argument_list|(
name|createThreadFactory
argument_list|(
name|sanitizedName
argument_list|,
literal|true
argument_list|)
argument_list|)
decl_stmt|;
name|onThreadPoolCreated
argument_list|(
name|answer
argument_list|,
name|source
argument_list|,
literal|null
argument_list|)
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Created new CachedThreadPool for source: {} with name: {}. -> {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|source
block|,
name|sanitizedName
block|,
name|answer
block|}
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|newFixedThreadPool (Object source, String name, int poolSize)
specifier|public
name|ExecutorService
name|newFixedThreadPool
parameter_list|(
name|Object
name|source
parameter_list|,
name|String
name|name
parameter_list|,
name|int
name|poolSize
parameter_list|)
block|{
name|ThreadPoolProfile
name|profile
init|=
operator|new
name|ThreadPoolProfile
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|profile
operator|.
name|setPoolSize
argument_list|(
name|poolSize
argument_list|)
expr_stmt|;
name|profile
operator|.
name|setMaxPoolSize
argument_list|(
name|poolSize
argument_list|)
expr_stmt|;
name|profile
operator|.
name|setKeepAliveTime
argument_list|(
literal|0L
argument_list|)
expr_stmt|;
return|return
name|newThreadPool
argument_list|(
name|source
argument_list|,
name|name
argument_list|,
name|profile
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|newSingleThreadScheduledExecutor (Object source, String name)
specifier|public
name|ScheduledExecutorService
name|newSingleThreadScheduledExecutor
parameter_list|(
name|Object
name|source
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
name|newScheduledThreadPool
argument_list|(
name|source
argument_list|,
name|name
argument_list|,
literal|1
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|newScheduledThreadPool (Object source, String name, ThreadPoolProfile profile)
specifier|public
name|ScheduledExecutorService
name|newScheduledThreadPool
parameter_list|(
name|Object
name|source
parameter_list|,
name|String
name|name
parameter_list|,
name|ThreadPoolProfile
name|profile
parameter_list|)
block|{
name|String
name|sanitizedName
init|=
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|profile
operator|.
name|addDefaults
argument_list|(
name|getDefaultThreadPoolProfile
argument_list|()
argument_list|)
expr_stmt|;
name|ScheduledExecutorService
name|answer
init|=
name|threadPoolFactory
operator|.
name|newScheduledThreadPool
argument_list|(
name|profile
argument_list|,
name|createThreadFactory
argument_list|(
name|sanitizedName
argument_list|,
literal|true
argument_list|)
argument_list|)
decl_stmt|;
name|onThreadPoolCreated
argument_list|(
name|answer
argument_list|,
name|source
argument_list|,
literal|null
argument_list|)
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Created new ScheduledThreadPool for source: {} with name: {}. -> {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|source
block|,
name|sanitizedName
block|,
name|answer
block|}
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|newScheduledThreadPool (Object source, String name, String profileId)
specifier|public
name|ScheduledExecutorService
name|newScheduledThreadPool
parameter_list|(
name|Object
name|source
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|profileId
parameter_list|)
block|{
name|ThreadPoolProfile
name|profile
init|=
name|getThreadPoolProfile
argument_list|(
name|profileId
argument_list|)
decl_stmt|;
if|if
condition|(
name|profile
operator|!=
literal|null
condition|)
block|{
return|return
name|newScheduledThreadPool
argument_list|(
name|source
argument_list|,
name|name
argument_list|,
name|profile
argument_list|)
return|;
block|}
else|else
block|{
comment|// no profile with that id
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|newScheduledThreadPool (Object source, String name, int poolSize)
specifier|public
name|ScheduledExecutorService
name|newScheduledThreadPool
parameter_list|(
name|Object
name|source
parameter_list|,
name|String
name|name
parameter_list|,
name|int
name|poolSize
parameter_list|)
block|{
name|ThreadPoolProfile
name|profile
init|=
operator|new
name|ThreadPoolProfile
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|profile
operator|.
name|setPoolSize
argument_list|(
name|poolSize
argument_list|)
expr_stmt|;
return|return
name|newScheduledThreadPool
argument_list|(
name|source
argument_list|,
name|name
argument_list|,
name|profile
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|shutdown (ExecutorService executorService)
specifier|public
name|void
name|shutdown
parameter_list|(
name|ExecutorService
name|executorService
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|executorService
argument_list|,
literal|"executorService"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|executorService
operator|.
name|isShutdown
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Shutdown ExecutorService: {}"
argument_list|,
name|executorService
argument_list|)
expr_stmt|;
name|executorService
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Shutdown ExecutorService: {} complete."
argument_list|,
name|executorService
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|executorService
operator|instanceof
name|ThreadPoolExecutor
condition|)
block|{
name|ThreadPoolExecutor
name|threadPool
init|=
operator|(
name|ThreadPoolExecutor
operator|)
name|executorService
decl_stmt|;
for|for
control|(
name|LifecycleStrategy
name|lifecycle
range|:
name|camelContext
operator|.
name|getLifecycleStrategies
argument_list|()
control|)
block|{
name|lifecycle
operator|.
name|onThreadPoolRemove
argument_list|(
name|camelContext
argument_list|,
name|threadPool
argument_list|)
expr_stmt|;
block|}
block|}
comment|// remove reference as its shutdown
name|executorServices
operator|.
name|remove
argument_list|(
name|executorService
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|shutdownNow (ExecutorService executorService)
specifier|public
name|List
argument_list|<
name|Runnable
argument_list|>
name|shutdownNow
parameter_list|(
name|ExecutorService
name|executorService
parameter_list|)
block|{
return|return
name|doShutdownNow
argument_list|(
name|executorService
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|doShutdownNow (ExecutorService executorService, boolean remove)
specifier|private
name|List
argument_list|<
name|Runnable
argument_list|>
name|doShutdownNow
parameter_list|(
name|ExecutorService
name|executorService
parameter_list|,
name|boolean
name|remove
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|executorService
argument_list|,
literal|"executorService"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Runnable
argument_list|>
name|answer
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|executorService
operator|.
name|isShutdown
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"ShutdownNow ExecutorService: {}"
argument_list|,
name|executorService
argument_list|)
expr_stmt|;
name|answer
operator|=
name|executorService
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"ShutdownNow ExecutorService: {} complete."
argument_list|,
name|executorService
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|executorService
operator|instanceof
name|ThreadPoolExecutor
condition|)
block|{
name|ThreadPoolExecutor
name|threadPool
init|=
operator|(
name|ThreadPoolExecutor
operator|)
name|executorService
decl_stmt|;
for|for
control|(
name|LifecycleStrategy
name|lifecycle
range|:
name|camelContext
operator|.
name|getLifecycleStrategies
argument_list|()
control|)
block|{
name|lifecycle
operator|.
name|onThreadPoolRemove
argument_list|(
name|camelContext
argument_list|,
name|threadPool
argument_list|)
expr_stmt|;
block|}
block|}
comment|// remove reference as its shutdown
if|if
condition|(
name|remove
condition|)
block|{
name|executorServices
operator|.
name|remove
argument_list|(
name|executorService
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Strategy callback when a new {@link java.util.concurrent.ExecutorService} have been created.      *      * @param executorService the created {@link java.util.concurrent.ExecutorService}      */
DECL|method|onNewExecutorService (ExecutorService executorService)
specifier|protected
name|void
name|onNewExecutorService
parameter_list|(
name|ExecutorService
name|executorService
parameter_list|)
block|{
comment|// noop
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
if|if
condition|(
name|threadNamePattern
operator|==
literal|null
condition|)
block|{
comment|// set default name pattern which includes the camel context name
name|threadNamePattern
operator|=
literal|"Camel ("
operator|+
name|camelContext
operator|.
name|getName
argument_list|()
operator|+
literal|") thread ##counter# - #name#"
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
comment|// noop
block|}
annotation|@
name|Override
DECL|method|doShutdown ()
specifier|protected
name|void
name|doShutdown
parameter_list|()
throws|throws
name|Exception
block|{
comment|// shutdown all executor services by looping
for|for
control|(
name|ExecutorService
name|executorService
range|:
name|executorServices
control|)
block|{
comment|// only log if something goes wrong as we want to shutdown them all
try|try
block|{
comment|// must not remove during looping, as we clear the list afterwards
name|doShutdownNow
argument_list|(
name|executorService
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error occurred during shutdown of ExecutorService: "
operator|+
name|executorService
operator|+
literal|". This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
name|executorServices
operator|.
name|clear
argument_list|()
expr_stmt|;
comment|// do not clear the default profile as we could potential be restarted
name|Iterator
argument_list|<
name|ThreadPoolProfile
argument_list|>
name|it
init|=
name|threadPoolProfiles
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ThreadPoolProfile
name|profile
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|profile
operator|.
name|isDefaultProfile
argument_list|()
condition|)
block|{
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Invoked when a new thread pool is created.      * This implementation will invoke the {@link LifecycleStrategy#onThreadPoolAdd(org.apache.camel.CamelContext,      * java.util.concurrent.ThreadPoolExecutor, String, String, String, String) LifecycleStrategy.onThreadPoolAdd} method,      * which for example will enlist the thread pool in JMX management.      *      * @param executorService the thread pool      * @param source          the source to use the thread pool      * @param threadPoolProfileId profile id, if the thread pool was created from a thread pool profile      */
DECL|method|onThreadPoolCreated (ExecutorService executorService, Object source, String threadPoolProfileId)
specifier|private
name|void
name|onThreadPoolCreated
parameter_list|(
name|ExecutorService
name|executorService
parameter_list|,
name|Object
name|source
parameter_list|,
name|String
name|threadPoolProfileId
parameter_list|)
block|{
comment|// add to internal list of thread pools
name|executorServices
operator|.
name|add
argument_list|(
name|executorService
argument_list|)
expr_stmt|;
name|String
name|id
decl_stmt|;
name|String
name|sourceId
init|=
literal|null
decl_stmt|;
name|String
name|routeId
init|=
literal|null
decl_stmt|;
comment|// extract id from source
if|if
condition|(
name|source
operator|instanceof
name|NamedNode
condition|)
block|{
name|id
operator|=
operator|(
operator|(
name|OptionalIdentifiedDefinition
argument_list|<
name|?
argument_list|>
operator|)
name|source
operator|)
operator|.
name|idOrCreate
argument_list|(
name|this
operator|.
name|camelContext
operator|.
name|getNodeIdFactory
argument_list|()
argument_list|)
expr_stmt|;
comment|// and let source be the short name of the pattern
name|sourceId
operator|=
operator|(
operator|(
name|NamedNode
operator|)
name|source
operator|)
operator|.
name|getShortName
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|source
operator|instanceof
name|String
condition|)
block|{
name|id
operator|=
operator|(
name|String
operator|)
name|source
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|source
operator|!=
literal|null
condition|)
block|{
comment|// fallback and use the simple class name with hashcode for the id so its unique for this given source
name|id
operator|=
name|source
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|"("
operator|+
name|ObjectHelper
operator|.
name|getIdentityHashCode
argument_list|(
name|source
argument_list|)
operator|+
literal|")"
expr_stmt|;
block|}
else|else
block|{
comment|// no source, so fallback and use the simple class name from thread pool and its hashcode identity so its unique
name|id
operator|=
name|executorService
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|"("
operator|+
name|ObjectHelper
operator|.
name|getIdentityHashCode
argument_list|(
name|executorService
argument_list|)
operator|+
literal|")"
expr_stmt|;
block|}
comment|// id is mandatory
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|id
argument_list|,
literal|"id for thread pool "
operator|+
name|executorService
argument_list|)
expr_stmt|;
comment|// extract route id if possible
if|if
condition|(
name|source
operator|instanceof
name|ProcessorDefinition
condition|)
block|{
name|RouteDefinition
name|route
init|=
name|ProcessorDefinitionHelper
operator|.
name|getRoute
argument_list|(
operator|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
operator|)
name|source
argument_list|)
decl_stmt|;
if|if
condition|(
name|route
operator|!=
literal|null
condition|)
block|{
name|routeId
operator|=
name|route
operator|.
name|idOrCreate
argument_list|(
name|this
operator|.
name|camelContext
operator|.
name|getNodeIdFactory
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// let lifecycle strategy be notified as well which can let it be managed in JMX as well
name|ThreadPoolExecutor
name|threadPool
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|executorService
operator|instanceof
name|ThreadPoolExecutor
condition|)
block|{
name|threadPool
operator|=
operator|(
name|ThreadPoolExecutor
operator|)
name|executorService
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|executorService
operator|instanceof
name|SizedScheduledExecutorService
condition|)
block|{
name|threadPool
operator|=
operator|(
operator|(
name|SizedScheduledExecutorService
operator|)
name|executorService
operator|)
operator|.
name|getScheduledThreadPoolExecutor
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|threadPool
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|LifecycleStrategy
name|lifecycle
range|:
name|camelContext
operator|.
name|getLifecycleStrategies
argument_list|()
control|)
block|{
name|lifecycle
operator|.
name|onThreadPoolAdd
argument_list|(
name|camelContext
argument_list|,
name|threadPool
argument_list|,
name|id
argument_list|,
name|sourceId
argument_list|,
name|routeId
argument_list|,
name|threadPoolProfileId
argument_list|)
expr_stmt|;
block|}
block|}
comment|// now call strategy to allow custom logic
name|onNewExecutorService
argument_list|(
name|executorService
argument_list|)
expr_stmt|;
block|}
DECL|method|createThreadFactory (String name, boolean isDaemon)
specifier|private
name|ThreadFactory
name|createThreadFactory
parameter_list|(
name|String
name|name
parameter_list|,
name|boolean
name|isDaemon
parameter_list|)
block|{
name|ThreadFactory
name|threadFactory
init|=
operator|new
name|CamelThreadFactory
argument_list|(
name|threadNamePattern
argument_list|,
name|name
argument_list|,
name|isDaemon
argument_list|)
decl_stmt|;
return|return
name|threadFactory
return|;
block|}
block|}
end_class

end_unit

