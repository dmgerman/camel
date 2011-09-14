begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
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
name|api
operator|.
name|management
operator|.
name|ManagedAttribute
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
name|api
operator|.
name|management
operator|.
name|ManagedResource
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
name|mock
operator|.
name|MockEndpoint
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
name|spi
operator|.
name|ManagementAware
import|;
end_import

begin_comment
comment|/**  * CustomEndpoint is used to test {@link org.apache.camel.management.JmxInstrumentationCustomMBeanTest}  * and must be declared a public class otherwise the mbean server connection cannot access its methods.  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
comment|// START SNIPPET: e1
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Our custom managed endpoint"
argument_list|)
DECL|class|CustomEndpoint
specifier|public
class|class
name|CustomEndpoint
extends|extends
name|MockEndpoint
implements|implements
name|ManagementAware
argument_list|<
name|CustomEndpoint
argument_list|>
block|{
DECL|method|CustomEndpoint (final String endpointUri, final Component component)
specifier|public
name|CustomEndpoint
parameter_list|(
specifier|final
name|String
name|endpointUri
parameter_list|,
specifier|final
name|Component
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
DECL|method|getManagedObject (CustomEndpoint object)
specifier|public
name|Object
name|getManagedObject
parameter_list|(
name|CustomEndpoint
name|object
parameter_list|)
block|{
return|return
name|this
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|createEndpointUri ()
specifier|protected
name|String
name|createEndpointUri
parameter_list|()
block|{
return|return
literal|"custom"
return|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|getFoo ()
specifier|public
name|String
name|getFoo
parameter_list|()
block|{
return|return
literal|"bar"
return|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|getEndpointUri ()
specifier|public
name|String
name|getEndpointUri
parameter_list|()
block|{
return|return
name|super
operator|.
name|getEndpointUri
argument_list|()
return|;
block|}
block|}
end_class

begin_comment
comment|// END SNIPPET: e1
end_comment

end_unit

