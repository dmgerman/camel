begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.extension
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|extension
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
name|Component
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
DECL|class|ComponentExtensionHelper
specifier|public
specifier|final
class|class
name|ComponentExtensionHelper
block|{
DECL|method|ComponentExtensionHelper ()
specifier|private
name|ComponentExtensionHelper
parameter_list|()
block|{     }
comment|/**      * @deprecated use {@link ObjectHelper#trySetCamelContext(Object, CamelContext)}      */
annotation|@
name|Deprecated
DECL|method|trySetCamelContext (T object, CamelContext camelContext)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|trySetCamelContext
parameter_list|(
name|T
name|object
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|)
block|{
return|return
name|ObjectHelper
operator|.
name|trySetCamelContext
argument_list|(
name|object
argument_list|,
name|camelContext
argument_list|)
return|;
block|}
comment|/**      * @deprecated use {@link ObjectHelper#trySetComponent(Object, Component)}      */
annotation|@
name|Deprecated
DECL|method|trySetComponent (T object, Component component)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|trySetComponent
parameter_list|(
name|T
name|object
parameter_list|,
name|Component
name|component
parameter_list|)
block|{
return|return
name|ObjectHelper
operator|.
name|trySetComponent
argument_list|(
name|object
argument_list|,
name|component
argument_list|)
return|;
block|}
block|}
end_class

end_unit

