begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.core.osgi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|core
operator|.
name|osgi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Dictionary
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EventObject
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
name|Properties
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
name|ConcurrentHashMap
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
name|management
operator|.
name|event
operator|.
name|CamelContextStartedEvent
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
name|management
operator|.
name|event
operator|.
name|CamelContextStoppingEvent
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
name|EventNotifierSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Bundle
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Constants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|ServiceRegistration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Version
import|;
end_import

begin_comment
comment|/**  * This {@link org.apache.camel.spi.EventNotifier} is in charge of registering  * {@link CamelContext} in the OSGi registry  */
end_comment

begin_class
DECL|class|OsgiCamelContextPublisher
specifier|public
class|class
name|OsgiCamelContextPublisher
extends|extends
name|EventNotifierSupport
block|{
DECL|field|CONTEXT_SYMBOLIC_NAME_PROPERTY
specifier|public
specifier|static
specifier|final
name|String
name|CONTEXT_SYMBOLIC_NAME_PROPERTY
init|=
literal|"camel.context.symbolicname"
decl_stmt|;
DECL|field|CONTEXT_VERSION_PROPERTY
specifier|public
specifier|static
specifier|final
name|String
name|CONTEXT_VERSION_PROPERTY
init|=
literal|"camel.context.version"
decl_stmt|;
DECL|field|CONTEXT_NAME_PROPERTY
specifier|public
specifier|static
specifier|final
name|String
name|CONTEXT_NAME_PROPERTY
init|=
literal|"camel.context.name"
decl_stmt|;
DECL|field|bundleContext
specifier|private
specifier|final
name|BundleContext
name|bundleContext
decl_stmt|;
DECL|field|registrations
specifier|private
specifier|final
name|Map
argument_list|<
name|CamelContext
argument_list|,
name|ServiceRegistration
argument_list|>
name|registrations
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|CamelContext
argument_list|,
name|ServiceRegistration
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|OsgiCamelContextPublisher (BundleContext bundleContext)
specifier|public
name|OsgiCamelContextPublisher
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|)
block|{
name|this
operator|.
name|bundleContext
operator|=
name|bundleContext
expr_stmt|;
block|}
DECL|method|notify (EventObject event)
specifier|public
name|void
name|notify
parameter_list|(
name|EventObject
name|event
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|event
operator|instanceof
name|CamelContextStartedEvent
condition|)
block|{
name|CamelContext
name|context
init|=
operator|(
operator|(
name|CamelContextStartedEvent
operator|)
name|event
operator|)
operator|.
name|getContext
argument_list|()
decl_stmt|;
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
name|CONTEXT_SYMBOLIC_NAME_PROPERTY
argument_list|,
name|bundleContext
operator|.
name|getBundle
argument_list|()
operator|.
name|getSymbolicName
argument_list|()
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|CONTEXT_VERSION_PROPERTY
argument_list|,
name|getBundleVersion
argument_list|(
name|bundleContext
operator|.
name|getBundle
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|CONTEXT_NAME_PROPERTY
argument_list|,
name|context
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Registering CamelContext [{}] of in OSGi registry"
argument_list|,
name|props
argument_list|)
expr_stmt|;
name|ServiceRegistration
name|reg
init|=
name|bundleContext
operator|.
name|registerService
argument_list|(
name|CamelContext
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|context
argument_list|,
name|props
argument_list|)
decl_stmt|;
name|registrations
operator|.
name|put
argument_list|(
name|context
argument_list|,
name|reg
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|event
operator|instanceof
name|CamelContextStoppingEvent
condition|)
block|{
name|CamelContext
name|context
init|=
operator|(
operator|(
name|CamelContextStoppingEvent
operator|)
name|event
operator|)
operator|.
name|getContext
argument_list|()
decl_stmt|;
name|ServiceRegistration
name|reg
init|=
name|registrations
operator|.
name|get
argument_list|(
name|context
argument_list|)
decl_stmt|;
if|if
condition|(
name|reg
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Unregistering CamelContext [{}] from OSGi registry"
argument_list|,
name|context
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|reg
operator|.
name|unregister
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|isEnabled (EventObject event)
specifier|public
name|boolean
name|isEnabled
parameter_list|(
name|EventObject
name|event
parameter_list|)
block|{
return|return
literal|true
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
name|registrations
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
DECL|method|getBundleVersion (Bundle bundle)
specifier|public
specifier|static
name|Version
name|getBundleVersion
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
block|{
name|Dictionary
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|headers
init|=
name|bundle
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
name|String
name|version
init|=
operator|(
name|String
operator|)
name|headers
operator|.
name|get
argument_list|(
name|Constants
operator|.
name|BUNDLE_VERSION
argument_list|)
decl_stmt|;
return|return
operator|(
name|version
operator|!=
literal|null
operator|)
condition|?
name|Version
operator|.
name|parseVersion
argument_list|(
name|version
argument_list|)
else|:
name|Version
operator|.
name|emptyVersion
return|;
block|}
block|}
end_class

end_unit

