begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.restlet
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|restlet
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
name|builder
operator|.
name|RouteBuilder
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
DECL|class|RestletPostXmlRouteAndJSONAsReturnTest
specifier|public
class|class
name|RestletPostXmlRouteAndJSONAsReturnTest
extends|extends
name|RestletTestSupport
block|{
DECL|field|REQUEST_MESSAGE
specifier|private
specifier|static
specifier|final
name|String
name|REQUEST_MESSAGE
init|=
literal|"<mail><body>HelloWorld!</body><subject>test</subject><to>x@y.net</to></mail>"
decl_stmt|;
DECL|field|REQUEST_MESSAGE_WITH_XML_TAG
specifier|private
specifier|static
specifier|final
name|String
name|REQUEST_MESSAGE_WITH_XML_TAG
init|=
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
operator|+
name|REQUEST_MESSAGE
decl_stmt|;
DECL|field|url
specifier|private
name|String
name|url
init|=
literal|"restlet:http://localhost:"
operator|+
name|portNum
operator|+
literal|"/users?restletMethod=POST"
decl_stmt|;
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
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
comment|// enable POST support
name|from
argument_list|(
name|url
argument_list|)
operator|.
name|process
argument_list|(
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
name|String
name|body
init|=
name|exchange
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
decl_stmt|;
name|assertNotNull
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Get a wrong request message"
argument_list|,
name|body
operator|.
name|indexOf
argument_list|(
name|REQUEST_MESSAGE
argument_list|)
operator|>=
literal|0
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"{OK}"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"application/json"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// route to restlet
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|testPostXml ()
specifier|public
name|void
name|testPostXml
parameter_list|()
throws|throws
name|Exception
block|{
name|postRequestMessage
argument_list|(
name|REQUEST_MESSAGE
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPostXmlWithXmlTag ()
specifier|public
name|void
name|testPostXmlWithXmlTag
parameter_list|()
throws|throws
name|Exception
block|{
name|postRequestMessage
argument_list|(
name|REQUEST_MESSAGE_WITH_XML_TAG
argument_list|)
expr_stmt|;
block|}
DECL|method|postRequestMessage (final String message)
specifier|protected
name|void
name|postRequestMessage
parameter_list|(
specifier|final
name|String
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
literal|"direct:start"
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
name|setBody
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"application/json"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|exchange
operator|.
name|hasOut
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|s
init|=
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"{OK}"
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

