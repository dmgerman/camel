begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms.jms
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
operator|.
name|jms
package|;
end_package

begin_comment
comment|/**  * Enum for different {@link javax.jms.Message} types.  */
end_comment

begin_enum
DECL|enum|JmsMessageType
specifier|public
enum|enum
name|JmsMessageType
block|{
comment|/**      * First the JMS Message types      */
DECL|enumConstant|Bytes
name|Bytes
block|,
DECL|enumConstant|Map
name|Map
block|,
DECL|enumConstant|Object
name|Object
block|,
DECL|enumConstant|Stream
name|Stream
block|,
DECL|enumConstant|Text
name|Text
block|,
comment|/**      * BlobMessage which is not supported by all JMS implementations      */
DECL|enumConstant|Blob
name|Blob
block|,
comment|/**      * The default type that can be used for empty messages.      */
DECL|enumConstant|Message
name|Message
block|}
end_enum

end_unit

