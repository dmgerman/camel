begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hl7
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hl7
package|;
end_package

begin_comment
comment|/**  * HL7 Constants  */
end_comment

begin_class
DECL|class|HL7Constants
specifier|public
specifier|final
class|class
name|HL7Constants
block|{
DECL|field|HL7_SENDING_APPLICATION
specifier|public
specifier|static
specifier|final
name|String
name|HL7_SENDING_APPLICATION
init|=
literal|"CamelHL7SendingApplication"
decl_stmt|;
DECL|field|HL7_SENDING_FACILITY
specifier|public
specifier|static
specifier|final
name|String
name|HL7_SENDING_FACILITY
init|=
literal|"CamelHL7SendingFacility"
decl_stmt|;
DECL|field|HL7_RECEIVING_APPLICATION
specifier|public
specifier|static
specifier|final
name|String
name|HL7_RECEIVING_APPLICATION
init|=
literal|"CamelHL7ReceivingApplication"
decl_stmt|;
DECL|field|HL7_RECEIVING_FACILITY
specifier|public
specifier|static
specifier|final
name|String
name|HL7_RECEIVING_FACILITY
init|=
literal|"CamelHL7ReceivingFacility"
decl_stmt|;
DECL|field|HL7_TIMESTAMP
specifier|public
specifier|static
specifier|final
name|String
name|HL7_TIMESTAMP
init|=
literal|"CamelHL7Timestamp"
decl_stmt|;
DECL|field|HL7_SECURITY
specifier|public
specifier|static
specifier|final
name|String
name|HL7_SECURITY
init|=
literal|"CamelHL7Security"
decl_stmt|;
DECL|field|HL7_MESSAGE_TYPE
specifier|public
specifier|static
specifier|final
name|String
name|HL7_MESSAGE_TYPE
init|=
literal|"CamelHL7MessageType"
decl_stmt|;
DECL|field|HL7_TRIGGER_EVENT
specifier|public
specifier|static
specifier|final
name|String
name|HL7_TRIGGER_EVENT
init|=
literal|"CamelHL7TriggerEvent"
decl_stmt|;
DECL|field|HL7_MESSAGE_CONTROL
specifier|public
specifier|static
specifier|final
name|String
name|HL7_MESSAGE_CONTROL
init|=
literal|"CamelHL7MessageControl"
decl_stmt|;
DECL|field|HL7_PROCESSING_ID
specifier|public
specifier|static
specifier|final
name|String
name|HL7_PROCESSING_ID
init|=
literal|"CamelHL7ProcessingId"
decl_stmt|;
DECL|field|HL7_VERSION_ID
specifier|public
specifier|static
specifier|final
name|String
name|HL7_VERSION_ID
init|=
literal|"CamelHL7VersionId"
decl_stmt|;
DECL|field|HL7_CONTEXT
specifier|public
specifier|static
specifier|final
name|String
name|HL7_CONTEXT
init|=
literal|"CamelHL7Context"
decl_stmt|;
DECL|field|HL7_CHARSET
specifier|public
specifier|static
specifier|final
name|String
name|HL7_CHARSET
init|=
literal|"CamelHL7Charset"
decl_stmt|;
DECL|method|HL7Constants ()
specifier|private
name|HL7Constants
parameter_list|()
block|{     }
block|}
end_class

end_unit

