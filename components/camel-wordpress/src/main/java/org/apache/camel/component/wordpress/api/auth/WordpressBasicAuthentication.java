begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.wordpress.api.auth
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|wordpress
operator|.
name|api
operator|.
name|auth
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|util
operator|.
name|Base64Utility
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|client
operator|.
name|ClientConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|client
operator|.
name|WebClient
import|;
end_import

begin_comment
comment|/**  * Basic Authentication implementation for Wordpress authentication mechanism. Should be used only on tested environments due to lack of security. Be aware that credentials will be passed over each  * request to your server.  *<p/>  * On environments without non HTTPS this a high security risk.  *<p/>  * To this implementation work, the<a href="https://github.com/WP-API/Basic-Auth">Basic Authentication Plugin</a> must be installed into the Wordpress server.  */
end_comment

begin_class
DECL|class|WordpressBasicAuthentication
specifier|public
class|class
name|WordpressBasicAuthentication
extends|extends
name|BaseWordpressAuthentication
block|{
DECL|method|WordpressBasicAuthentication ()
specifier|public
name|WordpressBasicAuthentication
parameter_list|()
block|{     }
DECL|method|WordpressBasicAuthentication (String username, String password)
specifier|public
name|WordpressBasicAuthentication
parameter_list|(
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|)
block|{
name|super
argument_list|(
name|username
argument_list|,
name|password
argument_list|)
expr_stmt|;
block|}
comment|/**      * HTTP Basic Authentication configuration over CXF {@link ClientConfiguration}.      *       * @see<a href= "http://cxf.apache.org/docs/jax-rs-client-api.html#JAX-RSClientAPI-ClientsandAuthentication">CXF Clients and Authentication</a>      */
annotation|@
name|Override
DECL|method|configureAuthentication (Object api)
specifier|public
name|void
name|configureAuthentication
parameter_list|(
name|Object
name|api
parameter_list|)
block|{
if|if
condition|(
name|isCredentialsSet
argument_list|()
condition|)
block|{
specifier|final
name|String
name|authorizationHeader
init|=
name|String
operator|.
name|format
argument_list|(
literal|"Basic "
argument_list|,
name|Base64Utility
operator|.
name|encode
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%s:%s"
argument_list|,
name|this
operator|.
name|username
argument_list|,
name|this
operator|.
name|password
argument_list|)
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|WebClient
operator|.
name|client
argument_list|(
name|api
argument_list|)
operator|.
name|header
argument_list|(
literal|"Authorization"
argument_list|,
name|authorizationHeader
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

