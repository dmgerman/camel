begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ignite.idgen
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ignite
operator|.
name|idgen
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
name|component
operator|.
name|ignite
operator|.
name|IgniteConstants
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
name|support
operator|.
name|MessageHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|IgniteAtomicSequence
import|;
end_import

begin_comment
comment|/**  * Ignite ID Generator producer.  */
end_comment

begin_class
DECL|class|IgniteIdGenProducer
specifier|public
class|class
name|IgniteIdGenProducer
extends|extends
name|DefaultAsyncProducer
block|{
DECL|field|endpoint
specifier|private
name|IgniteIdGenEndpoint
name|endpoint
decl_stmt|;
DECL|field|atomicSeq
specifier|private
name|IgniteAtomicSequence
name|atomicSeq
decl_stmt|;
DECL|method|IgniteIdGenProducer (IgniteIdGenEndpoint endpoint, IgniteAtomicSequence atomicSeq)
specifier|public
name|IgniteIdGenProducer
parameter_list|(
name|IgniteIdGenEndpoint
name|endpoint
parameter_list|,
name|IgniteAtomicSequence
name|atomicSeq
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|atomicSeq
operator|=
name|atomicSeq
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
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|Message
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
name|MessageHelper
operator|.
name|copyHeaders
argument_list|(
name|in
argument_list|,
name|out
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|Long
name|id
init|=
name|in
operator|.
name|getBody
argument_list|(
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|idGenOperationFor
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
case|case
name|ADD_AND_GET
case|:
name|out
operator|.
name|setBody
argument_list|(
name|atomicSeq
operator|.
name|addAndGet
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|GET
case|:
name|out
operator|.
name|setBody
argument_list|(
name|atomicSeq
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|GET_AND_ADD
case|:
name|out
operator|.
name|setBody
argument_list|(
name|atomicSeq
operator|.
name|getAndAdd
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|GET_AND_INCREMENT
case|:
name|out
operator|.
name|setBody
argument_list|(
name|atomicSeq
operator|.
name|getAndIncrement
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|INCREMENT_AND_GET
case|:
name|out
operator|.
name|setBody
argument_list|(
name|atomicSeq
operator|.
name|incrementAndGet
argument_list|()
argument_list|)
expr_stmt|;
break|break;
default|default:
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Operation not supported by Ignite ID Generator producer."
argument_list|)
argument_list|)
expr_stmt|;
break|break;
block|}
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
DECL|method|idGenOperationFor (Exchange exchange)
specifier|private
name|IgniteIdGenOperation
name|idGenOperationFor
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|IgniteConstants
operator|.
name|IGNITE_IDGEN_OPERATION
argument_list|,
name|endpoint
operator|.
name|getOperation
argument_list|()
argument_list|,
name|IgniteIdGenOperation
operator|.
name|class
argument_list|)
return|;
block|}
block|}
end_class

end_unit

