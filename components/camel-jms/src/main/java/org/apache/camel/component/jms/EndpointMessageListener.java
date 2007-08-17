begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jms
package|;
end_package

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
name|MessageListener
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|RuntimeCamelException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * A JMS {@link MessageListener} which can be used to delegate processing to a  * Camel endpoint.  *   * @version $Revision$  */
end_comment

begin_class
DECL|class|EndpointMessageListener
specifier|public
class|class
name|EndpointMessageListener
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
implements|implements
name|MessageListener
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|EndpointMessageListener
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|private
name|Endpoint
argument_list|<
name|E
argument_list|>
name|endpoint
decl_stmt|;
DECL|field|processor
specifier|private
name|Processor
name|processor
decl_stmt|;
DECL|field|binding
specifier|private
name|JmsBinding
name|binding
decl_stmt|;
DECL|field|eagerLoadingOfProperties
specifier|private
name|boolean
name|eagerLoadingOfProperties
decl_stmt|;
DECL|method|EndpointMessageListener (Endpoint<E> endpoint, Processor processor)
specifier|public
name|EndpointMessageListener
parameter_list|(
name|Endpoint
argument_list|<
name|E
argument_list|>
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
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
try|try
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
name|endpoint
operator|+
literal|" receiving JMS message: "
operator|+
name|message
argument_list|)
expr_stmt|;
block|}
name|JmsExchange
name|exchange
init|=
name|createExchange
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|eagerLoadingOfProperties
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
expr_stmt|;
block|}
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|createExchange (Message message)
specifier|public
name|JmsExchange
name|createExchange
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
operator|new
name|JmsExchange
argument_list|(
name|endpoint
operator|.
name|getContext
argument_list|()
argument_list|,
name|getBinding
argument_list|()
argument_list|,
name|message
argument_list|)
return|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|getBinding ()
specifier|public
name|JmsBinding
name|getBinding
parameter_list|()
block|{
if|if
condition|(
name|binding
operator|==
literal|null
condition|)
block|{
name|binding
operator|=
operator|new
name|JmsBinding
argument_list|()
expr_stmt|;
block|}
return|return
name|binding
return|;
block|}
comment|/**      * Sets the binding used to convert from a Camel message to and from a JMS      * message      *       * @param binding the binding to use      */
DECL|method|setBinding (JmsBinding binding)
specifier|public
name|void
name|setBinding
parameter_list|(
name|JmsBinding
name|binding
parameter_list|)
block|{
name|this
operator|.
name|binding
operator|=
name|binding
expr_stmt|;
block|}
DECL|method|isEagerLoadingOfProperties ()
specifier|public
name|boolean
name|isEagerLoadingOfProperties
parameter_list|()
block|{
return|return
name|eagerLoadingOfProperties
return|;
block|}
DECL|method|setEagerLoadingOfProperties (boolean eagerLoadingOfProperties)
specifier|public
name|void
name|setEagerLoadingOfProperties
parameter_list|(
name|boolean
name|eagerLoadingOfProperties
parameter_list|)
block|{
name|this
operator|.
name|eagerLoadingOfProperties
operator|=
name|eagerLoadingOfProperties
expr_stmt|;
block|}
block|}
end_class

end_unit

