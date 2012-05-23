begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
package|;
end_package

begin_comment
comment|/**  * The data format the user expects to see at the Camel CXF components.  It can be  * configured as a property (DataFormat) in the Camel CXF endpoint.  */
end_comment

begin_enum
DECL|enum|DataFormat
specifier|public
enum|enum
name|DataFormat
block|{
comment|/**      * PAYLOAD is the message payload of the message after message configured in      * the CXF endpoint is applied.  Streaming and non-streaming are both      * supported.      */
DECL|enumConstant|PAYLOAD
name|PAYLOAD
block|,
comment|/**      * RAW is the raw message that is received from the transport layer.      * Streaming and non-streaming are both supported.      */
DECL|enumConstant|RAW
name|RAW
block|,
comment|/**      * MESSAGE is the raw message that is received from the transport layer.      * Streaming and non-streaming are both supported.      * @deprecated - equivalent to RAW mode for Camel 2.x      */
DECL|enumConstant|Deprecated
annotation|@
name|Deprecated
DECL|enumConstant|MESSAGE
name|MESSAGE
block|{
specifier|public
name|DataFormat
name|dealias
parameter_list|()
block|{
return|return
name|RAW
return|;
block|}
block|}
block|,
comment|/**      * CXF_MESSAGE is the message that is received from the transport layer      * and then processed through the full set of CXF interceptors.  This       * provides the same functionality as the CXF MESSAGE mode providers.      */
DECL|enumConstant|CXF_MESSAGE
name|CXF_MESSAGE
block|,
comment|/**      * POJOs (Plain old Java objects) are the Java parameters to the method      * it is invoking on the target server.  The "serviceClass" property      * must be included in the endpoint.  Streaming is not available for this      * data format.      */
DECL|enumConstant|POJO
name|POJO
block|;
DECL|method|dealias ()
specifier|public
name|DataFormat
name|dealias
parameter_list|()
block|{
return|return
name|this
return|;
block|}
block|}
end_enum

end_unit

