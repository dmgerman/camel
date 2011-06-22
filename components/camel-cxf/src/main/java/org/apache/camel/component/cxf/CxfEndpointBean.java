begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|handler
operator|.
name|Handler
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
name|BusFactory
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
name|frontend
operator|.
name|AbstractServiceFactory
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
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|BeanNameAware
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|DisposableBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|NamedBean
import|;
end_import

begin_class
DECL|class|CxfEndpointBean
specifier|public
class|class
name|CxfEndpointBean
extends|extends
name|AbstractServiceFactory
implements|implements
name|DisposableBean
implements|,
name|BeanNameAware
implements|,
name|NamedBean
block|{
DECL|field|handlers
specifier|private
name|List
argument_list|<
name|Handler
argument_list|>
name|handlers
decl_stmt|;
DECL|field|schemaLocations
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|schemaLocations
decl_stmt|;
DECL|field|beanName
specifier|private
name|String
name|beanName
decl_stmt|;
DECL|method|CxfEndpointBean ()
specifier|public
name|CxfEndpointBean
parameter_list|()
block|{
name|this
argument_list|(
operator|new
name|ReflectionServiceFactoryBean
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|CxfEndpointBean (ReflectionServiceFactoryBean factory)
specifier|public
name|CxfEndpointBean
parameter_list|(
name|ReflectionServiceFactoryBean
name|factory
parameter_list|)
block|{
name|setServiceFactory
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
DECL|method|getHandlers ()
specifier|public
name|List
argument_list|<
name|Handler
argument_list|>
name|getHandlers
parameter_list|()
block|{
return|return
name|handlers
return|;
block|}
DECL|method|setHandlers (List<Handler> handlers)
specifier|public
name|void
name|setHandlers
parameter_list|(
name|List
argument_list|<
name|Handler
argument_list|>
name|handlers
parameter_list|)
block|{
name|this
operator|.
name|handlers
operator|=
name|handlers
expr_stmt|;
block|}
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Clean up the BusFactory's defaultBus
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|setBeanName (String name)
specifier|public
name|void
name|setBeanName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|beanName
operator|=
name|name
expr_stmt|;
block|}
DECL|method|getBeanName ()
specifier|public
name|String
name|getBeanName
parameter_list|()
block|{
return|return
name|beanName
return|;
block|}
DECL|method|setSchemaLocations (List<String> schemaLocations)
specifier|public
name|void
name|setSchemaLocations
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|schemaLocations
parameter_list|)
block|{
name|this
operator|.
name|schemaLocations
operator|=
name|schemaLocations
expr_stmt|;
block|}
DECL|method|getSchemaLocations ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getSchemaLocations
parameter_list|()
block|{
return|return
name|schemaLocations
return|;
block|}
block|}
end_class

end_unit

