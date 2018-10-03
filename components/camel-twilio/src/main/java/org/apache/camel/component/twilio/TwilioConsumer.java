begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.twilio
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|twilio
package|;
end_package

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
name|twilio
operator|.
name|internal
operator|.
name|TwilioApiName
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
name|component
operator|.
name|AbstractApiConsumer
import|;
end_import

begin_comment
comment|/**  * The Twilio consumer.  */
end_comment

begin_class
DECL|class|TwilioConsumer
specifier|public
class|class
name|TwilioConsumer
extends|extends
name|AbstractApiConsumer
argument_list|<
name|TwilioApiName
argument_list|,
name|TwilioConfiguration
argument_list|>
block|{
DECL|field|endpoint
specifier|protected
specifier|final
name|TwilioEndpoint
name|endpoint
decl_stmt|;
DECL|method|TwilioConsumer (TwilioEndpoint endpoint, Processor processor)
specifier|public
name|TwilioConsumer
parameter_list|(
name|TwilioEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doInvokeMethod (Map<String, Object> args)
specifier|protected
name|Object
name|doInvokeMethod
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|args
parameter_list|)
block|{
name|Object
name|executor
init|=
name|super
operator|.
name|doInvokeMethod
argument_list|(
name|args
argument_list|)
decl_stmt|;
return|return
name|endpoint
operator|.
name|execute
argument_list|(
name|executor
argument_list|,
name|method
argument_list|,
name|args
argument_list|)
return|;
block|}
block|}
end_class

end_unit

