begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.cdi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cdi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
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

begin_class
annotation|@
name|Vetoed
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
DECL|method|getReferencesByType (BeanManager manager, Class<T> type, Annotation... qualifiers)
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Set
argument_list|<
name|T
argument_list|>
name|getReferencesByType
parameter_list|(
name|BeanManager
name|manager
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Annotation
modifier|...
name|qualifiers
parameter_list|)
block|{
return|return
name|manager
operator|.
name|getBeans
argument_list|(
name|type
argument_list|,
name|qualifiers
argument_list|)
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|bean
lambda|->
name|getReference
argument_list|(
name|manager
argument_list|,
name|type
argument_list|,
name|bean
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toSet
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getReferenceByName (BeanManager manager, String name, Class<T> type)
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Optional
argument_list|<
name|T
argument_list|>
name|getReferenceByName
parameter_list|(
name|BeanManager
name|manager
parameter_list|,
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|Optional
operator|.
name|of
argument_list|(
name|manager
operator|.
name|getBeans
argument_list|(
name|name
argument_list|)
argument_list|)
operator|.
name|map
argument_list|(
name|manager
operator|::
name|resolve
argument_list|)
operator|.
name|map
argument_list|(
name|bean
lambda|->
name|getReference
argument_list|(
name|manager
argument_list|,
name|type
argument_list|,
name|bean
argument_list|)
argument_list|)
return|;
block|}
DECL|method|getReferenceByType (BeanManager manager, Class<T> type, Annotation... qualifiers)
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Optional
argument_list|<
name|T
argument_list|>
name|getReferenceByType
parameter_list|(
name|BeanManager
name|manager
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Annotation
modifier|...
name|qualifiers
parameter_list|)
block|{
return|return
name|Optional
operator|.
name|of
argument_list|(
name|manager
operator|.
name|getBeans
argument_list|(
name|type
argument_list|,
name|qualifiers
argument_list|)
argument_list|)
operator|.
name|map
argument_list|(
name|manager
operator|::
name|resolve
argument_list|)
operator|.
name|map
argument_list|(
name|bean
lambda|->
name|getReference
argument_list|(
name|manager
argument_list|,
name|type
argument_list|,
name|bean
argument_list|)
argument_list|)
return|;
block|}
DECL|method|getReference (BeanManager manager, Class<T> type, Bean<?> bean)
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|getReference
parameter_list|(
name|BeanManager
name|manager
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Bean
argument_list|<
name|?
argument_list|>
name|bean
parameter_list|)
block|{
return|return
name|type
operator|.
name|cast
argument_list|(
name|manager
operator|.
name|getReference
argument_list|(
name|bean
argument_list|,
name|type
argument_list|,
name|manager
operator|.
name|createCreationalContext
argument_list|(
name|bean
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

