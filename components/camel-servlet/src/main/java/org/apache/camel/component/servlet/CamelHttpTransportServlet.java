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
name|Iterator
import|;
end_import

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
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|AbstractApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

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
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
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
name|CamelServlet
argument_list|>
name|CAMEL_SERVLET_MAP
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|CamelServlet
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|servletName
specifier|private
name|String
name|servletName
decl_stmt|;
DECL|field|applicationContext
specifier|private
name|AbstractApplicationContext
name|applicationContext
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
name|servletName
operator|=
name|config
operator|.
name|getServletName
argument_list|()
expr_stmt|;
comment|// parser the servlet init parameters
name|CAMEL_SERVLET_MAP
operator|.
name|put
argument_list|(
name|servletName
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|String
name|contextConfigLocation
init|=
name|config
operator|.
name|getInitParameter
argument_list|(
literal|"contextConfigLocation"
argument_list|)
decl_stmt|;
if|if
condition|(
name|contextConfigLocation
operator|!=
literal|null
condition|)
block|{
comment|//Create a spring application context for it
name|applicationContext
operator|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
operator|new
name|String
index|[]
block|{
name|contextConfigLocation
block|}
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|applicationContext
operator|!=
literal|null
condition|)
block|{
name|applicationContext
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getCamelServlet (String servletName)
specifier|public
specifier|static
name|CamelServlet
name|getCamelServlet
parameter_list|(
name|String
name|servletName
parameter_list|)
block|{
name|CamelServlet
name|answer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|servletName
operator|!=
literal|null
condition|)
block|{
name|answer
operator|=
name|CAMEL_SERVLET_MAP
operator|.
name|get
argument_list|(
name|servletName
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|CAMEL_SERVLET_MAP
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
comment|// return the first one servlet
name|Iterator
argument_list|<
name|CamelServlet
argument_list|>
name|iterator
init|=
name|CAMEL_SERVLET_MAP
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|answer
operator|=
name|iterator
operator|.
name|next
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Since no servlet name is specified, using the first element of camelServlet map ["
operator|+
name|answer
operator|.
name|getServletName
argument_list|()
operator|+
literal|"]"
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

