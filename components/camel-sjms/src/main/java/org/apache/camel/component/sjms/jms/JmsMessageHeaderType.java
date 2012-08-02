begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_enum
DECL|enum|JmsMessageHeaderType
specifier|public
enum|enum
name|JmsMessageHeaderType
block|{
DECL|enumConstant|JMSDestination
name|JMSDestination
block|,
DECL|enumConstant|JMSDeliveryMode
name|JMSDeliveryMode
block|,
DECL|enumConstant|JMSExpiration
name|JMSExpiration
block|,
DECL|enumConstant|JMSPriority
name|JMSPriority
block|,
DECL|enumConstant|JMSMessageID
name|JMSMessageID
block|,
DECL|enumConstant|JMSTimestamp
name|JMSTimestamp
block|,
DECL|enumConstant|JMSCorrelationID
name|JMSCorrelationID
block|,
DECL|enumConstant|JMSReplyTo
name|JMSReplyTo
block|,
DECL|enumConstant|JMSType
name|JMSType
block|,
DECL|enumConstant|JMSRedelivered
name|JMSRedelivered
block|,
comment|/*      * Add known custom headers      */
DECL|enumConstant|JMSXGroupID
name|JMSXGroupID
block|}
end_enum

end_unit

