begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
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
name|component
operator|.
name|pojo
operator|.
name|PojoEndpoint
import|;
end_import

begin_comment
comment|/**  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|BeanEndpoint
specifier|public
class|class
name|BeanEndpoint
extends|extends
name|PojoEndpoint
block|{
DECL|method|BeanEndpoint (String uri, BeanComponent component, String pojoName)
specifier|public
name|BeanEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|BeanComponent
name|component
parameter_list|,
name|String
name|pojoName
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|,
name|pojoName
argument_list|)
expr_stmt|;
block|}
DECL|method|getBeanComponent ()
specifier|public
name|BeanComponent
name|getBeanComponent
parameter_list|()
block|{
return|return
operator|(
name|BeanComponent
operator|)
name|super
operator|.
name|getComponent
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|lookupService ()
specifier|protected
name|Object
name|lookupService
parameter_list|()
block|{
return|return
name|getBeanComponent
argument_list|()
operator|.
name|getApplicationContext
argument_list|()
operator|.
name|getBean
argument_list|(
name|getPojoName
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

