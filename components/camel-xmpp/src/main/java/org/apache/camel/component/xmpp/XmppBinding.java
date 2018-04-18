begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xmpp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|xmpp
package|;
end_package

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
name|Map
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
name|impl
operator|.
name|DefaultHeaderFilterStrategy
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
name|HeaderFilterStrategy
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
name|jivesoftware
operator|.
name|smack
operator|.
name|packet
operator|.
name|DefaultExtensionElement
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jivesoftware
operator|.
name|smack
operator|.
name|packet
operator|.
name|ExtensionElement
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jivesoftware
operator|.
name|smack
operator|.
name|packet
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jivesoftware
operator|.
name|smack
operator|.
name|packet
operator|.
name|Stanza
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jivesoftware
operator|.
name|smackx
operator|.
name|jiveproperties
operator|.
name|JivePropertiesManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jivesoftware
operator|.
name|smackx
operator|.
name|jiveproperties
operator|.
name|packet
operator|.
name|JivePropertiesExtension
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jivesoftware
operator|.
name|smackx
operator|.
name|pubsub
operator|.
name|packet
operator|.
name|PubSub
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
comment|/**  * A Strategy used to convert between a Camel {@link Exchange} and {@link XmppMessage} to and from a  * XMPP {@link Message}  */
end_comment

