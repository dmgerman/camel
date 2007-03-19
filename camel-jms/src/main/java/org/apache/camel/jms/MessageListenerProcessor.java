begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.jms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|jms
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

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|MessageListener
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Message
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|MessageListenerProcessor
specifier|public
class|class
name|MessageListenerProcessor
implements|implements
name|MessageListener
block|{
DECL|field|processor
specifier|private
name|Processor
argument_list|<
name|Exchange
argument_list|>
name|processor
decl_stmt|;
DECL|method|MessageListenerProcessor (Processor<Exchange> processor)
specifier|public
name|MessageListenerProcessor
parameter_list|(
name|Processor
argument_list|<
name|Exchange
argument_list|>
name|processor
parameter_list|)
block|{
name|this
operator|.
name|processor
operator|=
name|processor
expr_stmt|;
block|}
DECL|method|onMessage (Message message)
specifier|public
name|void
name|onMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|createMessageExchange
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|processor
operator|.
name|onExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|createMessageExchange (Message message)
specifier|protected
name|DefaultJmsExchange
name|createMessageExchange
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
operator|new
name|DefaultJmsExchange
argument_list|(
name|message
argument_list|)
return|;
block|}
block|}
end_class

end_unit

