begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bonita.api
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bonita
operator|.
name|api
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|client
operator|.
name|WebTarget
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|GenericType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MediaType
import|;
end_import

begin_import
import|import static
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|client
operator|.
name|Entity
operator|.
name|entity
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
name|bonita
operator|.
name|api
operator|.
name|model
operator|.
name|CaseCreationResponse
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
name|bonita
operator|.
name|api
operator|.
name|model
operator|.
name|ProcessDefinitionResponse
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
name|bonita
operator|.
name|api
operator|.
name|util
operator|.
name|BonitaAPIConfig
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
name|bonita
operator|.
name|api
operator|.
name|util
operator|.
name|BonitaAPIUtil
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
DECL|class|BonitaAPI
specifier|public
class|class
name|BonitaAPI
block|{
DECL|field|bonitaApiConfig
specifier|private
name|BonitaAPIConfig
name|bonitaApiConfig
decl_stmt|;
DECL|field|webTarget
specifier|private
name|WebTarget
name|webTarget
decl_stmt|;
DECL|method|BonitaAPI (BonitaAPIConfig bonitaApiConfig, WebTarget webTarget)
specifier|protected
name|BonitaAPI
parameter_list|(
name|BonitaAPIConfig
name|bonitaApiConfig
parameter_list|,
name|WebTarget
name|webTarget
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|bonitaApiConfig
operator|=
name|bonitaApiConfig
expr_stmt|;
name|this
operator|.
name|webTarget
operator|=
name|webTarget
expr_stmt|;
block|}
DECL|method|getBaseResource ()
specifier|private
name|WebTarget
name|getBaseResource
parameter_list|()
block|{
return|return
name|webTarget
return|;
block|}
DECL|method|getProcessDefinition (String processName)
specifier|public
name|ProcessDefinitionResponse
name|getProcessDefinition
parameter_list|(
name|String
name|processName
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|processName
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"processName is empty."
argument_list|)
throw|;
block|}
name|WebTarget
name|resource
init|=
name|getBaseResource
argument_list|()
operator|.
name|path
argument_list|(
literal|"process"
argument_list|)
operator|.
name|queryParam
argument_list|(
literal|"s"
argument_list|,
name|processName
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ProcessDefinitionResponse
argument_list|>
name|listProcess
init|=
name|resource
operator|.
name|request
argument_list|()
operator|.
name|accept
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
operator|.
name|get
argument_list|(
operator|new
name|GenericType
argument_list|<
name|List
argument_list|<
name|ProcessDefinitionResponse
argument_list|>
argument_list|>
argument_list|()
block|{                         }
argument_list|)
decl_stmt|;
if|if
condition|(
name|listProcess
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
return|return
name|listProcess
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"The process with name "
operator|+
name|processName
operator|+
literal|" has not been retrieved"
argument_list|)
throw|;
block|}
block|}
DECL|method|startCase (ProcessDefinitionResponse processDefinition, Map<String, Serializable> rawInputs)
specifier|public
name|CaseCreationResponse
name|startCase
parameter_list|(
name|ProcessDefinitionResponse
name|processDefinition
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Serializable
argument_list|>
name|rawInputs
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|processDefinition
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"ProcessDefinition is null"
argument_list|)
throw|;
block|}
if|if
condition|(
name|rawInputs
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The contract input is null"
argument_list|)
throw|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Serializable
argument_list|>
name|inputs
init|=
name|BonitaAPIUtil
operator|.
name|getInstance
argument_list|(
name|bonitaApiConfig
argument_list|)
operator|.
name|prepareInputs
argument_list|(
name|processDefinition
argument_list|,
name|rawInputs
argument_list|)
decl_stmt|;
name|WebTarget
name|resource
init|=
name|getBaseResource
argument_list|()
operator|.
name|path
argument_list|(
literal|"process/{processId}/instantiation"
argument_list|)
operator|.
name|resolveTemplate
argument_list|(
literal|"processId"
argument_list|,
name|processDefinition
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|resource
operator|.
name|request
argument_list|()
operator|.
name|accept
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
operator|.
name|post
argument_list|(
name|entity
argument_list|(
name|inputs
argument_list|,
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
argument_list|,
name|CaseCreationResponse
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|getBonitaApiConfig ()
specifier|public
name|BonitaAPIConfig
name|getBonitaApiConfig
parameter_list|()
block|{
return|return
name|bonitaApiConfig
return|;
block|}
DECL|method|setBonitaApiConfig (BonitaAPIConfig bonitaApiConfig)
specifier|public
name|void
name|setBonitaApiConfig
parameter_list|(
name|BonitaAPIConfig
name|bonitaApiConfig
parameter_list|)
block|{
name|this
operator|.
name|bonitaApiConfig
operator|=
name|bonitaApiConfig
expr_stmt|;
block|}
block|}
end_class

end_unit

