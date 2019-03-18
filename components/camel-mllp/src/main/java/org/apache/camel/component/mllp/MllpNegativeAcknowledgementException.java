begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * Abstract base for all MLLP Negative Acknowledgements  */
end_comment

begin_class
DECL|class|MllpNegativeAcknowledgementException
specifier|public
specifier|abstract
class|class
name|MllpNegativeAcknowledgementException
extends|extends
name|MllpAcknowledgementException
block|{
DECL|method|MllpNegativeAcknowledgementException (String message, byte[] hl7Message, byte[] hl7Acknowledgement)
specifier|public
name|MllpNegativeAcknowledgementException
parameter_list|(
name|String
name|message
parameter_list|,
name|byte
index|[]
name|hl7Message
parameter_list|,
name|byte
index|[]
name|hl7Acknowledgement
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|hl7Message
argument_list|,
name|hl7Acknowledgement
argument_list|)
expr_stmt|;
block|}
DECL|method|MllpNegativeAcknowledgementException (String message, byte[] hl7Message, byte[] hl7Acknowledgement, Throwable cause)
specifier|public
name|MllpNegativeAcknowledgementException
parameter_list|(
name|String
name|message
parameter_list|,
name|byte
index|[]
name|hl7Message
parameter_list|,
name|byte
index|[]
name|hl7Acknowledgement
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|hl7Message
argument_list|,
name|hl7Acknowledgement
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
DECL|method|getAcknowledgmentType ()
specifier|public
specifier|abstract
name|String
name|getAcknowledgmentType
parameter_list|()
function_decl|;
block|}
end_class

end_unit

