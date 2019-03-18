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
name|springframework
operator|.
name|boot
operator|.
name|autoconfigure
operator|.
name|AutoConfigureAfter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|autoconfigure
operator|.
name|condition
operator|.
name|ConditionalOnBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|autoconfigure
operator|.
name|condition
operator|.
name|ConditionalOnProperty
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|autoconfigure
operator|.
name|condition
operator|.
name|ConditionalOnWebApplication
import|;
end_import

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
name|EnableConfigurationProperties
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|web
operator|.
name|servlet
operator|.
name|ServletRegistrationBean
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
name|annotation
operator|.
name|Bean
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
name|annotation
operator|.
name|Configuration
import|;
end_import

begin_comment
comment|/**  * Servlet mapping auto-configuration.  */
end_comment

begin_class
annotation|@
name|Configuration
annotation|@
name|ConditionalOnProperty
argument_list|(
name|name
operator|=
literal|"camel.component.servlet.mapping.enabled"
argument_list|,
name|matchIfMissing
operator|=
literal|true
argument_list|)
annotation|@
name|ConditionalOnBean
argument_list|(
name|type
operator|=
literal|"org.apache.camel.spring.boot.CamelAutoConfiguration"
argument_list|)
annotation|@
name|AutoConfigureAfter
argument_list|(
name|name
operator|=
literal|"org.apache.camel.spring.boot.CamelAutoConfiguration"
argument_list|)
annotation|@
name|ConditionalOnWebApplication
annotation|@
name|EnableConfigurationProperties
argument_list|(
name|ServletMappingConfiguration
operator|.
name|class
argument_list|)
DECL|class|ServletMappingAutoConfiguration
specifier|public
class|class
name|ServletMappingAutoConfiguration
block|{
annotation|@
name|Bean
DECL|method|servletRegistrationBean (ServletMappingConfiguration config)
name|ServletRegistrationBean
name|servletRegistrationBean
parameter_list|(
name|ServletMappingConfiguration
name|config
parameter_list|)
block|{
name|ServletRegistrationBean
name|mapping
init|=
operator|new
name|ServletRegistrationBean
argument_list|()
decl_stmt|;
name|mapping
operator|.
name|setServlet
argument_list|(
operator|new
name|CamelHttpTransportServlet
argument_list|()
argument_list|)
expr_stmt|;
name|mapping
operator|.
name|addUrlMappings
argument_list|(
name|config
operator|.
name|getContextPath
argument_list|()
argument_list|)
expr_stmt|;
name|mapping
operator|.
name|setName
argument_list|(
name|config
operator|.
name|getServletName
argument_list|()
argument_list|)
expr_stmt|;
name|mapping
operator|.
name|setLoadOnStartup
argument_list|(
literal|1
argument_list|)
expr_stmt|;
return|return
name|mapping
return|;
block|}
block|}
end_class

end_unit

