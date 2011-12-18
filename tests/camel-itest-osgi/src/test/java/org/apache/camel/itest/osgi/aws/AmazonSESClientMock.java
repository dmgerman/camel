begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.osgi.aws
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|osgi
operator|.
name|aws
package|;
end_package

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|AmazonClientException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|AmazonServiceException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|auth
operator|.
name|BasicAWSCredentials
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleemail
operator|.
name|AmazonSimpleEmailServiceClient
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleemail
operator|.
name|model
operator|.
name|SendEmailRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleemail
operator|.
name|model
operator|.
name|SendEmailResult
import|;
end_import

begin_class
DECL|class|AmazonSESClientMock
specifier|public
class|class
name|AmazonSESClientMock
extends|extends
name|AmazonSimpleEmailServiceClient
block|{
DECL|field|sendEmailRequest
specifier|private
name|SendEmailRequest
name|sendEmailRequest
decl_stmt|;
DECL|method|AmazonSESClientMock ()
specifier|public
name|AmazonSESClientMock
parameter_list|()
block|{
name|super
argument_list|(
operator|new
name|BasicAWSCredentials
argument_list|(
literal|"myAccessKey"
argument_list|,
literal|"mySecretKey"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|sendEmail (SendEmailRequest sendEmailRequest)
specifier|public
name|SendEmailResult
name|sendEmail
parameter_list|(
name|SendEmailRequest
name|sendEmailRequest
parameter_list|)
throws|throws
name|AmazonServiceException
throws|,
name|AmazonClientException
block|{
name|this
operator|.
name|sendEmailRequest
operator|=
name|sendEmailRequest
expr_stmt|;
name|SendEmailResult
name|result
init|=
operator|new
name|SendEmailResult
argument_list|()
decl_stmt|;
name|result
operator|.
name|setMessageId
argument_list|(
literal|"1"
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
DECL|method|getSendEmailRequest ()
specifier|public
name|SendEmailRequest
name|getSendEmailRequest
parameter_list|()
block|{
return|return
name|sendEmailRequest
return|;
block|}
block|}
end_class

end_unit

