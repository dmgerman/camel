begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.servlet
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|servlet
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletConfig
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http
operator|.
name|CamelServlet
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
name|http
operator|.
name|HttpConsumer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Camel HTTP servlet which can be used in Camel routes to route servlet invocations in routes.  */
end_comment

begin_class
DECL|class|CamelHttpTransportServlet
specifier|public
class|class
name|CamelHttpTransportServlet
extends|extends
name|CamelServlet
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CamelHttpTransportServlet
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|CAMEL_SERVLET_MAP
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|CamelServletService
argument_list|>
name|CAMEL_SERVLET_MAP
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|CamelServletService
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|servletName
specifier|private
name|String
name|servletName
decl_stmt|;
DECL|method|init (ServletConfig config)
specifier|public
name|void
name|init
parameter_list|(
name|ServletConfig
name|config
parameter_list|)
throws|throws
name|ServletException
block|{
name|super
operator|.
name|init
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|this
operator|.
name|servletName
operator|=
name|config
operator|.
name|getServletName
argument_list|()
expr_stmt|;
comment|// do we already know this servlet?
name|CamelServletService
name|service
init|=
name|CAMEL_SERVLET_MAP
operator|.
name|get
argument_list|(
name|servletName
argument_list|)
decl_stmt|;
comment|// we cannot control the startup ordering, sometimes the Camel routes start first
comment|// other times the servlet, so we need to cater for both situations
if|if
condition|(
name|service
operator|==
literal|null
condition|)
block|{
comment|// no we don't so create a new early service with this servlet
name|service
operator|=
operator|new
name|DefaultCamelServletService
argument_list|(
name|servletName
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|CAMEL_SERVLET_MAP
operator|.
name|put
argument_list|(
name|servletName
argument_list|,
name|service
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// use this servlet
name|service
operator|.
name|setCamelServlet
argument_list|(
name|this
argument_list|)
expr_stmt|;
comment|// and start the existing consumers we already have registered
for|for
control|(
name|HttpConsumer
name|consumer
range|:
name|service
operator|.
name|getConsumers
argument_list|()
control|)
block|{
name|connect
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
block|}
name|LOG
operator|.
name|info
argument_list|(
literal|"Initialized CamelHttpTransportServlet["
operator|+
name|servletName
operator|+
literal|"]"
argument_list|)
expr_stmt|;
block|}
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
block|{
name|CAMEL_SERVLET_MAP
operator|.
name|remove
argument_list|(
name|servletName
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Destroyed CamelHttpTransportServlet["
operator|+
name|servletName
operator|+
literal|"]"
argument_list|)
expr_stmt|;
block|}
DECL|method|getCamelServletService (String servletName, HttpConsumer consumer)
specifier|public
specifier|static
specifier|synchronized
name|CamelServletService
name|getCamelServletService
parameter_list|(
name|String
name|servletName
parameter_list|,
name|HttpConsumer
name|consumer
parameter_list|)
block|{
comment|// we cannot control the startup ordering, sometimes the Camel routes start first
comment|// other times the servlet, so we need to cater for both situations
name|CamelServletService
name|answer
init|=
name|CAMEL_SERVLET_MAP
operator|.
name|get
argument_list|(
name|servletName
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|answer
operator|=
operator|new
name|DefaultCamelServletService
argument_list|(
name|servletName
argument_list|,
name|consumer
argument_list|)
expr_stmt|;
name|CAMEL_SERVLET_MAP
operator|.
name|put
argument_list|(
name|servletName
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|.
name|addConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|getServletName ()
specifier|public
name|String
name|getServletName
parameter_list|()
block|{
return|return
name|servletName
return|;
block|}
block|}
end_class

end_unit

