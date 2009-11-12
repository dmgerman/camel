begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.velocity
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|velocity
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
name|impl
operator|.
name|DefaultComponent
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
name|velocity
operator|.
name|app
operator|.
name|VelocityEngine
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|VelocityComponent
specifier|public
class|class
name|VelocityComponent
extends|extends
name|DefaultComponent
block|{
DECL|field|velocityEngine
specifier|private
name|VelocityEngine
name|velocityEngine
decl_stmt|;
DECL|method|getVelocityEngine ()
specifier|public
name|VelocityEngine
name|getVelocityEngine
parameter_list|()
block|{
return|return
name|velocityEngine
return|;
block|}
DECL|method|setVelocityEngine (VelocityEngine velocityEngine)
specifier|public
name|void
name|setVelocityEngine
parameter_list|(
name|VelocityEngine
name|velocityEngine
parameter_list|)
block|{
name|this
operator|.
name|velocityEngine
operator|=
name|velocityEngine
expr_stmt|;
block|}
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
name|String
name|propertiesFile
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"propertiesFile"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|encoding
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"encoding"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|boolean
name|cache
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"contentCache"
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
decl_stmt|;
name|VelocityEndpoint
name|answer
init|=
operator|new
name|VelocityEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|remaining
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setContentCache
argument_list|(
name|cache
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setPropertiesFile
argument_list|(
name|propertiesFile
argument_list|)
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|encoding
argument_list|)
condition|)
block|{
name|answer
operator|.
name|setEncoding
argument_list|(
name|encoding
argument_list|)
expr_stmt|;
block|}
name|answer
operator|.
name|setVelocityEngine
argument_list|(
name|velocityEngine
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

