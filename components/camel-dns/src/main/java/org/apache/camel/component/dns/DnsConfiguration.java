begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.dns
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|dns
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
name|spi
operator|.
name|UriParam
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
name|spi
operator|.
name|UriParams
import|;
end_import

begin_class
annotation|@
name|UriParams
DECL|class|DnsConfiguration
specifier|public
class|class
name|DnsConfiguration
block|{
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"_tcp"
argument_list|)
DECL|field|proto
name|String
name|proto
init|=
literal|"_tcp"
decl_stmt|;
annotation|@
name|UriParam
DECL|field|domain
name|String
name|domain
decl_stmt|;
DECL|method|getProto ()
specifier|public
name|String
name|getProto
parameter_list|()
block|{
return|return
name|proto
return|;
block|}
comment|/**      * The transport protocol of the desired service.      */
DECL|method|setProto (String proto)
specifier|public
name|void
name|setProto
parameter_list|(
name|String
name|proto
parameter_list|)
block|{
name|this
operator|.
name|proto
operator|=
name|proto
expr_stmt|;
block|}
DECL|method|getDomain ()
specifier|public
name|String
name|getDomain
parameter_list|()
block|{
return|return
name|domain
return|;
block|}
comment|/**      * The domain name;      */
DECL|method|setDomain (String domain)
specifier|public
name|void
name|setDomain
parameter_list|(
name|String
name|domain
parameter_list|)
block|{
name|this
operator|.
name|domain
operator|=
name|domain
expr_stmt|;
block|}
block|}
end_class

end_unit

