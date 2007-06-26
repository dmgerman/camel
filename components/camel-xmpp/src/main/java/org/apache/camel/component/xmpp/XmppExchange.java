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
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|DefaultExchange
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
name|Exchange
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
comment|/**  * Represents an {@ilnk Exchange} for working with XMPP  *  * @version $Revision:520964 $  */
end_comment

begin_class
DECL|class|XmppExchange
specifier|public
class|class
name|XmppExchange
extends|extends
name|DefaultExchange
block|{
DECL|field|binding
specifier|private
name|XmppBinding
name|binding
decl_stmt|;
DECL|method|XmppExchange (CamelContext context, XmppBinding binding)
specifier|public
name|XmppExchange
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|XmppBinding
name|binding
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|this
operator|.
name|binding
operator|=
name|binding
expr_stmt|;
block|}
DECL|method|XmppExchange (CamelContext context, XmppBinding binding, Message message)
specifier|public
name|XmppExchange
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|XmppBinding
name|binding
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
name|this
argument_list|(
name|context
argument_list|,
name|binding
argument_list|)
expr_stmt|;
name|setIn
argument_list|(
operator|new
name|XmppMessage
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getIn ()
specifier|public
name|XmppMessage
name|getIn
parameter_list|()
block|{
return|return
operator|(
name|XmppMessage
operator|)
name|super
operator|.
name|getIn
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getOut ()
specifier|public
name|XmppMessage
name|getOut
parameter_list|()
block|{
return|return
operator|(
name|XmppMessage
operator|)
name|super
operator|.
name|getOut
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getOut (boolean lazyCreate)
specifier|public
name|XmppMessage
name|getOut
parameter_list|(
name|boolean
name|lazyCreate
parameter_list|)
block|{
return|return
operator|(
name|XmppMessage
operator|)
name|super
operator|.
name|getOut
argument_list|(
name|lazyCreate
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getFault ()
specifier|public
name|XmppMessage
name|getFault
parameter_list|()
block|{
return|return
operator|(
name|XmppMessage
operator|)
name|super
operator|.
name|getFault
argument_list|()
return|;
block|}
DECL|method|getBinding ()
specifier|public
name|XmppBinding
name|getBinding
parameter_list|()
block|{
return|return
name|binding
return|;
block|}
annotation|@
name|Override
DECL|method|newInstance ()
specifier|public
name|Exchange
name|newInstance
parameter_list|()
block|{
return|return
operator|new
name|XmppExchange
argument_list|(
name|getContext
argument_list|()
argument_list|,
name|binding
argument_list|)
return|;
block|}
comment|// Expose the underlying XMPP APIs
comment|//-------------------------------------------------------------------------
comment|/**      * Return the underlying XMPP In message      *      * @return the XMPP In message      */
DECL|method|getInMessage ()
specifier|public
name|Message
name|getInMessage
parameter_list|()
block|{
return|return
name|getIn
argument_list|()
operator|.
name|getXmppMessage
argument_list|()
return|;
block|}
comment|/**      * Return the underlying XMPP Out message      *      * @return the XMPP out message      */
DECL|method|getOutMessage ()
specifier|public
name|Message
name|getOutMessage
parameter_list|()
block|{
return|return
name|getOut
argument_list|()
operator|.
name|getXmppMessage
argument_list|()
return|;
block|}
comment|/**      * Return the underlying XMPP Fault message      *      * @return the XMPP fault message      */
DECL|method|getFaultMessage ()
specifier|public
name|Message
name|getFaultMessage
parameter_list|()
block|{
return|return
name|getOut
argument_list|()
operator|.
name|getXmppMessage
argument_list|()
return|;
block|}
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
annotation|@
name|Override
DECL|method|createInMessage ()
specifier|protected
name|XmppMessage
name|createInMessage
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
DECL|method|createOutMessage ()
specifier|protected
name|XmppMessage
name|createOutMessage
parameter_list|()
block|{
return|return
operator|new
name|XmppMessage
argument_list|()
return|;
block|}
block|}
end_class

end_unit

