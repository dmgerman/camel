begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.avro
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|avro
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|ipc
operator|.
name|Callback
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|ipc
operator|.
name|Requestor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|ipc
operator|.
name|Transceiver
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
name|commons
operator|.
name|lang3
operator|.
name|StringUtils
import|;
end_import

begin_class
DECL|class|AvroProducer
specifier|public
specifier|abstract
class|class
name|AvroProducer
extends|extends
name|DefaultAsyncProducer
block|{
DECL|field|transceiver
name|Transceiver
name|transceiver
decl_stmt|;
DECL|field|requestor
name|Requestor
name|requestor
decl_stmt|;
DECL|method|AvroProducer (Endpoint endpoint)
specifier|public
name|AvroProducer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|createTransceiver ()
specifier|public
specifier|abstract
name|Transceiver
name|createTransceiver
parameter_list|()
throws|throws
name|Exception
function_decl|;
annotation|@
name|Override
DECL|method|process (final Exchange exchange, final AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
block|{
name|Object
name|request
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
try|try
block|{
name|String
name|messageName
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|AvroConstants
operator|.
name|AVRO_MESSAGE_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
condition|)
block|{
name|messageName
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|AvroConstants
operator|.
name|AVRO_MESSAGE_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|messageName
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getMessageName
argument_list|()
expr_stmt|;
block|}
name|requestor
operator|.
name|request
argument_list|(
name|messageName
argument_list|,
name|wrapObjectToArray
argument_list|(
name|request
argument_list|)
argument_list|,
operator|new
name|Callback
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|handleResult
parameter_list|(
name|Object
name|result
parameter_list|)
block|{
comment|// got result from avro, so set it on the exchange and invoke the callback
try|try
block|{
comment|// propagate headers
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeaders
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|handleError
parameter_list|(
name|Throwable
name|error
parameter_list|)
block|{
comment|// got error from avro, so set it on the exchange and invoke the callback
try|try
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|error
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|// okay we continue routing asynchronously
return|return
literal|false
return|;
block|}
DECL|method|wrapObjectToArray (Object object)
specifier|public
name|Object
index|[]
name|wrapObjectToArray
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
operator|instanceof
name|Object
index|[]
condition|)
block|{
return|return
operator|(
name|Object
index|[]
operator|)
name|object
return|;
block|}
else|else
block|{
name|Object
index|[]
name|wrapper
init|=
operator|new
name|Object
index|[
literal|1
index|]
decl_stmt|;
name|wrapper
index|[
literal|0
index|]
operator|=
name|object
expr_stmt|;
return|return
name|wrapper
return|;
block|}
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
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|transceiver
operator|=
name|createTransceiver
argument_list|()
expr_stmt|;
name|AvroConfiguration
name|configuration
init|=
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
if|if
condition|(
name|configuration
operator|.
name|isReflectionProtocol
argument_list|()
condition|)
block|{
name|requestor
operator|=
operator|new
name|AvroReflectRequestor
argument_list|(
name|configuration
operator|.
name|getProtocol
argument_list|()
argument_list|,
name|transceiver
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|requestor
operator|=
operator|new
name|AvroSpecificRequestor
argument_list|(
name|configuration
operator|.
name|getProtocol
argument_list|()
argument_list|,
name|transceiver
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|transceiver
operator|!=
literal|null
condition|)
block|{
name|transceiver
operator|.
name|close
argument_list|()
expr_stmt|;
name|transceiver
operator|=
literal|null
expr_stmt|;
block|}
name|requestor
operator|=
literal|null
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|AvroEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|AvroEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
block|}
end_class

end_unit

