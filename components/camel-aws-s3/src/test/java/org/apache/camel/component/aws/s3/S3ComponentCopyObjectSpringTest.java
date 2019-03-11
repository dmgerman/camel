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
name|ClassPathXmlApplicationContext
import|;
end_import

begin_class
DECL|class|S3ComponentCopyObjectSpringTest
specifier|public
class|class
name|S3ComponentCopyObjectSpringTest
extends|extends
name|CamelSpringTestSupport
block|{
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
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
name|uri
operator|=
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
name|template
operator|.
name|send
argument_list|(
literal|"direct:copyObject"
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
name|BUCKET_DESTINATION_NAME
argument_list|,
literal|"camelDestinationBucket"
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
literal|"camelKey"
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
name|DESTINATION_KEY
argument_list|,
literal|"camelDestinationKey"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
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
name|assertEquals
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
argument_list|,
literal|"11192828ahsh2723"
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
name|LAST_MODIFIED
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
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
argument_list|,
literal|"3a5c8b1ad448bca04584ecb55b836264"
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
name|USER_METADATA
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|ClassPathXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/component/aws/s3/S3ComponentSpringTest-context.xml"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

