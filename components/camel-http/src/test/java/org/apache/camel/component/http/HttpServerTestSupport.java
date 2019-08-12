begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|HttpResponseFactory
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
name|HttpExpectationVerifier
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
name|HttpProcessor
import|;
end_import

begin_comment
comment|/**  * Abstract base class for unit testing using a http server.  * This class contains an empty configuration to be used.  */
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
comment|/**      * Returns the org.apache.http.protocol.BasicHttpProcessor which should be      * used by the server.      *      * @return HttpProcessor      */
DECL|method|getBasicHttpProcessor ()
specifier|protected
name|HttpProcessor
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
comment|/**      * Returns the org.apache.http.HttpResponseFactory which should be used      * by the server.      *      * @return httpResponseFactory      */
DECL|method|getHttpResponseFactory ()
specifier|protected
name|HttpResponseFactory
name|getHttpResponseFactory
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
comment|/**      * Returns the org.apache.http.protocol.HttpExpectationVerifier which should be used      * by the server.      *      * @return httpExpectationVerifier      */
DECL|method|getHttpExpectationVerifier ()
specifier|protected
name|HttpExpectationVerifier
name|getHttpExpectationVerifier
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
block|}
end_class

end_unit

