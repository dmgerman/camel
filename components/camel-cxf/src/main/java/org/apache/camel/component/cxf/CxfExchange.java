begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
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
name|cxf
operator|.
name|message
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
name|cxf
operator|.
name|message
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
name|cxf
operator|.
name|transport
operator|.
name|Conduit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|Destination
import|;
end_import

begin_comment
comment|/**  * An {@link Exchange} for working with Apache CXF which expoes the underlying  * CXF messages via {@link #getInMessage()} and {@link #getOutMessage()} along with the  * {@link #getExchange()}   *  * @version $Revision$  */
end_comment

begin_class
DECL|class|CxfExchange
specifier|public
class|class
name|CxfExchange
extends|extends
name|DefaultExchange
block|{
DECL|field|binding
specifier|private
specifier|final
name|CxfBinding
name|binding
decl_stmt|;
DECL|field|exchange
specifier|private
name|Exchange
name|exchange
decl_stmt|;
DECL|method|CxfExchange (CamelContext context, CxfBinding binding)
specifier|public
name|CxfExchange
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|CxfBinding
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
DECL|method|CxfExchange (CamelContext context, CxfBinding binding, Exchange exchange)
specifier|public
name|CxfExchange
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|CxfBinding
name|binding
parameter_list|,
name|Exchange
name|exchange
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
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
name|setIn
argument_list|(
operator|new
name|CxfMessage
argument_list|(
name|exchange
operator|.
name|getInMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|setOut
argument_list|(
operator|new
name|CxfMessage
argument_list|(
name|exchange
operator|.
name|getOutMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|setFault
argument_list|(
operator|new
name|CxfMessage
argument_list|(
name|exchange
operator|.
name|getInFaultMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|CxfExchange (CamelContext context, CxfBinding binding, Message inMessage)
specifier|public
name|CxfExchange
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|CxfBinding
name|binding
parameter_list|,
name|Message
name|inMessage
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
name|this
operator|.
name|exchange
operator|=
name|inMessage
operator|.
name|getExchange
argument_list|()
expr_stmt|;
name|setIn
argument_list|(
operator|new
name|CxfMessage
argument_list|(
name|inMessage
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|exchange
operator|!=
literal|null
condition|)
block|{
name|setOut
argument_list|(
operator|new
name|CxfMessage
argument_list|(
name|exchange
operator|.
name|getOutMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|setFault
argument_list|(
operator|new
name|CxfMessage
argument_list|(
name|exchange
operator|.
name|getInFaultMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|getIn ()
specifier|public
name|CxfMessage
name|getIn
parameter_list|()
block|{
return|return
operator|(
name|CxfMessage
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
name|CxfMessage
name|getOut
parameter_list|()
block|{
return|return
operator|(
name|CxfMessage
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
name|CxfMessage
name|getOut
parameter_list|(
name|boolean
name|lazyCreate
parameter_list|)
block|{
return|return
operator|(
name|CxfMessage
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
name|CxfMessage
name|getFault
parameter_list|()
block|{
return|return
operator|(
name|CxfMessage
operator|)
name|super
operator|.
name|getFault
argument_list|()
return|;
block|}
comment|/**      * @return the Camel<-> JBI binding      */
DECL|method|getBinding ()
specifier|public
name|CxfBinding
name|getBinding
parameter_list|()
block|{
return|return
name|binding
return|;
block|}
comment|// Expose CXF APIs directly on the exchange
comment|//-------------------------------------------------------------------------
comment|/**      * Returns the underlying CXF message exchange for an inbound exchange      * or null for outbound messages      *      * @return the inbound message exchange      */
DECL|method|getExchange ()
specifier|public
name|Exchange
name|getExchange
parameter_list|()
block|{
return|return
name|exchange
return|;
block|}
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
name|getMessage
argument_list|()
return|;
block|}
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
name|getMessage
argument_list|()
return|;
block|}
DECL|method|getOutFaultMessage ()
specifier|public
name|Message
name|getOutFaultMessage
parameter_list|()
block|{
return|return
name|getExchange
argument_list|()
operator|.
name|getOutFaultMessage
argument_list|()
return|;
block|}
DECL|method|getInFaultMessage ()
specifier|public
name|Message
name|getInFaultMessage
parameter_list|()
block|{
return|return
name|getExchange
argument_list|()
operator|.
name|getInFaultMessage
argument_list|()
return|;
block|}
DECL|method|getDestination ()
specifier|public
name|Destination
name|getDestination
parameter_list|()
block|{
return|return
name|getExchange
argument_list|()
operator|.
name|getDestination
argument_list|()
return|;
block|}
DECL|method|getConduit (Message message)
specifier|public
name|Conduit
name|getConduit
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
name|getExchange
argument_list|()
operator|.
name|getConduit
argument_list|(
name|message
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createInMessage ()
specifier|protected
name|CxfMessage
name|createInMessage
parameter_list|()
block|{
return|return
operator|new
name|CxfMessage
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createOutMessage ()
specifier|protected
name|CxfMessage
name|createOutMessage
parameter_list|()
block|{
return|return
operator|new
name|CxfMessage
argument_list|()
return|;
block|}
block|}
end_class

end_unit

