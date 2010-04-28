begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.osgi.servlet.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|osgi
operator|.
name|servlet
operator|.
name|support
package|;
end_package

begin_comment
comment|// START SNIPPET: activator
end_comment

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Dictionary
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Hashtable
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|Servlet
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
name|servlet
operator|.
name|CamelHttpTransportServlet
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
name|osgi
operator|.
name|framework
operator|.
name|BundleActivator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|ServiceReference
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|http
operator|.
name|HttpContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|http
operator|.
name|HttpService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|osgi
operator|.
name|context
operator|.
name|BundleContextAware
import|;
end_import

begin_class
DECL|class|ServletActivator
specifier|public
specifier|final
class|class
name|ServletActivator
implements|implements
name|BundleActivator
implements|,
name|BundleContextAware
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
name|ServletActivator
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|registerService
specifier|private
specifier|static
name|boolean
name|registerService
decl_stmt|;
comment|/**      * HttpService reference.      */
DECL|field|httpServiceRef
specifier|private
name|ServiceReference
name|httpServiceRef
decl_stmt|;
comment|/**      * Called when the OSGi framework starts our bundle      */
DECL|method|start (BundleContext bc)
specifier|public
name|void
name|start
parameter_list|(
name|BundleContext
name|bc
parameter_list|)
throws|throws
name|Exception
block|{
name|registerServlet
argument_list|(
name|bc
argument_list|)
expr_stmt|;
block|}
comment|/**      * Called when the OSGi framework stops our bundle      */
DECL|method|stop (BundleContext bc)
specifier|public
name|void
name|stop
parameter_list|(
name|BundleContext
name|bc
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|httpServiceRef
operator|!=
literal|null
condition|)
block|{
name|bc
operator|.
name|ungetService
argument_list|(
name|httpServiceRef
argument_list|)
expr_stmt|;
name|httpServiceRef
operator|=
literal|null
expr_stmt|;
block|}
block|}
DECL|method|registerServlet (BundleContext bundleContext)
specifier|protected
name|void
name|registerServlet
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|)
throws|throws
name|Exception
block|{
name|httpServiceRef
operator|=
name|bundleContext
operator|.
name|getServiceReference
argument_list|(
name|HttpService
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|httpServiceRef
operator|!=
literal|null
operator|&&
operator|!
name|registerService
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Regist the servlet service"
argument_list|)
expr_stmt|;
specifier|final
name|HttpService
name|httpService
init|=
operator|(
name|HttpService
operator|)
name|bundleContext
operator|.
name|getService
argument_list|(
name|httpServiceRef
argument_list|)
decl_stmt|;
if|if
condition|(
name|httpService
operator|!=
literal|null
condition|)
block|{
comment|// create a default context to share between registrations
specifier|final
name|HttpContext
name|httpContext
init|=
name|httpService
operator|.
name|createDefaultHttpContext
argument_list|()
decl_stmt|;
comment|// register the hello world servlet
specifier|final
name|Dictionary
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|initParams
init|=
operator|new
name|Hashtable
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|initParams
operator|.
name|put
argument_list|(
literal|"matchOnUriPrefix"
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
name|initParams
operator|.
name|put
argument_list|(
literal|"servlet-name"
argument_list|,
literal|"camelServlet"
argument_list|)
expr_stmt|;
name|httpService
operator|.
name|registerServlet
argument_list|(
literal|"/camel/services"
argument_list|,
comment|// alias
operator|(
name|Servlet
operator|)
operator|new
name|CamelHttpTransportServlet
argument_list|()
argument_list|,
comment|// register servlet
name|initParams
argument_list|,
comment|// init params
name|httpContext
comment|// http context
argument_list|)
expr_stmt|;
name|registerService
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
DECL|method|setBundleContext (BundleContext bc)
specifier|public
name|void
name|setBundleContext
parameter_list|(
name|BundleContext
name|bc
parameter_list|)
block|{
try|try
block|{
name|registerServlet
argument_list|(
name|bc
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Can't register the servlet, the reason is "
operator|+
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

begin_comment
comment|// END SNIPPET: activator
end_comment

end_unit

