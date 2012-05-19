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
name|Exchange
import|;
end_import

begin_comment
comment|/**  * Event after an {@link Exchange} has been created.  *<p/>  *<b>Notice:</b> This event may be emitted after an {@link ExchangeSendingEvent}, and  * therefore its not guaranteed this event is the first event being send for a given {@link Exchange}  * lifecycle.  *  * @version   */
end_comment

begin_class
DECL|class|ExchangeCreatedEvent
specifier|public
class|class
name|ExchangeCreatedEvent
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
literal|19248832613958243L
decl_stmt|;
DECL|method|ExchangeCreatedEvent (Exchange source)
specifier|public
name|ExchangeCreatedEvent
parameter_list|(
name|Exchange
name|source
parameter_list|)
block|{
name|super
argument_list|(
name|source
argument_list|)
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
return|return
name|getExchange
argument_list|()
operator|.
name|getExchangeId
argument_list|()
operator|+
literal|" exchange created: "
operator|+
name|getExchange
argument_list|()
return|;
block|}
block|}
end_class

end_unit

