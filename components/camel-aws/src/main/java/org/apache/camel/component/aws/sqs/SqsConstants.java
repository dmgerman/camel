begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.sqs
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|sqs
package|;
end_package

begin_comment
comment|/**  * Constants used in Camel AWS SQS module  *  */
end_comment

begin_interface
DECL|interface|SqsConstants
specifier|public
interface|interface
name|SqsConstants
block|{
DECL|field|ATTRIBUTES
name|String
name|ATTRIBUTES
init|=
literal|"CamelAwsSqsAttributes"
decl_stmt|;
DECL|field|MD5_OF_BODY
name|String
name|MD5_OF_BODY
init|=
literal|"CamelAwsSqsMD5OfBody"
decl_stmt|;
DECL|field|MESSAGE_ID
name|String
name|MESSAGE_ID
init|=
literal|"CamelAwsSqsMessageId"
decl_stmt|;
DECL|field|RECEIPT_HANDLE
name|String
name|RECEIPT_HANDLE
init|=
literal|"CamelAwsSqsReceiptHandle"
decl_stmt|;
DECL|field|DELAY_HEADER
name|String
name|DELAY_HEADER
init|=
literal|"CamelAwsSqsDelaySeconds"
decl_stmt|;
block|}
end_interface

end_unit

