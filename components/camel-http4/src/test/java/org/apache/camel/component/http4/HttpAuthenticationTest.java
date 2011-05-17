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
name|AuthenticationValidationHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpStatus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|localserver
operator|.
name|LocalTestServer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|localserver
operator|.
name|RequestBasicAuth
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|localserver
operator|.
name|ResponseBasicUnauthorized
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|protocol
operator|.
name|BasicHttpProcessor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|protocol
operator|.
name|ResponseContent
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
DECL|class|HttpAuthenticationTest
specifier|public
class|class
name|HttpAuthenticationTest
extends|extends
name|BaseHttpTest
block|{
DECL|field|user
specifier|private
name|String
name|user
init|=
literal|"camel"
decl_stmt|;
DECL|field|password
specifier|private
name|String
name|password
init|=
literal|"password"
decl_stmt|;
annotation|@
name|Test
DECL|method|basicAuthenticationShouldSuccess ()
specifier|public
name|void
name|basicAuthenticationShouldSuccess
parameter_list|()
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
literal|"/search?authUsername="
operator|+
name|user
operator|+
literal|"&authPassword="
operator|+
name|password
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
DECL|method|basicAuthenticationShouldFailWithoutCreds ()
specifier|public
name|void
name|basicAuthenticationShouldFailWithoutCreds
parameter_list|()
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
literal|"/search?throwExceptionOnFailure=false"
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
name|assertExchangeFailed
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|basicAuthenticationShouldFailWithWrongCreds ()
specifier|public
name|void
name|basicAuthenticationShouldFailWithWrongCreds
parameter_list|()
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
literal|"/search?throwExceptionOnFailure=false&authUsername=camel&authPassword=wrong"
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
name|assertExchangeFailed
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getBasicHttpProcessor ()
specifier|protected
name|BasicHttpProcessor
name|getBasicHttpProcessor
parameter_list|()
block|{
name|BasicHttpProcessor
name|httpproc
init|=
operator|new
name|BasicHttpProcessor
argument_list|()
decl_stmt|;
name|httpproc
operator|.
name|addInterceptor
argument_list|(
operator|new
name|RequestBasicAuth
argument_list|()
argument_list|)
expr_stmt|;
name|httpproc
operator|.
name|addInterceptor
argument_list|(
operator|new
name|ResponseContent
argument_list|()
argument_list|)
expr_stmt|;
name|httpproc
operator|.
name|addInterceptor
argument_list|(
operator|new
name|ResponseBasicUnauthorized
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|httpproc
return|;
block|}
annotation|@
name|Override
DECL|method|registerHandler (LocalTestServer server)
specifier|protected
name|void
name|registerHandler
parameter_list|(
name|LocalTestServer
name|server
parameter_list|)
block|{
name|server
operator|.
name|register
argument_list|(
literal|"/search"
argument_list|,
operator|new
name|AuthenticationValidationHandler
argument_list|(
literal|"GET"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|getExpectedContent
argument_list|()
argument_list|,
name|user
argument_list|,
name|password
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|assertExchangeFailed (Exchange exchange)
specifier|protected
name|void
name|assertExchangeFailed
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
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
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
name|out
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|HttpStatus
operator|.
name|SC_UNAUTHORIZED
argument_list|,
name|headers
operator|.
name|get
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"0"
argument_list|,
name|headers
operator|.
name|get
argument_list|(
literal|"Content-Length"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|headers
operator|.
name|get
argument_list|(
literal|"Content-Type"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
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

