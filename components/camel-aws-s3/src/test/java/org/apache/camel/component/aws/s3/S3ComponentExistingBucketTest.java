begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|PutObjectRequest
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
name|BindToRegistry
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
name|EndpointInject
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
name|Exchange
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
name|ExchangePattern
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
name|Message
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
name|Processor
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
name|ProducerTemplate
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
name|builder
operator|.
name|RouteBuilder
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
name|mock
operator|.
name|MockEndpoint
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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

begin_class
DECL|class|S3ComponentExistingBucketTest
specifier|public
class|class
name|S3ComponentExistingBucketTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|BindToRegistry
argument_list|(
literal|"amazonS3Client"
argument_list|)
DECL|field|clientMock
name|AmazonS3ClientMock
name|clientMock
init|=
operator|new
name|AmazonS3ClientMock
argument_list|()
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"direct:start"
argument_list|)
DECL|field|template
specifier|private
name|ProducerTemplate
name|template
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"mock:result"
argument_list|)
DECL|field|result
specifier|private
name|MockEndpoint
name|result
decl_stmt|;
annotation|@
name|Test
DECL|method|sendIn ()
specifier|public
name|void
name|sendIn
parameter_list|()
throws|throws
name|Exception
block|{
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
name|ExchangePattern
operator|.
name|InOnly
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|S3Constants
operator|.
name|KEY
argument_list|,
literal|"CamelUnitTest"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"This is my bucket content."
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertResultExchange
argument_list|(
name|result
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|PutObjectRequest
name|putObjectRequest
init|=
name|clientMock
operator|.
name|putObjectRequests
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"REDUCED_REDUNDANCY"
argument_list|,
name|putObjectRequest
operator|.
name|getStorageClass
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"mycamelbucket"
argument_list|,
name|putObjectRequest
operator|.
name|getBucketName
argument_list|()
argument_list|)
expr_stmt|;
name|assertResponseMessage
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|sendInOut ()
specifier|public
name|void
name|sendInOut
parameter_list|()
throws|throws
name|Exception
block|{
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|S3Constants
operator|.
name|KEY
argument_list|,
literal|"CamelUnitTest"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"This is my bucket content."
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertResultExchange
argument_list|(
name|result
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|PutObjectRequest
name|putObjectRequest
init|=
name|clientMock
operator|.
name|putObjectRequests
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"REDUCED_REDUNDANCY"
argument_list|,
name|putObjectRequest
operator|.
name|getStorageClass
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"mycamelbucket"
argument_list|,
name|putObjectRequest
operator|.
name|getBucketName
argument_list|()
argument_list|)
expr_stmt|;
name|assertResponseMessage
argument_list|(
name|exchange
operator|.
name|getOut
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|sendCustomHeaderValues ()
specifier|public
name|void
name|sendCustomHeaderValues
parameter_list|()
throws|throws
name|Exception
block|{
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
specifier|final
name|Date
name|now
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|userMetadata
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|userMetadata
operator|.
name|put
argument_list|(
literal|"CamelName"
argument_list|,
literal|"Camel"
argument_list|)
expr_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|s3Headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|s3Headers
operator|.
name|put
argument_list|(
literal|"x-aws-s3-header"
argument_list|,
literal|"extra"
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
name|ExchangePattern
operator|.
name|InOnly
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|S3Constants
operator|.
name|STORAGE_CLASS
argument_list|,
literal|"STANDARD"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|S3Constants
operator|.
name|KEY
argument_list|,
literal|"CamelUnitTest"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|S3Constants
operator|.
name|CONTENT_LENGTH
argument_list|,
literal|26L
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|S3Constants
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"text/html"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|S3Constants
operator|.
name|CACHE_CONTROL
argument_list|,
literal|"no-cache"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|S3Constants
operator|.
name|CONTENT_DISPOSITION
argument_list|,
literal|"attachment;"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|S3Constants
operator|.
name|CONTENT_ENCODING
argument_list|,
literal|"gzip"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|S3Constants
operator|.
name|CONTENT_MD5
argument_list|,
literal|"TWF"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|S3Constants
operator|.
name|LAST_MODIFIED
argument_list|,
name|now
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|S3Constants
operator|.
name|USER_METADATA
argument_list|,
name|userMetadata
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|S3Constants
operator|.
name|S3_HEADERS
argument_list|,
name|s3Headers
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"This is my bucket content."
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertResultExchange
argument_list|(
name|result
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|PutObjectRequest
name|putObjectRequest
init|=
name|clientMock
operator|.
name|putObjectRequests
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"STANDARD"
argument_list|,
name|putObjectRequest
operator|.
name|getStorageClass
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"mycamelbucket"
argument_list|,
name|putObjectRequest
operator|.
name|getBucketName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|26L
argument_list|,
name|putObjectRequest
operator|.
name|getMetadata
argument_list|()
operator|.
name|getContentLength
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"text/html"
argument_list|,
name|putObjectRequest
operator|.
name|getMetadata
argument_list|()
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"no-cache"
argument_list|,
name|putObjectRequest
operator|.
name|getMetadata
argument_list|()
operator|.
name|getCacheControl
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"attachment;"
argument_list|,
name|putObjectRequest
operator|.
name|getMetadata
argument_list|()
operator|.
name|getContentDisposition
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"gzip"
argument_list|,
name|putObjectRequest
operator|.
name|getMetadata
argument_list|()
operator|.
name|getContentEncoding
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"TWF"
argument_list|,
name|putObjectRequest
operator|.
name|getMetadata
argument_list|()
operator|.
name|getContentMD5
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|now
argument_list|,
name|putObjectRequest
operator|.
name|getMetadata
argument_list|()
operator|.
name|getLastModified
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|userMetadata
argument_list|,
name|putObjectRequest
operator|.
name|getMetadata
argument_list|()
operator|.
name|getUserMetadata
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"extra"
argument_list|,
name|putObjectRequest
operator|.
name|getMetadata
argument_list|()
operator|.
name|getRawMetadataValue
argument_list|(
literal|"x-aws-s3-header"
argument_list|)
argument_list|)
expr_stmt|;
name|assertResponseMessage
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|assertResultExchange (Exchange resultExchange)
specifier|private
name|void
name|assertResultExchange
parameter_list|(
name|Exchange
name|resultExchange
parameter_list|)
block|{
name|assertIsInstanceOf
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"This is my bucket content."
argument_list|,
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"mycamelbucket"
argument_list|,
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|S3Constants
operator|.
name|BUCKET_NAME
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CamelUnitTest"
argument_list|,
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|S3Constants
operator|.
name|KEY
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|S3Constants
operator|.
name|VERSION_ID
argument_list|)
argument_list|)
expr_stmt|;
comment|// not
comment|// enabled
comment|// on
comment|// this
comment|// bucket
name|assertNull
argument_list|(
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|S3Constants
operator|.
name|LAST_MODIFIED
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|S3Constants
operator|.
name|E_TAG
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|S3Constants
operator|.
name|CONTENT_TYPE
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|S3Constants
operator|.
name|CONTENT_ENCODING
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0L
argument_list|,
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|S3Constants
operator|.
name|CONTENT_LENGTH
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|S3Constants
operator|.
name|CONTENT_DISPOSITION
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|S3Constants
operator|.
name|CONTENT_MD5
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|S3Constants
operator|.
name|CACHE_CONTROL
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|S3Constants
operator|.
name|USER_METADATA
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|S3Constants
operator|.
name|S3_HEADERS
argument_list|,
name|Map
operator|.
name|class
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|assertResponseMessage (Message message)
specifier|private
name|void
name|assertResponseMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"3a5c8b1ad448bca04584ecb55b836264"
argument_list|,
name|message
operator|.
name|getHeader
argument_list|(
name|S3Constants
operator|.
name|E_TAG
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|message
operator|.
name|getHeader
argument_list|(
name|S3Constants
operator|.
name|VERSION_ID
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|awsEndpoint
init|=
literal|"aws-s3://mycamelbucket?amazonS3Client=#amazonS3Client"
decl_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
name|awsEndpoint
operator|+
literal|"&storageClass=REDUCED_REDUNDANCY"
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|awsEndpoint
operator|+
literal|"&maxMessagesPerPoll=5"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

