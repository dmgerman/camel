begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.sns
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
name|sns
package|;
end_package

begin_comment
comment|/**  * Constants used in Camel AWS SNS module  */
end_comment

begin_interface
DECL|interface|SnsConstants
specifier|public
interface|interface
name|SnsConstants
block|{
DECL|field|MESSAGE_ID
name|String
name|MESSAGE_ID
init|=
literal|"CamelAwsSnsMessageId"
decl_stmt|;
DECL|field|SUBJECT
name|String
name|SUBJECT
init|=
literal|"CamelAwsSnsSubject"
decl_stmt|;
DECL|field|MESSAGE_STRUCTURE
name|String
name|MESSAGE_STRUCTURE
init|=
literal|"CamelAwsSnsMessageStructure"
decl_stmt|;
block|}
end_interface

end_unit

