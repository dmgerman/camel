begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atomix.client
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atomix
operator|.
name|client
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentMap
import|;
end_import

begin_import
import|import
name|io
operator|.
name|atomix
operator|.
name|resource
operator|.
name|Resource
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
name|AsyncCallback
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
name|camel
operator|.
name|component
operator|.
name|atomix
operator|.
name|AtomixAsyncMessageProcessor
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
name|spi
operator|.
name|InvokeOnHeader
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
name|support
operator|.
name|DefaultAsyncProducer
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atomix
operator|.
name|client
operator|.
name|AtomixClientConstants
operator|.
name|RESOURCE_ACTION_HAS_RESULT
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atomix
operator|.
name|client
operator|.
name|AtomixClientConstants
operator|.
name|RESOURCE_NAME
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|ObjectHelper
operator|.
name|invokeMethodSafe
import|;
end_import

begin_class
DECL|class|AbstractAtomixClientProducer
specifier|public
specifier|abstract
class|class
name|AbstractAtomixClientProducer
parameter_list|<
name|E
extends|extends
name|AbstractAtomixClientEndpoint
parameter_list|,
name|R
extends|extends
name|Resource
parameter_list|>
extends|extends
name|DefaultAsyncProducer
block|{
DECL|field|processors
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|AtomixAsyncMessageProcessor
argument_list|>
name|processors
decl_stmt|;
DECL|field|resources
specifier|private
name|ConcurrentMap
argument_list|<
name|String
argument_list|,
name|R
argument_list|>
name|resources
decl_stmt|;
DECL|method|AbstractAtomixClientProducer (E endpoint)
specifier|protected
name|AbstractAtomixClientProducer
parameter_list|(
name|E
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|processors
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|resources
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
specifier|final
name|Method
name|method
range|:
name|getClass
argument_list|()
operator|.
name|getDeclaredMethods
argument_list|()
control|)
block|{
name|InvokeOnHeader
index|[]
name|annotations
init|=
name|method
operator|.
name|getAnnotationsByType
argument_list|(
name|InvokeOnHeader
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|annotations
operator|!=
literal|null
operator|&&
name|annotations
operator|.
name|length
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|InvokeOnHeader
name|annotation
range|:
name|annotations
control|)
block|{
name|bind
argument_list|(
name|annotation
argument_list|,
name|method
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange, AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
specifier|final
name|Message
name|message
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
specifier|final
name|String
name|key
init|=
name|getProcessorKey
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|AtomixAsyncMessageProcessor
name|processor
init|=
name|this
operator|.
name|processors
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|processor
operator|!=
literal|null
condition|)
block|{
try|try
block|{
return|return
name|processor
operator|.
name|process
argument_list|(
name|message
argument_list|,
name|callback
argument_list|)
return|;
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
else|else
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"No handler for action "
operator|+
name|key
argument_list|)
throw|;
block|}
block|}
comment|// **********************************
comment|//
comment|// **********************************
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getAtomixEndpoint ()
specifier|protected
name|E
name|getAtomixEndpoint
parameter_list|()
block|{
return|return
operator|(
name|E
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
DECL|method|processResult (Message message, AsyncCallback callback, Object result)
specifier|protected
name|void
name|processResult
parameter_list|(
name|Message
name|message
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|,
name|Object
name|result
parameter_list|)
block|{
if|if
condition|(
name|result
operator|!=
literal|null
operator|&&
operator|!
operator|(
name|result
operator|instanceof
name|Void
operator|)
condition|)
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|RESOURCE_ACTION_HAS_RESULT
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|String
name|resultHeader
init|=
name|getAtomixEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getResultHeader
argument_list|()
decl_stmt|;
if|if
condition|(
name|resultHeader
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|resultHeader
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|message
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|RESOURCE_ACTION_HAS_RESULT
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|getResource (Message message)
specifier|protected
name|R
name|getResource
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|String
name|resourceName
init|=
name|getResourceName
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|resourceName
argument_list|,
name|RESOURCE_NAME
argument_list|)
expr_stmt|;
return|return
name|resources
operator|.
name|computeIfAbsent
argument_list|(
name|resourceName
argument_list|,
name|name
lambda|->
name|createResource
argument_list|(
name|name
argument_list|)
argument_list|)
return|;
block|}
DECL|method|getProcessorKey (Message message)
specifier|protected
specifier|abstract
name|String
name|getProcessorKey
parameter_list|(
name|Message
name|message
parameter_list|)
function_decl|;
DECL|method|getResourceName (Message message)
specifier|protected
specifier|abstract
name|String
name|getResourceName
parameter_list|(
name|Message
name|message
parameter_list|)
function_decl|;
DECL|method|createResource (String name)
specifier|protected
specifier|abstract
name|R
name|createResource
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|// ************************************
comment|// Binding helpers
comment|// ************************************
DECL|method|bind (InvokeOnHeader annotation, final Method method)
specifier|private
name|void
name|bind
parameter_list|(
name|InvokeOnHeader
name|annotation
parameter_list|,
specifier|final
name|Method
name|method
parameter_list|)
block|{
if|if
condition|(
name|method
operator|.
name|getParameterCount
argument_list|()
operator|==
literal|2
condition|)
block|{
if|if
condition|(
operator|!
name|Message
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|method
operator|.
name|getParameterTypes
argument_list|()
index|[
literal|0
index|]
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"First argument should be of type Message"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|AsyncCallback
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|method
operator|.
name|getParameterTypes
argument_list|()
index|[
literal|1
index|]
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Second argument should be of type AsyncCallback"
argument_list|)
throw|;
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"bind key={}, class={}, method={}"
argument_list|,
name|annotation
operator|.
name|value
argument_list|()
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
argument_list|,
name|method
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|processors
operator|.
name|put
argument_list|(
name|annotation
operator|.
name|value
argument_list|()
argument_list|,
parameter_list|(
name|m
parameter_list|,
name|c
parameter_list|)
lambda|->
operator|(
name|boolean
operator|)
name|invokeMethodSafe
argument_list|(
name|method
argument_list|,
name|this
argument_list|,
name|m
argument_list|,
name|c
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Illegal number of parameters for method: "
operator|+
name|method
operator|.
name|getName
argument_list|()
operator|+
literal|", required: 2, found: "
operator|+
name|method
operator|.
name|getParameterCount
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

