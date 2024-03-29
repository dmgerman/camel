begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Hashtable
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
name|CamelEvent
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
name|Version
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|event
operator|.
name|Event
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|event
operator|.
name|EventAdmin
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|util
operator|.
name|tracker
operator|.
name|ServiceTracker
import|;
end_import

begin_comment
comment|/**  * This {@link org.apache.camel.spi.EventNotifier} is in charge of propagating events  * to OSGi {@link EventAdmin} if present.  */
end_comment

begin_class
DECL|class|OsgiEventAdminNotifier
specifier|public
class|class
name|OsgiEventAdminNotifier
extends|extends
name|EventNotifierSupport
block|{
DECL|field|TYPE
specifier|public
specifier|static
specifier|final
name|String
name|TYPE
init|=
literal|"type"
decl_stmt|;
DECL|field|EVENT
specifier|public
specifier|static
specifier|final
name|String
name|EVENT
init|=
literal|"event"
decl_stmt|;
DECL|field|TIMESTAMP
specifier|public
specifier|static
specifier|final
name|String
name|TIMESTAMP
init|=
literal|"timestamp"
decl_stmt|;
DECL|field|BUNDLE
specifier|public
specifier|static
specifier|final
name|String
name|BUNDLE
init|=
literal|"bundle"
decl_stmt|;
DECL|field|BUNDLE_ID
specifier|public
specifier|static
specifier|final
name|String
name|BUNDLE_ID
init|=
literal|"bundle.id"
decl_stmt|;
DECL|field|BUNDLE_SYMBOLICNAME
specifier|public
specifier|static
specifier|final
name|String
name|BUNDLE_SYMBOLICNAME
init|=
literal|"bundle.symbolicName"
decl_stmt|;
DECL|field|BUNDLE_VERSION
specifier|public
specifier|static
specifier|final
name|String
name|BUNDLE_VERSION
init|=
literal|"bundle.version"
decl_stmt|;
DECL|field|CAUSE
specifier|public
specifier|static
specifier|final
name|String
name|CAUSE
init|=
literal|"cause"
decl_stmt|;
DECL|field|TOPIC_CAMEL_EVENTS
specifier|public
specifier|static
specifier|final
name|String
name|TOPIC_CAMEL_EVENTS
init|=
literal|"org/apache/camel/"
decl_stmt|;
DECL|field|TOPIC_CAMEL_CONTEXT_EVENTS
specifier|public
specifier|static
specifier|final
name|String
name|TOPIC_CAMEL_CONTEXT_EVENTS
init|=
name|TOPIC_CAMEL_EVENTS
operator|+
literal|"context/"
decl_stmt|;
DECL|field|TOPIC_CAMEL_EXCHANGE_EVENTS
specifier|public
specifier|static
specifier|final
name|String
name|TOPIC_CAMEL_EXCHANGE_EVENTS
init|=
name|TOPIC_CAMEL_EVENTS
operator|+
literal|"exchange/"
decl_stmt|;
DECL|field|TOPIC_CAMEL_SERVICE_EVENTS
specifier|public
specifier|static
specifier|final
name|String
name|TOPIC_CAMEL_SERVICE_EVENTS
init|=
name|TOPIC_CAMEL_EVENTS
operator|+
literal|"service/"
decl_stmt|;
DECL|field|TOPIC_CAMEL_ROUTE_EVENTS
specifier|public
specifier|static
specifier|final
name|String
name|TOPIC_CAMEL_ROUTE_EVENTS
init|=
name|TOPIC_CAMEL_EVENTS
operator|+
literal|"route/"
decl_stmt|;
DECL|field|bundleContext
specifier|private
specifier|final
name|BundleContext
name|bundleContext
decl_stmt|;
DECL|field|tracker
specifier|private
specifier|final
name|ServiceTracker
argument_list|<
name|EventAdmin
argument_list|,
name|EventAdmin
argument_list|>
name|tracker
decl_stmt|;
DECL|method|OsgiEventAdminNotifier (BundleContext bundleContext)
specifier|public
name|OsgiEventAdminNotifier
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
name|this
operator|.
name|tracker
operator|=
operator|new
name|ServiceTracker
argument_list|<>
argument_list|(
name|bundleContext
argument_list|,
name|EventAdmin
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|setIgnoreExchangeEvents
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|notify (CamelEvent event)
specifier|public
name|void
name|notify
parameter_list|(
name|CamelEvent
name|event
parameter_list|)
throws|throws
name|Exception
block|{
name|EventAdmin
name|eventAdmin
init|=
name|tracker
operator|.
name|getService
argument_list|()
decl_stmt|;
if|if
condition|(
name|eventAdmin
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
init|=
operator|new
name|Hashtable
argument_list|<>
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
name|TYPE
argument_list|,
name|getType
argument_list|(
name|event
argument_list|)
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|EVENT
argument_list|,
name|event
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|TIMESTAMP
argument_list|,
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|BUNDLE
argument_list|,
name|bundleContext
operator|.
name|getBundle
argument_list|()
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|BUNDLE_SYMBOLICNAME
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
name|BUNDLE_ID
argument_list|,
name|bundleContext
operator|.
name|getBundle
argument_list|()
operator|.
name|getBundleId
argument_list|()
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|BUNDLE_VERSION
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
try|try
block|{
name|props
operator|.
name|put
argument_list|(
name|CAUSE
argument_list|,
name|event
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"getCause"
argument_list|)
operator|.
name|invoke
argument_list|(
name|event
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// ignore
block|}
name|eventAdmin
operator|.
name|postEvent
argument_list|(
operator|new
name|Event
argument_list|(
name|getTopic
argument_list|(
name|event
argument_list|)
argument_list|,
name|props
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isEnabled (CamelEvent event)
specifier|public
name|boolean
name|isEnabled
parameter_list|(
name|CamelEvent
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
block|{
name|tracker
operator|.
name|open
argument_list|()
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
name|tracker
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
DECL|method|toUpper (String text)
specifier|public
specifier|static
name|String
name|toUpper
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|text
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|char
name|c
init|=
name|text
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|Character
operator|.
name|isUpperCase
argument_list|(
name|c
argument_list|)
operator|&&
name|sb
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|'_'
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|Character
operator|.
name|toUpperCase
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|getType (CamelEvent event)
specifier|public
specifier|static
name|String
name|getType
parameter_list|(
name|CamelEvent
name|event
parameter_list|)
block|{
return|return
name|event
operator|.
name|getType
argument_list|()
operator|.
name|name
argument_list|()
return|;
block|}
DECL|method|getTopic (CamelEvent event)
specifier|public
specifier|static
name|String
name|getTopic
parameter_list|(
name|CamelEvent
name|event
parameter_list|)
block|{
name|String
name|topic
decl_stmt|;
name|String
name|type
init|=
name|getType
argument_list|(
name|event
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|.
name|startsWith
argument_list|(
literal|"CamelContext"
argument_list|)
condition|)
block|{
name|topic
operator|=
name|TOPIC_CAMEL_CONTEXT_EVENTS
expr_stmt|;
name|type
operator|=
name|type
operator|.
name|substring
argument_list|(
literal|"CamelContext"
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|type
operator|.
name|startsWith
argument_list|(
literal|"Exchange"
argument_list|)
condition|)
block|{
name|topic
operator|=
name|TOPIC_CAMEL_EXCHANGE_EVENTS
expr_stmt|;
name|type
operator|=
name|type
operator|.
name|substring
argument_list|(
literal|"Exchange"
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|type
operator|.
name|startsWith
argument_list|(
literal|"Route"
argument_list|)
condition|)
block|{
name|topic
operator|=
name|TOPIC_CAMEL_ROUTE_EVENTS
expr_stmt|;
name|type
operator|=
name|type
operator|.
name|substring
argument_list|(
literal|"Route"
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|type
operator|.
name|startsWith
argument_list|(
literal|"Service"
argument_list|)
condition|)
block|{
name|topic
operator|=
name|TOPIC_CAMEL_SERVICE_EVENTS
expr_stmt|;
name|type
operator|=
name|type
operator|.
name|substring
argument_list|(
literal|"Service"
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|topic
operator|=
name|TOPIC_CAMEL_EVENTS
operator|+
literal|"unknown/"
expr_stmt|;
block|}
name|topic
operator|+=
name|toUpper
argument_list|(
name|type
argument_list|)
expr_stmt|;
return|return
name|topic
return|;
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

