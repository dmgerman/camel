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
name|Map
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
name|DefaultMessage
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

begin_comment
comment|/**  * Represents a {@link org.apache.camel.Message} for working with XMPP  *  * @version $Revision:520964 $  */
end_comment

begin_class
DECL|class|XmppMessage
specifier|public
class|class
name|XmppMessage
extends|extends
name|DefaultMessage
block|{
DECL|field|xmppMessage
specifier|private
name|Message
name|xmppMessage
decl_stmt|;
DECL|method|XmppMessage ()
specifier|public
name|XmppMessage
parameter_list|()
block|{
name|this
argument_list|(
operator|new
name|Message
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|XmppMessage (Message jmsMessage)
specifier|public
name|XmppMessage
parameter_list|(
name|Message
name|jmsMessage
parameter_list|)
block|{
name|this
operator|.
name|xmppMessage
operator|=
name|jmsMessage
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
if|if
condition|(
name|xmppMessage
operator|!=
literal|null
condition|)
block|{
return|return
literal|"XmppMessage: "
operator|+
name|xmppMessage
return|;
block|}
else|else
block|{
return|return
literal|"XmppMessage: "
operator|+
name|getBody
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|getExchange ()
specifier|public
name|XmppExchange
name|getExchange
parameter_list|()
block|{
return|return
operator|(
name|XmppExchange
operator|)
name|super
operator|.
name|getExchange
argument_list|()
return|;
block|}
comment|/**      * Returns the underlying XMPP message      */
DECL|method|getXmppMessage ()
specifier|public
name|Message
name|getXmppMessage
parameter_list|()
block|{
return|return
name|xmppMessage
return|;
block|}
DECL|method|setXmppMessage (Message xmppMessage)
specifier|public
name|void
name|setXmppMessage
parameter_list|(
name|Message
name|xmppMessage
parameter_list|)
block|{
name|this
operator|.
name|xmppMessage
operator|=
name|xmppMessage
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|newInstance ()
specifier|public
name|XmppMessage
name|newInstance
parameter_list|()
block|{
return|return
operator|new
name|XmppMessage
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createBody ()
specifier|protected
name|Object
name|createBody
parameter_list|()
block|{
if|if
condition|(
name|xmppMessage
operator|!=
literal|null
condition|)
block|{
return|return
name|getExchange
argument_list|()
operator|.
name|getBinding
argument_list|()
operator|.
name|extractBodyFromXmpp
argument_list|(
name|getExchange
argument_list|()
argument_list|,
name|xmppMessage
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|populateInitialHeaders (Map<String, Object> map)
specifier|protected
name|void
name|populateInitialHeaders
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
parameter_list|)
block|{
if|if
condition|(
name|xmppMessage
operator|!=
literal|null
condition|)
block|{
name|map
operator|.
name|putAll
argument_list|(
name|getExchange
argument_list|()
operator|.
name|getBinding
argument_list|()
operator|.
name|extractHeadersFromXmpp
argument_list|(
name|xmppMessage
argument_list|,
name|getExchange
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

