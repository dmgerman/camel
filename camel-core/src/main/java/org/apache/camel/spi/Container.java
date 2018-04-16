begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
package|;
end_package

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
comment|/**  * The<code>Container</code> interface defines an object that can be used  * to customize all Camel CONTEXTS created.  *<p/>  * A container can be used to globally intercept and customize {@link org.apache.camel.CamelContext}s,  * by registering a<code>LifecycleStrategy</code>, a<code>ProcessorFactory</code>,  * or any other SPI object.  *<p/>  * This implementation is<b>not</b> thread-safe. The {@link #manage(org.apache.camel.CamelContext)} method  * may be invoked concurrently if multiple Camel applications is being started concurrently, such as from  * application servers that may start deployments concurrently.  *  * @deprecated use {@link CamelContextTracker} and {@link org.apache.camel.impl.CamelContextTrackerRegistry}  */
end_comment

begin_comment
comment|// [TODO] Remove in 3.0
end_comment

begin_interface
annotation|@
name|Deprecated
DECL|interface|Container
specifier|public
interface|interface
name|Container
block|{
comment|/**      * The<code>Instance</code> class holds a<code>Container</code> singleton.      */
DECL|class|Instance
specifier|final
class|class
name|Instance
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
name|Container
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|container
specifier|private
specifier|static
name|Container
name|container
decl_stmt|;
DECL|field|CONTEXTS
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|CamelContext
argument_list|>
name|CONTEXTS
init|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|Instance ()
specifier|private
name|Instance
parameter_list|()
block|{         }
comment|/**          * Access the registered Container.          *          * @return the Container singleton          */
DECL|method|get ()
specifier|public
specifier|static
name|Container
name|get
parameter_list|()
block|{
return|return
name|container
return|;
block|}
comment|/**          * Register the Container.          *          * @param container the Container to register          */
DECL|method|set (Container container)
specifier|public
specifier|static
name|void
name|set
parameter_list|(
name|Container
name|container
parameter_list|)
block|{
name|Instance
operator|.
name|container
operator|=
name|container
expr_stmt|;
if|if
condition|(
name|container
operator|==
literal|null
condition|)
block|{
name|CONTEXTS
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|CONTEXTS
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// manage any pending CamelContext which was started before a Container was set
for|for
control|(
name|CamelContext
name|context
range|:
name|CONTEXTS
control|)
block|{
name|manageCamelContext
argument_list|(
name|container
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
name|CONTEXTS
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**          * Called by Camel when a<code>CamelContext</code> is being started.          *          * @param camelContext the CamelContext to manage          */
DECL|method|manage (CamelContext camelContext)
specifier|public
specifier|static
name|void
name|manage
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|Container
name|cnt
init|=
name|container
decl_stmt|;
if|if
condition|(
name|cnt
operator|!=
literal|null
condition|)
block|{
name|manageCamelContext
argument_list|(
name|cnt
argument_list|,
name|camelContext
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// Container not yet set so need to remember this CamelContext
name|CONTEXTS
operator|.
name|add
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|manageCamelContext (Container container, CamelContext context)
specifier|private
specifier|static
name|void
name|manageCamelContext
parameter_list|(
name|Container
name|container
parameter_list|,
name|CamelContext
name|context
parameter_list|)
block|{
try|try
block|{
name|container
operator|.
name|manage
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error during manage CamelContext "
operator|+
name|context
operator|.
name|getName
argument_list|()
operator|+
literal|". This exception is ignored."
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**          * Called by Camel when a<code>CamelContext</code> is being stopped.          *          * @param camelContext the CamelContext which is being stopped          */
DECL|method|unmanage (CamelContext camelContext)
specifier|public
specifier|static
name|void
name|unmanage
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|CONTEXTS
operator|.
name|remove
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Called by Camel before a<code>CamelContext</code> has been started.      *<p/>      * Notice this method is invoked when the {@link org.apache.camel.CamelContext} has been started.      * The context is<b>not</b> yet finished being configured. For example the id/name of the {@link org.apache.camel.CamelContext}      * has not been resolved yet, and may return<tt>null</tt>.      *<p/>      * The intention is implementations of {@link org.apache.camel.spi.Container} is able to configure the {@link org.apache.camel.CamelContext}      * before it has been fully started.      *<p/>      * To receive callbacks when the {@link org.apache.camel.CamelContext} is fully configured and has been started, then      * use {@link org.apache.camel.spi.EventNotifier} to listen for the {@link org.apache.camel.management.event.CamelContextStartedEvent}      * event.      *      * @param camelContext the CamelContext to manage      */
DECL|method|manage (CamelContext camelContext)
name|void
name|manage
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

