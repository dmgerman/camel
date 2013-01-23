begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ejb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ejb
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
name|component
operator|.
name|bean
operator|.
name|RegistryBean
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
name|spi
operator|.
name|Registry
import|;
end_import

begin_comment
comment|/**  * An implementation of a {@link org.apache.camel.component.bean.BeanHolder} which will look up  * an EJB bean from the JNDI {@link javax.naming.Context}  *  * @version   */
end_comment

begin_class
DECL|class|EjbRegistryBean
specifier|public
class|class
name|EjbRegistryBean
extends|extends
name|RegistryBean
block|{
DECL|field|registry
specifier|private
name|Registry
name|registry
decl_stmt|;
DECL|method|EjbRegistryBean (Registry registry, CamelContext context, String name)
specifier|public
name|EjbRegistryBean
parameter_list|(
name|Registry
name|registry
parameter_list|,
name|CamelContext
name|context
parameter_list|,
name|String
name|name
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
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"ejb: "
operator|+
name|getName
argument_list|()
return|;
block|}
DECL|method|lookupBean ()
specifier|protected
name|Object
name|lookupBean
parameter_list|()
block|{
return|return
name|registry
operator|.
name|lookupByName
argument_list|(
name|getName
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

