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
comment|/**  * An exception thrown if an InOut exchange times out receiving the OUT message  *  * @version   */
end_comment

begin_class
DECL|class|ExchangeTimedOutException
specifier|public
class|class
name|ExchangeTimedOutException
extends|extends
name|CamelExchangeException
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|7899162905421788853L
decl_stmt|;
DECL|field|timeout
specifier|private
specifier|final
name|long
name|timeout
decl_stmt|;
DECL|method|ExchangeTimedOutException (Exchange exchange, long timeout)
specifier|public
name|ExchangeTimedOutException
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|long
name|timeout
parameter_list|)
block|{
name|super
argument_list|(
literal|"The OUT message was not received within: "
operator|+
name|timeout
operator|+
literal|" millis"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
block|}
DECL|method|ExchangeTimedOutException (Exchange exchange, long timeout, String message)
specifier|public
name|ExchangeTimedOutException
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|long
name|timeout
parameter_list|,
name|String
name|message
parameter_list|)
block|{
name|super
argument_list|(
literal|"The OUT message was not received within: "
operator|+
name|timeout
operator|+
literal|" millis due "
operator|+
name|message
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
block|}
comment|/**      * Return the timeout which expired in milliseconds      */
DECL|method|getTimeout ()
specifier|public
name|long
name|getTimeout
parameter_list|()
block|{
return|return
name|timeout
return|;
block|}
block|}
end_class

end_unit

