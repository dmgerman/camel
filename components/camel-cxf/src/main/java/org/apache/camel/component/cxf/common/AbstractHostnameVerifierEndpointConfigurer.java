begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.common
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|common
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
name|HostnameVerifier
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
name|configuration
operator|.
name|jsse
operator|.
name|TLSClientParameters
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
name|transport
operator|.
name|http
operator|.
name|HTTPConduit
import|;
end_import

begin_class
DECL|class|AbstractHostnameVerifierEndpointConfigurer
specifier|public
class|class
name|AbstractHostnameVerifierEndpointConfigurer
extends|extends
name|AbstractTLSClientParameterConfigurer
block|{
DECL|field|hostnameVerifier
specifier|protected
specifier|final
name|HostnameVerifier
name|hostnameVerifier
decl_stmt|;
DECL|method|AbstractHostnameVerifierEndpointConfigurer (HostnameVerifier hostnameVerifier)
specifier|public
name|AbstractHostnameVerifierEndpointConfigurer
parameter_list|(
name|HostnameVerifier
name|hostnameVerifier
parameter_list|)
block|{
name|this
operator|.
name|hostnameVerifier
operator|=
name|hostnameVerifier
expr_stmt|;
block|}
DECL|method|setupHttpConduit (HTTPConduit httpConduit)
specifier|protected
name|void
name|setupHttpConduit
parameter_list|(
name|HTTPConduit
name|httpConduit
parameter_list|)
block|{
name|TLSClientParameters
name|tlsClientParameters
init|=
name|tryToGetTLSClientParametersFromConduit
argument_list|(
name|httpConduit
argument_list|)
decl_stmt|;
name|tlsClientParameters
operator|.
name|setHostnameVerifier
argument_list|(
name|hostnameVerifier
argument_list|)
expr_stmt|;
name|httpConduit
operator|.
name|setTlsClientParameters
argument_list|(
name|tlsClientParameters
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

