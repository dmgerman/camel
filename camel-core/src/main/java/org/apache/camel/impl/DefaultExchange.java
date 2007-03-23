begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|CamelContext
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
name|Message
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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

begin_comment
comment|/**  * A default implementation of {@link Exchange}  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|DefaultExchange
specifier|public
class|class
name|DefaultExchange
implements|implements
name|Exchange
block|{
DECL|field|context
specifier|protected
specifier|final
name|CamelContext
name|context
decl_stmt|;
DECL|field|headers
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
decl_stmt|;
DECL|field|in
specifier|private
name|Message
name|in
decl_stmt|;
DECL|field|out
specifier|private
name|Message
name|out
decl_stmt|;
DECL|field|fault
specifier|private
name|Message
name|fault
decl_stmt|;
DECL|field|exception
specifier|private
name|Throwable
name|exception
decl_stmt|;
DECL|field|exchangeId
specifier|private
name|String
name|exchangeId
decl_stmt|;
DECL|method|DefaultExchange (CamelContext context)
specifier|public
name|DefaultExchange
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
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
literal|"Exchange["
operator|+
name|in
operator|+
literal|"]"
return|;
block|}
DECL|method|copy ()
specifier|public
name|Exchange
name|copy
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
name|newInstance
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|copyFrom
argument_list|(
name|this
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
DECL|method|copyFrom (Exchange exchange)
specifier|public
name|void
name|copyFrom
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|setHeaders
argument_list|(
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
name|exchange
operator|.
name|getProperties
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|setIn
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|copy
argument_list|()
argument_list|)
expr_stmt|;
name|setOut
argument_list|(
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|copy
argument_list|()
argument_list|)
expr_stmt|;
name|setFault
argument_list|(
name|exchange
operator|.
name|getFault
argument_list|()
operator|.
name|copy
argument_list|()
argument_list|)
expr_stmt|;
name|setException
argument_list|(
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|newInstance ()
specifier|public
name|Exchange
name|newInstance
parameter_list|()
block|{
return|return
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
return|;
block|}
DECL|method|getContext ()
specifier|public
name|CamelContext
name|getContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
DECL|method|getProperty (String name)
specifier|public
name|Object
name|getProperty
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|headers
operator|!=
literal|null
condition|)
block|{
return|return
name|headers
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|setProperty (String name, Object value)
specifier|public
name|void
name|setProperty
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|getProperties
argument_list|()
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
DECL|method|getProperties ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getProperties
parameter_list|()
block|{
if|if
condition|(
name|headers
operator|==
literal|null
condition|)
block|{
name|headers
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|headers
return|;
block|}
DECL|method|setHeaders (Map<String, Object> headers)
specifier|public
name|void
name|setHeaders
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|)
block|{
name|this
operator|.
name|headers
operator|=
name|headers
expr_stmt|;
block|}
DECL|method|getIn ()
specifier|public
name|Message
name|getIn
parameter_list|()
block|{
if|if
condition|(
name|in
operator|==
literal|null
condition|)
block|{
name|in
operator|=
name|createInMessage
argument_list|()
expr_stmt|;
block|}
return|return
name|in
return|;
block|}
DECL|method|setIn (Message in)
specifier|public
name|void
name|setIn
parameter_list|(
name|Message
name|in
parameter_list|)
block|{
name|this
operator|.
name|in
operator|=
name|in
expr_stmt|;
name|configureMessage
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
DECL|method|getOut ()
specifier|public
name|Message
name|getOut
parameter_list|()
block|{
if|if
condition|(
name|out
operator|==
literal|null
condition|)
block|{
name|out
operator|=
name|createOutMessage
argument_list|()
expr_stmt|;
block|}
return|return
name|out
return|;
block|}
DECL|method|setOut (Message out)
specifier|public
name|void
name|setOut
parameter_list|(
name|Message
name|out
parameter_list|)
block|{
name|this
operator|.
name|out
operator|=
name|out
expr_stmt|;
name|configureMessage
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
DECL|method|getException ()
specifier|public
name|Throwable
name|getException
parameter_list|()
block|{
return|return
name|exception
return|;
block|}
DECL|method|setException (Throwable exception)
specifier|public
name|void
name|setException
parameter_list|(
name|Throwable
name|exception
parameter_list|)
block|{
name|this
operator|.
name|exception
operator|=
name|exception
expr_stmt|;
block|}
DECL|method|getFault ()
specifier|public
name|Message
name|getFault
parameter_list|()
block|{
return|return
name|fault
return|;
block|}
DECL|method|setFault (Message fault)
specifier|public
name|void
name|setFault
parameter_list|(
name|Message
name|fault
parameter_list|)
block|{
name|this
operator|.
name|fault
operator|=
name|fault
expr_stmt|;
name|configureMessage
argument_list|(
name|fault
argument_list|)
expr_stmt|;
block|}
DECL|method|getExchangeId ()
specifier|public
name|String
name|getExchangeId
parameter_list|()
block|{
return|return
name|exchangeId
return|;
block|}
DECL|method|setExchangeId (String id)
specifier|public
name|void
name|setExchangeId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|exchangeId
operator|=
name|id
expr_stmt|;
block|}
comment|/**      * Factory method used to lazily create the IN message      */
DECL|method|createInMessage ()
specifier|protected
name|Message
name|createInMessage
parameter_list|()
block|{
return|return
operator|new
name|DefaultMessage
argument_list|()
return|;
block|}
comment|/**      * Factory method to lazily create the OUT message      */
DECL|method|createOutMessage ()
specifier|protected
name|Message
name|createOutMessage
parameter_list|()
block|{
return|return
operator|new
name|DefaultMessage
argument_list|()
return|;
block|}
comment|/**      * Configures the message after it has been set on the exchange      */
DECL|method|configureMessage (Message message)
specifier|protected
name|void
name|configureMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
if|if
condition|(
name|message
operator|instanceof
name|MessageSupport
condition|)
block|{
name|MessageSupport
name|messageSupport
init|=
operator|(
name|MessageSupport
operator|)
name|message
decl_stmt|;
name|messageSupport
operator|.
name|setExchange
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

