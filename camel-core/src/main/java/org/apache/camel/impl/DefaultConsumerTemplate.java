begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ConsumerTemplate
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
name|util
operator|.
name|CamelContextHelper
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
name|util
operator|.
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|DefaultConsumerTemplate
specifier|public
class|class
name|DefaultConsumerTemplate
implements|implements
name|ConsumerTemplate
block|{
DECL|field|context
specifier|private
specifier|final
name|CamelContext
name|context
decl_stmt|;
DECL|field|consumerCache
specifier|private
specifier|final
name|ConsumerCache
name|consumerCache
init|=
operator|new
name|ConsumerCache
argument_list|()
decl_stmt|;
DECL|method|DefaultConsumerTemplate (CamelContext context)
specifier|public
name|DefaultConsumerTemplate
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
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
name|consumerCache
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
name|consumerCache
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
DECL|method|receive (String endpointUri)
specifier|public
name|Exchange
name|receive
parameter_list|(
name|String
name|endpointUri
parameter_list|)
block|{
name|Endpoint
name|endpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
name|endpointUri
argument_list|)
decl_stmt|;
return|return
name|consumerCache
operator|.
name|receive
argument_list|(
name|endpoint
argument_list|)
return|;
block|}
DECL|method|receive (Endpoint endpoinit)
specifier|public
name|Exchange
name|receive
parameter_list|(
name|Endpoint
name|endpoinit
parameter_list|)
block|{
return|return
name|receive
argument_list|(
name|endpoinit
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
return|;
block|}
DECL|method|receive (String endpointUri, long timeout)
specifier|public
name|Exchange
name|receive
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|long
name|timeout
parameter_list|)
block|{
name|Endpoint
name|endpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
name|endpointUri
argument_list|)
decl_stmt|;
return|return
name|consumerCache
operator|.
name|receive
argument_list|(
name|endpoint
argument_list|,
name|timeout
argument_list|)
return|;
block|}
DECL|method|receive (Endpoint endpoint, long timeout)
specifier|public
name|Exchange
name|receive
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|long
name|timeout
parameter_list|)
block|{
return|return
name|receive
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|,
name|timeout
argument_list|)
return|;
block|}
DECL|method|receiveNoWait (String endpointUri)
specifier|public
name|Exchange
name|receiveNoWait
parameter_list|(
name|String
name|endpointUri
parameter_list|)
block|{
name|Endpoint
name|endpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
name|endpointUri
argument_list|)
decl_stmt|;
return|return
name|consumerCache
operator|.
name|receiveNoWait
argument_list|(
name|endpoint
argument_list|)
return|;
block|}
DECL|method|receiveNoWait (Endpoint endpoint)
specifier|public
name|Exchange
name|receiveNoWait
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
return|return
name|receiveNoWait
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
return|;
block|}
DECL|method|receiveBody (String endpointUri)
specifier|public
name|Object
name|receiveBody
parameter_list|(
name|String
name|endpointUri
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|receive
argument_list|(
name|endpointUri
argument_list|)
decl_stmt|;
return|return
name|extractResultBody
argument_list|(
name|exchange
argument_list|)
return|;
block|}
DECL|method|receiveBody (Endpoint endpoint)
specifier|public
name|Object
name|receiveBody
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
return|return
name|receiveBody
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
return|;
block|}
DECL|method|receiveBody (String endpointUri, long timeout)
specifier|public
name|Object
name|receiveBody
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|long
name|timeout
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|receive
argument_list|(
name|endpointUri
argument_list|,
name|timeout
argument_list|)
decl_stmt|;
return|return
name|extractResultBody
argument_list|(
name|exchange
argument_list|)
return|;
block|}
DECL|method|receiveBody (Endpoint endpoint, long timeout)
specifier|public
name|Object
name|receiveBody
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|long
name|timeout
parameter_list|)
block|{
return|return
name|receiveBody
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|,
name|timeout
argument_list|)
return|;
block|}
DECL|method|receiveBodyNoWait (String endpointUri)
specifier|public
name|Object
name|receiveBodyNoWait
parameter_list|(
name|String
name|endpointUri
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|receiveNoWait
argument_list|(
name|endpointUri
argument_list|)
decl_stmt|;
return|return
name|extractResultBody
argument_list|(
name|exchange
argument_list|)
return|;
block|}
DECL|method|receiveBodyNoWait (Endpoint endpoint)
specifier|public
name|Object
name|receiveBodyNoWait
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
return|return
name|receiveBodyNoWait
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
return|;
block|}
DECL|method|receiveBody (String endpointUri, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|receiveBody
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|Object
name|body
init|=
name|receiveBody
argument_list|(
name|endpointUri
argument_list|)
decl_stmt|;
return|return
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|body
argument_list|)
return|;
block|}
DECL|method|receiveBody (Endpoint endpoint, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|receiveBody
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|receiveBody
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|,
name|type
argument_list|)
return|;
block|}
DECL|method|receiveBody (String endpointUri, long timeout, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|receiveBody
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|long
name|timeout
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|Object
name|body
init|=
name|receiveBody
argument_list|(
name|endpointUri
argument_list|,
name|timeout
argument_list|)
decl_stmt|;
return|return
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|body
argument_list|)
return|;
block|}
DECL|method|receiveBody (Endpoint endpoint, long timeout, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|receiveBody
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|long
name|timeout
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|receiveBody
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|,
name|timeout
argument_list|,
name|type
argument_list|)
return|;
block|}
DECL|method|receiveBodyNoWait (String endpointUri, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|receiveBodyNoWait
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|Object
name|body
init|=
name|receiveBodyNoWait
argument_list|(
name|endpointUri
argument_list|)
decl_stmt|;
return|return
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|body
argument_list|)
return|;
block|}
DECL|method|receiveBodyNoWait (Endpoint endpoint, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|receiveBodyNoWait
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|receiveBodyNoWait
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|,
name|type
argument_list|)
return|;
block|}
DECL|method|resolveMandatoryEndpoint (String endpointUri)
specifier|protected
name|Endpoint
name|resolveMandatoryEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|)
block|{
return|return
name|CamelContextHelper
operator|.
name|getMandatoryEndpoint
argument_list|(
name|context
argument_list|,
name|endpointUri
argument_list|)
return|;
block|}
comment|/**      * Extracts the body from the given result.      *<p/>      * If the exchange pattern is provided it will try to honor it and retrive the body      * from either IN or OUT according to the pattern.      *      * @param result   the result      * @return  the result, can be<tt>null</tt>.      */
DECL|method|extractResultBody (Exchange result)
specifier|protected
name|Object
name|extractResultBody
parameter_list|(
name|Exchange
name|result
parameter_list|)
block|{
name|Object
name|answer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|result
operator|!=
literal|null
condition|)
block|{
comment|// rethrow if there was an exception
if|if
condition|(
name|result
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
throw|throw
name|wrapRuntimeCamelException
argument_list|(
name|result
operator|.
name|getException
argument_list|()
argument_list|)
throw|;
block|}
comment|// okay no fault then return the response
if|if
condition|(
name|result
operator|.
name|hasOut
argument_list|()
condition|)
block|{
comment|// use OUT as the response
name|answer
operator|=
name|result
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// use IN as the response
name|answer
operator|=
name|result
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

