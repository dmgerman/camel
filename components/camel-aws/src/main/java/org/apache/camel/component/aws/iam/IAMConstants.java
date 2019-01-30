begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.iam
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
name|iam
package|;
end_package

begin_comment
comment|/**  * Constants used in Camel AWS IAM module  */
end_comment

begin_interface
DECL|interface|IAMConstants
specifier|public
interface|interface
name|IAMConstants
block|{
DECL|field|OPERATION
name|String
name|OPERATION
init|=
literal|"CamelAwsIAMOperation"
decl_stmt|;
DECL|field|USERNAME
name|String
name|USERNAME
init|=
literal|"CamelAwsIAMUsername"
decl_stmt|;
DECL|field|ACCESS_KEY_ID
name|String
name|ACCESS_KEY_ID
init|=
literal|"CamelAwsIAMAccessKeyID"
decl_stmt|;
DECL|field|ACCESS_KEY_STATUS
name|String
name|ACCESS_KEY_STATUS
init|=
literal|"CamelAwsIAMAccessKeyStatus"
decl_stmt|;
DECL|field|GROUP_NAME
name|String
name|GROUP_NAME
init|=
literal|"CamelAwsIAMGroupName"
decl_stmt|;
DECL|field|GROUP_PATH
name|String
name|GROUP_PATH
init|=
literal|"CamelAwsIAMGroupPath"
decl_stmt|;
block|}
end_interface

end_unit

