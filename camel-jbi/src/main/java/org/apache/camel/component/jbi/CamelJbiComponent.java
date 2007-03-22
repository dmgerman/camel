begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements. See the NOTICE  * file distributed with this work for additional information regarding copyright ownership. The ASF licenses this file  * to You under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the  * License. You may obtain a copy of the License at  *   * http://www.apache.org/licenses/LICENSE-2.0  *   * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on  * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the  * specific language governing permissions and limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jbi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jbi
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|servicemix
operator|.
name|common
operator|.
name|DefaultComponent
import|;
end_import

begin_comment
comment|/**  * Deploys the camel endpoints within JBI  *   * @version $Revision: 426415 $  */
end_comment

begin_class
DECL|class|CamelJbiComponent
specifier|public
class|class
name|CamelJbiComponent
extends|extends
name|DefaultComponent
block|{
DECL|field|endpoints
specifier|private
name|CamelJbiEndpoint
index|[]
name|endpoints
decl_stmt|;
comment|/**      * @return the endpoints      */
DECL|method|getEndpoints ()
specifier|public
name|CamelJbiEndpoint
index|[]
name|getEndpoints
parameter_list|()
block|{
return|return
name|this
operator|.
name|endpoints
return|;
block|}
comment|/**      * @param endpoints the endpoints to set      */
DECL|method|setEndpoints (CamelJbiEndpoint[] endpoints)
specifier|public
name|void
name|setEndpoints
parameter_list|(
name|CamelJbiEndpoint
index|[]
name|endpoints
parameter_list|)
block|{
name|this
operator|.
name|endpoints
operator|=
name|endpoints
expr_stmt|;
block|}
comment|/**      * @return List of endpoints      * @see org.apache.servicemix.common.DefaultComponent#getConfiguredEndpoints()      */
DECL|method|getConfiguredEndpoints ()
annotation|@
name|Override
specifier|protected
name|List
name|getConfiguredEndpoints
parameter_list|()
block|{
return|return
name|asList
argument_list|(
name|getEndpoints
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * @return Class[]      * @see org.apache.servicemix.common.DefaultComponent#getEndpointClasses()      */
DECL|method|getEndpointClasses ()
annotation|@
name|Override
specifier|protected
name|Class
index|[]
name|getEndpointClasses
parameter_list|()
block|{
return|return
operator|new
name|Class
index|[]
block|{
name|CamelJbiEndpoint
operator|.
name|class
block|}
return|;
block|}
block|}
end_class

end_unit

