begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ClientConfiguration
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
name|InstanceProfileCredentialsProvider
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|regions
operator|.
name|Regions
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
name|AmazonS3ClientBuilder
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
name|AmazonS3EncryptionClientBuilder
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
name|StaticEncryptionMaterialsProvider
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
name|S3Client
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Manage an AWS s3 client for all users to use (enabling temporary creds).  * This implementation is for remote instances to manage the credentials on their own (eliminating credential rotations)  */
end_comment

begin_class
DECL|class|S3ClientIAMOptimizedImpl
specifier|public
class|class
name|S3ClientIAMOptimizedImpl
implements|implements
name|S3Client
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|S3ClientIAMOptimizedImpl
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|configuration
specifier|private
name|S3Configuration
name|configuration
decl_stmt|;
DECL|field|maxConnections
specifier|private
name|int
name|maxConnections
decl_stmt|;
comment|/**      * Constructor that uses the config file.      */
DECL|method|S3ClientIAMOptimizedImpl (S3Configuration configuration, int maxConnections)
specifier|public
name|S3ClientIAMOptimizedImpl
parameter_list|(
name|S3Configuration
name|configuration
parameter_list|,
name|int
name|maxConnections
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Creating an AWS S3 client for an ec2 instance with IAM temporary credentials (normal for ec2s)."
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
name|this
operator|.
name|maxConnections
operator|=
name|maxConnections
expr_stmt|;
block|}
comment|/**      * Getting the s3 aws client that is used.      * @return Amazon S3 Client.      */
annotation|@
name|Override
DECL|method|getS3Client ()
specifier|public
name|AmazonS3
name|getS3Client
parameter_list|()
block|{
name|AmazonS3
name|client
init|=
literal|null
decl_stmt|;
name|AmazonS3ClientBuilder
name|clientBuilder
init|=
literal|null
decl_stmt|;
name|AmazonS3EncryptionClientBuilder
name|encClientBuilder
init|=
literal|null
decl_stmt|;
name|ClientConfiguration
name|clientConfiguration
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|configuration
operator|.
name|hasProxyConfiguration
argument_list|()
condition|)
block|{
name|clientConfiguration
operator|=
operator|new
name|ClientConfiguration
argument_list|()
expr_stmt|;
name|clientConfiguration
operator|.
name|setProxyHost
argument_list|(
name|configuration
operator|.
name|getProxyHost
argument_list|()
argument_list|)
expr_stmt|;
name|clientConfiguration
operator|.
name|setProxyPort
argument_list|(
name|configuration
operator|.
name|getProxyPort
argument_list|()
argument_list|)
expr_stmt|;
name|clientConfiguration
operator|.
name|setMaxConnections
argument_list|(
name|maxConnections
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|clientConfiguration
operator|=
operator|new
name|ClientConfiguration
argument_list|()
expr_stmt|;
name|clientConfiguration
operator|.
name|setMaxConnections
argument_list|(
name|maxConnections
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getAccessKey
argument_list|()
operator|!=
literal|null
operator|||
name|configuration
operator|.
name|getSecretKey
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Do not pass in unnecessary static credentials when selecting the IAM credential option."
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|configuration
operator|.
name|isUseEncryption
argument_list|()
condition|)
block|{
name|clientBuilder
operator|=
name|AmazonS3ClientBuilder
operator|.
name|standard
argument_list|()
operator|.
name|withCredentials
argument_list|(
operator|new
name|InstanceProfileCredentialsProvider
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|configuration
operator|.
name|isUseEncryption
argument_list|()
condition|)
block|{
name|StaticEncryptionMaterialsProvider
name|encryptionMaterialsProvider
init|=
operator|new
name|StaticEncryptionMaterialsProvider
argument_list|(
name|configuration
operator|.
name|getEncryptionMaterials
argument_list|()
argument_list|)
decl_stmt|;
name|encClientBuilder
operator|=
name|AmazonS3EncryptionClientBuilder
operator|.
name|standard
argument_list|()
operator|.
name|withClientConfiguration
argument_list|(
name|clientConfiguration
argument_list|)
operator|.
name|withEncryptionMaterials
argument_list|(
name|encryptionMaterialsProvider
argument_list|)
operator|.
name|withCredentials
argument_list|(
operator|new
name|InstanceProfileCredentialsProvider
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|clientBuilder
operator|=
name|AmazonS3ClientBuilder
operator|.
name|standard
argument_list|()
operator|.
name|withClientConfiguration
argument_list|(
name|clientConfiguration
argument_list|)
operator|.
name|withCredentials
argument_list|(
operator|new
name|InstanceProfileCredentialsProvider
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|configuration
operator|.
name|isUseEncryption
argument_list|()
condition|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getRegion
argument_list|()
argument_list|)
condition|)
block|{
name|clientBuilder
operator|=
name|clientBuilder
operator|.
name|withRegion
argument_list|(
name|Regions
operator|.
name|valueOf
argument_list|(
name|configuration
operator|.
name|getRegion
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|clientBuilder
operator|=
name|clientBuilder
operator|.
name|withPathStyleAccessEnabled
argument_list|(
name|configuration
operator|.
name|isPathStyleAccess
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|=
name|clientBuilder
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getRegion
argument_list|()
argument_list|)
condition|)
block|{
name|encClientBuilder
operator|=
name|encClientBuilder
operator|.
name|withRegion
argument_list|(
name|Regions
operator|.
name|valueOf
argument_list|(
name|configuration
operator|.
name|getRegion
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|encClientBuilder
operator|=
name|encClientBuilder
operator|.
name|withPathStyleAccessEnabled
argument_list|(
name|configuration
operator|.
name|isPathStyleAccess
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|=
name|encClientBuilder
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
return|return
name|client
return|;
block|}
block|}
end_class

end_unit

