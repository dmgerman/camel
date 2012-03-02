begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.stub
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|stub
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
name|component
operator|.
name|vm
operator|.
name|VmComponent
import|;
end_import

begin_comment
comment|/**   * Allows you to easily stub out a middleware transport by prefixing the URI with "stub:" which is   * handy for testing out routes, or isolating bits of middleware.  *  */
end_comment

begin_class
DECL|class|StubComponent
specifier|public
class|class
name|StubComponent
extends|extends
name|VmComponent
block|{
DECL|method|StubComponent ()
specifier|public
name|StubComponent
parameter_list|()
block|{ 	}
annotation|@
name|Override
DECL|method|validateURI (String uri, String path, Map<String, Object> parameters)
specifier|protected
name|void
name|validateURI
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|path
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
comment|// Don't validate so we can stub any URI
block|}
annotation|@
name|Override
DECL|method|validateParameters (String uri, Map<String, Object> parameters, String optionPrefix)
specifier|protected
name|void
name|validateParameters
parameter_list|(
name|String
name|uri
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|,
name|String
name|optionPrefix
parameter_list|)
block|{
comment|// Don't validate so we can stub any URI
block|}
block|}
end_class

end_unit

