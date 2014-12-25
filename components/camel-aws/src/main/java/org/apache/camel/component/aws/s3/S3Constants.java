begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.s3
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
package|;
end_package

begin_comment
comment|/**  * Constants used in Camel AWS S3 module  *   */
end_comment

begin_interface
DECL|interface|S3Constants
specifier|public
interface|interface
name|S3Constants
block|{
DECL|field|BUCKET_NAME
name|String
name|BUCKET_NAME
init|=
literal|"CamelAwsS3BucketName"
decl_stmt|;
DECL|field|CACHE_CONTROL
name|String
name|CACHE_CONTROL
init|=
literal|"CamelAwsS3ContentControl"
decl_stmt|;
DECL|field|CONTENT_DISPOSITION
name|String
name|CONTENT_DISPOSITION
init|=
literal|"CamelAwsS3ContentDisposition"
decl_stmt|;
DECL|field|CONTENT_ENCODING
name|String
name|CONTENT_ENCODING
init|=
literal|"CamelAwsS3ContentEncoding"
decl_stmt|;
DECL|field|CONTENT_LENGTH
name|String
name|CONTENT_LENGTH
init|=
literal|"CamelAwsS3ContentLength"
decl_stmt|;
DECL|field|CONTENT_MD5
name|String
name|CONTENT_MD5
init|=
literal|"CamelAwsS3ContentMD5"
decl_stmt|;
DECL|field|CONTENT_TYPE
name|String
name|CONTENT_TYPE
init|=
literal|"CamelAwsS3ContentType"
decl_stmt|;
DECL|field|E_TAG
name|String
name|E_TAG
init|=
literal|"CamelAwsS3ETag"
decl_stmt|;
DECL|field|KEY
name|String
name|KEY
init|=
literal|"CamelAwsS3Key"
decl_stmt|;
DECL|field|LAST_MODIFIED
name|String
name|LAST_MODIFIED
init|=
literal|"CamelAwsS3LastModified"
decl_stmt|;
DECL|field|STORAGE_CLASS
name|String
name|STORAGE_CLASS
init|=
literal|"CamelAwsS3StorageClass"
decl_stmt|;
DECL|field|VERSION_ID
name|String
name|VERSION_ID
init|=
literal|"CamelAwsS3VersionId"
decl_stmt|;
DECL|field|CANNED_ACL
name|String
name|CANNED_ACL
init|=
literal|"CamelAwsS3CannedAcl"
decl_stmt|;
DECL|field|ACL
name|String
name|ACL
init|=
literal|"CamelAwsS3Acl"
decl_stmt|;
DECL|field|USER_METADATA
name|String
name|USER_METADATA
init|=
literal|"CamelAwsS3UserMetadata"
decl_stmt|;
DECL|field|S3_HEADERS
name|String
name|S3_HEADERS
init|=
literal|"CamelAwsS3Headers"
decl_stmt|;
block|}
end_interface

end_unit

