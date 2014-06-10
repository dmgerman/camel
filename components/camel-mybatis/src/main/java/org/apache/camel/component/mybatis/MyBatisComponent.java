begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mybatis
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mybatis
package|;
end_package

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
name|io
operator|.
name|InputStream
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
name|Endpoint
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
name|UriEndpointComponent
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
name|IOHelper
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
name|ObjectHelper
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
name|ResourceHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ibatis
operator|.
name|session
operator|.
name|SqlSessionFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ibatis
operator|.
name|session
operator|.
name|SqlSessionFactoryBuilder
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|MyBatisComponent
specifier|public
class|class
name|MyBatisComponent
extends|extends
name|UriEndpointComponent
block|{
DECL|field|sqlSessionFactory
specifier|private
name|SqlSessionFactory
name|sqlSessionFactory
decl_stmt|;
DECL|field|configurationUri
specifier|private
name|String
name|configurationUri
init|=
literal|"SqlMapConfig.xml"
decl_stmt|;
DECL|method|MyBatisComponent ()
specifier|public
name|MyBatisComponent
parameter_list|()
block|{
name|super
argument_list|(
name|MyBatisEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|MyBatisEndpoint
name|answer
init|=
operator|new
name|MyBatisEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|remaining
argument_list|)
decl_stmt|;
name|setProperties
argument_list|(
name|answer
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|createSqlSessionFactory ()
specifier|protected
name|SqlSessionFactory
name|createSqlSessionFactory
parameter_list|()
throws|throws
name|IOException
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|configurationUri
argument_list|,
literal|"configurationUri"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|InputStream
name|is
init|=
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsInputStream
argument_list|(
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
argument_list|,
name|configurationUri
argument_list|)
decl_stmt|;
try|try
block|{
return|return
operator|new
name|SqlSessionFactoryBuilder
argument_list|()
operator|.
name|build
argument_list|(
name|is
argument_list|)
return|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getSqlSessionFactory ()
specifier|public
name|SqlSessionFactory
name|getSqlSessionFactory
parameter_list|()
block|{
return|return
name|sqlSessionFactory
return|;
block|}
DECL|method|setSqlSessionFactory (SqlSessionFactory sqlSessionFactory)
specifier|public
name|void
name|setSqlSessionFactory
parameter_list|(
name|SqlSessionFactory
name|sqlSessionFactory
parameter_list|)
block|{
name|this
operator|.
name|sqlSessionFactory
operator|=
name|sqlSessionFactory
expr_stmt|;
block|}
DECL|method|getConfigurationUri ()
specifier|public
name|String
name|getConfigurationUri
parameter_list|()
block|{
return|return
name|configurationUri
return|;
block|}
DECL|method|setConfigurationUri (String configurationUri)
specifier|public
name|void
name|setConfigurationUri
parameter_list|(
name|String
name|configurationUri
parameter_list|)
block|{
name|this
operator|.
name|configurationUri
operator|=
name|configurationUri
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
if|if
condition|(
name|sqlSessionFactory
operator|==
literal|null
condition|)
block|{
name|sqlSessionFactory
operator|=
name|createSqlSessionFactory
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

