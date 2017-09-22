begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.ha
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|ha
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|locks
operator|.
name|StampedLock
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
name|ha
operator|.
name|CamelClusterService
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
name|ha
operator|.
name|CamelClusterView
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
name|ReferenceCount
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
name|LockHelper
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
DECL|class|AbstractCamelClusterService
specifier|public
specifier|abstract
class|class
name|AbstractCamelClusterService
parameter_list|<
name|T
extends|extends
name|CamelClusterView
parameter_list|>
extends|extends
name|ServiceSupport
implements|implements
name|CamelClusterService
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
name|AbstractCamelClusterService
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|views
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|ViewHolder
argument_list|<
name|T
argument_list|>
argument_list|>
name|views
decl_stmt|;
DECL|field|lock
specifier|private
specifier|final
name|StampedLock
name|lock
decl_stmt|;
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|method|AbstractCamelClusterService ()
specifier|protected
name|AbstractCamelClusterService
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|AbstractCamelClusterService (String id)
specifier|protected
name|AbstractCamelClusterService
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
argument_list|(
name|id
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|AbstractCamelClusterService (String id, CamelContext camelContext)
specifier|protected
name|AbstractCamelClusterService
parameter_list|(
name|String
name|id
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|views
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|lock
operator|=
operator|new
name|StampedLock
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setId (String id)
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
annotation|@
name|Override
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
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
name|LockHelper
operator|.
name|doWithWriteLock
argument_list|(
name|lock
argument_list|,
parameter_list|()
lambda|->
block|{
for|for
control|(
name|ViewHolder
argument_list|<
name|T
argument_list|>
name|holder
range|:
name|views
operator|.
name|values
argument_list|()
control|)
block|{
name|holder
operator|.
name|get
argument_list|()
operator|.
name|setCamelContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getCamelContext ()
specifier|public
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
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|LockHelper
operator|.
name|doWithReadLockT
argument_list|(
name|lock
argument_list|,
parameter_list|()
lambda|->
block|{
for|for
control|(
name|ViewHolder
argument_list|<
name|T
argument_list|>
name|holder
range|:
name|views
operator|.
name|values
argument_list|()
control|)
block|{
name|holder
operator|.
name|get
argument_list|()
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
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
name|LockHelper
operator|.
name|doWithReadLockT
argument_list|(
name|lock
argument_list|,
parameter_list|()
lambda|->
block|{
for|for
control|(
name|ViewHolder
argument_list|<
name|T
argument_list|>
name|holder
range|:
name|views
operator|.
name|values
argument_list|()
control|)
block|{
name|holder
operator|.
name|get
argument_list|()
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getView (String namespace)
specifier|public
name|CamelClusterView
name|getView
parameter_list|(
name|String
name|namespace
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|LockHelper
operator|.
name|callWithWriteLock
argument_list|(
name|lock
argument_list|,
parameter_list|()
lambda|->
block|{
name|ViewHolder
argument_list|<
name|T
argument_list|>
name|holder
init|=
name|views
operator|.
name|get
argument_list|(
name|namespace
argument_list|)
decl_stmt|;
if|if
condition|(
name|holder
operator|==
literal|null
condition|)
block|{
name|T
name|view
init|=
name|createView
argument_list|(
name|namespace
argument_list|)
decl_stmt|;
name|view
operator|.
name|setCamelContext
argument_list|(
name|this
operator|.
name|camelContext
argument_list|)
expr_stmt|;
name|holder
operator|=
operator|new
name|ViewHolder
argument_list|<>
argument_list|(
name|view
argument_list|)
expr_stmt|;
name|views
operator|.
name|put
argument_list|(
name|namespace
argument_list|,
name|holder
argument_list|)
expr_stmt|;
block|}
comment|// Add reference and eventually start the route.
return|return
name|holder
operator|.
name|retain
argument_list|()
return|;
block|}
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|releaseView (CamelClusterView view)
specifier|public
name|void
name|releaseView
parameter_list|(
name|CamelClusterView
name|view
parameter_list|)
throws|throws
name|Exception
block|{
name|LockHelper
operator|.
name|doWithWriteLock
argument_list|(
name|lock
argument_list|,
parameter_list|()
lambda|->
block|{
name|ViewHolder
argument_list|<
name|T
argument_list|>
name|holder
init|=
name|views
operator|.
name|get
argument_list|(
name|view
operator|.
name|getNamespace
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|holder
operator|!=
literal|null
condition|)
block|{
name|holder
operator|.
name|release
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getNamespaces ()
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|getNamespaces
parameter_list|()
block|{
return|return
name|LockHelper
operator|.
name|supplyWithReadLock
argument_list|(
name|lock
argument_list|,
parameter_list|()
lambda|->
block|{
comment|// copy the key set so it is not modifiable and thread safe
comment|// thus a little inefficient.
return|return
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|views
operator|.
name|keySet
argument_list|()
argument_list|)
return|;
block|}
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|startView (String namespace)
specifier|public
name|void
name|startView
parameter_list|(
name|String
name|namespace
parameter_list|)
throws|throws
name|Exception
block|{
name|LockHelper
operator|.
name|doWithWriteLockT
argument_list|(
name|lock
argument_list|,
parameter_list|()
lambda|->
block|{
name|ViewHolder
argument_list|<
name|T
argument_list|>
name|holder
init|=
name|views
operator|.
name|get
argument_list|(
name|namespace
argument_list|)
decl_stmt|;
if|if
condition|(
name|holder
operator|!=
literal|null
condition|)
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Force start of view {}"
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
name|holder
operator|.
name|startView
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Error forcing start of view {}: it does not exist"
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|stopView (String namespace)
specifier|public
name|void
name|stopView
parameter_list|(
name|String
name|namespace
parameter_list|)
throws|throws
name|Exception
block|{
name|LockHelper
operator|.
name|doWithWriteLockT
argument_list|(
name|lock
argument_list|,
parameter_list|()
lambda|->
block|{
name|ViewHolder
argument_list|<
name|T
argument_list|>
name|holder
init|=
name|views
operator|.
name|get
argument_list|(
name|namespace
argument_list|)
decl_stmt|;
if|if
condition|(
name|holder
operator|!=
literal|null
condition|)
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Force stop of view {}"
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
name|holder
operator|.
name|stopView
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Error forcing stop of view {}: it does not exist"
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|// **********************************
comment|// Implementation
comment|// **********************************
DECL|method|createView (String namespace)
specifier|protected
specifier|abstract
name|T
name|createView
parameter_list|(
name|String
name|namespace
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|// **********************************
comment|// Helpers
comment|// **********************************
DECL|class|ViewHolder
specifier|private
specifier|final
class|class
name|ViewHolder
parameter_list|<
name|V
extends|extends
name|CamelClusterView
parameter_list|>
block|{
DECL|field|view
specifier|private
specifier|final
name|V
name|view
decl_stmt|;
DECL|field|count
specifier|private
specifier|final
name|ReferenceCount
name|count
decl_stmt|;
DECL|method|ViewHolder (V view)
name|ViewHolder
parameter_list|(
name|V
name|view
parameter_list|)
block|{
name|this
operator|.
name|view
operator|=
name|view
expr_stmt|;
name|this
operator|.
name|count
operator|=
name|ReferenceCount
operator|.
name|on
argument_list|(
parameter_list|()
lambda|->
block|{
try|try
block|{
name|this
operator|.
name|startView
argument_list|()
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
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
argument_list|,
parameter_list|()
lambda|->
block|{
try|try
block|{
name|this
operator|.
name|stopView
argument_list|()
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
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|get ()
name|V
name|get
parameter_list|()
block|{
return|return
name|view
return|;
block|}
DECL|method|retain ()
name|V
name|retain
parameter_list|()
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Retain view {}, old-refs={}"
argument_list|,
name|view
operator|.
name|getNamespace
argument_list|()
argument_list|,
name|count
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|count
operator|.
name|retain
argument_list|()
expr_stmt|;
return|return
name|get
argument_list|()
return|;
block|}
DECL|method|release ()
name|void
name|release
parameter_list|()
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Release view {}, old-refs={}"
argument_list|,
name|view
operator|.
name|getNamespace
argument_list|()
argument_list|,
name|count
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|count
operator|.
name|release
argument_list|()
expr_stmt|;
block|}
DECL|method|startView ()
name|void
name|startView
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|AbstractCamelClusterService
operator|.
name|this
operator|.
name|isRunAllowed
argument_list|()
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Start view {}"
argument_list|,
name|view
operator|.
name|getNamespace
argument_list|()
argument_list|)
expr_stmt|;
name|view
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Can't start view {} as cluster service is not running, view will be started on service start-up"
argument_list|,
name|view
operator|.
name|getNamespace
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|stopView ()
name|void
name|stopView
parameter_list|()
throws|throws
name|Exception
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Stop view {}"
argument_list|,
name|view
operator|.
name|getNamespace
argument_list|()
argument_list|)
expr_stmt|;
name|view
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

