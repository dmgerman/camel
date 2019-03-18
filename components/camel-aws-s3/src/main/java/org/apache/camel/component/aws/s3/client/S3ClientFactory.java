begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.s3.client
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
package|;
end_package

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
name|impl
operator|.
name|S3ClientIAMOptimizedImpl
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
name|impl
operator|.
name|S3ClientStandardImpl
import|;
end_import

begin_comment
comment|/**  * Factory class to return the correct type of AWS S3 aws.  */
end_comment

begin_class
DECL|class|S3ClientFactory
specifier|public
specifier|final
class|class
name|S3ClientFactory
block|{
DECL|method|S3ClientFactory ()
specifier|private
name|S3ClientFactory
parameter_list|()
block|{
comment|// Prevent instantiation of this factory class.
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Do not instantiate a Factory class! Refer to the class "
operator|+
literal|"to learn how to properly use this factory implementation."
argument_list|)
throw|;
block|}
comment|/**      * Return the correct aws s3 client (based on remote vs local).      * @param maxConnections max connections      * @return AWSS3Client      */
DECL|method|getAWSS3Client (S3Configuration configuration, int maxConnections)
specifier|public
specifier|static
name|S3Client
name|getAWSS3Client
parameter_list|(
name|S3Configuration
name|configuration
parameter_list|,
name|int
name|maxConnections
parameter_list|)
block|{
return|return
name|configuration
operator|.
name|isUseIAMCredentials
argument_list|()
condition|?
operator|new
name|S3ClientIAMOptimizedImpl
argument_list|(
name|configuration
argument_list|,
name|maxConnections
argument_list|)
else|:
operator|new
name|S3ClientStandardImpl
argument_list|(
name|configuration
argument_list|,
name|maxConnections
argument_list|)
return|;
block|}
block|}
end_class

end_unit

