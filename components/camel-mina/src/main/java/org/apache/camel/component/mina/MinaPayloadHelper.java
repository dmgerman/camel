begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mina
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mina
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
name|Exchange
import|;
end_import

begin_comment
comment|/**  * Helper to get and set the correct payload when transfering data using camel-mina.  * Always use this helper instead of direct access on the exchange object.  *<p/>  * This helper ensures that we can also transfer exchange objects over the wire using the  *<tt>exchangePayload=true</tt> option.  *  * @see org.apache.camel.component.mina.MinaPayloadHolder  * @version $Revision$  */
end_comment

begin_class
DECL|class|MinaPayloadHelper
specifier|public
specifier|final
class|class
name|MinaPayloadHelper
block|{
DECL|method|MinaPayloadHelper ()
specifier|private
name|MinaPayloadHelper
parameter_list|()
block|{
comment|//Utility Class
block|}
DECL|method|getIn (MinaEndpoint endpoint, Exchange exchange)
specifier|public
specifier|static
name|Object
name|getIn
parameter_list|(
name|MinaEndpoint
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|endpoint
operator|.
name|isTransferExchange
argument_list|()
condition|)
block|{
comment|// we should transfer the entire exchange over the wire (includes in/out)
return|return
name|MinaPayloadHolder
operator|.
name|marshal
argument_list|(
name|exchange
argument_list|)
return|;
block|}
else|else
block|{
comment|// normal transfer using the body only
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
return|;
block|}
block|}
DECL|method|getOut (MinaEndpoint endpoint, Exchange exchange)
specifier|public
specifier|static
name|Object
name|getOut
parameter_list|(
name|MinaEndpoint
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|endpoint
operator|.
name|isTransferExchange
argument_list|()
condition|)
block|{
comment|// we should transfer the entire exchange over the wire (includes in/out)
return|return
name|MinaPayloadHolder
operator|.
name|marshal
argument_list|(
name|exchange
argument_list|)
return|;
block|}
else|else
block|{
comment|// normal transfer using the body only
return|return
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
return|;
block|}
block|}
DECL|method|setIn (Exchange exchange, Object payload)
specifier|public
specifier|static
name|void
name|setIn
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|payload
parameter_list|)
block|{
if|if
condition|(
name|payload
operator|instanceof
name|MinaPayloadHolder
condition|)
block|{
name|MinaPayloadHolder
operator|.
name|unmarshal
argument_list|(
name|exchange
argument_list|,
operator|(
name|MinaPayloadHolder
operator|)
name|payload
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// normal transfer using the body only
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|payload
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|setOut (Exchange exchange, Object payload)
specifier|public
specifier|static
name|void
name|setOut
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|payload
parameter_list|)
block|{
if|if
condition|(
name|payload
operator|instanceof
name|MinaPayloadHolder
condition|)
block|{
name|MinaPayloadHolder
operator|.
name|unmarshal
argument_list|(
name|exchange
argument_list|,
operator|(
name|MinaPayloadHolder
operator|)
name|payload
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// normal transfer using the body only
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|payload
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

