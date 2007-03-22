begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|javax
operator|.
name|jbi
operator|.
name|management
operator|.
name|DeploymentException
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
name|Deployer
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
name|ServiceMixComponent
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
name|ServiceUnit
import|;
end_import

begin_comment
comment|/**  * Deploys service units  * @version $Revision: 426415 $  */
end_comment

begin_class
DECL|class|CamelContainerDeployer
specifier|public
class|class
name|CamelContainerDeployer
implements|implements
name|Deployer
block|{
DECL|field|builders
specifier|private
name|RouteBuilder
index|[]
name|builders
decl_stmt|;
DECL|field|serviceUnit
name|ServiceUnit
name|serviceUnit
decl_stmt|;
DECL|method|CamelContainerDeployer (ServiceMixComponent component,RouteBuilder[] builders)
name|CamelContainerDeployer
parameter_list|(
name|ServiceMixComponent
name|component
parameter_list|,
name|RouteBuilder
index|[]
name|builders
parameter_list|)
block|{
name|this
operator|.
name|serviceUnit
operator|=
operator|new
name|ServiceUnit
argument_list|(
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|builders
operator|=
name|builders
expr_stmt|;
comment|//need to wire-up here
block|}
comment|/**      * @param serviceUnitName      * @param serviceUnitRootPath      * @return      * @see org.apache.servicemix.common.Deployer#canDeploy(java.lang.String, java.lang.String)      */
DECL|method|canDeploy (String serviceUnitName,String serviceUnitRootPath)
specifier|public
name|boolean
name|canDeploy
parameter_list|(
name|String
name|serviceUnitName
parameter_list|,
name|String
name|serviceUnitRootPath
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
return|return
literal|false
return|;
block|}
comment|/**      * @param serviceUnitName      * @param serviceUnitRootPath      * @return      * @throws DeploymentException      * @see org.apache.servicemix.common.Deployer#deploy(java.lang.String, java.lang.String)      */
DECL|method|deploy (String serviceUnitName,String serviceUnitRootPath)
specifier|public
name|ServiceUnit
name|deploy
parameter_list|(
name|String
name|serviceUnitName
parameter_list|,
name|String
name|serviceUnitRootPath
parameter_list|)
throws|throws
name|DeploymentException
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
comment|/**      * @param su      * @throws DeploymentException      * @see org.apache.servicemix.common.Deployer#undeploy(org.apache.servicemix.common.ServiceUnit)      */
DECL|method|undeploy (ServiceUnit su)
specifier|public
name|void
name|undeploy
parameter_list|(
name|ServiceUnit
name|su
parameter_list|)
throws|throws
name|DeploymentException
block|{
comment|// TODO Auto-generated method stub
block|}
block|}
end_class

end_unit

