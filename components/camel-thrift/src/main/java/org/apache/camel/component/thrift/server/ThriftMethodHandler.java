begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.thrift.server
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|thrift
operator|.
name|server
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
name|Arrays
import|;
end_import

begin_import
import|import
name|javassist
operator|.
name|util
operator|.
name|proxy
operator|.
name|MethodHandler
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
name|component
operator|.
name|thrift
operator|.
name|ThriftConstants
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
name|thrift
operator|.
name|ThriftConsumer
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
name|thrift
operator|.
name|ThriftEndpoint
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
name|thrift
operator|.
name|ThriftUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|thrift
operator|.
name|TApplicationException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|thrift
operator|.
name|TException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|thrift
operator|.
name|async
operator|.
name|AsyncMethodCallback
import|;
end_import

begin_comment
comment|/**  * Thrift server methods invocation handler  */
end_comment

begin_class
DECL|class|ThriftMethodHandler
specifier|public
class|class
name|ThriftMethodHandler
implements|implements
name|MethodHandler
block|{
DECL|field|endpoint
specifier|private
specifier|final
name|ThriftEndpoint
name|endpoint
decl_stmt|;
DECL|field|consumer
specifier|private
specifier|final
name|ThriftConsumer
name|consumer
decl_stmt|;
DECL|method|ThriftMethodHandler (ThriftEndpoint endpoint, ThriftConsumer consumer)
specifier|public
name|ThriftMethodHandler
parameter_list|(
name|ThriftEndpoint
name|endpoint
parameter_list|,
name|ThriftConsumer
name|consumer
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
name|consumer
operator|=
name|consumer
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
DECL|method|invoke (Object self, Method thisMethod, Method proceed, Object[] args)
specifier|public
name|Object
name|invoke
parameter_list|(
name|Object
name|self
parameter_list|,
name|Method
name|thisMethod
parameter_list|,
name|Method
name|proceed
parameter_list|,
name|Object
index|[]
name|args
parameter_list|)
throws|throws
name|Throwable
block|{
if|if
condition|(
name|proceed
operator|==
literal|null
condition|)
block|{
comment|// Detects async methods invocation as a last argument is instance of
comment|// {org.apache.thrift.async.AsyncMethodCallback}
if|if
condition|(
name|args
operator|.
name|length
operator|>
literal|0
operator|&&
name|args
index|[
name|args
operator|.
name|length
operator|-
literal|1
index|]
operator|instanceof
name|AsyncMethodCallback
condition|)
block|{
name|AsyncMethodCallback
name|callback
init|=
operator|(
name|AsyncMethodCallback
operator|)
name|args
index|[
name|args
operator|.
name|length
operator|-
literal|1
index|]
decl_stmt|;
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
if|if
condition|(
name|args
operator|.
name|length
operator|>=
literal|2
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|Arrays
operator|.
name|copyOfRange
argument_list|(
name|args
argument_list|,
literal|0
argument_list|,
name|args
operator|.
name|length
operator|-
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|ThriftConstants
operator|.
name|THRIFT_METHOD_NAME_HEADER
argument_list|,
name|thisMethod
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|doneSync
lambda|->
block|{
name|Message
name|message
init|=
literal|null
decl_stmt|;
name|Object
name|response
init|=
literal|null
decl_stmt|;
name|Exception
name|exception
init|=
name|exchange
operator|.
name|getException
argument_list|()
decl_stmt|;
if|if
condition|(
name|exception
operator|!=
literal|null
condition|)
block|{
name|callback
operator|.
name|onError
argument_list|(
name|exception
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
name|message
operator|=
name|exchange
operator|.
name|getOut
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|message
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
name|Class
name|returnType
init|=
name|ThriftUtils
operator|.
name|findMethodReturnType
argument_list|(
name|args
index|[
name|args
operator|.
name|length
operator|-
literal|1
index|]
operator|.
name|getClass
argument_list|()
argument_list|,
literal|"onComplete"
argument_list|)
decl_stmt|;
if|if
condition|(
name|returnType
operator|!=
literal|null
condition|)
block|{
name|response
operator|=
name|message
operator|.
name|getBody
argument_list|(
name|returnType
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|callback
operator|.
name|onError
argument_list|(
operator|new
name|TException
argument_list|(
literal|"Unable to detect method return type"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|callback
operator|.
name|onError
argument_list|(
operator|new
name|TException
argument_list|(
literal|"Unable process null message"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|callback
operator|.
name|onComplete
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|args
argument_list|)
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|ThriftConstants
operator|.
name|THRIFT_METHOD_NAME_HEADER
argument_list|,
name|thisMethod
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|Object
name|responseBody
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|thisMethod
operator|.
name|getReturnType
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|responseBody
operator|==
literal|null
operator|&&
operator|!
name|thisMethod
operator|.
name|getReturnType
argument_list|()
operator|.
name|equals
argument_list|(
name|Void
operator|.
name|TYPE
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|TApplicationException
argument_list|(
literal|"Return type requires not empty body"
argument_list|)
throw|;
block|}
return|return
name|responseBody
return|;
block|}
return|return
literal|null
return|;
block|}
else|else
block|{
return|return
name|proceed
operator|.
name|invoke
argument_list|(
name|self
argument_list|,
name|args
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

