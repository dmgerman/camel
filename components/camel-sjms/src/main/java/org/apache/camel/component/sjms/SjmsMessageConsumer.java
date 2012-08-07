begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sjms
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Exchanger
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
name|javax
operator|.
name|jms
operator|.
name|MessageListener
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
name|sjms
operator|.
name|jms
operator|.
name|ConnectionResource
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
name|sjms
operator|.
name|jms
operator|.
name|SessionPool
import|;
end_import

begin_comment
comment|/**  * TODO Add Class documentation for SjmsMessageConsumer  *  */
end_comment

begin_interface
DECL|interface|SjmsMessageConsumer
specifier|public
interface|interface
name|SjmsMessageConsumer
extends|extends
name|MessageListener
block|{
DECL|method|handleMessage (Message message)
name|void
name|handleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
function_decl|;
DECL|method|createMessageConsumer (ConnectionResource connectionResource, String destinationName)
name|SjmsMessageConsumer
name|createMessageConsumer
parameter_list|(
name|ConnectionResource
name|connectionResource
parameter_list|,
name|String
name|destinationName
parameter_list|)
throws|throws
name|Exception
function_decl|;
DECL|method|createMessageConsumerListener (SessionPool sessionPool, String destinationName, Exchanger<Object> exchanger)
name|SjmsMessageConsumer
name|createMessageConsumerListener
parameter_list|(
name|SessionPool
name|sessionPool
parameter_list|,
name|String
name|destinationName
parameter_list|,
name|Exchanger
argument_list|<
name|Object
argument_list|>
name|exchanger
parameter_list|)
throws|throws
name|Exception
function_decl|;
DECL|method|destroyMessageConsumer ()
name|void
name|destroyMessageConsumer
parameter_list|()
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

