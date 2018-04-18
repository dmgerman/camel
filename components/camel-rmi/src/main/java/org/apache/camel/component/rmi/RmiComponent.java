begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.rmi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|rmi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|RmiComponent
specifier|public
class|class
name|RmiComponent
extends|extends
name|UriEndpointComponent
block|{
DECL|method|RmiComponent ()
specifier|public
name|RmiComponent
parameter_list|()
block|{
name|super
argument_list|(
name|RmiEndpoint
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
name|RmiEndpoint
name|rmi
init|=
operator|new
name|RmiEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|)
decl_stmt|;
comment|// lookup remote interfaces
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"remoteInterfaces"
argument_list|,
name|Iterator
operator|.
name|class
argument_list|)
decl_stmt|;
while|while
condition|(
name|it
operator|!=
literal|null
operator|&&
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|next
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|Class
operator|.
name|class
argument_list|,
name|next
argument_list|)
decl_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|clazz
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|classes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|interfaces
init|=
name|classes
decl_stmt|;
name|rmi
operator|.
name|setRemoteInterfaces
argument_list|(
name|interfaces
argument_list|)
expr_stmt|;
block|}
name|setProperties
argument_list|(
name|rmi
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|rmi
return|;
block|}
block|}
end_class

end_unit

