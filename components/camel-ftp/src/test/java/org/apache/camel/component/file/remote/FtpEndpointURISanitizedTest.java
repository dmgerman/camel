begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file.remote
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
operator|.
name|remote
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
name|Consumer
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
name|Endpoint
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
name|Producer
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
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|equalTo
import|;
end_import

begin_comment
comment|/**  * Test to ensure the FtpEndpoint URI is sanitized.  */
end_comment

begin_class
DECL|class|FtpEndpointURISanitizedTest
specifier|public
class|class
name|FtpEndpointURISanitizedTest
extends|extends
name|FtpServerTestSupport
block|{
DECL|field|password
specifier|private
name|String
name|password
init|=
literal|"secret"
decl_stmt|;
DECL|method|getFtpUrl ()
specifier|protected
name|String
name|getFtpUrl
parameter_list|()
block|{
return|return
literal|"ftp://admin@localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/////foo?password="
operator|+
name|password
operator|+
literal|"&delay=5000"
return|;
block|}
annotation|@
name|Test
DECL|method|testFtpDirectoryRelative ()
specifier|public
name|void
name|testFtpDirectoryRelative
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|getFtpUrl
argument_list|()
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
operator|(
operator|(
name|FtpEndpoint
operator|)
name|endpoint
operator|)
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getDirectoryName
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFtpConsumerUriSanitized ()
specifier|public
name|void
name|testFtpConsumerUriSanitized
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|getFtpUrl
argument_list|()
argument_list|)
decl_stmt|;
name|Consumer
name|consumer
init|=
name|endpoint
operator|.
name|createConsumer
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|consumer
operator|.
name|toString
argument_list|()
operator|.
name|contains
argument_list|(
name|password
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFtpProducerUriSanitized ()
specifier|public
name|void
name|testFtpProducerUriSanitized
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|getFtpUrl
argument_list|()
argument_list|)
decl_stmt|;
name|Producer
name|producer
init|=
name|endpoint
operator|.
name|createProducer
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|producer
operator|.
name|toString
argument_list|()
operator|.
name|contains
argument_list|(
name|password
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

