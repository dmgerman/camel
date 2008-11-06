begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.spring
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
operator|.
name|spring
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
name|cxf
operator|.
name|frontend
operator|.
name|AbstractWSDLBasedEndpointFactory
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

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|CxfEndpointBean
specifier|public
class|class
name|CxfEndpointBean
extends|extends
name|AbstractWSDLBasedEndpointFactory
block|{
DECL|field|handlers
specifier|private
name|List
name|handlers
decl_stmt|;
DECL|method|CxfEndpointBean ()
specifier|public
name|CxfEndpointBean
parameter_list|()
block|{
name|setServiceFactory
argument_list|(
operator|new
name|ReflectionServiceFactoryBean
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getHandlers ()
specifier|public
name|List
name|getHandlers
parameter_list|()
block|{
return|return
name|handlers
return|;
block|}
DECL|method|setHandlers (List handlers)
specifier|public
name|void
name|setHandlers
parameter_list|(
name|List
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
block|}
end_class

end_unit

