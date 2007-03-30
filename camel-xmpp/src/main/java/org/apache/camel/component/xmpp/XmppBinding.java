begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * A Strategy used to convert between a Camel {@XmppExchange} and {@XmppMessage} to and from a  * XMPP {@link Message}  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|XmppBinding
specifier|public
class|class
name|XmppBinding
block|{
comment|/**      * Populates the given XMPP message from the inbound exchange      */
DECL|method|populateXmppMessage (Message message, XmppExchange exchange)
specifier|public
name|void
name|populateXmppMessage
parameter_list|(
name|Message
name|message
parameter_list|,
name|XmppExchange
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
name|shouldOutputHeader
argument_list|(
name|exchange
argument_list|,
name|name
argument_list|,
name|value
argument_list|)
condition|)
block|{
name|message
operator|.
name|setProperty
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
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
name|message
operator|.
name|setProperty
argument_list|(
literal|"exchangeId"
argument_list|,
name|id
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Extracts the body from the XMPP message      *      * @param exchange      * @param message      */
DECL|method|extractBodyFromXmpp (XmppExchange exchange, Message message)
specifier|public
name|Object
name|extractBodyFromXmpp
parameter_list|(
name|XmppExchange
name|exchange
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
return|return
name|message
operator|.
name|getBody
argument_list|()
return|;
block|}
comment|/**      * Strategy to allow filtering of headers which are put on the XMPP message      */
DECL|method|shouldOutputHeader (XmppExchange exchange, String headerName, Object headerValue)
specifier|protected
name|boolean
name|shouldOutputHeader
parameter_list|(
name|XmppExchange
name|exchange
parameter_list|,
name|String
name|headerName
parameter_list|,
name|Object
name|headerValue
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

