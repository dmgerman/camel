begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jetty
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jetty
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
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
name|ExecutionException
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
name|Future
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
name|TimeUnit
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
name|TimeoutException
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
name|ExchangeTimedOutException
import|;
end_import

begin_comment
comment|/**  * A {@link Future} task which to be used to retrieve the HTTP response which comes back asynchronously.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|JettyFutureGetBody
specifier|public
class|class
name|JettyFutureGetBody
implements|implements
name|Future
argument_list|<
name|String
argument_list|>
implements|,
name|Serializable
block|{
DECL|field|exchange
specifier|private
specifier|final
name|Exchange
name|exchange
decl_stmt|;
DECL|field|httpExchange
specifier|private
specifier|final
name|JettyContentExchange
name|httpExchange
decl_stmt|;
DECL|field|cancelled
specifier|private
name|boolean
name|cancelled
decl_stmt|;
DECL|field|throwExceptionOnFailure
specifier|private
name|boolean
name|throwExceptionOnFailure
decl_stmt|;
DECL|method|JettyFutureGetBody (Exchange exchange, JettyContentExchange httpExchange, boolean throwExceptionOnFailure)
specifier|public
name|JettyFutureGetBody
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|JettyContentExchange
name|httpExchange
parameter_list|,
name|boolean
name|throwExceptionOnFailure
parameter_list|)
block|{
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
name|this
operator|.
name|httpExchange
operator|=
name|httpExchange
expr_stmt|;
name|this
operator|.
name|throwExceptionOnFailure
operator|=
name|throwExceptionOnFailure
expr_stmt|;
block|}
DECL|method|cancel (boolean mayInterrupt)
specifier|public
name|boolean
name|cancel
parameter_list|(
name|boolean
name|mayInterrupt
parameter_list|)
block|{
name|httpExchange
operator|.
name|cancel
argument_list|()
expr_stmt|;
name|cancelled
operator|=
literal|true
expr_stmt|;
return|return
literal|true
return|;
block|}
DECL|method|isCancelled ()
specifier|public
name|boolean
name|isCancelled
parameter_list|()
block|{
return|return
name|cancelled
return|;
block|}
DECL|method|isDone ()
specifier|public
name|boolean
name|isDone
parameter_list|()
block|{
return|return
name|httpExchange
operator|.
name|isBodyComplete
argument_list|()
return|;
block|}
DECL|method|get ()
specifier|public
name|String
name|get
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|ExecutionException
block|{
comment|// wait for body to be done
if|if
condition|(
operator|!
name|isDone
argument_list|()
condition|)
block|{
try|try
block|{
name|httpExchange
operator|.
name|waitForBodyToComplete
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|interrupt
argument_list|()
expr_stmt|;
comment|// ignore
block|}
block|}
return|return
name|doGetBody
argument_list|()
return|;
block|}
DECL|method|get (long timeout, TimeUnit timeUnit)
specifier|public
name|String
name|get
parameter_list|(
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|timeUnit
parameter_list|)
throws|throws
name|InterruptedException
throws|,
name|ExecutionException
throws|,
name|TimeoutException
block|{
if|if
condition|(
operator|!
name|isDone
argument_list|()
condition|)
block|{
name|boolean
name|done
init|=
name|httpExchange
operator|.
name|waitForBodyToComplete
argument_list|(
name|timeout
argument_list|,
name|timeUnit
argument_list|)
decl_stmt|;
if|if
condition|(
name|done
condition|)
block|{
return|return
name|doGetBody
argument_list|()
return|;
block|}
else|else
block|{
comment|// timeout occurred
name|ExchangeTimedOutException
name|cause
init|=
operator|new
name|ExchangeTimedOutException
argument_list|(
name|exchange
argument_list|,
name|timeout
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ExecutionException
argument_list|(
name|cause
argument_list|)
throw|;
block|}
block|}
return|return
name|doGetBody
argument_list|()
return|;
block|}
DECL|method|doGetBody ()
specifier|private
name|String
name|doGetBody
parameter_list|()
throws|throws
name|ExecutionException
block|{
try|try
block|{
if|if
condition|(
name|httpExchange
operator|.
name|isFailed
argument_list|()
operator|&&
name|throwExceptionOnFailure
condition|)
block|{
throw|throw
operator|new
name|JettyHttpOperationFailedException
argument_list|(
name|httpExchange
operator|.
name|getUrl
argument_list|()
argument_list|,
name|httpExchange
operator|.
name|getResponseStatus
argument_list|()
argument_list|,
name|httpExchange
operator|.
name|getBody
argument_list|()
argument_list|)
throw|;
block|}
else|else
block|{
return|return
name|httpExchange
operator|.
name|getBody
argument_list|()
return|;
block|}
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ExecutionException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|JettyHttpOperationFailedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ExecutionException
argument_list|(
name|e
argument_list|)
throw|;
block|}
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
literal|"JettyFutureGetBody["
operator|+
name|httpExchange
operator|.
name|getUrl
argument_list|()
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

