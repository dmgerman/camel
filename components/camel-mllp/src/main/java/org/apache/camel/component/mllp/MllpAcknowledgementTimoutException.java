begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mllp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mllp
package|;
end_package

begin_comment
comment|/**  * Raised when a MLLP Producer does not receive a HL7 acknowledgement within the configured timespan  */
end_comment

begin_class
DECL|class|MllpAcknowledgementTimoutException
specifier|public
class|class
name|MllpAcknowledgementTimoutException
extends|extends
name|MllpTimeoutException
block|{
DECL|method|MllpAcknowledgementTimoutException (String message)
specifier|public
name|MllpAcknowledgementTimoutException
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
DECL|method|MllpAcknowledgementTimoutException (String message, byte[] mllpPayload)
specifier|public
name|MllpAcknowledgementTimoutException
parameter_list|(
name|String
name|message
parameter_list|,
name|byte
index|[]
name|mllpPayload
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|mllpPayload
argument_list|)
expr_stmt|;
block|}
DECL|method|MllpAcknowledgementTimoutException (String message, Throwable cause)
specifier|public
name|MllpAcknowledgementTimoutException
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
DECL|method|MllpAcknowledgementTimoutException (String message, byte[] mllpPayload, Throwable cause)
specifier|public
name|MllpAcknowledgementTimoutException
parameter_list|(
name|String
name|message
parameter_list|,
name|byte
index|[]
name|mllpPayload
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|mllpPayload
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

