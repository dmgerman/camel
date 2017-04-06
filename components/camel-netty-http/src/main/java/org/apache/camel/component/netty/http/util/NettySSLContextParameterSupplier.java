begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright 2016 Red Hat, Inc.  *  * Red Hat licenses this file to you under the Apache License, version  * 2.0 (the "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *     http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or  * implied.  See the License for the specific language governing  * permissions and limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty.http.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty
operator|.
name|http
operator|.
name|util
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
name|util
operator|.
name|jsse
operator|.
name|SSLContextParameters
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
name|util
operator|.
name|jsse
operator|.
name|GlobalSSLContextParametersSupplier
import|;
end_import

begin_comment
comment|/**  * Class for binding a SSSLContextParametersSupplier to the registry.  */
end_comment

begin_class
DECL|class|NettySSLContextParameterSupplier
specifier|public
class|class
name|NettySSLContextParameterSupplier
implements|implements
name|GlobalSSLContextParametersSupplier
block|{
DECL|field|sslContextParameters
specifier|private
name|SSLContextParameters
name|sslContextParameters
decl_stmt|;
DECL|method|NettySSLContextParameterSupplier ()
specifier|public
name|NettySSLContextParameterSupplier
parameter_list|()
block|{     }
DECL|method|getSslContextParameters ()
specifier|public
name|SSLContextParameters
name|getSslContextParameters
parameter_list|()
block|{
return|return
name|sslContextParameters
return|;
block|}
DECL|method|setSslContextParameters (SSLContextParameters sslContextParameters)
specifier|public
name|void
name|setSslContextParameters
parameter_list|(
name|SSLContextParameters
name|sslContextParameters
parameter_list|)
block|{
name|this
operator|.
name|sslContextParameters
operator|=
name|sslContextParameters
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|SSLContextParameters
name|get
parameter_list|()
block|{
return|return
name|sslContextParameters
return|;
block|}
block|}
end_class

end_unit

