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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|spring
operator|.
name|CamelSpringTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jclouds
operator|.
name|ContextBuilder
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
name|compute
operator|.
name|domain
operator|.
name|Image
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|AbstractApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_class
DECL|class|JcloudsSpringBlobstoreTest
specifier|public
class|class
name|JcloudsSpringBlobstoreTest
extends|extends
name|CamelSpringTestSupport
block|{
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:result-foo"
argument_list|)
DECL|field|resultFoo
specifier|protected
name|MockEndpoint
name|resultFoo
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:result-bar"
argument_list|)
DECL|field|resultBar
specifier|protected
name|MockEndpoint
name|resultBar
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|setUpClass ()
specifier|public
specifier|static
name|void
name|setUpClass
parameter_list|()
throws|throws
name|Exception
block|{
name|BlobStore
name|blobStore
init|=
name|ContextBuilder
operator|.
name|newBuilder
argument_list|(
literal|"transient"
argument_list|)
operator|.
name|credentials
argument_list|(
literal|"id"
argument_list|,
literal|"credential"
argument_list|)
operator|.
name|buildView
argument_list|(
name|BlobStoreContext
operator|.
name|class
argument_list|)
operator|.
name|getBlobStore
argument_list|()
decl_stmt|;
name|blobStore
operator|.
name|createContainerInLocation
argument_list|(
literal|null
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|blobStore
operator|.
name|createContainerInLocation
argument_list|(
literal|null
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"classpath:blobstore-test.xml"
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testBlobStorePut ()
specifier|public
name|void
name|testBlobStorePut
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|resultFoo
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Some message"
argument_list|)
expr_stmt|;
name|resultFoo
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBlobStoreGet ()
specifier|public
name|void
name|testBlobStoreGet
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|resultFoo
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Some message"
argument_list|)
expr_stmt|;
name|resultFoo
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testProduceWithUrlParametes ()
specifier|public
name|void
name|testProduceWithUrlParametes
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|resultBar
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start-with-url-parameters"
argument_list|,
literal|"Some message"
argument_list|)
expr_stmt|;
name|resultBar
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBlobStoreCount ()
specifier|public
name|void
name|testBlobStoreCount
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|Long
name|count
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:count"
argument_list|,
literal|"Some message"
argument_list|,
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Long
argument_list|(
literal|1
argument_list|)
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBlobStoreRemove ()
specifier|public
name|void
name|testBlobStoreRemove
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|Long
name|count
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:remove"
argument_list|,
literal|"Some message"
argument_list|,
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Long
argument_list|(
literal|0
argument_list|)
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBlobStoreClear ()
specifier|public
name|void
name|testBlobStoreClear
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|Long
name|count
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:clear"
argument_list|,
literal|"Some message"
argument_list|,
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Long
argument_list|(
literal|0
argument_list|)
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBlobStoreDelete ()
specifier|public
name|void
name|testBlobStoreDelete
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:delete"
argument_list|,
literal|"Some message"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

