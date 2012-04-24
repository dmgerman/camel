begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cdi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cdi
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
name|impl
operator|.
name|DefaultCamelContext
import|;
end_import

begin_comment
comment|/**  * CDI {@link org.apache.camel.CamelContext} class.  */
end_comment

begin_class
DECL|class|CdiCamelContext
specifier|public
class|class
name|CdiCamelContext
extends|extends
name|DefaultCamelContext
block|{
DECL|method|CdiCamelContext ()
specifier|public
name|CdiCamelContext
parameter_list|()
block|{
name|setRegistry
argument_list|(
operator|new
name|CdiBeanRegistry
argument_list|()
argument_list|)
expr_stmt|;
name|setInjector
argument_list|(
operator|new
name|CdiInjector
argument_list|(
name|getInjector
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

