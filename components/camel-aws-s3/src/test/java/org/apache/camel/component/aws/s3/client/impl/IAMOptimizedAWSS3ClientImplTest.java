begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.s3.client.impl
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
name|s3
operator|.
name|client
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|s3
operator|.
name|AmazonS3
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
name|s3
operator|.
name|AmazonS3EncryptionClient
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
name|s3
operator|.
name|model
operator|.
name|EncryptionMaterials
import|;
end_import

begin_import
import|import
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
name|s3
operator|.
name|S3Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
import|;
end_import

begin_comment
comment|/**  * Basic testing to ensure that the IAMOptimizedAWSS3ClientImplTest class is returning a standard client that is  * capable of encryption given certain parameters. This client is new to Camel as of 02-15-2018 and enables IAM  * temporary credentials to improve security.  */
end_comment

begin_class
DECL|class|IAMOptimizedAWSS3ClientImplTest
specifier|public
class|class
name|IAMOptimizedAWSS3ClientImplTest
block|{
DECL|field|MAX_CONNECTIONS
specifier|private
specifier|static
specifier|final
name|int
name|MAX_CONNECTIONS
init|=
literal|1
decl_stmt|;
annotation|@
name|Test
DECL|method|iamOptimizedAWSS3ClientImplNoEncryption ()
specifier|public
name|void
name|iamOptimizedAWSS3ClientImplNoEncryption
parameter_list|()
block|{
name|S3ClientIAMOptimizedImpl
name|iamOptimizedAWSS3Client
init|=
operator|new
name|S3ClientIAMOptimizedImpl
argument_list|(
name|getS3ConfigurationNoEncryption
argument_list|()
argument_list|,
name|MAX_CONNECTIONS
argument_list|)
decl_stmt|;
name|AmazonS3
name|s3Client
init|=
name|iamOptimizedAWSS3Client
operator|.
name|getS3Client
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|s3Client
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|s3Client
operator|instanceof
name|AmazonS3EncryptionClient
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|iamOptimizedAWSS3ClientImplUseEncryption ()
specifier|public
name|void
name|iamOptimizedAWSS3ClientImplUseEncryption
parameter_list|()
block|{
name|S3ClientIAMOptimizedImpl
name|iamOptimizedAWSS3Client
init|=
operator|new
name|S3ClientIAMOptimizedImpl
argument_list|(
name|getS3ConfigurationUseEncryption
argument_list|()
argument_list|,
name|MAX_CONNECTIONS
argument_list|)
decl_stmt|;
name|AmazonS3
name|s3Client
init|=
name|iamOptimizedAWSS3Client
operator|.
name|getS3Client
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|s3Client
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|s3Client
operator|instanceof
name|AmazonS3EncryptionClient
argument_list|)
expr_stmt|;
block|}
DECL|method|getS3ConfigurationNoEncryption ()
specifier|private
name|S3Configuration
name|getS3ConfigurationNoEncryption
parameter_list|()
block|{
name|S3Configuration
name|s3Configuration
init|=
name|mock
argument_list|(
name|S3Configuration
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|s3Configuration
operator|.
name|getRegion
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"US_EAST_1"
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|s3Configuration
operator|.
name|isUseEncryption
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return
name|s3Configuration
return|;
block|}
DECL|method|getS3ConfigurationUseEncryption ()
specifier|private
name|S3Configuration
name|getS3ConfigurationUseEncryption
parameter_list|()
block|{
name|S3Configuration
name|s3Configuration
init|=
name|mock
argument_list|(
name|S3Configuration
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|s3Configuration
operator|.
name|getRegion
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"US_EAST_1"
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|s3Configuration
operator|.
name|isUseEncryption
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|s3Configuration
return|;
block|}
block|}
end_class

end_unit

