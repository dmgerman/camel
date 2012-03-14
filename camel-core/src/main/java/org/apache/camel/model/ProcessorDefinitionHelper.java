begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|RouteContext
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * Helper class for ProcessorDefinition and the other model classes.  */
end_comment

begin_class
DECL|class|ProcessorDefinitionHelper
specifier|public
specifier|final
class|class
name|ProcessorDefinitionHelper
block|{
DECL|method|ProcessorDefinitionHelper ()
specifier|private
name|ProcessorDefinitionHelper
parameter_list|()
block|{     }
comment|/**      * Looks for the given type in the list of outputs and recurring all the children as well.      *      * @param outputs  list of outputs, can be null or empty.      * @param type     the type to look for      * @return         the found definitions, or<tt>null</tt> if not found      */
DECL|method|filterTypeInOutputs (List<ProcessorDefinition<?>> outputs, Class<T> type)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Iterator
argument_list|<
name|T
argument_list|>
name|filterTypeInOutputs
parameter_list|(
name|List
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|outputs
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|List
argument_list|<
name|T
argument_list|>
name|found
init|=
operator|new
name|ArrayList
argument_list|<
name|T
argument_list|>
argument_list|()
decl_stmt|;
name|doFindType
argument_list|(
name|outputs
argument_list|,
name|type
argument_list|,
name|found
argument_list|)
expr_stmt|;
return|return
name|found
operator|.
name|iterator
argument_list|()
return|;
block|}
comment|/**      * Looks for the given type in the list of outputs and recurring all the children as well.      * Will stop at first found and return it.      *      * @param outputs  list of outputs, can be null or empty.      * @param type     the type to look for      * @return         the first found type, or<tt>null</tt> if not found      */
DECL|method|findFirstTypeInOutputs (List<ProcessorDefinition<?>> outputs, Class<T> type)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|findFirstTypeInOutputs
parameter_list|(
name|List
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|outputs
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|List
argument_list|<
name|T
argument_list|>
name|found
init|=
operator|new
name|ArrayList
argument_list|<
name|T
argument_list|>
argument_list|()
decl_stmt|;
name|doFindType
argument_list|(
name|outputs
argument_list|,
name|type
argument_list|,
name|found
argument_list|)
expr_stmt|;
if|if
condition|(
name|found
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|found
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
comment|/**      * Is the given child the first in the outputs from the parent?      *      * @param parentType the type the parent must be      * @param node the node      * @return<tt>true</tt> if first child,<tt>false</tt> otherwise      */
DECL|method|isFirstChildOfType (Class<?> parentType, ProcessorDefinition<?> node)
specifier|public
specifier|static
name|boolean
name|isFirstChildOfType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|parentType
parameter_list|,
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|node
parameter_list|)
block|{
if|if
condition|(
name|node
operator|==
literal|null
operator|||
name|node
operator|.
name|getParent
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|node
operator|.
name|getParent
argument_list|()
operator|.
name|getOutputs
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
operator|(
name|node
operator|.
name|getParent
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|equals
argument_list|(
name|parentType
argument_list|)
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|node
operator|.
name|getParent
argument_list|()
operator|.
name|getOutputs
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|equals
argument_list|(
name|node
argument_list|)
return|;
block|}
comment|/**      * Is the given node parent(s) of the given type      * @param parentType   the parent type      * @param node         the current node      * @param recursive    whether or not to check grand parent(s) as well      * @return<tt>true</tt> if parent(s) is of given type,<tt>false</tt> otherwise      */
DECL|method|isParentOfType (Class<?> parentType, ProcessorDefinition<?> node, boolean recursive)
specifier|public
specifier|static
name|boolean
name|isParentOfType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|parentType
parameter_list|,
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|node
parameter_list|,
name|boolean
name|recursive
parameter_list|)
block|{
if|if
condition|(
name|node
operator|==
literal|null
operator|||
name|node
operator|.
name|getParent
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|parentType
operator|.
name|isAssignableFrom
argument_list|(
name|node
operator|.
name|getParent
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
name|recursive
condition|)
block|{
comment|// recursive up the tree of parents
return|return
name|isParentOfType
argument_list|(
name|parentType
argument_list|,
name|node
operator|.
name|getParent
argument_list|()
argument_list|,
literal|true
argument_list|)
return|;
block|}
else|else
block|{
comment|// no match
return|return
literal|false
return|;
block|}
block|}
comment|/**      * Gets the route definition the given node belongs to.      *      * @param node the node      * @return the route, or<tt>null</tt> if not possible to find      */
DECL|method|getRoute (ProcessorDefinition<?> node)
specifier|public
specifier|static
name|RouteDefinition
name|getRoute
parameter_list|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|node
parameter_list|)
block|{
if|if
condition|(
name|node
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|def
init|=
name|node
decl_stmt|;
comment|// drill to the top
while|while
condition|(
name|def
operator|!=
literal|null
operator|&&
name|def
operator|.
name|getParent
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|def
operator|=
name|def
operator|.
name|getParent
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|def
operator|instanceof
name|RouteDefinition
condition|)
block|{
return|return
operator|(
name|RouteDefinition
operator|)
name|def
return|;
block|}
else|else
block|{
comment|// not found
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
DECL|method|doFindType (List<ProcessorDefinition<?>> outputs, Class<T> type, List<T> found)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|void
name|doFindType
parameter_list|(
name|List
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|outputs
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|List
argument_list|<
name|T
argument_list|>
name|found
parameter_list|)
block|{
if|if
condition|(
name|outputs
operator|==
literal|null
operator|||
name|outputs
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
for|for
control|(
name|ProcessorDefinition
name|out
range|:
name|outputs
control|)
block|{
if|if
condition|(
name|type
operator|.
name|isInstance
argument_list|(
name|out
argument_list|)
condition|)
block|{
name|found
operator|.
name|add
argument_list|(
operator|(
name|T
operator|)
name|out
argument_list|)
expr_stmt|;
block|}
comment|// send is much common
if|if
condition|(
name|out
operator|instanceof
name|SendDefinition
condition|)
block|{
name|SendDefinition
name|send
init|=
operator|(
name|SendDefinition
operator|)
name|out
decl_stmt|;
name|List
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|children
init|=
name|send
operator|.
name|getOutputs
argument_list|()
decl_stmt|;
name|doFindType
argument_list|(
name|children
argument_list|,
name|type
argument_list|,
name|found
argument_list|)
expr_stmt|;
block|}
comment|// special for choice
if|if
condition|(
name|out
operator|instanceof
name|ChoiceDefinition
condition|)
block|{
name|ChoiceDefinition
name|choice
init|=
operator|(
name|ChoiceDefinition
operator|)
name|out
decl_stmt|;
for|for
control|(
name|WhenDefinition
name|when
range|:
name|choice
operator|.
name|getWhenClauses
argument_list|()
control|)
block|{
name|List
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|children
init|=
name|when
operator|.
name|getOutputs
argument_list|()
decl_stmt|;
name|doFindType
argument_list|(
name|children
argument_list|,
name|type
argument_list|,
name|found
argument_list|)
expr_stmt|;
block|}
comment|// otherwise is optional
if|if
condition|(
name|choice
operator|.
name|getOtherwise
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|children
init|=
name|choice
operator|.
name|getOtherwise
argument_list|()
operator|.
name|getOutputs
argument_list|()
decl_stmt|;
name|doFindType
argument_list|(
name|children
argument_list|,
name|type
argument_list|,
name|found
argument_list|)
expr_stmt|;
block|}
block|}
comment|// try children as well
name|List
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|children
init|=
name|out
operator|.
name|getOutputs
argument_list|()
decl_stmt|;
name|doFindType
argument_list|(
name|children
argument_list|,
name|type
argument_list|,
name|found
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Is there any outputs in the given list.      *<p/>      * Is used for check if the route output has any real outputs (non abstracts)      *      * @param outputs           the outputs      * @param excludeAbstract   whether or not to exclude abstract outputs (e.g. skip onException etc.)      * @return<tt>true</tt> if has outputs, otherwise<tt>false</tt> is returned      */
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
DECL|method|hasOutputs (List<ProcessorDefinition<?>> outputs, boolean excludeAbstract)
specifier|public
specifier|static
name|boolean
name|hasOutputs
parameter_list|(
name|List
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|outputs
parameter_list|,
name|boolean
name|excludeAbstract
parameter_list|)
block|{
if|if
condition|(
name|outputs
operator|==
literal|null
operator|||
name|outputs
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
name|excludeAbstract
condition|)
block|{
return|return
operator|!
name|outputs
operator|.
name|isEmpty
argument_list|()
return|;
block|}
for|for
control|(
name|ProcessorDefinition
name|output
range|:
name|outputs
control|)
block|{
if|if
condition|(
name|output
operator|instanceof
name|TransactedDefinition
operator|||
name|output
operator|instanceof
name|PolicyDefinition
condition|)
block|{
comment|// special for those as they wrap entire output, so we should just check its output
return|return
name|hasOutputs
argument_list|(
name|output
operator|.
name|getOutputs
argument_list|()
argument_list|,
name|excludeAbstract
argument_list|)
return|;
block|}
if|if
condition|(
operator|!
name|output
operator|.
name|isAbstract
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Determines whether a new thread pool will be created or not.      *<p/>      * This is used to know if a new thread pool will be created, and therefore is not shared by others, and therefore      * exclusive to the definition.      *      * @param routeContext   the route context      * @param definition     the node definition which may leverage executor service.      * @param useDefault     whether to fallback and use a default thread pool, if no explicit configured      * @return<tt>true</tt> if a new thread pool will be created,<tt>false</tt> if not      * @see #getConfiguredExecutorService(org.apache.camel.spi.RouteContext, String, ExecutorServiceAwareDefinition, boolean)      */
DECL|method|willCreateNewThreadPool (RouteContext routeContext, ExecutorServiceAwareDefinition<?> definition, boolean useDefault)
specifier|public
specifier|static
name|boolean
name|willCreateNewThreadPool
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|ExecutorServiceAwareDefinition
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|,
name|boolean
name|useDefault
parameter_list|)
block|{
name|ExecutorServiceManager
name|manager
init|=
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|manager
argument_list|,
literal|"ExecutorServiceManager"
argument_list|,
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|definition
operator|.
name|getExecutorService
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// no there is a custom thread pool configured
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
name|definition
operator|.
name|getExecutorServiceRef
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ExecutorService
name|answer
init|=
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
name|definition
operator|.
name|getExecutorServiceRef
argument_list|()
argument_list|,
name|ExecutorService
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// if no existing thread pool, then we will have to create a new thread pool
return|return
name|answer
operator|==
literal|null
return|;
block|}
elseif|else
if|if
condition|(
name|useDefault
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Will lookup in {@link org.apache.camel.spi.Registry} for a {@link ExecutorService} registered with the given      *<tt>executorServiceRef</tt> name.      *<p/>      * This method will lookup for configured thread pool in the following order      *<ul>      *<li>from the {@link org.apache.camel.spi.Registry} if found</li>      *<li>from the known list of {@link org.apache.camel.spi.ThreadPoolProfile ThreadPoolProfile(s)}.</li>      *<li>if none found, then<tt>null</tt> is returned.</li>      *</ul>      * @param routeContext   the route context      * @param name           name which is appended to the thread name, when the {@link java.util.concurrent.ExecutorService}      *                       is created based on a {@link org.apache.camel.spi.ThreadPoolProfile}.      * @param source         the source to use the thread pool      * @param executorServiceRef reference name of the thread pool      * @return the executor service, or<tt>null</tt> if none was found.      */
DECL|method|lookupExecutorServiceRef (RouteContext routeContext, String name, Object source, String executorServiceRef)
specifier|public
specifier|static
name|ExecutorService
name|lookupExecutorServiceRef
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|String
name|name
parameter_list|,
name|Object
name|source
parameter_list|,
name|String
name|executorServiceRef
parameter_list|)
block|{
name|ExecutorServiceManager
name|manager
init|=
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|manager
argument_list|,
literal|"ExecutorServiceManager"
argument_list|,
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|executorServiceRef
argument_list|,
literal|"executorServiceRef"
argument_list|)
expr_stmt|;
comment|// lookup in registry first and use existing thread pool if exists
name|ExecutorService
name|answer
init|=
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
name|executorServiceRef
argument_list|,
name|ExecutorService
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
comment|// then create a thread pool assuming the ref is a thread pool profile id
name|answer
operator|=
name|manager
operator|.
name|newThreadPool
argument_list|(
name|source
argument_list|,
name|name
argument_list|,
name|executorServiceRef
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Will lookup and get the configured {@link java.util.concurrent.ExecutorService} from the given definition.      *<p/>      * This method will lookup for configured thread pool in the following order      *<ul>      *<li>from the definition if any explicit configured executor service.</li>      *<li>from the {@link org.apache.camel.spi.Registry} if found</li>      *<li>from the known list of {@link org.apache.camel.spi.ThreadPoolProfile ThreadPoolProfile(s)}.</li>      *<li>if none found, then<tt>null</tt> is returned.</li>      *</ul>      * The various {@link ExecutorServiceAwareDefinition} should use this helper method to ensure they support      * configured executor services in the same coherent way.      *      * @param routeContext   the route context      * @param name           name which is appended to the thread name, when the {@link java.util.concurrent.ExecutorService}      *                       is created based on a {@link org.apache.camel.spi.ThreadPoolProfile}.      * @param definition     the node definition which may leverage executor service.      * @param useDefault     whether to fallback and use a default thread pool, if no explicit configured      * @return the configured executor service, or<tt>null</tt> if none was configured.      * @throws IllegalArgumentException is thrown if lookup of executor service in {@link org.apache.camel.spi.Registry} was not found      */
DECL|method|getConfiguredExecutorService (RouteContext routeContext, String name, ExecutorServiceAwareDefinition<?> definition, boolean useDefault)
specifier|public
specifier|static
name|ExecutorService
name|getConfiguredExecutorService
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|String
name|name
parameter_list|,
name|ExecutorServiceAwareDefinition
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|,
name|boolean
name|useDefault
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
name|ExecutorServiceManager
name|manager
init|=
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|manager
argument_list|,
literal|"ExecutorServiceManager"
argument_list|,
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
comment|// prefer to use explicit configured executor on the definition
if|if
condition|(
name|definition
operator|.
name|getExecutorService
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|definition
operator|.
name|getExecutorService
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|definition
operator|.
name|getExecutorServiceRef
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// lookup in registry first and use existing thread pool if exists
name|ExecutorService
name|answer
init|=
name|lookupExecutorServiceRef
argument_list|(
name|routeContext
argument_list|,
name|name
argument_list|,
name|definition
argument_list|,
name|definition
operator|.
name|getExecutorServiceRef
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"ExecutorServiceRef "
operator|+
name|definition
operator|.
name|getExecutorServiceRef
argument_list|()
operator|+
literal|" not found in registry or as a thread pool profile."
argument_list|)
throw|;
block|}
return|return
name|answer
return|;
block|}
elseif|else
if|if
condition|(
name|useDefault
condition|)
block|{
return|return
name|manager
operator|.
name|newDefaultThreadPool
argument_list|(
name|definition
argument_list|,
name|name
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Will lookup in {@link org.apache.camel.spi.Registry} for a {@link ScheduledExecutorService} registered with the given      *<tt>executorServiceRef</tt> name.      *<p/>      * This method will lookup for configured thread pool in the following order      *<ul>      *<li>from the {@link org.apache.camel.spi.Registry} if found</li>      *<li>from the known list of {@link org.apache.camel.spi.ThreadPoolProfile ThreadPoolProfile(s)}.</li>      *<li>if none found, then<tt>null</tt> is returned.</li>      *</ul>      * @param routeContext   the route context      * @param name           name which is appended to the thread name, when the {@link java.util.concurrent.ExecutorService}      *                       is created based on a {@link org.apache.camel.spi.ThreadPoolProfile}.      * @param source         the source to use the thread pool      * @param executorServiceRef reference name of the thread pool      * @return the executor service, or<tt>null</tt> if none was found.      */
DECL|method|lookupScheduledExecutorServiceRef (RouteContext routeContext, String name, Object source, String executorServiceRef)
specifier|public
specifier|static
name|ScheduledExecutorService
name|lookupScheduledExecutorServiceRef
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|String
name|name
parameter_list|,
name|Object
name|source
parameter_list|,
name|String
name|executorServiceRef
parameter_list|)
block|{
name|ExecutorServiceManager
name|manager
init|=
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|manager
argument_list|,
literal|"ExecutorServiceManager"
argument_list|,
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|executorServiceRef
argument_list|,
literal|"executorServiceRef"
argument_list|)
expr_stmt|;
comment|// lookup in registry first and use existing thread pool if exists
name|ScheduledExecutorService
name|answer
init|=
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
name|executorServiceRef
argument_list|,
name|ScheduledExecutorService
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
comment|// then create a thread pool assuming the ref is a thread pool profile id
name|answer
operator|=
name|manager
operator|.
name|newScheduledThreadPool
argument_list|(
name|source
argument_list|,
name|name
argument_list|,
name|executorServiceRef
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Will lookup and get the configured {@link java.util.concurrent.ScheduledExecutorService} from the given definition.      *<p/>      * This method will lookup for configured thread pool in the following order      *<ul>      *<li>from the definition if any explicit configured executor service.</li>      *<li>from the {@link org.apache.camel.spi.Registry} if found</li>      *<li>from the known list of {@link org.apache.camel.spi.ThreadPoolProfile ThreadPoolProfile(s)}.</li>      *<li>if none found, then<tt>null</tt> is returned.</li>      *</ul>      * The various {@link ExecutorServiceAwareDefinition} should use this helper method to ensure they support      * configured executor services in the same coherent way.      *      * @param routeContext   the rout context      * @param name           name which is appended to the thread name, when the {@link java.util.concurrent.ExecutorService}      *                       is created based on a {@link org.apache.camel.spi.ThreadPoolProfile}.      * @param definition     the node definition which may leverage executor service.      * @param useDefault     whether to fallback and use a default thread pool, if no explicit configured      * @return the configured executor service, or<tt>null</tt> if none was configured.      * @throws IllegalArgumentException is thrown if the found instance is not a ScheduledExecutorService type,      * or lookup of executor service in {@link org.apache.camel.spi.Registry} was not found      */
DECL|method|getConfiguredScheduledExecutorService (RouteContext routeContext, String name, ExecutorServiceAwareDefinition<?> definition, boolean useDefault)
specifier|public
specifier|static
name|ScheduledExecutorService
name|getConfiguredScheduledExecutorService
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|String
name|name
parameter_list|,
name|ExecutorServiceAwareDefinition
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|,
name|boolean
name|useDefault
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
name|ExecutorServiceManager
name|manager
init|=
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|manager
argument_list|,
literal|"ExecutorServiceManager"
argument_list|,
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
comment|// prefer to use explicit configured executor on the definition
if|if
condition|(
name|definition
operator|.
name|getExecutorService
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ExecutorService
name|executorService
init|=
name|definition
operator|.
name|getExecutorService
argument_list|()
decl_stmt|;
if|if
condition|(
name|executorService
operator|instanceof
name|ScheduledExecutorService
condition|)
block|{
return|return
operator|(
name|ScheduledExecutorService
operator|)
name|executorService
return|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"ExecutorServiceRef "
operator|+
name|definition
operator|.
name|getExecutorServiceRef
argument_list|()
operator|+
literal|" is not an ScheduledExecutorService instance"
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|definition
operator|.
name|getExecutorServiceRef
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ScheduledExecutorService
name|answer
init|=
name|lookupScheduledExecutorServiceRef
argument_list|(
name|routeContext
argument_list|,
name|name
argument_list|,
name|definition
argument_list|,
name|definition
operator|.
name|getExecutorServiceRef
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"ExecutorServiceRef "
operator|+
name|definition
operator|.
name|getExecutorServiceRef
argument_list|()
operator|+
literal|" not found in registry or as a thread pool profile."
argument_list|)
throw|;
block|}
return|return
name|answer
return|;
block|}
elseif|else
if|if
condition|(
name|useDefault
condition|)
block|{
return|return
name|manager
operator|.
name|newDefaultScheduledThreadPool
argument_list|(
name|definition
argument_list|,
name|name
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

