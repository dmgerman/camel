begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.linkedin
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|linkedin
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|WebApplicationException
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
name|Response
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
name|RuntimeCamelException
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
name|linkedin
operator|.
name|api
operator|.
name|LinkedInException
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
name|linkedin
operator|.
name|api
operator|.
name|model
operator|.
name|Error
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
name|linkedin
operator|.
name|internal
operator|.
name|LinkedInApiName
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
name|linkedin
operator|.
name|internal
operator|.
name|LinkedInPropertiesHelper
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
name|support
operator|.
name|component
operator|.
name|AbstractApiProducer
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
name|support
operator|.
name|component
operator|.
name|ApiMethod
import|;
end_import

begin_comment
comment|/**  * The LinkedIn producer.  */
end_comment

begin_class
DECL|class|LinkedInProducer
specifier|public
class|class
name|LinkedInProducer
extends|extends
name|AbstractApiProducer
argument_list|<
name|LinkedInApiName
argument_list|,
name|LinkedInConfiguration
argument_list|>
block|{
DECL|method|LinkedInProducer (LinkedInEndpoint endpoint)
specifier|public
name|LinkedInProducer
parameter_list|(
name|LinkedInEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|LinkedInPropertiesHelper
operator|.
name|getHelper
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doInvokeMethod (ApiMethod method, Map<String, Object> properties)
specifier|protected
name|Object
name|doInvokeMethod
parameter_list|(
name|ApiMethod
name|method
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
throws|throws
name|RuntimeCamelException
block|{
try|try
block|{
return|return
name|super
operator|.
name|doInvokeMethod
argument_list|(
name|method
argument_list|,
name|properties
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|RuntimeCamelException
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|WebApplicationException
condition|)
block|{
specifier|final
name|WebApplicationException
name|cause
init|=
operator|(
name|WebApplicationException
operator|)
name|e
operator|.
name|getCause
argument_list|()
decl_stmt|;
specifier|final
name|Response
name|response
init|=
name|cause
operator|.
name|getResponse
argument_list|()
decl_stmt|;
if|if
condition|(
name|response
operator|.
name|hasEntity
argument_list|()
condition|)
block|{
comment|// try and convert it to LinkedInException
specifier|final
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|linkedin
operator|.
name|api
operator|.
name|model
operator|.
name|Error
name|error
init|=
name|response
operator|.
name|readEntity
argument_list|(
name|Error
operator|.
name|class
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Error invoking %s: %s"
argument_list|,
name|method
operator|.
name|getName
argument_list|()
argument_list|,
name|error
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|,
operator|new
name|LinkedInException
argument_list|(
name|error
argument_list|,
name|response
argument_list|)
argument_list|)
throw|;
block|}
block|}
throw|throw
name|e
throw|;
block|}
block|}
block|}
end_class

end_unit

