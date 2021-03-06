begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jetty
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jetty
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|Filter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|FilterChain
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|FilterConfig
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletResponse
import|;
end_import

begin_comment
comment|/**  * The camel filter wrapper that processes only initially dispatched requests.  * Re-dispatched requests are ignored.  */
end_comment

begin_class
DECL|class|CamelFilterWrapper
specifier|public
class|class
name|CamelFilterWrapper
implements|implements
name|Filter
block|{
DECL|field|wrapped
specifier|private
name|Filter
name|wrapped
decl_stmt|;
DECL|method|CamelFilterWrapper (Filter wrapped)
specifier|public
name|CamelFilterWrapper
parameter_list|(
name|Filter
name|wrapped
parameter_list|)
block|{
name|this
operator|.
name|wrapped
operator|=
name|wrapped
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doFilter (ServletRequest request, ServletResponse response, FilterChain chain)
specifier|public
name|void
name|doFilter
parameter_list|(
name|ServletRequest
name|request
parameter_list|,
name|ServletResponse
name|response
parameter_list|,
name|FilterChain
name|chain
parameter_list|)
throws|throws
name|IOException
throws|,
name|ServletException
block|{
if|if
condition|(
name|request
operator|.
name|getAttribute
argument_list|(
name|CamelContinuationServlet
operator|.
name|EXCHANGE_ATTRIBUTE_NAME
argument_list|)
operator|==
literal|null
condition|)
block|{
name|wrapped
operator|.
name|doFilter
argument_list|(
name|request
argument_list|,
name|response
argument_list|,
name|chain
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|chain
operator|.
name|doFilter
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
block|{
name|wrapped
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|init (FilterConfig config)
specifier|public
name|void
name|init
parameter_list|(
name|FilterConfig
name|config
parameter_list|)
throws|throws
name|ServletException
block|{
name|Object
name|o
init|=
name|config
operator|.
name|getServletContext
argument_list|()
operator|.
name|getAttribute
argument_list|(
literal|"javax.servlet.context.tempdir"
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|==
literal|null
condition|)
block|{
comment|//when run in embedded mode, Jetty 8 will forget to set this property,
comment|//but the MultiPartFilter requires it (will NPE if not set) so we'll
comment|//go ahead and set it to the default tmp dir on the system.
try|try
block|{
name|File
name|file
init|=
name|Files
operator|.
name|createTempFile
argument_list|(
literal|"camel"
argument_list|,
literal|""
argument_list|)
operator|.
name|toFile
argument_list|()
decl_stmt|;
name|file
operator|.
name|delete
argument_list|()
expr_stmt|;
name|config
operator|.
name|getServletContext
argument_list|()
operator|.
name|setAttribute
argument_list|(
literal|"javax.servlet.context.tempdir"
argument_list|,
name|file
operator|.
name|getParentFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|//ignore
block|}
block|}
name|wrapped
operator|.
name|init
argument_list|(
name|config
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

