begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jbi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jbi
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
name|servicemix
operator|.
name|client
operator|.
name|Destination
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jbi
operator|.
name|messaging
operator|.
name|MessageExchange
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jbi
operator|.
name|messaging
operator|.
name|MessagingException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jbi
operator|.
name|messaging
operator|.
name|NormalizedMessage
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jbi
operator|.
name|messaging
operator|.
name|MessageExchangeFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamSource
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
import|;
end_import

begin_comment
comment|/**  * The binding of how Camel messages get mapped to JBI and back again  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|JbiBinding
specifier|public
class|class
name|JbiBinding
block|{
comment|/**      * Extracts the body from the given normalized message      */
DECL|method|extractBodyFromJbi (JbiExchange exchange, NormalizedMessage normalizedMessage)
specifier|public
name|Object
name|extractBodyFromJbi
parameter_list|(
name|JbiExchange
name|exchange
parameter_list|,
name|NormalizedMessage
name|normalizedMessage
parameter_list|)
block|{
comment|// TODO we may wish to turn this into a POJO such as a JAXB/DOM
return|return
name|normalizedMessage
operator|.
name|getContent
argument_list|()
return|;
block|}
comment|/**      * Creates a JBI {@link MessageExchange} from the given Camel {@link Exchange}      *      */
DECL|method|makeJbiMessageExchange (Exchange camelExchange, Destination jbiDestination)
specifier|public
name|MessageExchange
name|makeJbiMessageExchange
parameter_list|(
name|Exchange
name|camelExchange
parameter_list|,
name|Destination
name|jbiDestination
parameter_list|)
throws|throws
name|MessagingException
block|{
name|MessageExchange
name|jbiExchange
init|=
name|createJbiMessageExchange
argument_list|(
name|camelExchange
argument_list|,
name|jbiDestination
argument_list|)
decl_stmt|;
name|NormalizedMessage
name|normalizedMessage
init|=
name|jbiExchange
operator|.
name|getMessage
argument_list|(
literal|"in"
argument_list|)
decl_stmt|;
name|normalizedMessage
operator|.
name|setContent
argument_list|(
name|getJbiInContent
argument_list|(
name|camelExchange
argument_list|)
argument_list|)
expr_stmt|;
name|addJbiHeaders
argument_list|(
name|jbiExchange
argument_list|,
name|normalizedMessage
argument_list|,
name|camelExchange
argument_list|)
expr_stmt|;
return|return
name|jbiExchange
return|;
block|}
DECL|method|makeJbiMessageExchange (Exchange camelExchange, MessageExchangeFactory exchangeFactory)
specifier|public
name|MessageExchange
name|makeJbiMessageExchange
parameter_list|(
name|Exchange
name|camelExchange
parameter_list|,
name|MessageExchangeFactory
name|exchangeFactory
parameter_list|)
throws|throws
name|MessagingException
block|{
name|MessageExchange
name|jbiExchange
init|=
name|createJbiMessageExchange
argument_list|(
name|camelExchange
argument_list|,
name|exchangeFactory
argument_list|)
decl_stmt|;
name|NormalizedMessage
name|normalizedMessage
init|=
name|jbiExchange
operator|.
name|getMessage
argument_list|(
literal|"in"
argument_list|)
decl_stmt|;
if|if
condition|(
name|normalizedMessage
operator|==
literal|null
condition|)
block|{
name|normalizedMessage
operator|=
name|jbiExchange
operator|.
name|createMessage
argument_list|()
expr_stmt|;
name|jbiExchange
operator|.
name|setMessage
argument_list|(
name|normalizedMessage
argument_list|,
literal|"in"
argument_list|)
expr_stmt|;
block|}
name|normalizedMessage
operator|.
name|setContent
argument_list|(
name|getJbiInContent
argument_list|(
name|camelExchange
argument_list|)
argument_list|)
expr_stmt|;
name|addJbiHeaders
argument_list|(
name|jbiExchange
argument_list|,
name|normalizedMessage
argument_list|,
name|camelExchange
argument_list|)
expr_stmt|;
return|return
name|jbiExchange
return|;
block|}
DECL|method|createJbiMessageExchange (Exchange camelExchange, MessageExchangeFactory exchangeFactory)
specifier|protected
name|MessageExchange
name|createJbiMessageExchange
parameter_list|(
name|Exchange
name|camelExchange
parameter_list|,
name|MessageExchangeFactory
name|exchangeFactory
parameter_list|)
throws|throws
name|MessagingException
block|{
comment|// TODO we should deal with other forms of MEM
return|return
name|exchangeFactory
operator|.
name|createInOnlyExchange
argument_list|()
return|;
block|}
DECL|method|createJbiMessageExchange (Exchange camelExchange, Destination jbiDestination)
specifier|protected
name|MessageExchange
name|createJbiMessageExchange
parameter_list|(
name|Exchange
name|camelExchange
parameter_list|,
name|Destination
name|jbiDestination
parameter_list|)
throws|throws
name|MessagingException
block|{
comment|// TODO we should deal with other forms of MEM
return|return
name|jbiDestination
operator|.
name|createInOnlyExchange
argument_list|()
return|;
block|}
DECL|method|getJbiInContent (Exchange camelExchange)
specifier|protected
name|Source
name|getJbiInContent
parameter_list|(
name|Exchange
name|camelExchange
parameter_list|)
block|{
comment|// TODO this should be more smart
name|Object
name|value
init|=
name|camelExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|String
condition|)
block|{
return|return
operator|new
name|StreamSource
argument_list|(
operator|new
name|StringReader
argument_list|(
name|value
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
return|return
name|camelExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Source
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|addJbiHeaders (MessageExchange jbiExchange, NormalizedMessage normalizedMessage, Exchange camelExchange)
specifier|protected
name|void
name|addJbiHeaders
parameter_list|(
name|MessageExchange
name|jbiExchange
parameter_list|,
name|NormalizedMessage
name|normalizedMessage
parameter_list|,
name|Exchange
name|camelExchange
parameter_list|)
block|{
name|Set
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|entries
init|=
name|camelExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|entrySet
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|entries
control|)
block|{
name|normalizedMessage
operator|.
name|setProperty
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

