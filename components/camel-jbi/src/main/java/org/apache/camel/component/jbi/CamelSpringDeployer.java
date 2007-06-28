begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|spring
operator|.
name|SpringCamelContext
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
name|xbean
operator|.
name|AbstractXBeanDeployer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xbean
operator|.
name|kernel
operator|.
name|Kernel
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xbean
operator|.
name|server
operator|.
name|spring
operator|.
name|loader
operator|.
name|PureSpringLoader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xbean
operator|.
name|server
operator|.
name|spring
operator|.
name|loader
operator|.
name|SpringLoader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContext
import|;
end_import

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
name|Collection
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

begin_comment
comment|/**  * A deployer of the spring XML file  *  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|CamelSpringDeployer
specifier|public
class|class
name|CamelSpringDeployer
extends|extends
name|AbstractXBeanDeployer
block|{
DECL|field|springLoader
specifier|private
name|PureSpringLoader
name|springLoader
init|=
operator|new
name|PureSpringLoader
argument_list|()
decl_stmt|;
DECL|field|component
specifier|private
specifier|final
name|CamelJbiComponent
name|component
decl_stmt|;
DECL|method|CamelSpringDeployer (CamelJbiComponent component)
specifier|public
name|CamelSpringDeployer
parameter_list|(
name|CamelJbiComponent
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|component
operator|=
name|component
expr_stmt|;
block|}
DECL|method|getXBeanFile ()
specifier|protected
name|String
name|getXBeanFile
parameter_list|()
block|{
return|return
literal|"camel-context"
return|;
block|}
DECL|method|getServices (Kernel kernel)
specifier|protected
name|List
name|getServices
parameter_list|(
name|Kernel
name|kernel
parameter_list|)
block|{
try|try
block|{
name|List
name|services
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|ApplicationContext
name|applicationContext
init|=
name|springLoader
operator|.
name|getApplicationContext
argument_list|()
decl_stmt|;
name|SpringCamelContext
name|camelContext
init|=
name|SpringCamelContext
operator|.
name|springCamelContext
argument_list|(
name|applicationContext
argument_list|)
decl_stmt|;
comment|// now lets iterate through all the endpoints
name|Collection
argument_list|<
name|Endpoint
argument_list|>
name|endpoints
init|=
name|camelContext
operator|.
name|getSingletonEndpoints
argument_list|()
decl_stmt|;
for|for
control|(
name|Endpoint
name|endpoint
range|:
name|endpoints
control|)
block|{
if|if
condition|(
name|component
operator|.
name|isEndpointExposedOnNmr
argument_list|(
name|endpoint
argument_list|)
condition|)
block|{
name|services
operator|.
name|add
argument_list|(
name|component
operator|.
name|createJbiEndpointFromCamel
argument_list|(
name|endpoint
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|services
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|createSpringLoader ()
specifier|protected
name|SpringLoader
name|createSpringLoader
parameter_list|()
block|{
return|return
name|springLoader
return|;
block|}
block|}
end_class

end_unit

