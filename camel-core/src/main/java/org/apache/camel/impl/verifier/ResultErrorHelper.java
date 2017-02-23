begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.verifier
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|verifier
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
name|java
operator|.
name|util
operator|.
name|Optional
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
name|ComponentVerifier
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
DECL|class|ResultErrorHelper
specifier|public
specifier|final
class|class
name|ResultErrorHelper
block|{
DECL|method|ResultErrorHelper ()
specifier|private
name|ResultErrorHelper
parameter_list|()
block|{     }
comment|// **********************************
comment|// Helpers
comment|// **********************************
DECL|method|requiresOption (String parameterName, Map<String, Object> parameters)
specifier|public
specifier|static
name|Optional
argument_list|<
name|ComponentVerifier
operator|.
name|Error
argument_list|>
name|requiresOption
parameter_list|(
name|String
name|parameterName
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|parameters
operator|.
name|get
argument_list|(
name|parameterName
argument_list|)
argument_list|)
condition|)
block|{
return|return
name|Optional
operator|.
name|of
argument_list|(
name|ResultErrorBuilder
operator|.
name|withMissingOption
argument_list|(
name|parameterName
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
return|;
block|}
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
block|}
end_class

end_unit

