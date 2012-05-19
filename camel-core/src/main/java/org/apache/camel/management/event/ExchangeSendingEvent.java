begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management.event
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
operator|.
name|event
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
name|Endpoint
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

begin_comment
comment|/**  * Event for<b>before</b> sending an {@link Exchange} to an {@link Endpoint}.  *<p/>  * This event is emitted before attempting to send the {@link Exchange} to an {@link Endpoint}.  * There is still some internal processing occurring before the actual sending takes places, and  * therefore its not a 100% guarantee that the sending actually happens, as there may cause an  * internal error before.  *<p/>  * The {@link ExchangeSentEvent} is an event which is emitted<b>after</b> the sending is done.  *  * @see ExchangeSentEvent  * @version   */
end_comment

begin_class
DECL|class|ExchangeSendingEvent
specifier|public
class|class
name|ExchangeSendingEvent
extends|extends
name|AbstractExchangeEvent
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|19248832613958122L
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|Endpoint
name|endpoint
decl_stmt|;
DECL|method|ExchangeSendingEvent (Exchange source, Endpoint endpoint)
specifier|public
name|ExchangeSendingEvent
parameter_list|(
name|Exchange
name|source
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
DECL|method|getEndpoint ()
specifier|public
name|Endpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|endpoint
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|getExchange
argument_list|()
operator|.
name|getExchangeId
argument_list|()
operator|+
literal|" exchange "
operator|+
name|getExchange
argument_list|()
operator|+
literal|" is being sent to: "
operator|+
name|endpoint
return|;
block|}
block|}
end_class

end_unit

