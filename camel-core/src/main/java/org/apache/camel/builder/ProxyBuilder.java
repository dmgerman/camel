begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
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
name|CamelContext
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
name|component
operator|.
name|bean
operator|.
name|ProxyHelper
import|;
end_import

begin_comment
comment|/**  * A build to create Camel proxies.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|ProxyBuilder
specifier|public
specifier|final
class|class
name|ProxyBuilder
block|{
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|endpoint
specifier|private
name|Endpoint
name|endpoint
decl_stmt|;
DECL|field|voidAsInOnly
specifier|private
name|boolean
name|voidAsInOnly
decl_stmt|;
DECL|method|ProxyBuilder (CamelContext camelContext)
specifier|public
name|ProxyBuilder
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
comment|/**      * Send the proxied message to this endpoint      *      * @param url  uri of endpoint      * @return the builder      */
DECL|method|endpoint (String url)
specifier|public
name|ProxyBuilder
name|endpoint
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|camelContext
operator|.
name|getEndpoint
argument_list|(
name|url
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Send the proxied message to this endpoint      *      * @param endpoint  the endpoint      * @return the builder      */
DECL|method|endpoint (Endpoint endpoint)
specifier|public
name|ProxyBuilder
name|endpoint
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Let methods which return<tt>void</tt> be regarded as<tt>InOnly</tt> methods (eg Fire and forget).      *      * @return the builder      */
DECL|method|voidAsInOnly ()
specifier|public
name|ProxyBuilder
name|voidAsInOnly
parameter_list|()
block|{
name|voidAsInOnly
operator|=
literal|true
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Builds the proxy.      *      * @param interfaceClasses  the service interface(s)      * @return the proxied bean      * @throws Exception is thrown if error creating the proxy      */
DECL|method|build (Class<T>.... interfaceClasses)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|build
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
modifier|...
name|interfaceClasses
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|ProxyHelper
operator|.
name|createProxy
argument_list|(
name|endpoint
argument_list|,
name|voidAsInOnly
argument_list|,
name|interfaceClasses
argument_list|)
return|;
block|}
block|}
end_class

end_unit

