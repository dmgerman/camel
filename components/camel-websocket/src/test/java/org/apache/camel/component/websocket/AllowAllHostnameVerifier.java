begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.websocket
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|websocket
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
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLSession
import|;
end_import

begin_comment
comment|/*  * Since async-http-client 1.9.0, by default it will use  * DefaultHostnameVerifier which strictly check if the hostname match (the CN part in cert)  * Add this class to ensure the SSL enabled test can pass on any machine  */
end_comment

begin_class
DECL|class|AllowAllHostnameVerifier
specifier|public
class|class
name|AllowAllHostnameVerifier
implements|implements
name|HostnameVerifier
block|{
annotation|@
name|Override
DECL|method|verify (String s, SSLSession sslSession)
specifier|public
name|boolean
name|verify
parameter_list|(
name|String
name|s
parameter_list|,
name|SSLSession
name|sslSession
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

