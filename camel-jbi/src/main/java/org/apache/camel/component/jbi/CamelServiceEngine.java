begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|RouteBuilder
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
name|BaseComponent
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
name|BaseServiceUnitManager
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
name|Deployer
import|;
end_import

begin_comment
comment|/**  * Deploys the camel endpoints within JBI  * @version $Revision: 426415 $  */
end_comment

begin_class
DECL|class|CamelServiceEngine
specifier|public
class|class
name|CamelServiceEngine
extends|extends
name|BaseComponent
block|{
DECL|field|builders
specifier|private
name|RouteBuilder
index|[]
name|builders
decl_stmt|;
comment|/* (non-Javadoc)      * @see org.servicemix.common.BaseComponent#createServiceUnitManager()      */
DECL|method|createServiceUnitManager ()
specifier|public
name|BaseServiceUnitManager
name|createServiceUnitManager
parameter_list|()
block|{
name|Deployer
index|[]
name|deployers
init|=
operator|new
name|Deployer
index|[]
block|{
operator|new
name|CamelContainerDeployer
argument_list|(
name|this
argument_list|,
name|builders
argument_list|)
block|}
decl_stmt|;
return|return
operator|new
name|BaseServiceUnitManager
argument_list|(
name|this
argument_list|,
name|deployers
argument_list|)
return|;
block|}
comment|/**      * @return the builders      */
DECL|method|getBuilders ()
specifier|public
name|RouteBuilder
index|[]
name|getBuilders
parameter_list|()
block|{
return|return
name|this
operator|.
name|builders
return|;
block|}
comment|/**      * @param builders the builders to set      */
DECL|method|setBuilders (RouteBuilder[] builders)
specifier|public
name|void
name|setBuilders
parameter_list|(
name|RouteBuilder
index|[]
name|builders
parameter_list|)
block|{
name|this
operator|.
name|builders
operator|=
name|builders
expr_stmt|;
block|}
block|}
end_class

end_unit

