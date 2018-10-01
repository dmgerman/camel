begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mina2
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mina2
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
name|DefaultExchangeHolder
import|;
end_import

begin_comment
comment|/**  * Helper to get and set the correct payload when transferring data using camel-mina2.  * Always use this helper instead of direct access on the exchange object.  *<p/>  * This helper ensures that we can also transfer exchange objects over the wire using the  *<tt>transferExchange=true</tt> option.  */
end_comment

begin_class
DECL|class|Mina2PayloadHelper
specifier|public
specifier|final
class|class
name|Mina2PayloadHelper
block|{
DECL|method|Mina2PayloadHelper ()
specifier|private
name|Mina2PayloadHelper
parameter_list|()
block|{
comment|//Utility Class
block|}
DECL|method|getIn (Mina2Endpoint endpoint, Exchange exchange)
specifier|public
specifier|static
name|Object
name|getIn
parameter_list|(
name|Mina2Endpoint
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
name|getConfiguration
argument_list|()
operator|.
name|isTransferExchange
argument_list|()
condition|)
block|{
comment|// we should transfer the entire exchange over the wire (includes in/out)
return|return
name|DefaultExchangeHolder
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
DECL|method|getOut (Mina2Endpoint endpoint, Exchange exchange)
specifier|public
specifier|static
name|Object
name|getOut
parameter_list|(
name|Mina2Endpoint
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
name|getConfiguration
argument_list|()
operator|.
name|isTransferExchange
argument_list|()
condition|)
block|{
comment|// we should transfer the entire exchange over the wire (includes in/out)
return|return
name|DefaultExchangeHolder
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
name|DefaultExchangeHolder
condition|)
block|{
name|DefaultExchangeHolder
operator|.
name|unmarshal
argument_list|(
name|exchange
argument_list|,
operator|(
name|DefaultExchangeHolder
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
name|DefaultExchangeHolder
condition|)
block|{
name|DefaultExchangeHolder
operator|.
name|unmarshal
argument_list|(
name|exchange
argument_list|,
operator|(
name|DefaultExchangeHolder
operator|)
name|payload
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// normal transfer using the body only and preserve the headers
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeaders
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
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

