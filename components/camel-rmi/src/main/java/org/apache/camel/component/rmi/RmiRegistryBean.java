begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|rmi
operator|.
name|AccessException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|rmi
operator|.
name|NotBoundException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|rmi
operator|.
name|RemoteException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|rmi
operator|.
name|registry
operator|.
name|Registry
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
name|NoSuchBeanException
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
name|RuntimeCamelException
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
name|bean
operator|.
name|ParameterMappingStrategy
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
name|bean
operator|.
name|RegistryBean
import|;
end_import

begin_class
DECL|class|RmiRegistryBean
specifier|public
class|class
name|RmiRegistryBean
extends|extends
name|RegistryBean
block|{
DECL|field|registry
specifier|private
specifier|final
name|Registry
name|registry
decl_stmt|;
DECL|method|RmiRegistryBean (CamelContext context, String name, Registry registry)
specifier|public
name|RmiRegistryBean
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|String
name|name
parameter_list|,
name|Registry
name|registry
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|this
operator|.
name|registry
operator|=
name|registry
expr_stmt|;
block|}
DECL|method|RmiRegistryBean (CamelContext context, String name, ParameterMappingStrategy parameterMappingStrategy, Registry registry)
specifier|public
name|RmiRegistryBean
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|String
name|name
parameter_list|,
name|ParameterMappingStrategy
name|parameterMappingStrategy
parameter_list|,
name|Registry
name|registry
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|this
operator|.
name|registry
operator|=
name|registry
expr_stmt|;
name|setParameterMappingStrategy
argument_list|(
name|parameterMappingStrategy
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|lookupBean ()
specifier|protected
name|Object
name|lookupBean
parameter_list|()
throws|throws
name|NoSuchBeanException
block|{
try|try
block|{
return|return
name|registry
operator|.
name|lookup
argument_list|(
name|getName
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NotBoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|NoSuchBeanException
argument_list|(
name|getName
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|AccessException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|RemoteException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

