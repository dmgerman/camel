begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http4
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http4
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
name|component
operator|.
name|http4
operator|.
name|handler
operator|.
name|BasicValidationHandler
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

begin_comment
comment|/**  *  * @version   */
end_comment

begin_class
DECL|class|HttpMethodsTest
specifier|public
class|class
name|HttpMethodsTest
extends|extends
name|BaseHttpTest
block|{
annotation|@
name|Test
DECL|method|httpGet ()
specifier|public
name|void
name|httpGet
parameter_list|()
throws|throws
name|Exception
block|{
name|localServer
operator|.
name|register
argument_list|(
literal|"/"
argument_list|,
operator|new
name|BasicValidationHandler
argument_list|(
literal|"GET"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|getExpectedContent
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
literal|"http4://"
operator|+
name|getHostName
argument_list|()
operator|+
literal|":"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/"
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
block|{             }
block|}
argument_list|)
decl_stmt|;
name|assertExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|httpPost ()
specifier|public
name|void
name|httpPost
parameter_list|()
throws|throws
name|Exception
block|{
name|localServer
operator|.
name|register
argument_list|(
literal|"/"
argument_list|,
operator|new
name|BasicValidationHandler
argument_list|(
literal|"POST"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|getExpectedContent
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
literal|"http4://"
operator|+
name|getHostName
argument_list|()
operator|+
literal|":"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/"
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
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
literal|"POST"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|httpPostWithBody ()
specifier|public
name|void
name|httpPostWithBody
parameter_list|()
throws|throws
name|Exception
block|{
name|localServer
operator|.
name|register
argument_list|(
literal|"/"
argument_list|,
operator|new
name|BasicValidationHandler
argument_list|(
literal|"POST"
argument_list|,
literal|null
argument_list|,
literal|"rocks camel?"
argument_list|,
name|getExpectedContent
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
literal|"http4://"
operator|+
name|getHostName
argument_list|()
operator|+
literal|":"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/"
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
literal|"rocks camel?"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|httpPut ()
specifier|public
name|void
name|httpPut
parameter_list|()
throws|throws
name|Exception
block|{
name|localServer
operator|.
name|register
argument_list|(
literal|"/"
argument_list|,
operator|new
name|BasicValidationHandler
argument_list|(
literal|"PUT"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|getExpectedContent
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
literal|"http4://"
operator|+
name|getHostName
argument_list|()
operator|+
literal|":"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/"
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
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
literal|"PUT"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|httpTrace ()
specifier|public
name|void
name|httpTrace
parameter_list|()
throws|throws
name|Exception
block|{
name|localServer
operator|.
name|register
argument_list|(
literal|"/"
argument_list|,
operator|new
name|BasicValidationHandler
argument_list|(
literal|"TRACE"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|getExpectedContent
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
literal|"http4://"
operator|+
name|getHostName
argument_list|()
operator|+
literal|":"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/"
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
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
literal|"TRACE"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|httpOptions ()
specifier|public
name|void
name|httpOptions
parameter_list|()
throws|throws
name|Exception
block|{
name|localServer
operator|.
name|register
argument_list|(
literal|"/"
argument_list|,
operator|new
name|BasicValidationHandler
argument_list|(
literal|"OPTIONS"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|getExpectedContent
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
literal|"http4://"
operator|+
name|getHostName
argument_list|()
operator|+
literal|":"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/"
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
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
literal|"OPTIONS"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|httpDelete ()
specifier|public
name|void
name|httpDelete
parameter_list|()
throws|throws
name|Exception
block|{
name|localServer
operator|.
name|register
argument_list|(
literal|"/"
argument_list|,
operator|new
name|BasicValidationHandler
argument_list|(
literal|"DELETE"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|getExpectedContent
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
literal|"http4://"
operator|+
name|getHostName
argument_list|()
operator|+
literal|":"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/"
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
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
literal|"DELETE"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|httpHead ()
specifier|public
name|void
name|httpHead
parameter_list|()
throws|throws
name|Exception
block|{
name|localServer
operator|.
name|register
argument_list|(
literal|"/"
argument_list|,
operator|new
name|BasicValidationHandler
argument_list|(
literal|"HEAD"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|getExpectedContent
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
literal|"http4://"
operator|+
name|getHostName
argument_list|()
operator|+
literal|":"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/"
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
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
literal|"HEAD"
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
name|Message
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertHeaders
argument_list|(
name|out
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|out
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

