begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.yql
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|yql
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
name|component
operator|.
name|yql
operator|.
name|configuration
operator|.
name|YqlConfiguration
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
name|yql
operator|.
name|configuration
operator|.
name|YqlConfigurationValidator
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
name|DefaultComponent
import|;
end_import

begin_class
DECL|class|YqlComponent
specifier|public
class|class
name|YqlComponent
extends|extends
name|DefaultComponent
block|{
annotation|@
name|Override
DECL|method|createEndpoint (final String uri, final String remaining, final Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
specifier|final
name|String
name|uri
parameter_list|,
specifier|final
name|String
name|remaining
parameter_list|,
specifier|final
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
specifier|final
name|YqlConfiguration
name|configuration
init|=
operator|new
name|YqlConfiguration
argument_list|()
decl_stmt|;
name|configuration
operator|.
name|setQuery
argument_list|(
name|remaining
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setFormat
argument_list|(
operator|(
name|String
operator|)
name|parameters
operator|.
name|getOrDefault
argument_list|(
literal|"format"
argument_list|,
literal|"json"
argument_list|)
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setDiagnostics
argument_list|(
name|Boolean
operator|.
name|getBoolean
argument_list|(
operator|(
name|String
operator|)
name|parameters
operator|.
name|getOrDefault
argument_list|(
literal|"diagnostics"
argument_list|,
literal|"false"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setCallback
argument_list|(
operator|(
name|String
operator|)
name|parameters
operator|.
name|getOrDefault
argument_list|(
literal|"callback"
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|Endpoint
name|endpoint
init|=
operator|new
name|YqlEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|configuration
argument_list|)
decl_stmt|;
name|YqlConfigurationValidator
operator|.
name|validateProperties
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|configuration
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
block|}
end_class

end_unit

