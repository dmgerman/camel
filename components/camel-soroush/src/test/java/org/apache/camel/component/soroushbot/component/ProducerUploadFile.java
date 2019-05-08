begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.soroushbot.component
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|soroushbot
operator|.
name|component
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
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
name|RoutesBuilder
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
name|component
operator|.
name|soroushbot
operator|.
name|models
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
name|component
operator|.
name|soroushbot
operator|.
name|models
operator|.
name|MinorType
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
name|soroushbot
operator|.
name|models
operator|.
name|SoroushMessage
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
name|soroushbot
operator|.
name|support
operator|.
name|SoroushBotTestSupport
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
name|soroushbot
operator|.
name|support
operator|.
name|SoroushBotWS
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
DECL|class|ProducerUploadFile
specifier|public
class|class
name|ProducerUploadFile
extends|extends
name|SoroushBotTestSupport
block|{
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"direct:soroush"
argument_list|)
DECL|field|endpoint
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Endpoint
name|endpoint
decl_stmt|;
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|SoroushBotWS
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RoutesBuilder
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
name|from
argument_list|(
literal|"direct:soroush"
argument_list|)
operator|.
name|to
argument_list|(
literal|"soroush://"
operator|+
name|Endpoint
operator|.
name|uploadFile
operator|+
literal|"/token"
argument_list|)
operator|.
name|process
argument_list|(
name|exchange
lambda|->
block|{
name|SoroushMessage
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|SoroushMessage
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|body
operator|.
name|getFileUrl
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
literal|"file url is null"
argument_list|)
throw|;
block|}
if|if
condition|(
name|body
operator|.
name|getThumbnailUrl
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
literal|"thumb url is null"
argument_list|)
throw|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:soroush"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|autoUploadTest ()
specifier|public
name|void
name|autoUploadTest
parameter_list|()
throws|throws
name|Exception
block|{
name|SoroushMessage
name|body
init|=
operator|new
name|SoroushMessage
argument_list|()
decl_stmt|;
name|body
operator|.
name|setType
argument_list|(
name|MinorType
operator|.
name|TEXT
argument_list|)
expr_stmt|;
name|body
operator|.
name|setFrom
argument_list|(
literal|"b1"
argument_list|)
expr_stmt|;
name|body
operator|.
name|setTo
argument_list|(
literal|"u1"
argument_list|)
expr_stmt|;
name|String
name|fileContent
init|=
literal|"hello"
decl_stmt|;
name|String
name|thumbContent
init|=
literal|"world"
decl_stmt|;
name|body
operator|.
name|setFile
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|fileContent
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|body
operator|.
name|setThumbnail
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|thumbContent
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|context
argument_list|()
operator|.
name|createProducerTemplate
argument_list|()
operator|.
name|sendBody
argument_list|(
name|endpoint
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mockEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:soroush"
argument_list|)
decl_stmt|;
name|mockEndpoint
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"no message sent."
argument_list|,
name|SoroushBotWS
operator|.
name|getReceivedMessages
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|SoroushMessage
name|mockedMessage
init|=
name|mockEndpoint
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|SoroushMessage
operator|.
name|class
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|fileIdToContent
init|=
name|SoroushBotWS
operator|.
name|getFileIdToContent
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"file uploaded successfully"
argument_list|,
name|fileIdToContent
operator|.
name|size
argument_list|()
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|fileIdToContent
operator|.
name|get
argument_list|(
name|mockedMessage
operator|.
name|getFileUrl
argument_list|()
argument_list|)
argument_list|,
name|fileContent
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|fileIdToContent
operator|.
name|get
argument_list|(
name|mockedMessage
operator|.
name|getThumbnailUrl
argument_list|()
argument_list|)
argument_list|,
name|thumbContent
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

