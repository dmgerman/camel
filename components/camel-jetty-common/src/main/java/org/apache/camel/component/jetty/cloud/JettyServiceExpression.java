begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jetty.cloud
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
operator|.
name|cloud
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
name|impl
operator|.
name|cloud
operator|.
name|DefaultServiceCallExpression
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

begin_class
DECL|class|JettyServiceExpression
specifier|public
specifier|final
class|class
name|JettyServiceExpression
extends|extends
name|DefaultServiceCallExpression
block|{
DECL|method|JettyServiceExpression ()
specifier|public
name|JettyServiceExpression
parameter_list|()
block|{     }
DECL|method|JettyServiceExpression (String hostHeader, String portHeader)
specifier|public
name|JettyServiceExpression
parameter_list|(
name|String
name|hostHeader
parameter_list|,
name|String
name|portHeader
parameter_list|)
block|{
name|super
argument_list|(
name|hostHeader
argument_list|,
name|portHeader
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doBuildCamelEndpointUri (String host, Integer port, String contextPath, String scheme)
specifier|protected
name|String
name|doBuildCamelEndpointUri
parameter_list|(
name|String
name|host
parameter_list|,
name|Integer
name|port
parameter_list|,
name|String
name|contextPath
parameter_list|,
name|String
name|scheme
parameter_list|)
block|{
if|if
condition|(
operator|!
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|scheme
argument_list|,
literal|"jetty"
argument_list|)
condition|)
block|{
return|return
name|super
operator|.
name|doBuildCamelEndpointUri
argument_list|(
name|host
argument_list|,
name|port
argument_list|,
name|contextPath
argument_list|,
name|scheme
argument_list|)
return|;
block|}
name|String
name|answer
init|=
name|scheme
operator|+
literal|":http://"
operator|+
name|host
decl_stmt|;
if|if
condition|(
name|port
operator|!=
literal|null
condition|)
block|{
name|answer
operator|=
name|answer
operator|+
literal|":"
operator|+
name|port
expr_stmt|;
block|}
if|if
condition|(
name|contextPath
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|contextPath
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|contextPath
operator|=
literal|"/"
operator|+
name|contextPath
expr_stmt|;
block|}
name|answer
operator|+=
name|contextPath
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

