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
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
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
name|spi
operator|.
name|CamelContextRegistry
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
comment|/**  * The default {@link CamelContextRegistry}  */
end_comment

begin_class
DECL|class|DefaultCamelContextRegistry
specifier|public
specifier|final
class|class
name|DefaultCamelContextRegistry
implements|implements
name|CamelContextRegistry
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
name|DefaultCamelContextRegistry
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|contexts
specifier|private
specifier|final
name|Set
argument_list|<
name|CamelContext
argument_list|>
name|contexts
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|CamelContext
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|listeners
specifier|private
specifier|final
name|Set
argument_list|<
name|Listener
argument_list|>
name|listeners
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|Listener
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * Clear all contexts and listeners, such as for testing purpose.      */
DECL|method|clear ()
specifier|public
specifier|synchronized
name|void
name|clear
parameter_list|()
block|{
name|contexts
operator|.
name|clear
argument_list|()
expr_stmt|;
name|listeners
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
DECL|method|afterCreate (CamelContext camelContext)
specifier|synchronized
name|void
name|afterCreate
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|registerContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
DECL|method|beforeStart (CamelContext camelContext)
specifier|synchronized
name|void
name|beforeStart
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
if|if
condition|(
operator|!
name|contexts
operator|.
name|contains
argument_list|(
name|camelContext
argument_list|)
condition|)
block|{
name|registerContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|afterStop (CamelContext camelContext)
specifier|synchronized
name|void
name|afterStop
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|unregisterContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
DECL|method|registerContext (CamelContext camelContext)
specifier|private
name|void
name|registerContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|contexts
operator|.
name|add
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
for|for
control|(
name|Listener
name|listener
range|:
name|listeners
control|)
block|{
try|try
block|{
name|listener
operator|.
name|contextAdded
argument_list|(
name|camelContext
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
literal|"Error calling registry listener. This exception is ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|unregisterContext (CamelContext camelContext)
specifier|private
name|void
name|unregisterContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|contexts
operator|.
name|remove
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
for|for
control|(
name|Listener
name|listener
range|:
name|listeners
control|)
block|{
try|try
block|{
name|listener
operator|.
name|contextRemoved
argument_list|(
name|camelContext
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
literal|"Error calling registry listener. This exception is ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|addListener (Listener listener, boolean withCallback)
specifier|public
specifier|synchronized
name|void
name|addListener
parameter_list|(
name|Listener
name|listener
parameter_list|,
name|boolean
name|withCallback
parameter_list|)
block|{
if|if
condition|(
name|withCallback
condition|)
block|{
for|for
control|(
name|CamelContext
name|ctx
range|:
name|contexts
control|)
block|{
name|listener
operator|.
name|contextAdded
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
block|}
block|}
name|listeners
operator|.
name|add
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|removeListener (Listener listener, boolean withCallback)
specifier|public
specifier|synchronized
name|void
name|removeListener
parameter_list|(
name|Listener
name|listener
parameter_list|,
name|boolean
name|withCallback
parameter_list|)
block|{
name|listeners
operator|.
name|add
argument_list|(
name|listener
argument_list|)
expr_stmt|;
if|if
condition|(
name|withCallback
condition|)
block|{
for|for
control|(
name|CamelContext
name|ctx
range|:
name|contexts
control|)
block|{
name|listener
operator|.
name|contextAdded
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|getContexts ()
specifier|public
specifier|synchronized
name|Set
argument_list|<
name|CamelContext
argument_list|>
name|getContexts
parameter_list|()
block|{
return|return
operator|new
name|LinkedHashSet
argument_list|<
name|CamelContext
argument_list|>
argument_list|(
name|contexts
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getContexts (String name)
specifier|public
specifier|synchronized
name|Set
argument_list|<
name|CamelContext
argument_list|>
name|getContexts
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Set
argument_list|<
name|CamelContext
argument_list|>
name|result
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|CamelContext
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|CamelContext
name|ctx
range|:
name|contexts
control|)
block|{
if|if
condition|(
name|ctx
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
name|ctx
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
DECL|method|getRequiredContext (String name)
specifier|public
specifier|synchronized
name|CamelContext
name|getRequiredContext
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Iterator
argument_list|<
name|CamelContext
argument_list|>
name|it
init|=
name|getContexts
argument_list|(
name|name
argument_list|)
operator|.
name|iterator
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Cannot find CamelContext with name: "
operator|+
name|name
argument_list|)
throw|;
block|}
return|return
name|it
operator|.
name|next
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getContext (String name)
specifier|public
specifier|synchronized
name|CamelContext
name|getContext
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Iterator
argument_list|<
name|CamelContext
argument_list|>
name|it
init|=
name|getContexts
argument_list|(
name|name
argument_list|)
operator|.
name|iterator
argument_list|()
decl_stmt|;
return|return
name|it
operator|.
name|hasNext
argument_list|()
condition|?
name|it
operator|.
name|next
argument_list|()
else|:
literal|null
return|;
block|}
block|}
end_class

end_unit

