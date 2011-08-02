begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jms
package|;
end_package

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
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|JMSException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Queue
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
name|management
operator|.
name|ManagedAttribute
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
name|ManagedOperation
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
name|ManagedResource
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
name|BrowsableEndpoint
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
name|MessageHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jms
operator|.
name|core
operator|.
name|JmsOperations
import|;
end_import

begin_comment
comment|/**  * An endpoint for a JMS Queue which is also browsable  *  * @version   */
end_comment

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed JMS Queue Endpoint"
argument_list|)
DECL|class|JmsQueueEndpoint
specifier|public
class|class
name|JmsQueueEndpoint
extends|extends
name|JmsEndpoint
implements|implements
name|BrowsableEndpoint
block|{
DECL|field|maximumBrowseSize
specifier|private
name|int
name|maximumBrowseSize
init|=
operator|-
literal|1
decl_stmt|;
DECL|field|queueBrowseStrategy
specifier|private
specifier|final
name|QueueBrowseStrategy
name|queueBrowseStrategy
decl_stmt|;
DECL|method|JmsQueueEndpoint (Queue destination)
specifier|public
name|JmsQueueEndpoint
parameter_list|(
name|Queue
name|destination
parameter_list|)
throws|throws
name|JMSException
block|{
name|this
argument_list|(
literal|"jms:queue:"
operator|+
name|destination
operator|.
name|getQueueName
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|setDestination
argument_list|(
name|destination
argument_list|)
expr_stmt|;
block|}
DECL|method|JmsQueueEndpoint (String uri, JmsComponent component, String destination, JmsConfiguration configuration)
specifier|public
name|JmsQueueEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|JmsComponent
name|component
parameter_list|,
name|String
name|destination
parameter_list|,
name|JmsConfiguration
name|configuration
parameter_list|)
block|{
name|this
argument_list|(
name|uri
argument_list|,
name|component
argument_list|,
name|destination
argument_list|,
name|configuration
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|JmsQueueEndpoint (String uri, JmsComponent component, String destination, JmsConfiguration configuration, QueueBrowseStrategy queueBrowseStrategy)
specifier|public
name|JmsQueueEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|JmsComponent
name|component
parameter_list|,
name|String
name|destination
parameter_list|,
name|JmsConfiguration
name|configuration
parameter_list|,
name|QueueBrowseStrategy
name|queueBrowseStrategy
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|,
name|destination
argument_list|,
literal|false
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
if|if
condition|(
name|queueBrowseStrategy
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|queueBrowseStrategy
operator|=
name|createQueueBrowseStrategy
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|queueBrowseStrategy
operator|=
name|queueBrowseStrategy
expr_stmt|;
block|}
block|}
DECL|method|JmsQueueEndpoint (String endpointUri, String destination, QueueBrowseStrategy queueBrowseStrategy)
specifier|public
name|JmsQueueEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|String
name|destination
parameter_list|,
name|QueueBrowseStrategy
name|queueBrowseStrategy
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|destination
argument_list|,
literal|false
argument_list|)
expr_stmt|;
if|if
condition|(
name|queueBrowseStrategy
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|queueBrowseStrategy
operator|=
name|createQueueBrowseStrategy
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|queueBrowseStrategy
operator|=
name|queueBrowseStrategy
expr_stmt|;
block|}
block|}
DECL|method|JmsQueueEndpoint (String endpointUri, String destination)
specifier|public
name|JmsQueueEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|String
name|destination
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|destination
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|queueBrowseStrategy
operator|=
name|createQueueBrowseStrategy
argument_list|()
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|getMaximumBrowseSize ()
specifier|public
name|int
name|getMaximumBrowseSize
parameter_list|()
block|{
return|return
name|maximumBrowseSize
return|;
block|}
comment|/**      * If a number is set> 0 then this limits the number of messages that are      * returned when browsing the queue      */
annotation|@
name|ManagedAttribute
DECL|method|setMaximumBrowseSize (int maximumBrowseSize)
specifier|public
name|void
name|setMaximumBrowseSize
parameter_list|(
name|int
name|maximumBrowseSize
parameter_list|)
block|{
name|this
operator|.
name|maximumBrowseSize
operator|=
name|maximumBrowseSize
expr_stmt|;
block|}
DECL|method|getExchanges ()
specifier|public
name|List
argument_list|<
name|Exchange
argument_list|>
name|getExchanges
parameter_list|()
block|{
if|if
condition|(
name|queueBrowseStrategy
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
name|String
name|queue
init|=
name|getDestinationName
argument_list|()
decl_stmt|;
name|JmsOperations
name|template
init|=
name|getConfiguration
argument_list|()
operator|.
name|createInOnlyTemplate
argument_list|(
name|this
argument_list|,
literal|false
argument_list|,
name|queue
argument_list|)
decl_stmt|;
return|return
name|queueBrowseStrategy
operator|.
name|browse
argument_list|(
name|template
argument_list|,
name|queue
argument_list|,
name|this
argument_list|)
return|;
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Current number of Exchanges in Queue"
argument_list|)
DECL|method|queueSize ()
specifier|public
name|long
name|queueSize
parameter_list|()
block|{
return|return
name|getExchanges
argument_list|()
operator|.
name|size
argument_list|()
return|;
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Get Exchange from queue by index"
argument_list|)
DECL|method|browseExchange (Integer index)
specifier|public
name|String
name|browseExchange
parameter_list|(
name|Integer
name|index
parameter_list|)
block|{
name|List
argument_list|<
name|Exchange
argument_list|>
name|exchanges
init|=
name|getExchanges
argument_list|()
decl_stmt|;
if|if
condition|(
name|index
operator|>=
name|exchanges
operator|.
name|size
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Exchange
name|exchange
init|=
name|exchanges
operator|.
name|get
argument_list|(
name|index
argument_list|)
decl_stmt|;
if|if
condition|(
name|exchange
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// must use java type with JMX such as java.lang.String
return|return
name|exchange
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Get message body from queue by index"
argument_list|)
DECL|method|browseMessageBody (Integer index)
specifier|public
name|String
name|browseMessageBody
parameter_list|(
name|Integer
name|index
parameter_list|)
block|{
name|List
argument_list|<
name|Exchange
argument_list|>
name|exchanges
init|=
name|getExchanges
argument_list|()
decl_stmt|;
if|if
condition|(
name|index
operator|>=
name|exchanges
operator|.
name|size
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Exchange
name|exchange
init|=
name|exchanges
operator|.
name|get
argument_list|(
name|index
argument_list|)
decl_stmt|;
if|if
condition|(
name|exchange
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Object
name|body
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
name|body
operator|=
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|body
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
expr_stmt|;
block|}
comment|// must use java type with JMX such as java.lang.String
return|return
name|body
operator|!=
literal|null
condition|?
name|body
operator|.
name|toString
argument_list|()
else|:
literal|null
return|;
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Get message as XML from queue by index"
argument_list|)
DECL|method|browseMessageAsXml (Integer index)
specifier|public
name|String
name|browseMessageAsXml
parameter_list|(
name|Integer
name|index
parameter_list|)
block|{
name|List
argument_list|<
name|Exchange
argument_list|>
name|exchanges
init|=
name|getExchanges
argument_list|()
decl_stmt|;
if|if
condition|(
name|index
operator|>=
name|exchanges
operator|.
name|size
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Exchange
name|exchange
init|=
name|exchanges
operator|.
name|get
argument_list|(
name|index
argument_list|)
decl_stmt|;
if|if
condition|(
name|exchange
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Message
name|msg
init|=
name|exchange
operator|.
name|hasOut
argument_list|()
condition|?
name|exchange
operator|.
name|getOut
argument_list|()
else|:
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|String
name|xml
init|=
name|MessageHelper
operator|.
name|dumpAsXml
argument_list|(
name|msg
argument_list|)
decl_stmt|;
return|return
name|xml
return|;
block|}
DECL|method|createQueueBrowseStrategy ()
specifier|protected
name|QueueBrowseStrategy
name|createQueueBrowseStrategy
parameter_list|()
block|{
return|return
operator|new
name|DefaultQueueBrowseStrategy
argument_list|()
return|;
block|}
block|}
end_class

end_unit

