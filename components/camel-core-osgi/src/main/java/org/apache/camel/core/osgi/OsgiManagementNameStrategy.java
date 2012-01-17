begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.core.osgi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|core
operator|.
name|osgi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
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
name|impl
operator|.
name|DefaultManagementNameStrategy
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

begin_comment
comment|/**  * OSGI enhanced {@link org.apache.camel.spi.ManagementNameStrategy}.  *<p/>  * This {@link org.apache.camel.spi.ManagementNameStrategy} supports the default  * tokens (see {@link DefaultManagementNameStrategy}) and the following additional OSGi specific tokens  *<ul>  *<li>#bundleId# - The bundle id</li>  *<li>#symbolicName# - The bundle symbolic name</li>  *</ul>  *<p/>  * This implementation will by default use a name pattern as<tt>#bundleId#-#name#</tt> and in case  * of a clash, then the pattern will fallback to be using the counter as<tt>#bundleId#-#name#-#counter#</tt>.  *  * @see DefaultManagementNameStrategy  */
end_comment

begin_class
DECL|class|OsgiManagementNameStrategy
specifier|public
class|class
name|OsgiManagementNameStrategy
extends|extends
name|DefaultManagementNameStrategy
block|{
DECL|field|bundleContext
specifier|private
specifier|final
name|BundleContext
name|bundleContext
decl_stmt|;
DECL|method|OsgiManagementNameStrategy (CamelContext camelContext, BundleContext bundleContext)
specifier|public
name|OsgiManagementNameStrategy
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|BundleContext
name|bundleContext
parameter_list|)
block|{
name|super
argument_list|(
name|camelContext
argument_list|,
literal|"#bundleId#-#name#"
argument_list|,
literal|"#bundleId#-#name#-#counter#"
argument_list|)
expr_stmt|;
name|this
operator|.
name|bundleContext
operator|=
name|bundleContext
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|customResolveManagementName (String pattern, String answer)
specifier|protected
name|String
name|customResolveManagementName
parameter_list|(
name|String
name|pattern
parameter_list|,
name|String
name|answer
parameter_list|)
block|{
name|String
name|bundleId
init|=
literal|""
operator|+
name|bundleContext
operator|.
name|getBundle
argument_list|()
operator|.
name|getBundleId
argument_list|()
decl_stmt|;
name|String
name|symbolicName
init|=
name|Matcher
operator|.
name|quoteReplacement
argument_list|(
name|bundleContext
operator|.
name|getBundle
argument_list|()
operator|.
name|getSymbolicName
argument_list|()
argument_list|)
decl_stmt|;
name|answer
operator|=
name|answer
operator|.
name|replaceFirst
argument_list|(
literal|"#bundleId#"
argument_list|,
name|bundleId
argument_list|)
expr_stmt|;
name|answer
operator|=
name|answer
operator|.
name|replaceFirst
argument_list|(
literal|"#symbolicName#"
argument_list|,
name|symbolicName
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

