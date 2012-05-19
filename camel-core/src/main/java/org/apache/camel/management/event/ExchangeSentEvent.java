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
comment|/**  * Event for<b>after</b> an {@link Exchange} has been sent to an {@link Endpoint}.  *  * @see ExchangeSendingEvent  * @version  */
end_comment

begin_class
DECL|class|ExchangeSentEvent
specifier|public
class|class
name|ExchangeSentEvent
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
literal|19248832613958123L
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|Endpoint
name|endpoint
decl_stmt|;
DECL|field|timeTaken
specifier|private
specifier|final
name|long
name|timeTaken
decl_stmt|;
DECL|method|ExchangeSentEvent (Exchange source, Endpoint endpoint, long timeTaken)
specifier|public
name|ExchangeSentEvent
parameter_list|(
name|Exchange
name|source
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|,
name|long
name|timeTaken
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
name|this
operator|.
name|timeTaken
operator|=
name|timeTaken
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
DECL|method|getTimeTaken ()
specifier|public
name|long
name|getTimeTaken
parameter_list|()
block|{
return|return
name|timeTaken
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
literal|" sent to: "
operator|+
name|endpoint
operator|+
literal|" took: "
operator|+
name|timeTaken
operator|+
literal|" ms."
return|;
block|}
block|}
end_class

end_unit

