begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|InvalidHeaderTypeException
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
name|MessageSupport
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
name|java
operator|.
name|util
operator|.
name|Enumeration
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
comment|/**  * @version $Revision:520964 $  */
end_comment

begin_class
DECL|class|DefaultJmsMessage
specifier|public
class|class
name|DefaultJmsMessage
extends|extends
name|MessageSupport
implements|implements
name|JmsMessage
block|{
DECL|field|jmsMessage
specifier|private
name|Message
name|jmsMessage
decl_stmt|;
DECL|field|lazyHeaders
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|lazyHeaders
decl_stmt|;
DECL|method|DefaultJmsMessage ()
specifier|public
name|DefaultJmsMessage
parameter_list|()
block|{     }
DECL|method|DefaultJmsMessage (Message jmsMessage)
specifier|public
name|DefaultJmsMessage
parameter_list|(
name|Message
name|jmsMessage
parameter_list|)
block|{
name|this
operator|.
name|jmsMessage
operator|=
name|jmsMessage
expr_stmt|;
block|}
DECL|method|getJmsMessage ()
specifier|public
name|Message
name|getJmsMessage
parameter_list|()
block|{
return|return
name|jmsMessage
return|;
block|}
DECL|method|setJmsMessage (Message jmsMessage)
specifier|public
name|void
name|setJmsMessage
parameter_list|(
name|Message
name|jmsMessage
parameter_list|)
block|{
name|this
operator|.
name|jmsMessage
operator|=
name|jmsMessage
expr_stmt|;
block|}
DECL|method|getHeader (String name)
specifier|public
name|Object
name|getHeader
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|jmsMessage
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|Object
name|value
init|=
name|jmsMessage
operator|.
name|getObjectProperty
argument_list|(
name|name
argument_list|)
decl_stmt|;
try|try
block|{
return|return
name|value
return|;
block|}
catch|catch
parameter_list|(
name|ClassCastException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|InvalidHeaderTypeException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|value
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|JMSException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MessagePropertyAcessException
argument_list|(
name|name
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|setHeader (String name, Object value)
specifier|public
name|void
name|setHeader
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|jmsMessage
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|jmsMessage
operator|.
name|setObjectProperty
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMSException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MessagePropertyAcessException
argument_list|(
name|name
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|lazyHeaders
operator|==
literal|null
condition|)
block|{
name|lazyHeaders
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
name|lazyHeaders
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getHeaders ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getHeaders
parameter_list|()
block|{
if|if
condition|(
name|jmsMessage
operator|!=
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|answer
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Enumeration
name|names
decl_stmt|;
try|try
block|{
name|names
operator|=
name|jmsMessage
operator|.
name|getPropertyNames
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMSException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MessagePropertyNamesAcessException
argument_list|(
name|e
argument_list|)
throw|;
block|}
while|while
condition|(
name|names
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|String
name|name
init|=
name|names
operator|.
name|nextElement
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
try|try
block|{
name|Object
name|value
init|=
name|jmsMessage
operator|.
name|getObjectProperty
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|answer
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMSException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MessagePropertyAcessException
argument_list|(
name|name
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|answer
return|;
block|}
else|else
block|{
return|return
name|lazyHeaders
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|newInstance ()
specifier|public
name|DefaultJmsMessage
name|newInstance
parameter_list|()
block|{
return|return
operator|new
name|DefaultJmsMessage
argument_list|()
return|;
block|}
block|}
end_class

end_unit

