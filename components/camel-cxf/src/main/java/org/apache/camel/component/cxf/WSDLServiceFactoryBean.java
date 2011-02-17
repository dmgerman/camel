begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
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
name|List
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|factory
operator|.
name|AbstractServiceConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|factory
operator|.
name|ReflectionServiceFactoryBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|invoker
operator|.
name|Invoker
import|;
end_import

begin_comment
comment|/**  * A service factory bean class that create a service factory without requiring a service class  * (SEI).  * It will pick the first one service name and first one port/endpoint name in the WSDL, if   * there is service name or port/endpoint name setted.  * @version   */
end_comment

begin_class
DECL|class|WSDLServiceFactoryBean
specifier|public
class|class
name|WSDLServiceFactoryBean
extends|extends
name|ReflectionServiceFactoryBean
block|{
DECL|method|WSDLServiceFactoryBean ()
specifier|public
name|WSDLServiceFactoryBean
parameter_list|()
block|{
comment|// set up the service configure to help us find the service name and endpoint name from WSDL
name|WSDLServiceConfiguration
name|configuration
init|=
operator|new
name|WSDLServiceConfiguration
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|AbstractServiceConfiguration
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|AbstractServiceConfiguration
argument_list|>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|this
operator|.
name|setServiceConfigurations
argument_list|(
name|list
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|initializeWSDLOperations ()
specifier|protected
name|void
name|initializeWSDLOperations
parameter_list|()
block|{
comment|// skip this operation that requires service class
block|}
annotation|@
name|Override
DECL|method|checkServiceClassAnnotations (Class<?> sc)
specifier|protected
name|void
name|checkServiceClassAnnotations
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|sc
parameter_list|)
block|{
comment|// skip this operation that requires service class
block|}
annotation|@
name|Override
DECL|method|createInvoker ()
specifier|protected
name|Invoker
name|createInvoker
parameter_list|()
block|{
comment|// Camel specific invoker will be set
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

