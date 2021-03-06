begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.eventadmin
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|eventadmin
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
name|Map
operator|.
name|Entry
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
name|CamelExchangeException
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
name|Exchange
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
name|Message
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
name|CamelContextHelper
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
name|DefaultProducer
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
comment|/**  * EventAdmin producer  */
end_comment

begin_class
DECL|class|EventAdminProducer
specifier|public
class|class
name|EventAdminProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|endpoint
specifier|private
specifier|final
name|EventAdminEndpoint
name|endpoint
decl_stmt|;
DECL|field|tracker
specifier|private
name|ServiceTracker
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|tracker
decl_stmt|;
DECL|method|EventAdminProducer (EventAdminEndpoint endpoint)
specifier|public
name|EventAdminProducer
parameter_list|(
name|EventAdminEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|tracker
operator|=
operator|new
name|ServiceTracker
argument_list|<>
argument_list|(
name|endpoint
operator|.
name|getComponent
argument_list|()
operator|.
name|getBundleContext
argument_list|()
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
name|this
operator|.
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
name|this
operator|.
name|tracker
operator|.
name|close
argument_list|()
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|EventAdmin
name|admin
init|=
operator|(
name|EventAdmin
operator|)
name|this
operator|.
name|tracker
operator|.
name|getService
argument_list|()
decl_stmt|;
if|if
condition|(
name|admin
operator|!=
literal|null
condition|)
block|{
name|Event
name|event
init|=
name|getEvent
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|isSend
argument_list|()
condition|)
block|{
name|admin
operator|.
name|sendEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|admin
operator|.
name|postEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"EventAdmin service not present"
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
block|}
DECL|method|getTopic (Exchange exchange)
specifier|protected
name|String
name|getTopic
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|String
name|topic
init|=
name|in
operator|.
name|getHeader
argument_list|(
name|EventAdminConstants
operator|.
name|EVENTADMIN_TOPIC
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|topic
operator|!=
literal|null
condition|)
block|{
name|in
operator|.
name|removeHeader
argument_list|(
name|EventAdminConstants
operator|.
name|EVENTADMIN_TOPIC
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|topic
operator|==
literal|null
condition|)
block|{
name|topic
operator|=
name|endpoint
operator|.
name|getTopic
argument_list|()
expr_stmt|;
block|}
return|return
name|topic
return|;
block|}
DECL|method|getEvent (Exchange exchange)
specifier|protected
name|Event
name|getEvent
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|CamelContext
name|context
init|=
name|endpoint
operator|.
name|getCamelContext
argument_list|()
decl_stmt|;
name|Event
name|event
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Event
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|in
operator|.
name|getBody
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|event
operator|==
literal|null
condition|)
block|{
name|String
name|topic
init|=
name|getTopic
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|Dictionary
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|props
init|=
name|getProperties
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|event
operator|=
operator|new
name|Event
argument_list|(
name|topic
argument_list|,
name|props
argument_list|)
expr_stmt|;
block|}
return|return
name|event
return|;
block|}
DECL|method|getProperties (Exchange exchange)
specifier|protected
name|Dictionary
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|getProperties
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|CamelContext
name|context
init|=
name|endpoint
operator|.
name|getCamelContext
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|map
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Map
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|in
operator|.
name|getBody
argument_list|()
argument_list|)
decl_stmt|;
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|dict
init|=
operator|new
name|Hashtable
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|keyString
init|=
name|CamelContextHelper
operator|.
name|convertTo
argument_list|(
name|context
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|keyString
operator|!=
literal|null
condition|)
block|{
name|Object
name|val
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
comment|// TODO: convert to acceptable value
name|dict
operator|.
name|put
argument_list|(
name|keyString
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|dict
return|;
block|}
block|}
end_class

end_unit

