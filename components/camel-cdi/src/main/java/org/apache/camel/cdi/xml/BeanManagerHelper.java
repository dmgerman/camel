begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.cdi.xml
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cdi
operator|.
name|xml
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|Bean
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|BeanManager
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

begin_class
DECL|class|BeanManagerHelper
specifier|final
class|class
name|BeanManagerHelper
block|{
DECL|method|BeanManagerHelper ()
specifier|private
name|BeanManagerHelper
parameter_list|()
block|{     }
DECL|method|getCamelContextById (BeanManager manager, String camelContextId)
specifier|static
name|CamelContext
name|getCamelContextById
parameter_list|(
name|BeanManager
name|manager
parameter_list|,
name|String
name|camelContextId
parameter_list|)
block|{
name|Bean
argument_list|<
name|?
argument_list|>
name|bean
init|=
name|manager
operator|.
name|resolve
argument_list|(
name|manager
operator|.
name|getBeans
argument_list|(
name|camelContextId
argument_list|)
argument_list|)
decl_stmt|;
return|return
operator|(
name|CamelContext
operator|)
name|manager
operator|.
name|getReference
argument_list|(
name|bean
argument_list|,
name|CamelContext
operator|.
name|class
argument_list|,
name|manager
operator|.
name|createCreationalContext
argument_list|(
name|bean
argument_list|)
argument_list|)
return|;
block|}
DECL|method|getDefaultCamelContext (BeanManager manager)
specifier|static
name|CamelContext
name|getDefaultCamelContext
parameter_list|(
name|BeanManager
name|manager
parameter_list|)
block|{
name|Bean
argument_list|<
name|?
argument_list|>
name|bean
init|=
name|manager
operator|.
name|resolve
argument_list|(
name|manager
operator|.
name|getBeans
argument_list|(
name|CamelContext
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
return|return
operator|(
name|CamelContext
operator|)
name|manager
operator|.
name|getReference
argument_list|(
name|bean
argument_list|,
name|CamelContext
operator|.
name|class
argument_list|,
name|manager
operator|.
name|createCreationalContext
argument_list|(
name|bean
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

