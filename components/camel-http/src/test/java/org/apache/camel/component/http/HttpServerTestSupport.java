begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLContext
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
name|apache
operator|.
name|http
operator|.
name|ConnectionReuseStrategy
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
name|params
operator|.
name|HttpParams
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
name|junit
operator|.
name|After
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

begin_comment
comment|/**  * Abstract base class for unit testing using a http server.  * The setUp method starts the server before the camel context is started and  * the tearDown method stops the server after the camel context is stopped.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|HttpServerTestSupport
specifier|public
specifier|abstract
class|class
name|HttpServerTestSupport
extends|extends
name|CamelTestSupport
block|{
DECL|field|localServer
specifier|protected
name|LocalTestServer
name|localServer
decl_stmt|;
annotation|@
name|Before
annotation|@
name|Override
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|localServer
operator|=
operator|new
name|LocalTestServer
argument_list|(
name|getBasicHttpProcessor
argument_list|()
argument_list|,
name|getConnectionReuseStrategy
argument_list|()
argument_list|,
name|getHttpParams
argument_list|()
argument_list|,
name|getSSLContext
argument_list|()
argument_list|)
expr_stmt|;
name|registerHandler
argument_list|(
name|localServer
argument_list|)
expr_stmt|;
name|localServer
operator|.
name|start
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
annotation|@
name|Override
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
if|if
condition|(
name|localServer
operator|!=
literal|null
condition|)
block|{
name|localServer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Returns the org.apache.http.protocol.BasicHttpProcessor which should be      * used by the server.      *      * @return basicHttpProcessor      */
DECL|method|getBasicHttpProcessor ()
specifier|protected
name|BasicHttpProcessor
name|getBasicHttpProcessor
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
comment|/**      * Returns the org.apache.http.ConnectionReuseStrategy which should be used      * by the server.      *      * @return connectionReuseStrategy      */
DECL|method|getConnectionReuseStrategy ()
specifier|protected
name|ConnectionReuseStrategy
name|getConnectionReuseStrategy
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
comment|/**      * Returns the org.apache.http.params.HttpParams which should be used by      * the server.      *      * @return httpParams      */
DECL|method|getHttpParams ()
specifier|protected
name|HttpParams
name|getHttpParams
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
comment|/**      * Returns the javax.net.ssl.SSLContext which should be used by the server.      *      * @return sslContext      * @throws Exception      */
DECL|method|getSSLContext ()
specifier|protected
name|SSLContext
name|getSSLContext
parameter_list|()
throws|throws
name|Exception
block|{
return|return
literal|null
return|;
block|}
comment|/**      * Register the org.apache.http.protocol.HttpRequestHandler which handles      * the request and set the proper response (headers and content).      *      * @param server      */
DECL|method|registerHandler (LocalTestServer server)
specifier|protected
name|void
name|registerHandler
parameter_list|(
name|LocalTestServer
name|server
parameter_list|)
block|{     }
comment|/**      * Obtains the host name of the local test server.      *      * @return hostName      */
DECL|method|getHostName ()
specifier|protected
name|String
name|getHostName
parameter_list|()
block|{
return|return
name|localServer
operator|.
name|getServiceHostName
argument_list|()
return|;
block|}
comment|/**      * Obtains the port of the local test server.      *      * @return port      */
DECL|method|getPort ()
specifier|protected
name|int
name|getPort
parameter_list|()
block|{
return|return
name|localServer
operator|.
name|getServicePort
argument_list|()
return|;
block|}
block|}
end_class

end_unit

