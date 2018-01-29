begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.s3.springboot
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
name|springboot
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|S3Operations
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
name|spring
operator|.
name|boot
operator|.
name|ComponentConfigurationPropertiesCommon
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|NestedConfigurationProperty
import|;
end_import

begin_comment
comment|/**  * The aws-s3 component is used for storing and retrieving objecct from Amazon  * S3 Storage Service.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.aws-s3"
argument_list|)
DECL|class|S3ComponentConfiguration
specifier|public
class|class
name|S3ComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * The AWS S3 default configuration      */
DECL|field|configuration
specifier|private
name|S3ConfigurationNestedConfiguration
name|configuration
decl_stmt|;
comment|/**      * Amazon AWS Access Key      */
DECL|field|accessKey
specifier|private
name|String
name|accessKey
decl_stmt|;
comment|/**      * Amazon AWS Secret Key      */
DECL|field|secretKey
specifier|private
name|String
name|secretKey
decl_stmt|;
comment|/**      * The region where the bucket is located. This option is used in the      * com.amazonaws.services.s3.model.CreateBucketRequest.      */
DECL|field|region
specifier|private
name|String
name|region
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
decl_stmt|;
DECL|method|getConfiguration ()
specifier|public
name|S3ConfigurationNestedConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration ( S3ConfigurationNestedConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|S3ConfigurationNestedConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
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
DECL|method|getResolvePropertyPlaceholders ()
specifier|public
name|Boolean
name|getResolvePropertyPlaceholders
parameter_list|()
block|{
return|return
name|resolvePropertyPlaceholders
return|;
block|}
DECL|method|setResolvePropertyPlaceholders ( Boolean resolvePropertyPlaceholders)
specifier|public
name|void
name|setResolvePropertyPlaceholders
parameter_list|(
name|Boolean
name|resolvePropertyPlaceholders
parameter_list|)
block|{
name|this
operator|.
name|resolvePropertyPlaceholders
operator|=
name|resolvePropertyPlaceholders
expr_stmt|;
block|}
DECL|class|S3ConfigurationNestedConfiguration
specifier|public
specifier|static
class|class
name|S3ConfigurationNestedConfiguration
block|{
DECL|field|CAMEL_NESTED_CLASS
specifier|public
specifier|static
specifier|final
name|Class
name|CAMEL_NESTED_CLASS
init|=
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
operator|.
name|class
decl_stmt|;
comment|/**          * Setup the partSize which is used in multi part upload, the default          * size is 25M.          */
DECL|field|partSize
specifier|private
name|Long
name|partSize
init|=
literal|26214400L
decl_stmt|;
comment|/**          * If it is true, camel will upload the file with multi part format, the          * part size is decided by the option of `partSize`          */
DECL|field|multiPartUpload
specifier|private
name|Boolean
name|multiPartUpload
init|=
literal|false
decl_stmt|;
comment|/**          * The region with which the AWS-S3 client wants to work with.          */
DECL|field|amazonS3Endpoint
specifier|private
name|String
name|amazonS3Endpoint
decl_stmt|;
comment|/**          * Amazon AWS Access Key          */
DECL|field|accessKey
specifier|private
name|String
name|accessKey
decl_stmt|;
comment|/**          * Amazon AWS Secret Key          */
DECL|field|secretKey
specifier|private
name|String
name|secretKey
decl_stmt|;
comment|/**          * Reference to a `com.amazonaws.services.sqs.AmazonS3` in the          * link:registry.html[Registry].          */
DECL|field|amazonS3Client
specifier|private
name|AmazonS3
name|amazonS3Client
decl_stmt|;
comment|/**          * The prefix which is used in the          * com.amazonaws.services.s3.model.ListObjectsRequest to only consume          * objects we are interested in.          */
DECL|field|prefix
specifier|private
name|String
name|prefix
decl_stmt|;
comment|/**          * Name of the bucket. The bucket will be created if it doesn't already          * exists.          */
DECL|field|bucketName
specifier|private
name|String
name|bucketName
decl_stmt|;
comment|/**          * To get the object from the bucket with the given file name          */
DECL|field|fileName
specifier|private
name|String
name|fileName
decl_stmt|;
comment|/**          * The region where the bucket is located. This option is used in the          * `com.amazonaws.services.s3.model.CreateBucketRequest`.          */
DECL|field|region
specifier|private
name|String
name|region
decl_stmt|;
comment|/**          * If it is true, the exchange body will be set to a stream to the          * contents of the file. If false, the headers will be set with the S3          * object metadata, but the body will be null. This option is strongly          * related to autocloseBody option. In case of setting includeBody to          * true and autocloseBody to false, it will be up to the caller to close          * the S3Object stream. Setting autocloseBody to true, will close the          * S3Object stream automatically.          */
DECL|field|includeBody
specifier|private
name|Boolean
name|includeBody
init|=
literal|true
decl_stmt|;
comment|/**          * Delete objects from S3 after they have been retrieved. The delete is          * only performed if the Exchange is committed. If a rollback occurs,          * the object is not deleted.          *<p/>          * If this option is false, then the same objects will be retrieve over          * and over again on the polls. Therefore you need to use the Idempotent          * Consumer EIP in the route to filter out duplicates. You can filter          * using the {@link S3Constants#BUCKET_NAME} and {@link S3Constants#KEY}          * headers, or only the {@link S3Constants#KEY} header.          */
DECL|field|deleteAfterRead
specifier|private
name|Boolean
name|deleteAfterRead
init|=
literal|true
decl_stmt|;
comment|/**          * Delete file object after the S3 file has been uploaded          */
DECL|field|deleteAfterWrite
specifier|private
name|Boolean
name|deleteAfterWrite
init|=
literal|false
decl_stmt|;
comment|/**          * The policy for this queue to set in the          * `com.amazonaws.services.s3.AmazonS3#setBucketPolicy()` method.          */
DECL|field|policy
specifier|private
name|String
name|policy
decl_stmt|;
comment|/**          * The storage class to set in the          * `com.amazonaws.services.s3.model.PutObjectRequest` request.          */
DECL|field|storageClass
specifier|private
name|String
name|storageClass
decl_stmt|;
comment|/**          * Sets the server-side encryption algorithm when encrypting the object          * using AWS-managed keys. For example use<tt>AES256</tt>.          */
DECL|field|serverSideEncryption
specifier|private
name|String
name|serverSideEncryption
decl_stmt|;
comment|/**          * To define a proxy host when instantiating the SQS client          */
DECL|field|proxyHost
specifier|private
name|String
name|proxyHost
decl_stmt|;
comment|/**          * Specify a proxy port to be used inside the client definition.          */
DECL|field|proxyPort
specifier|private
name|Integer
name|proxyPort
decl_stmt|;
comment|/**          * Whether or not the S3 client should use path style access          */
DECL|field|pathStyleAccess
specifier|private
name|Boolean
name|pathStyleAccess
init|=
literal|false
decl_stmt|;
comment|/**          * The operation to do in case the user don't want to do only an upload          */
DECL|field|operation
specifier|private
name|S3Operations
name|operation
decl_stmt|;
comment|/**          * If this option is true and includeBody is true, then the          * S3Object.close() method will be called on exchange completion. This          * option is strongly related to includeBody option. In case of setting          * includeBody to true and autocloseBody to false, it will be up to the          * caller to close the S3Object stream. Setting autocloseBody to true,          * will close the S3Object stream automatically.          */
DECL|field|autocloseBody
specifier|private
name|Boolean
name|autocloseBody
init|=
literal|true
decl_stmt|;
comment|/**          * The encryption materials to use in case of Symmetric/Asymmetric          * client usage          */
annotation|@
name|NestedConfigurationProperty
DECL|field|encryptionMaterials
specifier|private
name|EncryptionMaterials
name|encryptionMaterials
decl_stmt|;
comment|/**          * Define if encryption must be used or not          */
DECL|field|useEncryption
specifier|private
name|Boolean
name|useEncryption
init|=
literal|false
decl_stmt|;
comment|/**          * Define if KMS must be used or not          */
DECL|field|useAwsKMS
specifier|private
name|Boolean
name|useAwsKMS
init|=
literal|false
decl_stmt|;
comment|/**          * Define the id of KMS key to use in case KMS is enabled          */
DECL|field|awsKMSKeyId
specifier|private
name|String
name|awsKMSKeyId
decl_stmt|;
comment|/**          * Define if disabled Chunked Encoding is true or false          */
DECL|field|chunkedEncodingDisabled
specifier|private
name|Boolean
name|chunkedEncodingDisabled
init|=
literal|false
decl_stmt|;
DECL|method|getPartSize ()
specifier|public
name|Long
name|getPartSize
parameter_list|()
block|{
return|return
name|partSize
return|;
block|}
DECL|method|setPartSize (Long partSize)
specifier|public
name|void
name|setPartSize
parameter_list|(
name|Long
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
DECL|method|getMultiPartUpload ()
specifier|public
name|Boolean
name|getMultiPartUpload
parameter_list|()
block|{
return|return
name|multiPartUpload
return|;
block|}
DECL|method|setMultiPartUpload (Boolean multiPartUpload)
specifier|public
name|void
name|setMultiPartUpload
parameter_list|(
name|Boolean
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
DECL|method|getIncludeBody ()
specifier|public
name|Boolean
name|getIncludeBody
parameter_list|()
block|{
return|return
name|includeBody
return|;
block|}
DECL|method|setIncludeBody (Boolean includeBody)
specifier|public
name|void
name|setIncludeBody
parameter_list|(
name|Boolean
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
DECL|method|getDeleteAfterRead ()
specifier|public
name|Boolean
name|getDeleteAfterRead
parameter_list|()
block|{
return|return
name|deleteAfterRead
return|;
block|}
DECL|method|setDeleteAfterRead (Boolean deleteAfterRead)
specifier|public
name|void
name|setDeleteAfterRead
parameter_list|(
name|Boolean
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
DECL|method|getDeleteAfterWrite ()
specifier|public
name|Boolean
name|getDeleteAfterWrite
parameter_list|()
block|{
return|return
name|deleteAfterWrite
return|;
block|}
DECL|method|setDeleteAfterWrite (Boolean deleteAfterWrite)
specifier|public
name|void
name|setDeleteAfterWrite
parameter_list|(
name|Boolean
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
DECL|method|getPathStyleAccess ()
specifier|public
name|Boolean
name|getPathStyleAccess
parameter_list|()
block|{
return|return
name|pathStyleAccess
return|;
block|}
DECL|method|setPathStyleAccess (Boolean pathStyleAccess)
specifier|public
name|void
name|setPathStyleAccess
parameter_list|(
name|Boolean
name|pathStyleAccess
parameter_list|)
block|{
name|this
operator|.
name|pathStyleAccess
operator|=
name|pathStyleAccess
expr_stmt|;
block|}
DECL|method|getOperation ()
specifier|public
name|S3Operations
name|getOperation
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
DECL|method|setOperation (S3Operations operation)
specifier|public
name|void
name|setOperation
parameter_list|(
name|S3Operations
name|operation
parameter_list|)
block|{
name|this
operator|.
name|operation
operator|=
name|operation
expr_stmt|;
block|}
DECL|method|getAutocloseBody ()
specifier|public
name|Boolean
name|getAutocloseBody
parameter_list|()
block|{
return|return
name|autocloseBody
return|;
block|}
DECL|method|setAutocloseBody (Boolean autocloseBody)
specifier|public
name|void
name|setAutocloseBody
parameter_list|(
name|Boolean
name|autocloseBody
parameter_list|)
block|{
name|this
operator|.
name|autocloseBody
operator|=
name|autocloseBody
expr_stmt|;
block|}
DECL|method|getEncryptionMaterials ()
specifier|public
name|EncryptionMaterials
name|getEncryptionMaterials
parameter_list|()
block|{
return|return
name|encryptionMaterials
return|;
block|}
DECL|method|setEncryptionMaterials ( EncryptionMaterials encryptionMaterials)
specifier|public
name|void
name|setEncryptionMaterials
parameter_list|(
name|EncryptionMaterials
name|encryptionMaterials
parameter_list|)
block|{
name|this
operator|.
name|encryptionMaterials
operator|=
name|encryptionMaterials
expr_stmt|;
block|}
DECL|method|getUseEncryption ()
specifier|public
name|Boolean
name|getUseEncryption
parameter_list|()
block|{
return|return
name|useEncryption
return|;
block|}
DECL|method|setUseEncryption (Boolean useEncryption)
specifier|public
name|void
name|setUseEncryption
parameter_list|(
name|Boolean
name|useEncryption
parameter_list|)
block|{
name|this
operator|.
name|useEncryption
operator|=
name|useEncryption
expr_stmt|;
block|}
DECL|method|getUseAwsKMS ()
specifier|public
name|Boolean
name|getUseAwsKMS
parameter_list|()
block|{
return|return
name|useAwsKMS
return|;
block|}
DECL|method|setUseAwsKMS (Boolean useAwsKMS)
specifier|public
name|void
name|setUseAwsKMS
parameter_list|(
name|Boolean
name|useAwsKMS
parameter_list|)
block|{
name|this
operator|.
name|useAwsKMS
operator|=
name|useAwsKMS
expr_stmt|;
block|}
DECL|method|getAwsKMSKeyId ()
specifier|public
name|String
name|getAwsKMSKeyId
parameter_list|()
block|{
return|return
name|awsKMSKeyId
return|;
block|}
DECL|method|setAwsKMSKeyId (String awsKMSKeyId)
specifier|public
name|void
name|setAwsKMSKeyId
parameter_list|(
name|String
name|awsKMSKeyId
parameter_list|)
block|{
name|this
operator|.
name|awsKMSKeyId
operator|=
name|awsKMSKeyId
expr_stmt|;
block|}
DECL|method|getChunkedEncodingDisabled ()
specifier|public
name|Boolean
name|getChunkedEncodingDisabled
parameter_list|()
block|{
return|return
name|chunkedEncodingDisabled
return|;
block|}
DECL|method|setChunkedEncodingDisabled (Boolean chunkedEncodingDisabled)
specifier|public
name|void
name|setChunkedEncodingDisabled
parameter_list|(
name|Boolean
name|chunkedEncodingDisabled
parameter_list|)
block|{
name|this
operator|.
name|chunkedEncodingDisabled
operator|=
name|chunkedEncodingDisabled
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

