begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.servlet.springboot
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
operator|.
name|springboot
package|;
end_package

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_comment
comment|/**  * Mapping settings for the servlet component.  */
end_comment

begin_class
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.servlet.mapping"
argument_list|)
DECL|class|ServletMappingConfiguration
specifier|public
class|class
name|ServletMappingConfiguration
block|{
comment|/**      * Context path used by the servlet component for automatic mapping.      */
DECL|field|contextPath
specifier|private
name|String
name|contextPath
init|=
literal|"/camel/*"
decl_stmt|;
comment|/**      * The name of the Camel servlet.      */
DECL|field|servletName
specifier|private
name|String
name|servletName
init|=
literal|"CamelServlet"
decl_stmt|;
comment|/**      * Enables the automatic mapping of the servlet component into the Spring web context.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
init|=
literal|true
decl_stmt|;
DECL|method|ServletMappingConfiguration ()
specifier|public
name|ServletMappingConfiguration
parameter_list|()
block|{     }
DECL|method|getContextPath ()
specifier|public
name|String
name|getContextPath
parameter_list|()
block|{
return|return
name|contextPath
return|;
block|}
DECL|method|setContextPath (String contextPath)
specifier|public
name|void
name|setContextPath
parameter_list|(
name|String
name|contextPath
parameter_list|)
block|{
name|this
operator|.
name|contextPath
operator|=
name|contextPath
expr_stmt|;
block|}
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
DECL|method|setServletName (String servletName)
specifier|public
name|void
name|setServletName
parameter_list|(
name|String
name|servletName
parameter_list|)
block|{
name|this
operator|.
name|servletName
operator|=
name|servletName
expr_stmt|;
block|}
DECL|method|getEnabled ()
specifier|public
name|Boolean
name|getEnabled
parameter_list|()
block|{
return|return
name|enabled
return|;
block|}
DECL|method|setEnabled (Boolean enabled)
specifier|public
name|void
name|setEnabled
parameter_list|(
name|Boolean
name|enabled
parameter_list|)
block|{
name|this
operator|.
name|enabled
operator|=
name|enabled
expr_stmt|;
block|}
block|}
end_class

end_unit

