begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jclouds
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jclouds
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Lists
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
name|jclouds
operator|.
name|blobstore
operator|.
name|BlobStore
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jclouds
operator|.
name|blobstore
operator|.
name|BlobStoreContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jclouds
operator|.
name|blobstore
operator|.
name|BlobStoreContextFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jclouds
operator|.
name|io
operator|.
name|payloads
operator|.
name|StringPayload
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
DECL|class|JcloudsBlobStoreConsumerTest
specifier|public
class|class
name|JcloudsBlobStoreConsumerTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|TEST_CONTAINER
specifier|private
specifier|static
specifier|final
name|String
name|TEST_CONTAINER
init|=
literal|"testContainer"
decl_stmt|;
DECL|field|TEST_BLOB1
specifier|private
specifier|static
specifier|final
name|String
name|TEST_BLOB1
init|=
literal|"testBlob1"
decl_stmt|;
DECL|field|TEST_BLOB2
specifier|private
specifier|static
specifier|final
name|String
name|TEST_BLOB2
init|=
literal|"testBlob2"
decl_stmt|;
DECL|field|TEST_CONTAINER_WITH_DIR
specifier|private
specifier|static
specifier|final
name|String
name|TEST_CONTAINER_WITH_DIR
init|=
literal|"testContainerWithDirectories"
decl_stmt|;
DECL|field|TEST_BLOB_IN_DIR
specifier|private
specifier|static
specifier|final
name|String
name|TEST_BLOB_IN_DIR
init|=
literal|"dir/testBlob"
decl_stmt|;
DECL|field|TEST_BLOB_IN_OTHER
specifier|private
specifier|static
specifier|final
name|String
name|TEST_BLOB_IN_OTHER
init|=
literal|"other/testBlob"
decl_stmt|;
DECL|field|contextFactory
name|BlobStoreContextFactory
name|contextFactory
init|=
operator|new
name|BlobStoreContextFactory
argument_list|()
decl_stmt|;
DECL|field|blobStoreContext
name|BlobStoreContext
name|blobStoreContext
init|=
name|contextFactory
operator|.
name|createContext
argument_list|(
literal|"transient"
argument_list|,
literal|"identity"
argument_list|,
literal|"credential"
argument_list|)
decl_stmt|;
DECL|field|blobStore
name|BlobStore
name|blobStore
init|=
name|blobStoreContext
operator|.
name|getBlobStore
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testBlobStoreGetOneBlob ()
specifier|public
name|void
name|testBlobStoreGetOneBlob
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|String
name|message
init|=
literal|"Some message"
decl_stmt|;
name|JcloudsBlobStoreHelper
operator|.
name|writeBlob
argument_list|(
name|blobStore
argument_list|,
name|TEST_CONTAINER
argument_list|,
name|TEST_BLOB1
argument_list|,
operator|new
name|StringPayload
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mockEndpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:results"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|mockEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBlobStoreGetTwoBlobs ()
specifier|public
name|void
name|testBlobStoreGetTwoBlobs
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|String
name|message1
init|=
literal|"Blob 1"
decl_stmt|;
name|JcloudsBlobStoreHelper
operator|.
name|writeBlob
argument_list|(
name|blobStore
argument_list|,
name|TEST_CONTAINER
argument_list|,
name|TEST_BLOB1
argument_list|,
operator|new
name|StringPayload
argument_list|(
name|message1
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|message2
init|=
literal|"Blob 2"
decl_stmt|;
name|JcloudsBlobStoreHelper
operator|.
name|writeBlob
argument_list|(
name|blobStore
argument_list|,
name|TEST_CONTAINER
argument_list|,
name|TEST_BLOB2
argument_list|,
operator|new
name|StringPayload
argument_list|(
name|message2
argument_list|)
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mockEndpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:results"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|mockEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|message1
argument_list|,
name|message2
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBlobStoreWithDirectory ()
specifier|public
name|void
name|testBlobStoreWithDirectory
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|String
name|message1
init|=
literal|"Blob in directory"
decl_stmt|;
name|JcloudsBlobStoreHelper
operator|.
name|writeBlob
argument_list|(
name|blobStore
argument_list|,
name|TEST_CONTAINER_WITH_DIR
argument_list|,
name|TEST_BLOB_IN_DIR
argument_list|,
operator|new
name|StringPayload
argument_list|(
name|message1
argument_list|)
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mockEndpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:results-in-dir"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|mockEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|message1
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBlobStoreWithMultipleDirectories ()
specifier|public
name|void
name|testBlobStoreWithMultipleDirectories
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|String
name|message1
init|=
literal|"Blob in directory"
decl_stmt|;
name|String
name|message2
init|=
literal|"Blob in other directory"
decl_stmt|;
name|JcloudsBlobStoreHelper
operator|.
name|writeBlob
argument_list|(
name|blobStore
argument_list|,
name|TEST_CONTAINER_WITH_DIR
argument_list|,
name|TEST_BLOB_IN_DIR
argument_list|,
operator|new
name|StringPayload
argument_list|(
name|message1
argument_list|)
argument_list|)
expr_stmt|;
name|JcloudsBlobStoreHelper
operator|.
name|writeBlob
argument_list|(
name|blobStore
argument_list|,
name|TEST_CONTAINER_WITH_DIR
argument_list|,
name|TEST_BLOB_IN_OTHER
argument_list|,
operator|new
name|StringPayload
argument_list|(
name|message2
argument_list|)
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mockEndpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:results-in-dir"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|mockEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|message1
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
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
name|blobStore
operator|.
name|createContainerInLocation
argument_list|(
literal|null
argument_list|,
name|TEST_CONTAINER
argument_list|)
expr_stmt|;
name|blobStore
operator|.
name|createContainerInLocation
argument_list|(
literal|null
argument_list|,
name|TEST_CONTAINER_WITH_DIR
argument_list|)
expr_stmt|;
operator|(
operator|(
name|JcloudsComponent
operator|)
name|context
operator|.
name|getComponent
argument_list|(
literal|"jclouds"
argument_list|)
operator|)
operator|.
name|setBlobStores
argument_list|(
name|Lists
operator|.
name|newArrayList
argument_list|(
name|blobStore
argument_list|)
argument_list|)
expr_stmt|;
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"jclouds:blobstore:transient?container="
operator|+
name|TEST_CONTAINER
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:results"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jclouds:blobstore:transient?container="
operator|+
name|TEST_CONTAINER_WITH_DIR
operator|+
literal|"&directory=dir"
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:results-in-dir"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

