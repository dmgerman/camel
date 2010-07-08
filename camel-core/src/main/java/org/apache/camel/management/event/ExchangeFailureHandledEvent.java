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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Processor
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|ExchangeFailureHandledEvent
specifier|public
class|class
name|ExchangeFailureHandledEvent
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
literal|7554809462006009547L
decl_stmt|;
DECL|field|failureHandler
specifier|private
specifier|final
name|Processor
name|failureHandler
decl_stmt|;
DECL|field|deadLetterChannel
specifier|private
specifier|final
name|boolean
name|deadLetterChannel
decl_stmt|;
DECL|field|handled
specifier|private
specifier|final
name|boolean
name|handled
decl_stmt|;
DECL|method|ExchangeFailureHandledEvent (Exchange source, Processor failureHandler, boolean deadLetterChannel)
specifier|public
name|ExchangeFailureHandledEvent
parameter_list|(
name|Exchange
name|source
parameter_list|,
name|Processor
name|failureHandler
parameter_list|,
name|boolean
name|deadLetterChannel
parameter_list|)
block|{
name|super
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|this
operator|.
name|failureHandler
operator|=
name|failureHandler
expr_stmt|;
name|this
operator|.
name|deadLetterChannel
operator|=
name|deadLetterChannel
expr_stmt|;
name|this
operator|.
name|handled
operator|=
name|source
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|ERRORHANDLER_HANDLED
argument_list|,
literal|false
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|getFailureHandler ()
specifier|public
name|Processor
name|getFailureHandler
parameter_list|()
block|{
return|return
name|failureHandler
return|;
block|}
DECL|method|isDeadLetterChannel ()
specifier|public
name|boolean
name|isDeadLetterChannel
parameter_list|()
block|{
return|return
name|deadLetterChannel
return|;
block|}
DECL|method|isHandled ()
specifier|public
name|boolean
name|isHandled
parameter_list|()
block|{
return|return
name|handled
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
if|if
condition|(
name|isDeadLetterChannel
argument_list|()
condition|)
block|{
return|return
name|getExchange
argument_list|()
operator|.
name|getExchangeId
argument_list|()
operator|+
literal|" exchange failed: "
operator|+
name|getExchange
argument_list|()
operator|+
literal|" but was handled by dead letter channel: "
operator|+
name|failureHandler
return|;
block|}
else|else
block|{
return|return
name|getExchange
argument_list|()
operator|.
name|getExchangeId
argument_list|()
operator|+
literal|" exchange failed: "
operator|+
name|getExchange
argument_list|()
operator|+
literal|" but was processed by: "
operator|+
name|failureHandler
return|;
block|}
block|}
block|}
end_class

end_unit

