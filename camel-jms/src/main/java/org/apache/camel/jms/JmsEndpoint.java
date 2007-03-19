begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ExchangeConverter
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
name|CamelContainer
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
name|DefaultEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jms
operator|.
name|core
operator|.
name|JmsOperations
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jms
operator|.
name|core
operator|.
name|MessageCreator
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Destination
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|JMSException
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

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Session
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|JmsEndpoint
specifier|public
class|class
name|JmsEndpoint
extends|extends
name|DefaultEndpoint
argument_list|<
name|JmsExchange
argument_list|>
block|{
DECL|field|template
specifier|private
name|JmsOperations
name|template
decl_stmt|;
DECL|field|destination
specifier|private
name|Destination
name|destination
decl_stmt|;
DECL|method|JmsEndpoint (String uri, CamelContainer container, Destination destination, JmsOperations template)
specifier|public
name|JmsEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|CamelContainer
name|container
parameter_list|,
name|Destination
name|destination
parameter_list|,
name|JmsOperations
name|template
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|container
argument_list|)
expr_stmt|;
name|this
operator|.
name|destination
operator|=
name|destination
expr_stmt|;
name|this
operator|.
name|template
operator|=
name|template
expr_stmt|;
block|}
DECL|method|send (final JmsExchange exchange)
specifier|public
name|void
name|send
parameter_list|(
specifier|final
name|JmsExchange
name|exchange
parameter_list|)
block|{
name|template
operator|.
name|send
argument_list|(
name|getDestination
argument_list|()
argument_list|,
operator|new
name|MessageCreator
argument_list|()
block|{
specifier|public
name|Message
name|createMessage
parameter_list|(
name|Session
name|session
parameter_list|)
throws|throws
name|JMSException
block|{
return|return
name|exchange
operator|.
name|createMessage
argument_list|(
name|session
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|send (Exchange exchange)
specifier|public
name|void
name|send
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// lets convert to the type of an exchange
name|JmsExchange
name|jmsExchange
init|=
name|convertTo
argument_list|(
name|JmsExchange
operator|.
name|class
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|send
argument_list|(
name|jmsExchange
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the JMS destination for this endpoint      */
DECL|method|getDestination ()
specifier|public
name|Destination
name|getDestination
parameter_list|()
block|{
return|return
name|destination
return|;
block|}
DECL|method|getTemplate ()
specifier|public
name|JmsOperations
name|getTemplate
parameter_list|()
block|{
return|return
name|template
return|;
block|}
DECL|method|createExchange ()
specifier|public
name|JmsExchange
name|createExchange
parameter_list|()
block|{
return|return
operator|new
name|DefaultJmsExchange
argument_list|()
return|;
block|}
block|}
end_class

end_unit

