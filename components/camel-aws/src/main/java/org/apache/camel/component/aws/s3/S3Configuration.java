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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|UriParam
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
name|spi
operator|.
name|UriParams
import|;
end_import

begin_class
annotation|@
name|UriParams
DECL|class|S3Configuration
specifier|public
class|class
name|S3Configuration
implements|implements
name|Cloneable
block|{
DECL|field|bucketName
specifier|private
name|String
name|bucketName
decl_stmt|;
annotation|@
name|UriParam
DECL|field|amazonS3Client
specifier|private
name|AmazonS3
name|amazonS3Client
decl_stmt|;
annotation|@
name|UriParam
DECL|field|accessKey
specifier|private
name|String
name|accessKey
decl_stmt|;
annotation|@
name|UriParam
DECL|field|secretKey
specifier|private
name|String
name|secretKey
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|fileName
specifier|private
name|String
name|fileName
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|prefix
specifier|private
name|String
name|prefix
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|region
specifier|private
name|String
name|region
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|deleteAfterRead
specifier|private
name|boolean
name|deleteAfterRead
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|deleteAfterWrite
specifier|private
name|boolean
name|deleteAfterWrite
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|multiPartUpload
specifier|private
name|boolean
name|multiPartUpload
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|""
operator|+
literal|25
operator|*
literal|1024
operator|*
literal|1024
argument_list|)
DECL|field|partSize
specifier|private
name|long
name|partSize
init|=
literal|25
operator|*
literal|1024
operator|*
literal|1024
decl_stmt|;
annotation|@
name|UriParam
DECL|field|amazonS3Endpoint
specifier|private
name|String
name|amazonS3Endpoint
decl_stmt|;
annotation|@
name|UriParam
DECL|field|policy
specifier|private
name|String
name|policy
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|storageClass
specifier|private
name|String
name|storageClass
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|serverSideEncryption
specifier|private
name|String
name|serverSideEncryption
decl_stmt|;
annotation|@
name|UriParam
DECL|field|proxyHost
specifier|private
name|String
name|proxyHost
decl_stmt|;
annotation|@
name|UriParam
DECL|field|proxyPort
specifier|private
name|Integer
name|proxyPort
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|includeBody
specifier|private
name|boolean
name|includeBody
init|=
literal|true
decl_stmt|;
DECL|method|getPartSize ()
specifier|public
name|long
name|getPartSize
parameter_list|()
block|{
return|return
name|partSize
return|;
block|}
comment|/**      * *Camel 2.15.0*: Setup the partSize which is used in multi part upload, the default size is 25M.      */
DECL|method|setPartSize (long partSize)
specifier|public
name|void
name|setPartSize
parameter_list|(
name|long
name|partSize
parameter_list|)
block|{
name|this
operator|.
name|partSize
operator|=
name|partSize
expr_stmt|;
block|}
DECL|method|isMultiPartUpload ()
specifier|public
name|boolean
name|isMultiPartUpload
parameter_list|()
block|{
return|return
name|multiPartUpload
return|;
block|}
comment|/**      * *Camel 2.15.0*: If it is true, camel will upload the file with multi part format, the part size is decided by the option of `partSize`      */
DECL|method|setMultiPartUpload (boolean multiPartUpload)
specifier|public
name|void
name|setMultiPartUpload
parameter_list|(
name|boolean
name|multiPartUpload
parameter_list|)
block|{
name|this
operator|.
name|multiPartUpload
operator|=
name|multiPartUpload
expr_stmt|;
block|}
comment|/**      * The region with which the AWS-S3 client wants to work with.      */
DECL|method|setAmazonS3Endpoint (String amazonS3Endpoint)
specifier|public
name|void
name|setAmazonS3Endpoint
parameter_list|(
name|String
name|amazonS3Endpoint
parameter_list|)
block|{
name|this
operator|.
name|amazonS3Endpoint
operator|=
name|amazonS3Endpoint
expr_stmt|;
block|}
DECL|method|getAmazonS3Endpoint ()
specifier|public
name|String
name|getAmazonS3Endpoint
parameter_list|()
block|{
return|return
name|amazonS3Endpoint
return|;
block|}
DECL|method|getAccessKey ()
specifier|public
name|String
name|getAccessKey
parameter_list|()
block|{
return|return
name|accessKey
return|;
block|}
comment|/**      * Amazon AWS Access Key      */
DECL|method|setAccessKey (String accessKey)
specifier|public
name|void
name|setAccessKey
parameter_list|(
name|String
name|accessKey
parameter_list|)
block|{
name|this
operator|.
name|accessKey
operator|=
name|accessKey
expr_stmt|;
block|}
DECL|method|getSecretKey ()
specifier|public
name|String
name|getSecretKey
parameter_list|()
block|{
return|return
name|secretKey
return|;
block|}
comment|/**      * Amazon AWS Secret Key      */
DECL|method|setSecretKey (String secretKey)
specifier|public
name|void
name|setSecretKey
parameter_list|(
name|String
name|secretKey
parameter_list|)
block|{
name|this
operator|.
name|secretKey
operator|=
name|secretKey
expr_stmt|;
block|}
DECL|method|getAmazonS3Client ()
specifier|public
name|AmazonS3
name|getAmazonS3Client
parameter_list|()
block|{
return|return
name|amazonS3Client
return|;
block|}
comment|/**      * Reference to a `com.amazonaws.services.sqs.AmazonS3` in the link:registry.html[Registry].      */
DECL|method|setAmazonS3Client (AmazonS3 amazonS3Client)
specifier|public
name|void
name|setAmazonS3Client
parameter_list|(
name|AmazonS3
name|amazonS3Client
parameter_list|)
block|{
name|this
operator|.
name|amazonS3Client
operator|=
name|amazonS3Client
expr_stmt|;
block|}
DECL|method|getPrefix ()
specifier|public
name|String
name|getPrefix
parameter_list|()
block|{
return|return
name|prefix
return|;
block|}
comment|/**      * *Camel 2.10.1*: The prefix which is used in the com.amazonaws.services.s3.model.ListObjectsRequest      * to only consume objects we are interested in.      */
DECL|method|setPrefix (String prefix)
specifier|public
name|void
name|setPrefix
parameter_list|(
name|String
name|prefix
parameter_list|)
block|{
name|this
operator|.
name|prefix
operator|=
name|prefix
expr_stmt|;
block|}
DECL|method|getBucketName ()
specifier|public
name|String
name|getBucketName
parameter_list|()
block|{
return|return
name|bucketName
return|;
block|}
comment|/**      * Name of the bucket. The bucket will be created if it don't already exists.      */
DECL|method|setBucketName (String bucketName)
specifier|public
name|void
name|setBucketName
parameter_list|(
name|String
name|bucketName
parameter_list|)
block|{
name|this
operator|.
name|bucketName
operator|=
name|bucketName
expr_stmt|;
block|}
DECL|method|getFileName ()
specifier|public
name|String
name|getFileName
parameter_list|()
block|{
return|return
name|fileName
return|;
block|}
comment|/**      * To get the object from the bucket with the given file name      */
DECL|method|setFileName (String fileName)
specifier|public
name|void
name|setFileName
parameter_list|(
name|String
name|fileName
parameter_list|)
block|{
name|this
operator|.
name|fileName
operator|=
name|fileName
expr_stmt|;
block|}
DECL|method|getRegion ()
specifier|public
name|String
name|getRegion
parameter_list|()
block|{
return|return
name|region
return|;
block|}
comment|/**      * The region where the bucket is located. This option is used in the      * `com.amazonaws.services.s3.model.CreateBucketRequest`.      */
DECL|method|setRegion (String region)
specifier|public
name|void
name|setRegion
parameter_list|(
name|String
name|region
parameter_list|)
block|{
name|this
operator|.
name|region
operator|=
name|region
expr_stmt|;
block|}
comment|/**      * *Camel 2.17*: If it is true, the exchange body will be set to a stream to the contents of the file.      * If false, the headers will be set with the S3 object metadata, but the body will be null.      */
DECL|method|setIncludeBody (boolean includeBody)
specifier|public
name|void
name|setIncludeBody
parameter_list|(
name|boolean
name|includeBody
parameter_list|)
block|{
name|this
operator|.
name|includeBody
operator|=
name|includeBody
expr_stmt|;
block|}
DECL|method|isIncludeBody ()
specifier|public
name|boolean
name|isIncludeBody
parameter_list|()
block|{
return|return
name|includeBody
return|;
block|}
DECL|method|isDeleteAfterRead ()
specifier|public
name|boolean
name|isDeleteAfterRead
parameter_list|()
block|{
return|return
name|deleteAfterRead
return|;
block|}
comment|/**      * Delete objects from S3 after they have been retrieved.  The delete is only performed if the Exchange is committed.      * If a rollback occurs, the object is not deleted.      *<p/>      * If this option is false, then the same objects will be retrieve over and over again on the polls. Therefore you      * need to use the Idempotent Consumer EIP in the route to filter out duplicates. You can filter using the      * {@link S3Constants#BUCKET_NAME} and {@link S3Constants#KEY} headers, or only the {@link S3Constants#KEY} header.      */
DECL|method|setDeleteAfterRead (boolean deleteAfterRead)
specifier|public
name|void
name|setDeleteAfterRead
parameter_list|(
name|boolean
name|deleteAfterRead
parameter_list|)
block|{
name|this
operator|.
name|deleteAfterRead
operator|=
name|deleteAfterRead
expr_stmt|;
block|}
DECL|method|isDeleteAfterWrite ()
specifier|public
name|boolean
name|isDeleteAfterWrite
parameter_list|()
block|{
return|return
name|deleteAfterWrite
return|;
block|}
comment|/**      * *Camel 2.11.0*: Delete file object after the S3 file has been uploaded      */
DECL|method|setDeleteAfterWrite (boolean deleteAfterWrite)
specifier|public
name|void
name|setDeleteAfterWrite
parameter_list|(
name|boolean
name|deleteAfterWrite
parameter_list|)
block|{
name|this
operator|.
name|deleteAfterWrite
operator|=
name|deleteAfterWrite
expr_stmt|;
block|}
DECL|method|getPolicy ()
specifier|public
name|String
name|getPolicy
parameter_list|()
block|{
return|return
name|policy
return|;
block|}
comment|/**      * *Camel 2.8.4*: The policy for this queue to set in the `com.amazonaws.services.s3.AmazonS3#setBucketPolicy()` method.      */
DECL|method|setPolicy (String policy)
specifier|public
name|void
name|setPolicy
parameter_list|(
name|String
name|policy
parameter_list|)
block|{
name|this
operator|.
name|policy
operator|=
name|policy
expr_stmt|;
block|}
DECL|method|getStorageClass ()
specifier|public
name|String
name|getStorageClass
parameter_list|()
block|{
return|return
name|storageClass
return|;
block|}
comment|/**      * *Camel 2.8.4*: The storage class to set in the `com.amazonaws.services.s3.model.PutObjectRequest` request.      */
DECL|method|setStorageClass (String storageClass)
specifier|public
name|void
name|setStorageClass
parameter_list|(
name|String
name|storageClass
parameter_list|)
block|{
name|this
operator|.
name|storageClass
operator|=
name|storageClass
expr_stmt|;
block|}
DECL|method|getServerSideEncryption ()
specifier|public
name|String
name|getServerSideEncryption
parameter_list|()
block|{
return|return
name|serverSideEncryption
return|;
block|}
comment|/**      * *Camel 2.16*: Sets the server-side encryption algorithm when encrypting the object using AWS-managed keys.      * For example use<tt>AES256</tt>.      */
DECL|method|setServerSideEncryption (String serverSideEncryption)
specifier|public
name|void
name|setServerSideEncryption
parameter_list|(
name|String
name|serverSideEncryption
parameter_list|)
block|{
name|this
operator|.
name|serverSideEncryption
operator|=
name|serverSideEncryption
expr_stmt|;
block|}
DECL|method|getProxyHost ()
specifier|public
name|String
name|getProxyHost
parameter_list|()
block|{
return|return
name|proxyHost
return|;
block|}
comment|/**      * *Camel 2.16*: To define a proxy host when instantiating the SQS client      */
DECL|method|setProxyHost (String proxyHost)
specifier|public
name|void
name|setProxyHost
parameter_list|(
name|String
name|proxyHost
parameter_list|)
block|{
name|this
operator|.
name|proxyHost
operator|=
name|proxyHost
expr_stmt|;
block|}
DECL|method|getProxyPort ()
specifier|public
name|Integer
name|getProxyPort
parameter_list|()
block|{
return|return
name|proxyPort
return|;
block|}
comment|/**      * *Camel 2.16*: Specify a proxy port to be used inside the client definition.      */
DECL|method|setProxyPort (Integer proxyPort)
specifier|public
name|void
name|setProxyPort
parameter_list|(
name|Integer
name|proxyPort
parameter_list|)
block|{
name|this
operator|.
name|proxyPort
operator|=
name|proxyPort
expr_stmt|;
block|}
block|}
end_class

end_unit

