begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.ses
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
name|ses
package|;
end_package

begin_comment
comment|/**  * Constants used in Camel AWS SES component  */
end_comment

begin_interface
DECL|interface|SesConstants
specifier|public
interface|interface
name|SesConstants
block|{
DECL|field|FROM
name|String
name|FROM
init|=
literal|"CamelAwsSesFrom"
decl_stmt|;
DECL|field|MESSAGE_ID
name|String
name|MESSAGE_ID
init|=
literal|"CamelAwsSesMessageId"
decl_stmt|;
DECL|field|REPLY_TO_ADDRESSES
name|String
name|REPLY_TO_ADDRESSES
init|=
literal|"CamelAwsSesReplyToAddresses"
decl_stmt|;
DECL|field|RETURN_PATH
name|String
name|RETURN_PATH
init|=
literal|"CamelAwsSesReturnPath"
decl_stmt|;
DECL|field|SUBJECT
name|String
name|SUBJECT
init|=
literal|"CamelAwsSesSubject"
decl_stmt|;
DECL|field|TO
name|String
name|TO
init|=
literal|"CamelAwsSesTo"
decl_stmt|;
block|}
end_interface

end_unit