begin_class
DECL|class|XmppBinding
specifier|public
class|class
name|XmppBinding
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
name|XmppBinding
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|headerFilterStrategy
specifier|private
name|HeaderFilterStrategy
name|headerFilterStrategy
decl_stmt|;
DECL|method|XmppBinding ()
specifier|public
name|XmppBinding
parameter_list|()
block|{
name|this
operator|.
name|headerFilterStrategy
operator|=
operator|new
name|DefaultHeaderFilterStrategy
argument_list|()
expr_stmt|;
block|}
DECL|method|XmppBinding (HeaderFilterStrategy headerFilterStrategy)
specifier|public
name|XmppBinding
parameter_list|(
name|HeaderFilterStrategy
name|headerFilterStrategy
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|headerFilterStrategy
argument_list|,
literal|"headerFilterStrategy"
argument_list|)
expr_stmt|;
name|this
operator|.
name|headerFilterStrategy
operator|=
name|headerFilterStrategy
expr_stmt|;
block|}
comment|/**      * Populates the given XMPP message from the inbound exchange      */
DECL|method|populateXmppMessage (Message message, Exchange exchange)
specifier|public
name|void
name|populateXmppMessage
parameter_list|(
name|Message
name|message
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|message
operator|.
name|setBody
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|entries
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|entrySet
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|entries
control|)
block|{
name|String
name|name
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Object
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|headerFilterStrategy
operator|.
name|applyFilterToCamelHeaders
argument_list|(
name|name
argument_list|,
name|value
argument_list|,
name|exchange
argument_list|)
condition|)
block|{
if|if
condition|(
literal|"subject"
operator|.
name|equalsIgnoreCase
argument_list|(
name|name
argument_list|)
condition|)
block|{
comment|// special for subject
name|String
name|subject
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|message
operator|.
name|setSubject
argument_list|(
name|subject
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"language"
operator|.
name|equalsIgnoreCase
argument_list|(
name|name
argument_list|)
condition|)
block|{
comment|// special for language
name|String
name|language
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|message
operator|.
name|setLanguage
argument_list|(
name|language
argument_list|)
expr_stmt|;
block|}
else|else
block|{
try|try
block|{
name|JivePropertiesManager
operator|.
name|addProperty
argument_list|(
name|message
argument_list|,
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Added property name: {} value: {}"
argument_list|,
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|iae
parameter_list|)
block|{
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
literal|"Cannot add property "
operator|+
name|name
operator|+
literal|" to XMPP message due: "
argument_list|,
name|iae
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
name|String
name|id
init|=
name|exchange
operator|.
name|getExchangeId
argument_list|()
decl_stmt|;
if|if
condition|(
name|id
operator|!=
literal|null
condition|)
block|{
name|JivePropertiesManager
operator|.
name|addProperty
argument_list|(
name|message
argument_list|,
literal|"exchangeId"
argument_list|,
name|id
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Populates the given XMPP stanza from the inbound exchange      */
DECL|method|populateXmppStanza (Stanza stanza, Exchange exchange)
specifier|public
name|void
name|populateXmppStanza
parameter_list|(
name|Stanza
name|stanza
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|Set
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|entries
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|entrySet
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|entries
control|)
block|{
name|String
name|name
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Object
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|headerFilterStrategy
operator|.
name|applyFilterToCamelHeaders
argument_list|(
name|name
argument_list|,
name|value
argument_list|,
name|exchange
argument_list|)
condition|)
block|{
try|try
block|{
name|JivePropertiesManager
operator|.
name|addProperty
argument_list|(
name|stanza
argument_list|,
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Added property name: "
operator|+
name|name
operator|+
literal|" value: "
operator|+
name|value
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|iae
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Not adding property "
operator|+
name|name
operator|+
literal|" to XMPP message due to "
operator|+
name|iae
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|String
name|id
init|=
name|exchange
operator|.
name|getExchangeId
argument_list|()
decl_stmt|;
if|if
condition|(
name|id
operator|!=
literal|null
condition|)
block|{
name|JivePropertiesManager
operator|.
name|addProperty
argument_list|(
name|stanza
argument_list|,
literal|"exchangeId"
argument_list|,
name|id
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Extracts the body from the XMPP message      */
DECL|method|extractBodyFromXmpp (Exchange exchange, Stanza stanza)
specifier|public
name|Object
name|extractBodyFromXmpp
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Stanza
name|stanza
parameter_list|)
block|{
return|return
operator|(
name|stanza
operator|instanceof
name|Message
operator|)
condition|?
name|getMessageBody
argument_list|(
operator|(
name|Message
operator|)
name|stanza
argument_list|)
else|:
name|stanza
return|;
block|}
DECL|method|getMessageBody (Message message)
specifier|private
name|Object
name|getMessageBody
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|String
name|messageBody
init|=
name|message
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|messageBody
operator|==
literal|null
condition|)
block|{
comment|//probably a pubsub message
return|return
name|message
return|;
block|}
return|return
name|messageBody
return|;
block|}
DECL|method|extractHeadersFromXmpp (Stanza stanza, Exchange exchange)
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|extractHeadersFromXmpp
parameter_list|(
name|Stanza
name|stanza
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|answer
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|ExtensionElement
name|jpe
init|=
name|stanza
operator|.
name|getExtension
argument_list|(
name|JivePropertiesExtension
operator|.
name|NAMESPACE
argument_list|)
decl_stmt|;
if|if
condition|(
name|jpe
operator|instanceof
name|JivePropertiesExtension
condition|)
block|{
name|extractHeadersFrom
argument_list|(
operator|(
name|JivePropertiesExtension
operator|)
name|jpe
argument_list|,
name|exchange
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|jpe
operator|instanceof
name|DefaultExtensionElement
condition|)
block|{
name|extractHeadersFrom
argument_list|(
operator|(
name|DefaultExtensionElement
operator|)
name|jpe
argument_list|,
name|exchange
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|stanza
operator|instanceof
name|Message
condition|)
block|{
name|Message
name|xmppMessage
init|=
operator|(
name|Message
operator|)
name|stanza
decl_stmt|;
name|answer
operator|.
name|put
argument_list|(
name|XmppConstants
operator|.
name|MESSAGE_TYPE
argument_list|,
name|xmppMessage
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|put
argument_list|(
name|XmppConstants
operator|.
name|SUBJECT
argument_list|,
name|xmppMessage
operator|.
name|getSubject
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|put
argument_list|(
name|XmppConstants
operator|.
name|THREAD_ID
argument_list|,
name|xmppMessage
operator|.
name|getThread
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|stanza
operator|instanceof
name|PubSub
condition|)
block|{
name|PubSub
name|pubsubPacket
init|=
operator|(
name|PubSub
operator|)
name|stanza
decl_stmt|;
name|answer
operator|.
name|put
argument_list|(
name|XmppConstants
operator|.
name|MESSAGE_TYPE
argument_list|,
name|pubsubPacket
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|answer
operator|.
name|put
argument_list|(
name|XmppConstants
operator|.
name|FROM
argument_list|,
name|stanza
operator|.
name|getFrom
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|put
argument_list|(
name|XmppConstants
operator|.
name|PACKET_ID
argument_list|,
name|stanza
operator|.
name|getStanzaId
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|put
argument_list|(
name|XmppConstants
operator|.
name|STANZA_ID
argument_list|,
name|stanza
operator|.
name|getStanzaId
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|put
argument_list|(
name|XmppConstants
operator|.
name|TO
argument_list|,
name|stanza
operator|.
name|getTo
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|extractHeadersFrom (JivePropertiesExtension jpe, Exchange exchange, Map<String, Object> answer)
specifier|private
name|void
name|extractHeadersFrom
parameter_list|(
name|JivePropertiesExtension
name|jpe
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|answer
parameter_list|)
block|{
for|for
control|(
name|String
name|name
range|:
name|jpe
operator|.
name|getPropertyNames
argument_list|()
control|)
block|{
name|Object
name|value
init|=
name|jpe
operator|.
name|getProperty
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|headerFilterStrategy
operator|.
name|applyFilterToExternalHeaders
argument_list|(
name|name
argument_list|,
name|value
argument_list|,
name|exchange
argument_list|)
condition|)
block|{
name|answer
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|extractHeadersFrom (DefaultExtensionElement jpe, Exchange exchange, Map<String, Object> answer)
specifier|private
name|void
name|extractHeadersFrom
parameter_list|(
name|DefaultExtensionElement
name|jpe
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|answer
parameter_list|)
block|{
for|for
control|(
name|String
name|name
range|:
name|jpe
operator|.
name|getNames
argument_list|()
control|)
block|{
name|Object
name|value
init|=
name|jpe
operator|.
name|getValue
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|headerFilterStrategy
operator|.
name|applyFilterToExternalHeaders
argument_list|(
name|name
argument_list|,
name|value
argument_list|,
name|exchange
argument_list|)
condition|)
block|{
name|answer
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

