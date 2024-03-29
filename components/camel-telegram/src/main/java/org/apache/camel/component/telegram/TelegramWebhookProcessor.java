begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.telegram
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|telegram
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|ObjectMapper
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
name|AsyncProcessor
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
name|component
operator|.
name|telegram
operator|.
name|model
operator|.
name|Update
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
name|AsyncProcessorConverterHelper
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
name|AsyncProcessorSupport
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
name|telegram
operator|.
name|util
operator|.
name|TelegramMessageHelper
operator|.
name|populateExchange
import|;
end_import

begin_class
DECL|class|TelegramWebhookProcessor
specifier|public
class|class
name|TelegramWebhookProcessor
extends|extends
name|AsyncProcessorSupport
implements|implements
name|AsyncProcessor
block|{
DECL|field|MAPPER
specifier|private
specifier|static
specifier|final
name|ObjectMapper
name|MAPPER
init|=
operator|new
name|ObjectMapper
argument_list|()
decl_stmt|;
DECL|field|next
specifier|private
name|AsyncProcessor
name|next
decl_stmt|;
DECL|method|TelegramWebhookProcessor (Processor next)
specifier|public
name|TelegramWebhookProcessor
parameter_list|(
name|Processor
name|next
parameter_list|)
block|{
name|this
operator|.
name|next
operator|=
name|AsyncProcessorConverterHelper
operator|.
name|convert
argument_list|(
name|next
argument_list|)
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
comment|// Let's process the body and transform it into a Telegram incoming message
name|Update
name|update
decl_stmt|;
try|try
init|(
name|InputStream
name|source
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
init|)
block|{
name|update
operator|=
name|MAPPER
operator|.
name|readValue
argument_list|(
name|source
argument_list|,
name|Update
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|ex
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
name|populateExchange
argument_list|(
name|exchange
argument_list|,
name|update
argument_list|)
expr_stmt|;
return|return
name|next
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|doneSync
lambda|->
block|{
comment|// No response data expected
name|exchange
operator|.
name|getMessage
argument_list|()
operator|.
name|setBody
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
name|doneSync
argument_list|)
expr_stmt|;
block|}
argument_list|)
return|;
block|}
block|}
end_class

end_unit

