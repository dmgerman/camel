begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.jaxrs
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
operator|.
name|jaxrs
package|;
end_package

begin_enum
DECL|enum|BindingStyle
specifier|public
enum|enum
name|BindingStyle
block|{
comment|/**      *<i>Only available for consumers.</i>      * This binding style processes request parameters, multiparts, etc. and maps them to IN headers, IN attachments and to the message body.      * It aims to eliminate low-level processing of {@link org.apache.cxf.message.MessageContentsList}.      * It also also adds more flexibility and simplicity to the response mapping.      */
DECL|enumConstant|SimpleConsumer
name|SimpleConsumer
block|,
comment|/**      * This is the traditional binding style, which simply dumps the {@link org.apache.cxf.message.MessageContentsList} coming in from the CXF stack      * onto the IN message body. The user is then responsible for processing it according to the contract defined by the JAX-RS method signature.      */
DECL|enumConstant|Default
name|Default
block|,
comment|/**      * A custom binding set by the user.      */
DECL|enumConstant|Custom
name|Custom
block|}
end_enum

end_unit

