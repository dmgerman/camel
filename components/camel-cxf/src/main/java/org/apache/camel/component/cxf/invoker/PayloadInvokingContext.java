begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.invoker
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|invoker
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|IdentityHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
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
name|cxf
operator|.
name|message
operator|.
name|Message
import|;
end_import

begin_class
DECL|class|PayloadInvokingContext
specifier|public
class|class
name|PayloadInvokingContext
extends|extends
name|AbstractInvokingContext
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|Logger
operator|.
name|getLogger
argument_list|(
name|PayloadInvokingContext
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
DECL|method|PayloadInvokingContext ()
specifier|public
name|PayloadInvokingContext
parameter_list|()
block|{      }
DECL|method|setRequestOutMessageContent (Message message, Map<Class, Object> contents)
specifier|public
name|void
name|setRequestOutMessageContent
parameter_list|(
name|Message
name|message
parameter_list|,
name|Map
argument_list|<
name|Class
argument_list|,
name|Object
argument_list|>
name|contents
parameter_list|)
block|{
name|PayloadMessage
name|request
init|=
operator|(
name|PayloadMessage
operator|)
name|contents
operator|.
name|get
argument_list|(
name|PayloadMessage
operator|.
name|class
argument_list|)
decl_stmt|;
name|Element
name|header
init|=
name|request
operator|.
name|getHeader
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Element
argument_list|>
name|payload
init|=
name|request
operator|.
name|getPayload
argument_list|()
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINEST
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|finest
argument_list|(
literal|"header = "
operator|+
name|header
operator|+
literal|", paylaod = "
operator|+
name|payload
argument_list|)
expr_stmt|;
block|}
name|message
operator|.
name|put
argument_list|(
name|Element
operator|.
name|class
argument_list|,
name|header
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|List
operator|.
name|class
argument_list|,
name|payload
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getResponseObject (Exchange exchange, Map<String, Object> responseContext)
specifier|public
name|Object
name|getResponseObject
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|responseContext
parameter_list|)
block|{
name|PayloadMessage
name|payloadMsg
init|=
literal|null
decl_stmt|;
name|Message
name|msg
init|=
name|exchange
operator|.
name|getInMessage
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Element
argument_list|>
name|payload
init|=
name|getResponseObject
argument_list|(
name|msg
argument_list|,
name|responseContext
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|Element
name|header
init|=
name|exchange
operator|.
name|getInMessage
argument_list|()
operator|.
name|get
argument_list|(
name|Element
operator|.
name|class
argument_list|)
decl_stmt|;
name|payloadMsg
operator|=
operator|new
name|PayloadMessage
argument_list|(
name|payload
argument_list|,
name|header
argument_list|)
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINEST
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|finest
argument_list|(
name|payloadMsg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|payloadMsg
return|;
block|}
annotation|@
name|Override
DECL|method|getResponseObject (Message inMessage, Map<String, Object> responseContext, Class <T> clazz)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|getResponseObject
parameter_list|(
name|Message
name|inMessage
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|responseContext
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
block|{
name|T
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|inMessage
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
literal|null
operator|!=
name|responseContext
condition|)
block|{
name|responseContext
operator|.
name|putAll
argument_list|(
name|inMessage
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"set responseContext to be"
operator|+
name|responseContext
argument_list|)
expr_stmt|;
block|}
name|retval
operator|=
name|inMessage
operator|.
name|get
argument_list|(
name|clazz
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
DECL|method|setResponseContent (Message outMessage, Object resultPayload)
specifier|public
name|void
name|setResponseContent
parameter_list|(
name|Message
name|outMessage
parameter_list|,
name|Object
name|resultPayload
parameter_list|)
block|{
if|if
condition|(
name|resultPayload
operator|!=
literal|null
condition|)
block|{
name|PayloadMessage
name|payloadMessage
init|=
operator|(
name|PayloadMessage
operator|)
name|resultPayload
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINEST
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|finest
argument_list|(
name|payloadMessage
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|outMessage
operator|.
name|put
argument_list|(
name|List
operator|.
name|class
argument_list|,
name|payloadMessage
operator|.
name|getPayload
argument_list|()
argument_list|)
expr_stmt|;
name|outMessage
operator|.
name|put
argument_list|(
name|Element
operator|.
name|class
argument_list|,
name|payloadMessage
operator|.
name|getHeader
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getRequestContent (Message inMessage)
specifier|public
name|Map
argument_list|<
name|Class
argument_list|,
name|Object
argument_list|>
name|getRequestContent
parameter_list|(
name|Message
name|inMessage
parameter_list|)
block|{
name|List
argument_list|<
name|Element
argument_list|>
name|payload
init|=
name|inMessage
operator|.
name|get
argument_list|(
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|Element
name|header
init|=
name|inMessage
operator|.
name|get
argument_list|(
name|Element
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINEST
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|finest
argument_list|(
literal|"Header = "
operator|+
name|header
operator|+
literal|", Payload = "
operator|+
name|payload
argument_list|)
expr_stmt|;
block|}
name|Map
argument_list|<
name|Class
argument_list|,
name|Object
argument_list|>
name|contents
init|=
operator|new
name|IdentityHashMap
argument_list|<
name|Class
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|contents
operator|.
name|put
argument_list|(
name|PayloadMessage
operator|.
name|class
argument_list|,
operator|new
name|PayloadMessage
argument_list|(
name|payload
argument_list|,
name|header
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|contents
return|;
block|}
block|}
end_class

end_unit

