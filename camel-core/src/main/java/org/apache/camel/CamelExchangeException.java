begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

begin_comment
comment|/**  * An exception caused by a specific message {@link Exchange}  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|CamelExchangeException
specifier|public
class|class
name|CamelExchangeException
extends|extends
name|CamelException
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|8721487431101572630L
decl_stmt|;
comment|// exchange is not guaranted to be serializable so we set it as transient
DECL|field|exchange
specifier|private
specifier|final
specifier|transient
name|Exchange
name|exchange
decl_stmt|;
DECL|method|CamelExchangeException (String message, Exchange exchange)
specifier|public
name|CamelExchangeException
parameter_list|(
name|String
name|message
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|super
argument_list|(
name|createMessage
argument_list|(
name|message
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
block|}
DECL|method|CamelExchangeException (String message, Exchange exchange, Throwable cause)
specifier|public
name|CamelExchangeException
parameter_list|(
name|String
name|message
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|createMessage
argument_list|(
name|message
argument_list|,
name|exchange
argument_list|)
argument_list|,
name|cause
argument_list|)
expr_stmt|;
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
block|}
comment|/**      * Returns the exchange which caused the exception      */
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
DECL|method|createMessage (String message, Exchange exchange)
specifier|protected
specifier|static
name|String
name|createMessage
parameter_list|(
name|String
name|message
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|message
operator|+
literal|" on the exchange: "
operator|+
name|exchange
return|;
block|}
block|}
end_class

end_unit

